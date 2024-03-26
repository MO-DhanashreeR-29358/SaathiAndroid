package com.mosl.tabapp.helper.fileuploader

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import base.fragments.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mosl.app.core.utils.alert
import com.mosl.ekycexpress.R
import com.yalantis.ucrop.UCrop
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaathiFileUploader(private val fragment:com.mosl.app.coreui.base.BaseFragment, private val subject: PublishSubject<SaathiUploadUiSubject>) {
    private var docTypeStr = ""
    private var selectedImageUri: Uri? = null
    val context = fragment.requireContext()
    private val SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage"
    private var docTypePref = context.getSharedPreferences(
        PREF_DOCTYPE,
        Activity.MODE_PRIVATE
    )
    private var editor = docTypePref.edit()

    val permissionsArr = mutableListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        //Manifest.permission.SEND_SMS
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_IMAGES)
            add(Manifest.permission.READ_MEDIA_VIDEO)
            add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }.toTypedArray()

    private fun savePref(docType: String) {
        editor.putString(DOCTYPE_PREF_KEY, docType)
        editor.apply()
    }

    private fun clearPref() {
        editor.clear()
        editor.apply()
    }

    fun openCamera(docType: String = "") {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            val photoFile: File? = try {
                createFile()
            } catch (ex: IOException) {
                Timber.e(ex)
                null
            }
            photoFile?.also {
                val dataDir = (context.applicationContext).applicationInfo.dataDir
                val authority = if (dataDir != null && (dataDir.toLowerCase(Locale.ENGLISH)
                        .contains("dev.debug"))
                ) {
                    INVESTOR_INTERNAL_DEBUG
                } else if (dataDir != null &&
                    dataDir.toLowerCase(Locale.ENGLISH)
                        .contains("internal.debug")
                ) {
                    TRADER_INTERNAL_DEBUG
                } else if (dataDir != null && (dataDir.toLowerCase(Locale.ENGLISH)
                        .contains("dev"))
                ) {
                    INVESTOR_INTERNAL_RELEASE
                } else if (dataDir != null
                    && dataDir.toLowerCase(Locale.ENGLISH)
                        .contains("internal")
                ) {
                    TRADER_INTERNAL_RELEASE
                } else if (dataDir != null &&
                    dataDir.toLowerCase(Locale.ENGLISH)
                        .contains("mobile.debug")
                ) {
                    INVESTOR_PRODUCTION_DEBUG
                } else if (dataDir != null &&
                    dataDir.toLowerCase(Locale.ENGLISH)
                        .contains("com.debug")
                ) {
                    TRADER_PRODUCTION_DEBUG
                } else if (dataDir != null && dataDir.toLowerCase(Locale.ENGLISH)
                        .contains("mobile")
                ) {
                    INVESTOR_PRODUCTION_RELEASE
                } else if(dataDir != null && dataDir.toLowerCase(Locale.ENGLISH)
                        .contains("uat.debug"))
                    {
                            SAATHI_UAT_DEBUG
                    }
                else {
                    TRADER_PRODUCTION_RELEASE
                }
                val photoURI: Uri = FileProvider.getUriForFile(context, authority, it)
                selectedImageUri = photoURI
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                docTypeStr = docType
                savePref(DOC_TYPE_CAMERA)
                fragment.startActivityForResult(intent, REQUEST_CODE_CAMERA)
            }
        }
    }

    @Throws(IOException::class)
    private fun createFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(storageDir, "/JPEG")
        if (!file.exists()) {
            file.mkdirs()
        }
        return File((file.absolutePath + "/" + timeStamp))

    }

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    REQUEST_CODE_File_PICKER -> {
                        val fileUri = data?.data
                        fileUri?.let { uri ->
                            val contentResolver = context.contentResolver
                            val mimeTypeMap = MimeTypeMap.getSingleton()
                            val strMimeType =
                                mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
                            if (strMimeType.equals("jpg") || strMimeType.equals(
                                    "png"
                                ) || strMimeType.equals(
                                    "jpeg"
                                )
                            ) {
                                if (isSizeAbove(uri)) {
                                    alert()
                                } else {
                                    startCrop(uri)
                                }
                            } else {
                                subject.onNext(SaathiUploadUiSubject.SelectedFileEvent(uri.toString()))
                            }
                        }
                    }
                    REQUEST_CODE_IMAGE_PICKER -> {
                        val imageUri = data?.data
                        imageUri?.let { uri ->
                            if (isSizeAbove(uri)) {
                                alert()
                            } else {
                                startCrop(uri)
                            }
                        }
                    }
                    REQUEST_CODE_CAMERA -> {
                        selectedImageUri?.let {
                            if (isSizeAbove(it)) {
                                alert()
                            } else {
                                startCrop(it)
                            }
                        }
                    }
                    UCrop.REQUEST_CROP -> {
                        if (resultCode == Activity.RESULT_OK) {
                            data?.let {
                                val resultUri = UCrop.getOutput(it)
                                resultUri?.let { uri ->
                                    docTypeStr = docTypePref.getString(DOCTYPE_PREF_KEY, "")
                                        .toString()
                                    subject.onNext(
                                        if (docTypeStr.isEmpty()) {
                                            SaathiUploadUiSubject.SelectedFileEvent(uri.toString())
                                        } else {
                                            SaathiUploadUiSubject.SelectedImageEvent(
                                                uri.toString(),
                                                docTypeStr
                                            )
                                        }
                                    )
//                                    clearPref()
                                }
                            }
                        } else if (resultCode == UCrop.RESULT_ERROR) {
                            data?.let {
                                val error = UCrop.getError(it)
                                Timber.d("onCaptureImageError---> $error")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
            context.alert("Please use Add From Device for better experience")
        }
    }

    fun hasAllPermission(): Boolean {
        var hasAllPermissions = true
        for (a in permissionsArr) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    a
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasAllPermissions = false
                break
            }
        }
        return hasAllPermissions
    }

    fun openImageChooser(docType: String ) {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = MimeType.ALL.id
            val mimeTypes =
                arrayOf(MimeType.PNG.id, MimeType.JPG.id, MimeType.JPEG.id)
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            docTypeStr = docType
            savePref(DOC_TYPE_GALLERY)
            fragment.startActivityForResult(this, REQUEST_CODE_IMAGE_PICKER)
        }
    }

    fun openFileChooser(docType: String) {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            type = MimeType.ALL.id
            val mimeTypes =
                arrayOf(
                    MimeType.PNG.id,
                    MimeType.JPG.id,
                    MimeType.JPEG.id,
                    MimeType.PDF.id,
                    MimeType.ZIP.id
                )
            docTypeStr = docType
            savePref(DOC_TYPE_FILE)
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            fragment.startActivityForResult(this, REQUEST_CODE_File_PICKER)
        }
    }

    private fun startCrop(imageUri: Uri?) {
        val destinationFileName: String =
            SAMPLE_CROPPED_IMAGE_NAME
        if (imageUri != null) {
            UCrop.of(
                imageUri,
                Uri.fromFile(File(context.cacheDir, destinationFileName))
            )
                .start(fragment.requireContext(), fragment)
        }

    }

    private fun alert() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.alert_dialog_title)
            .setMessage(FILE_LARGE_MESSAGE)
            .setNegativeButton("Ok", null)
            .setCancelable(false)
            .show()
    }

    private fun isSizeAbove(uri: Uri): Boolean {
        val cursor: Cursor? =
            context.contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            val sizeIndex: Int = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            val fileSizeInBytes: Long = cursor.getLong(sizeIndex)
            cursor.close()

            val fileSizeInKB = fileSizeInBytes / 1024
            val fileSizeInMB = fileSizeInKB / 1024
            return fileSizeInMB >= FILE_UPLOAD_SIZE_LIMT
        }

        return false
    }

    fun reTakeOption(): Boolean{
        if (docTypePref.getString(DOCTYPE_PREF_KEY, "").isNullOrEmpty()){
            return false
        }else{
            when(docTypePref.getString(DOCTYPE_PREF_KEY, "")){
                DOC_TYPE_CAMERA ->{
                    openCamera()
                }
                DOC_TYPE_GALLERY -> {
                    openImageChooser("")
                }
                DOC_TYPE_FILE ->{
                    openFileChooser("")
                }
                else -> {
                    openCamera()
                }
            }
            return true
        }
    }

    companion object {
        private const val REQUEST_CODE_File_PICKER = 100
        private const val REQUEST_CODE_IMAGE_PICKER = 200
        private const val REQUEST_CODE_CAMERA = 300
        const val REQUEST_CODE_PERMISSION_REQUEST = 400
        const val DOC_TYPE_CAMERA = "CAMERA"
        const val DOC_TYPE_GALLERY = "GALLERY"
        const val DOC_TYPE_FILE = "FILE"
        const val PREF_DOCTYPE = "doc_type"
        const val DOCTYPE_PREF_KEY = "strDocType"
        const val TRADER_INTERNAL_DEBUG = "mosl.powerapp.com.internal.debug.fileprovider"
        const val TRADER_INTERNAL_RELEASE = "mosl.powerapp.com.internal.fileprovider"
        const val TRADER_PRODUCTION_DEBUG = "mosl.powerapp.com.debug.fileprovider"
        const val TRADER_PRODUCTION_RELEASE = "mosl.powerapp.com.fileprovider"
        const val INVESTOR_INTERNAL_DEBUG = "com.mosl.mobile.dev.debug.fileprovider"
        const val INVESTOR_INTERNAL_RELEASE = "com.mosl.mobile.dev.fileprovider"
        const val INVESTOR_PRODUCTION_DEBUG = "com.mosl.mobile.debug.fileprovider"
        const val INVESTOR_PRODUCTION_RELEASE = "com.mosl.mobile.fileprovider"
        const val SAATHI_UAT_DEBUG="com.mosl.tabapp.uat.debug.fileprovider"
        const val FILE_UPLOAD_SIZE_LIMT = 10
        const val FILE_LARGE_MESSAGE =
            "Please upload image less than $FILE_UPLOAD_SIZE_LIMT MB"

    }

    enum class MimeType(val id: String) {
        PNG("image/png"),
        JPG("image/jpg"),
        JPEG("image/jpeg"),
        PDF("application/pdf"),
        ZIP("application/zip"),
        ALL("*/*")
    }
}