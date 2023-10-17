package com.mosl.tabapp.util

import android.content.pm.PackageManager
import android.os.Build
import com.mosl.tabapp.BuildConfig
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object RootDeviceHelper {

    fun isRooted(packageManager: PackageManager): Boolean {
        return (checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4(
            packageManager
        )) && !BuildConfig.DEBUG
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/system/bin/.ext/.su",
            "/system/usr/we-need-root/su-backup",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime()
                .exec(arrayOf("/system/xbin/which", "su"))
            val `in` = BufferedReader(InputStreamReader(process!!.inputStream))
            `in`.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic") ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK built for x86") ||
                Build.MANUFACTURER.contains("Genymotion") ||
                Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
                "google_sdk" == Build.PRODUCT)
    }

    fun checkRootMethod4(packageManager: PackageManager): Boolean {
        val malwarePackages = arrayOf(
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser"
        )
        for (malwarepackage in malwarePackages) {
            try {
                packageManager.getPackageInfo(malwarepackage, 0)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.i("Package Not Found")
            }
        }
        return false
    }
}
