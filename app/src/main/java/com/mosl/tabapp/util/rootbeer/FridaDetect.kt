package com.app.aspirecollection.Util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets

object FridaDetect {
    // Check for known Frida libraries.
    private fun isFridaLibraryLoaded(): Boolean {
        val fridaLibraries = listOf(
            "libfrida-gum.so",
            "libfrida-gumjs.so",
            "libfrida-agent.so"
        )
        for (lib in fridaLibraries) {
            if (isLibraryLoaded(lib)) {
                Timber.d("Detected Frida library: $lib")
                return true
            }
        }
        return false
    }

    // Check if a specific library is loaded.
    private fun isLibraryLoaded(libName: String): Boolean {
        return try {
            System.loadLibrary(libName)
            true
        } catch (e: UnsatisfiedLinkError) {
            false
        }
    }

    // Check for known Frida processes.
    private fun isFridaProcessRunning(): Boolean {
        val fridaProcesses = listOf("frida-server", "frida")
        return isProcessRunning(fridaProcesses)
    }

    // Check for specific processes running in the system.
    private fun isProcessRunning(processNames: List<String>): Boolean {
        try {
            val process = Runtime.getRuntime().exec("ps")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                for (processName in processNames) {
                    if (line?.contains(processName) == true) {
                        Timber.d("Detected Frida process: $processName")
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            Timber.d("Error checking processes: ${e.message}")
        }
        return false
    }

    // Detect Frida's default open port (27042).
    private fun isFridaPortOpen(): Boolean {
        return isPortOpen(27042)
    }

    // Check if a specific port is open.
    private fun isPortOpen(port: Int): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("netstat -an")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                if (line?.contains(":$port") == true && line!!.contains("LISTEN")) {
                    Timber.d("Detected open port: $port")
                    return true
                }
            }
            false
        } catch (e: Exception) {
            Timber.d("Error checking port $port: ${e.message}")
            false
        }
    }
    // Detect suspicious processes
    private fun detectSuspiciousProcesses(): Boolean {
        val suspiciousProcesses = listOf("frida", "objection", "frida-server")
        val procDirectory = File("/proc")

        if (procDirectory.isDirectory) {
            procDirectory.listFiles()?.forEach { process ->
                val processName = process.name
                suspiciousProcesses.forEach { suspect ->
                    if (processName.contains(suspect, ignoreCase = true)) {
                        Timber.d("SecurityCheck Suspicious process detected: $processName")
                        return true
                    }
                }
            }
        }
        return false
    }

    // Detect suspicious files (Frida server)
    private fun isFridaFileDetected(): Boolean {
        val suspiciousPaths = listOf(
            "/data/local/tmp/frida-server",
            "/data/local/tmp/objection",
            "/data/local/tmp/re.frida.server"
        )

        suspiciousPaths.forEach { path ->
            val file = File(path)
            if (file.exists()) {
                Timber.d("SecurityCheck Suspicious file detected: $path")
                return true
            }
        }
        return false
    }

    // Detect Frida listening port
    private fun isFridaPortDetected(): Boolean {
        var value = false
        GlobalScope.launch(Dispatchers.IO) {
            var socket: Socket? = null
            try {
                socket = Socket("127.0.0.1", 27042)
                // If connection is successful, Frida is likely running
                throw SecurityException("Frida detected!")
                value = true
            } catch (e: IOException) {
                // No Frida server detected
                Timber.d("No Frida server detected on port 27042.")
                value = false
            } finally {
                socket?.close()
            }
        }
        return value
    }
    //Combined Frida detection method.
    fun isFridaDetected(): Boolean {
        return isFridaLibraryLoaded() || isFridaProcessRunning() || isFridaPortOpen() || detectSuspiciousProcesses()
                || isFridaFileDetected() || isFridaPortDetected()
    }
}

