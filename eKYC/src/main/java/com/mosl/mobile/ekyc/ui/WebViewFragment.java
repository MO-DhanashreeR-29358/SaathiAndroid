package com.mosl.mobile.ekyc.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.mosl.mobile.ekyc.R;

import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;

/**
 * Created by Shefali on 11/08/16.
 */
public class WebViewFragment extends Fragment {
    View wvView;
    private WebView webView;
    private ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        try {
            wvView = inflater.inflate(R.layout.activity_webview, container, false);
            setUpView(wvView);
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
//            Crashlytics.logException(e);
        }
        return wvView;
    }

    public void setUpView(View wvView) {
        webView = (WebView) wvView.findViewById(R.id.webView);
        progressBar = (ProgressBar) wvView.findViewById(R.id.progressBar);
//        Bundle bundle = getIntent().getExtras();
        // String url = getIntent().getExtras().getString("URL");
        progressBar.setMax(100);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://crm.motilaloswal.com/");
        WebViewFragment.this.progressBar.setProgress(0);
        //startWebView(url);
        webView.setWebChromeClient(new MyWebViewClient());
    }
    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            WebViewFragment.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }
    private void setValue(int progress) {
        this.progressBar.setProgress(progress);
        if (progress == 100) {
            progressBar.setVisibility(View.GONE);
        }

    }
}
