package com.mosl.shared.util

import android.content.ContentResolver
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import java.net.Inet4Address
import java.net.NetworkInterface
import java.text.DateFormat

fun getLocalIpAddress(): String {
  try {
    val en = NetworkInterface.getNetworkInterfaces()
    while (en.hasMoreElements()) {
      val intf = en.nextElement()
      val enumIpAddr = intf.inetAddresses
      while (enumIpAddr.hasMoreElements()) {
        val inetAddress = enumIpAddr.nextElement()
        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
          return inetAddress.getHostAddress()
        }
      }
    }
  } catch (ex: Exception) {
    ex.printStackTrace()
  }
  return "127.0.0.1"
}

fun getDeviceID(contentResolver: ContentResolver): String {
  return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}

fun hasGPSDevice(context: Context): Boolean {
  val mgr = context
    .getSystemService(Context.LOCATION_SERVICE) as LocationManager
  val providers = mgr.allProviders ?: return false
  return providers.contains(LocationManager.GPS_PROVIDER)
}

fun isLocationFromMockProvider(
  context: Context,
  location: Location
): Boolean {
  return if (Build.VERSION.SDK_INT >= 18) {
    location.isFromMockProvider
  } else {
    isMockSettingsON(context)
  }
}

fun formatMonth(month: String): String {
  val inputFormat: DateFormat = AppDateFormats.df_date10
  val outputFormat: DateFormat = AppDateFormats.df_Date1
  val date = inputFormat.parse(month)
  return outputFormat.format(date)
}

@Suppress("DEPRECATION") fun isMockSettingsON(context: Context): Boolean {
  return Settings.Secure.getString(
    context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION
  ) != "0"
}

fun getFormatedDate(
  date: String,
  inputFormatedDate: DateFormat,
  outputFormatedDate: DateFormat
): String {
  val time = inputFormatedDate.parse(date) ?: "0000-00-00'T'00:00:00"
  return outputFormatedDate.format(time)
}

fun isEmulator(): Boolean {
  return (Build.FINGERPRINT.startsWith("generic")
          || Build.FINGERPRINT.startsWith("unknown")
          || Build.MODEL.contains("google_sdk")
          || Build.MODEL.contains("Emulator")
          || Build.MODEL.contains("Android SDK built for x86")
          || Build.MANUFACTURER.contains("Genymotion")
          || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
          || "google_sdk" == Build.PRODUCT)
}