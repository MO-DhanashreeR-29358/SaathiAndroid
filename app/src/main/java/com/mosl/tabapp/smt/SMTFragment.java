package com.mosl.tabapp.smt;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.core.CommonFragment;
import java.util.HashMap;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

import static android.content.Context.DOWNLOAD_SERVICE;

public class SMTFragment extends CommonFragment implements MOSLConstants, SMTCallBack {
	private View view;
	private ProgressBar progressBar;
	private WebView webView;

	@Override protected int getLayout() {
		return R.layout.frag_webview;
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		view = super.onCreateView(inflater, container, savedInstanceState);
		//attachLayout(R.layout.frag_webview);
		hideAppTitle();
		setUpView(Objects.requireNonNull(this.getArguments()).getString(URL_PATH));

		return view;
	}

	@SuppressLint("SetJavaScriptEnabled") private void setUpView(String url) {
		webView = view.findViewById(R.id.webView);
		progressBar = view.findViewById(R.id.progressBar);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new SMTFragment.WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());

		webView.setDownloadListener(
			(url1, userAgent, contentDisposition, mimetype, contentLength) -> {
				DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url1));
				request.setMimeType(mimetype);
				String cookies = CookieManager.getInstance().getCookie(url1);
				request.addRequestHeader("cookie", cookies);
				request.addRequestHeader("User-Agent", userAgent);
				request.setDescription("Downloading File");
				request.setTitle(URLUtil.guessFileName(url1, contentDisposition, mimetype));
				request.allowScanningByMediaScanner();
				request.setNotificationVisibility(
					DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				DownloadManager dm =
					(DownloadManager) Objects.requireNonNull(getContext())
						.getSystemService(DOWNLOAD_SERVICE);
				dm.enqueue(request);
				progressBar.setVisibility(View.GONE);
			});

		webView.loadUrl(url + "&Os=android");
		webView.addJavascriptInterface(new SMTInterface(this), "Saathi");
	}

	@Override public void openEkyc(String OAOUserId) {
		HashMap<String, String> map = new HashMap();
		map.put(NEW_USERID, OAOUserId);
		map.put(BRANCHEMPCODE, Objects.requireNonNull(userManager.getUser()).employeeCode);
		map.put(COMMANDTYPE, "RESUME");
		map.put(APPLICATIONTYPE, "CRM");
		map.put(OSTYPE, "ANDROID");
		map.put(APPLOGINID, userManager.getUser().employeeCode);
		map.put(TOKENKEY, userManager.getUser().token);
		//gotoNewEkyc(getInputBody(map));
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

	@Override public void onFragmentReopened(@NotNull Bundle args) {
		super.onFragmentReopened(args);
		webView.loadUrl(args.getString(URL_PATH) + "&Os=android");
	}
}

