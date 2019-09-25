package com.mosl.tabapp.Permisssion

import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionCheckHelperFragment(val fragment: Fragment) {

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
    val value = ActivityCompat.checkSelfPermission(fragment.requireContext(), CAMERA)
    return value == PackageManager.PERMISSION_GRANTED
  }

  fun locationPermission(): Boolean {
    val value = ActivityCompat.checkSelfPermission(fragment.requireContext(), ACCESS_FINE_LOCATION)
    return value == PackageManager.PERMISSION_GRANTED
  }

  private fun storagePermission(): Boolean {
    val value =
      ActivityCompat.checkSelfPermission(fragment.requireContext(), WRITE_EXTERNAL_STORAGE)
    return value == PackageManager.PERMISSION_GRANTED
  }

  fun requestCamera() {
    requestCameraPermission()
  }

  fun requestLocation() {
    requestLocationPermission()

  }

  fun requestStorage() {

    requestStoragePermission()
  }

  fun requestAllPermissions() {
    fragment.requestPermissions(
        arrayOf(CAMERA, permission.ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE),
        PERMISSION_ALL_CODE
    )
  }

  private fun requestCameraPermission() {
    fragment.requestPermissions(arrayOf(CAMERA), PERMISSION_CAMERA_CODE)
  }

  private fun requestLocationPermission() {
    fragment.requestPermissions(arrayOf(ACCESS_FINE_LOCATION), PERMISSION_CAMERA_CODE)
  }

  private fun requestStoragePermission() {
    fragment.requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), PERMISSION_WRITE_STORAGE_CODE)
  }

  fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    when (requestCode) {
      PERMISSION_ALL_CODE -> if (grantResults.isNotEmpty()) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          listener.OnPermissionGranted()
        } else {
          //requestAllPermissions()
        }
      }
    }
  }

  interface OnPermissionChangeListener {
    fun OnPermissionGranted()
  }
}
