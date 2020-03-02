package com.mosl.tabapp.permission

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.mosl.tabapp.R
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageUploadHelper(
  private val fragment: Fragment
) {
  val context = fragment.requireContext()
  var callback: ImageUploadCallback? = null
  private val REQUEST_CAMERA_PHOTO = 0
  private val REQUEST_GALLERY_PHOTO = 1
  private var selectedImageUri: Uri? = null
  private var absolutePath: String? = null
  private var mCurrentPhotoPath: String? = null
  fun setImageCallback(callback: ImageUploadCallback) {
    this.callback = callback
  }

  fun showDialog() {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!
      .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(R.layout.dialog_add_document)
    dialog.window!!
      .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    dialog.setCanceledOnTouchOutside(true)
    val tvTakePhoto = dialog.findViewById<TextView>(R.id.tvTakePhoto)
    val tvCancel = dialog.findViewById<TextView>(R.id.tvCancel)
    val tvAddFromDevice = dialog.findViewById<TextView>(R.id.tvAddFromDevice)

    tvCancel.text = context.getString(R.string.text_label_cancel)
    tvTakePhoto.text = context.getString(R.string.text_label_take_photo)
    tvAddFromDevice.text = context.getString(R.string.text_label_add_from_device)
    tvTakePhoto.setOnClickListener {
      dialog.dismiss()
      cameraIntent()
    }
    tvAddFromDevice.setOnClickListener {
      dialog.dismiss()
      galleryIntent()
    }
    tvCancel.setOnClickListener { dialog.dismiss() }
    dialog.show()
  }

  private fun galleryIntent() {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    if (intent.resolveActivity(context.packageManager) != null) {
      fragment.startActivityForResult(
        Intent.createChooser(intent, "Select Picture"),
        REQUEST_GALLERY_PHOTO
      )
    }
  }

  private fun cameraIntent() {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(context.packageManager) != null) {
      val photoFile: File? = try {
        createFile()
      } catch (ex: IOException) {
        Timber.e(ex)
        null
      }
      photoFile?.also {
        //val authority = context.applicationContext.packageName + ".provider"
        val authority = "com.mosl.tabapp.debug.provider"
        val photoURI: Uri = FileProvider.getUriForFile(context, authority, it)
        selectedImageUri = photoURI
        absolutePath = photoFile.absolutePath
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        fragment.startActivityForResult(intent, REQUEST_CAMERA_PHOTO)
      }
    }
  }

  fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    val actualImage: File
    var imageUri: Uri
    when (requestCode) {
      REQUEST_CAMERA_PHOTO -> {
        if (resultCode == RESULT_OK) {
          selectedImageUri = data?.data
          Timber.d("onActivityResult selectedImageUri--->$selectedImageUri")
          Timber.d("data found--->${data?.data}")
          selectedImageUri?.let {
            val selectedImage = it
            context.contentResolver.notifyChange(selectedImage, null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              imageUri = Uri.parse(absolutePath)
              actualImage = File(imageUri.path!!)
            } else {
              actualImage = File(it.path)
            }
            imageUri = Uri.fromFile(actualImage)
            addUri(imageUri, actualImage)
          }
        }
      }
      REQUEST_GALLERY_PHOTO -> {
        if (resultCode == RESULT_OK) {
          selectedImageUri = data?.data
          actualImage = File(selectedImageUri?.path!!)
          addUri(selectedImageUri, actualImage)
        }
      }

    }

  }

  private fun addUri(imageUri: Uri?, file: File?) {
    if (imageUri != null && file != null) {
      callback?.onImageCaptured(imageUri, file)
    } else {
      Toast.makeText(context, "Image already selected", Toast.LENGTH_SHORT).show()
    }
  }

  @Throws(IOException::class)
  private fun createFile(): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
      "JPEG_${timeStamp}_", /* prefix */
      ".jpg", /* suffix */
      storageDir /* directory */
    ).apply {
      // Save a file: path for use with ACTION_VIEW intents
      mCurrentPhotoPath = absolutePath
    }
  }

  interface ImageUploadCallback {
    fun onImageCaptured(uri: Uri, file: File)
  }
}