package com.mosl.tabapp.util.keylogger

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
class MyAccessibilityService: AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val currentPackage = event.packageName.toString()
            // Check if a third-party keyboard is active
            if (currentPackage == "com.thirdparty.keyboard") {
                notifyUserToSwitchKeyboard()
            }
        }
    }
    override fun onInterrupt() {
        // Handle interruption
    }
    private fun notifyUserToSwitchKeyboard() {
        Toast.makeText(this@MyAccessibilityService,"Third-party keyboard detected", Toast.LENGTH_LONG).show()
        // Show a notification or dialog to inform the user
        Log.d("MyAccessibilityService", "Third-party keyboard detected. Please switch back.")
        // Implement your notification logic here
    }
}