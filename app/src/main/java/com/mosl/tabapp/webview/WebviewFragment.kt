package com.mosl.tabapp.webview

import android.app.Activity.RESULT_OK
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.mosl.app.core.rx.applyUiModel
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.webview.CommonURLRequest
import com.mosl.shared.Encryptor.AESEncryptor
import com.mosl.tabapp.R
import com.mosl.tabapp.banner.BannerNavigator
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragWebviewBinding
import com.mosl.tabapp.util.nullSafe
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class WebviewFragment : CommonFragment() {
    lateinit var binding: FragWebviewBinding
    internal var pageLoadStarted = false
    internal val subject = PublishSubject.create<WebViewRequest>()

    @Inject
    lateinit var saathiApiNew: SaathiApi

    @Inject
    lateinit var encryptor: AESEncryptor


    @Inject
    lateinit var bannerNavigator: BannerNavigator

    private var mUploadMessage: ValueCallback<Uri>? = null
    lateinit var mCapturedImageURI: Uri
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    private val INPUT_FILE_REQUEST_CODE = 1
    private val FILECHOOSER_RESULTCODE = 1
    override fun getLayout() = R.layout.frag_webview
    private var pageId = ""
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragWebviewBinding.bind(view)
        setupView()
        parseArguments(requireArguments())
        start()
    }

    private fun parseArguments(args: Bundle) {
        var oaoUserID = ""
        pageId = args.getString(PAGE_ID).nullSafe()
        val url = args.getString(URL)
        if (args.containsKey(OAO_USER_ID)) {
            oaoUserID = args.getString(OAO_USER_ID).toString()
        }
        when {
            url != null -> subject.onNext(WebViewRequest.Url(url))
            pageId != null -> subject.onNext(WebViewRequest.Id(pageId, oaoUserID))
        }
    }

    private fun setupView() {
        hideAppTitle()
        binding.progressBar.max = 100
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(
                view: WebView,
                url: String
            ) {
                super.onPageFinished(view, url)
                if (isAdded) {
                    binding.progressBar.visibility = View.GONE
                    if (pageLoadStarted) {
                        view.clearHistory()
                        pageLoadStarted = false
                    }
                }
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                if (isAdded) binding.progressBar.progress = newProgress
            }

            // For Android 5.0
            override fun onShowFileChooser(
                view: WebView?,
                filePath: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                // Double check that we don't have any existing callbacks
                mFilePathCallback?.onReceiveValue(null)
                mFilePathCallback = filePath
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent?.resolveActivity(requireContext().packageManager) != null) {
                    // Create the File where the photo should go
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath()
                        takePictureIntent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "image/*"
                val intentArray: Array<Intent>
                intentArray = takePictureIntent?.let { arrayOf(it) } ?: arrayOf<Intent>()
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
                return true
            }

            // openFileChooser for Android 3.0+
            fun openFileChooser(
                uploadMsg: ValueCallback<Uri>?,
                acceptType: String?
            ) {
                mUploadMessage = uploadMsg
                // Create AndroidExampleFolder at sdcard
                val imageStorageDir = File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                    ), "AndroidExampleFolder"
                )
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs()
                }


                mCapturedImageURI = Uri.fromFile(imageStorageDir.absoluteFile)

                // Camera capture image intent
                val captureIntent = Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE
                )
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI)
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"

                // Create file chooser intent
                val chooserIntent = Intent.createChooser(i, "Image Chooser")

                // Set camera intent to file chooser
                chooserIntent.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent)
                )

                // On select image call onActivityResult method of activity
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)
            }

            // openFileChooser for Android < 3.0
            fun openFileChooser(uploadMsg: ValueCallback<Uri>?) {
                openFileChooser(uploadMsg, "")
            }

            //openFileChooser for other Android versions
            fun openFileChooser(
                uploadMsg: ValueCallback<Uri>?,
                acceptType: String?,
                capture: String?
            ) {
                openFileChooser(uploadMsg, acceptType)
            }
        }

        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true

        binding.webView.setDownloadListener { url, userAgent, contentDisposition, miType, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(miType)
            val cookies = CookieManager.getInstance()
                .getCookie(url)
            request.addRequestHeader("cookie", cookies)
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, miType))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            (this.context?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager)?.enqueue(
                request
            )
        }

    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri>? = null

            // Check that the response is a good one
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback!!.onReceiveValue(results)
            mFilePathCallback = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == mUploadMessage) {
                    return
                }
                var result: Uri? = null
                try {
                    result = if (resultCode != RESULT_OK) {
                        null
                    } else {

                        // retrieve from the private variable if the intent is null
                        if (data == null) mCapturedImageURI else data.data
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(), "activity :$e",
                        Toast.LENGTH_LONG
                    ).show()
                }
                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
        }
        return
    }

    fun start() {
        val isForPrimeEntry = pageId == PRIME_ENTRY_ID
        disposable.add(subject.filter { event -> event is WebViewRequest.Url }
            .map { request -> request as WebViewRequest.Url }
            .map { it.url }
            .subscribe { this.loadUrl(it, isForPrimeEntry) })

        disposable.add(subject.filter { event -> event is WebViewRequest.Id }
            .map { request -> request as WebViewRequest.Id }
            .switchMap { pageID ->
                getUrlObservable(pageID)
                    .compose(applyUiModel())
            }
            .subscribe { this.renderUrl(it) })
    }

    internal fun renderUrl(uiModel: UiModel<Pair<String?, String>>) {
        toggleProgressBar(uiModel.inProgress)

        uiModel.onFailure {
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {
            when (it.second) {

                FRESH_REGISTRATION_ID, EXISTING_APPLICATIONS_ID, RESUME_APPLICATIONS_ID -> {
                    it.first?.apply { bannerNavigator.openUrlInAppCrome(this.replace(" ", "%20")) }
                }
                else -> it.first?.apply {
                    loadUrl(
                        this.replace(" ", "%20"),
                        it.second == PRIME_ENTRY_ID
                    )
                }
            }

        }
    }

    private fun getUrlObservable(webViewRequest: WebViewRequest.Id): Observable<Pair<String?, String>> {

        when (webViewRequest.id) {
            INCENTIVE_REPORT_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(INCENTIVE_REPORT_KEY, null, userManager.user.employeeCode)
                ).map { it ->
                    Pair(it.url, webViewRequest.id)
                }
            }
            FRESH_REGISTRATION_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(FRESH_REGISTRATION_KEY, null, null)
                )
                    .map {
                        Pair(it.url, webViewRequest.id)
                    }
            }
            EXISTING_APPLICATIONS_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(EXISTING_APPLICATIONS_KEY, null, null)
                ).map {
                    Pair(it.url, webViewRequest.id)
                }
            }
            E_MODIFICATION_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(E_MODIFICATION_KEY, null, null)
                ).map { Pair(it.url, webViewRequest.id) }
            }
            NEW_LAS_APPLICATION_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(NEW_LAS_APPLICATION_KEY, null, null)
                ).map { Pair(it.url, webViewRequest.id) }
            }
            RESUME_LAS_APPLICATION_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(RESUME_LAS_APPLICATION_KEY, null, null)
                ).map { Pair(it.url, webViewRequest.id) }
            }
            RESUME_APPLICATIONS_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(RESUME_APPLICATIONS_KEY, webViewRequest.oaoUserId, null)
                ).map {
                    Pair(it.url, webViewRequest.id)
                }
            }
            STADIUM_PITCH_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(STADIUM_PITCH_KEY, null, null)
                ).map {
                    Pair(it.url, webViewRequest.id)
                }
            }
            COLLATERAL_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(COLLATERAL_KEY, null, null)
                ).map {
                    Pair(it.url, webViewRequest.id)
                }
            }
            MOST_GUIDE_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(MOST_GUIDE_KEY, webViewRequest.oaoUserId, null)
                ).map {
                    Pair(it.url, webViewRequest.id)
                }
            }
            SIP_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(SIP_KEY, null, null)
                ).map {
                    Pair(it.url, webViewRequest.id)
                }
            }
            PRIME_ENTRY_ID -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(PRIME_ENTRY_KEY, null, null)
                ).map {
                    Pair(it.url, webViewRequest.id)
                }
            }
            else -> {
                return saathiApiNew.getEkycExpressUrl(
                    CommonURLRequest(CHECK_APPROVAL_KEY, null, null)
                ).map {
                    Pair(it.url, webViewRequest.id)
                }
            }
        }
    }

    private fun loadUrl(url: String, isForPrimeEntry: Boolean = false) {
        if (isForPrimeEntry) {
            val extraHeaders: MutableMap<String, String> =
                HashMap()
            extraHeaders["token"] = encryptor.encrypt(userManager.user.token)
            binding.webView.loadUrl(url, extraHeaders)
        } else
            binding.webView.loadUrl(url)
        pageLoadStarted = true
    }

    override fun onFragmentReopened(args: Bundle) {
        parseArguments(args)
    }

    companion object {
        const val URL = "url"
        const val PAGE_ID = "page_id"
        const val OAO_USER_ID = "oaoUserID"
        const val INCENTIVE_REPORT_ID = "1"
        const val INCENTIVE_REPORT_KEY = "IncentiveReport"
        const val FRESH_REGISTRATION_ID = "2"
        const val FRESH_REGISTRATION_KEY = "FreshRegistration"
        const val EXISTING_APPLICATIONS_ID = "3"
        const val EXISTING_APPLICATIONS_KEY = "ExistingApplication"
        const val RESUME_APPLICATIONS_ID = "4"
        const val RESUME_APPLICATIONS_KEY = "OAOForm"
        const val STADIUM_PITCH_ID = "5"
        const val STADIUM_PITCH_KEY = "StadiumPitch"
        const val COLLATERAL_ID = "6"
        const val COLLATERAL_KEY = "Collateral"
        const val MOST_GUIDE_ID = "7"
        const val MOST_GUIDE_KEY = "MostGuide"
        const val CHECK_APPROVAL_ID = "8"
        const val CHECK_APPROVAL_KEY = "ChequeApproval"
        const val E_MODIFICATION_ID = "9"
        const val E_MODIFICATION_KEY = "EModification"
        const val SIP_ID = "11"
        const val SIP_KEY = "SIP"
        const val NEW_LAS_APPLICATION_ID = "12"
        const val NEW_LAS_APPLICATION_KEY = "LASaccount"
        const val RESUME_LAS_APPLICATION_ID = "13"
        const val RESUME_LAS_APPLICATION_KEY = "ExistingLASaccount"
        const val PRIME_ENTRY_ID = "14"
        const val PRIME_ENTRY_KEY = "PrimeEntry"
    }
}