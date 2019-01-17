package com.mosl.tabapp.smt;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.mosl.mobile.ekyc.ui.PostLoginActivity;
import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.ui.MOSLCommonActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import java.util.HashMap;

import static android.content.Context.DOWNLOAD_SERVICE;

public class SMTFragment extends MOSLCommonFragment implements MOSLConstants,SMTCallBack {
        private View view;
        private WebView webView;
        private ProgressBar progressBar;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
            view = super.onCreateView(inflater, container, savedInstanceState);
            attachLayout(R.layout.frag_webview);
            hideAppTitle();
            setUpView(this.getArguments().getString(URL_PATH));

            return view;
        }

        private void setUpView(String url) {
            webView = view.findViewById(R.id.webView);
            progressBar = view.findViewById(R.id.progressBar);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new SMTFragment.WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());

            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition,
                    String mimetype, long contentLength) {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimetype);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading File");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager dm =
                        (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    progressBar.setVisibility(View.GONE);
                }
            });


            webView.loadUrl(url);
            webView.addJavascriptInterface(new SMTInterface(this), "UpperMost");
        }

    @Override public void openEkyc(String OAOUserId) {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put(USERID, OAOUserId);
        gotoEkycPage(CALL_PROSPECT_DETAILS, hashMap);
    }

    public class WebViewClient extends android.webkit.WebViewClient {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                // TODO Auto-generated method stub

                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);

            }

        }


    }

