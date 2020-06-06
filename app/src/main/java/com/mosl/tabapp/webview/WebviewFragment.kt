package com.mosl.tabapp.webview

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.mosl.app.core.rx.applyUiModel
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.webview.EkycExpressRequest
import com.mosl.saathi.api.model.webview.IncentiveReportRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragWebviewBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class WebviewFragment : CommonFragment() {
    lateinit var binding: FragWebviewBinding
    internal var pageLoadStarted = false
    internal val subject = PublishSubject.create<WebViewRequest>()

    @Inject
    lateinit var saathiApiNew: SaathiApi

    override fun getLayout() = R.layout.frag_webview

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragWebviewBinding.bind(view)
        setupView()
        start()
        parseArguments(arguments!!)
    }

    private fun parseArguments(args: Bundle) {
        val pageId = args.getString(PAGE_ID)
        val url = args.getString(URL)
        when {
            url != null -> subject.onNext(WebViewRequest.Url(url))
            pageId != null -> subject.onNext(WebViewRequest.Id(pageId))
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
        }

        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = false
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

    fun start() {
        disposable.add(subject.filter { event -> event is WebViewRequest.Url }
            .map { request -> request as WebViewRequest.Url }
            .map { it.url }
            .subscribe { this.loadUrl(it) })

        disposable.add(subject.filter { event -> event is WebViewRequest.Id }
            .map { request -> request as WebViewRequest.Id }
            .map { it.id }
            .switchMap { pageID ->
                getUrlObservable(pageID)
                    .compose(applyUiModel())
            }
            .subscribe { this.renderUrl(it) })
    }

    internal fun renderUrl(uiModel: UiModel<String>) {
        toggleProgressBar(uiModel.inProgress)

        uiModel.onFailure {
            showOnFailurePopup(it)
        }
        uiModel.onSuccess {

            loadUrl(it)

        }
    }

    internal fun getUrlObservable(pageId: String): Observable<String> {

        if (pageId.equals(INCENTIVE_REPORT_ID)) {
            return saathiApiNew.getIncentiveReport(
                IncentiveReportRequest(userManager.user.employeeCode)
            ).map {
                it.apply {
                    this.replace(" ", "%20")
                }
            }
        } else if (pageId.equals(FRESH_REGISTRATION_ID)) {
            return saathiApiNew.getEkycExpressUrl(
                EkycExpressRequest(FRESH_REGISTRATION_KEY, null)
            )
                .map {
                    it.url?.apply {
                        this.replace(" ", "%20")
                    }
                }
        } else if (pageId.equals(EXISTING_APPLICATIONS_ID)) {
            return saathiApiNew.getEkycExpressUrl(
                EkycExpressRequest(EXISTING_APPLICATIONS_KEY, null)
            ).map {
                it.url?.apply {
                    this.replace(" ", "%20")
                }
            }
        } else {
            return saathiApiNew.getEkycExpressUrl(
                EkycExpressRequest(RESUME_APPLICATIONS_KEY, arguments?.getString(OAO_USER_ID))
            ).map {
                it.url?.apply {
                    this.replace(" ", "%20")
                }
            }
        }
    }

    private fun loadUrl(url: String) {
        binding.webView.loadUrl(url)
        pageLoadStarted = true
    }

    override fun onFragmentReopened(args: Bundle) {
        parseArguments(args)
    }

    companion object {
        const val URL = "url"
        const val PAGE_ID = "page_id"
        const val ENC_UCID = "encUcid"
        const val INCENTIVE_REPORT_ID = "1"
        const val FRESH_REGISTRATION_ID = "2"
        const val FRESH_REGISTRATION_KEY = "FreshRegistration"
        const val EXISTING_APPLICATIONS_ID = "3"
        const val EXISTING_APPLICATIONS_KEY = "ExistingApplication"
        const val RESUME_APPLICATIONS_ID = "4"
        const val RESUME_APPLICATIONS_KEY = "OAOForm"
        const val OAO_USER_ID = "oaoUserID"
    }
}