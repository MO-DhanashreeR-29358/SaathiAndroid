package com.mosl.tabapp.Permisssion

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class PermissionCheckHelper(val activityCompat: AppCompatActivity) {

  private val PERMISSION_CAMERA_CODE = 1001
  private val PERMISSION_WRITE_STORAGE_CODE = 1002
  private val PERMISSION_ALL_CODE = 1003
  lateinit var listener: OnPermissionChangeListener

  fun setPermissionHelper(listener: OnPermissionChangeListener) {
    this.listener = listener
  }

  fun checkAll(): Boolean {
    return cameraPermission() && storagePermission()
  }

  private fun cameraPermission(): Boolean {
    val value = ActivityCompat.checkSelfPermission(activityCompat, CAMERA)
    return value == PackageManager.PERMISSION_GRANTED
  }

  private fun storagePermission(): Boolean {
    val value =
      ActivityCompat.checkSelfPermission(activityCompat, WRITE_EXTERNAL_STORAGE)
    return value == PackageManager.PERMISSION_GRANTED
  }

  fun requestCamera() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestCameraPermission()
    }
  }

  fun requestStorage() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestStoragePermission()
    }
  }

  @RequiresApi(VERSION_CODES.M) fun requestAllPermissions() {
    activityCompat.requestPermissions(
        arrayOf(CAMERA, ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE), PERMISSION_ALL_CODE
    )
  }

  @RequiresApi(VERSION_CODES.M)
  private fun requestCameraPermission() {
    activityCompat.requestPermissions(arrayOf(CAMERA), PERMISSION_CAMERA_CODE)
  }

  @RequiresApi(VERSION_CODES.M)
  private fun requestStoragePermission() {
    activityCompat.requestPermissions(
        arrayOf(WRITE_EXTERNAL_STORAGE), PERMISSION_WRITE_STORAGE_CODE
    )
  }

  interface OnPermissionChangeListener {
    fun OnPermissionGranted()
  }
}
