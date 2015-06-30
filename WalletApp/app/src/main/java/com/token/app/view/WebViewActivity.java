package com.token.app.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.token.app.R;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends Activity {

	private static final String TAG = WebViewActivity.class.getSimpleName();
	private WebView paymentWebView;
	private String webUrl;
	private ProgressBar progress;
	private boolean isChat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.webview);
		if (getIntent().getExtras() != null) {
			isChat = true;
			webUrl = getIntent().getExtras().getString("url");
		}
		if (TextUtils.isEmpty(webUrl)) {
			finish();
			Toast.makeText(this, "No url", Toast.LENGTH_LONG).show();
			return;
		}
		initview();
	}

	private void initview() {
		findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		progress = (ProgressBar) findViewById(R.id.progressBar);
		initWebView();
		paymentWebView.setWebViewClient(new MyWebViewClient());
		WebSettings webSettings = paymentWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		paymentWebView.loadUrl(webUrl);
	}

	private void initWebView() {
		paymentWebView = (WebView) findViewById(R.id.paymentWebView);
		paymentWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (isChat) {
				if (url.equals("http://24bh.com/livehelp/offline.php?LANGUAGE=en")) {
					setResult(1);
					finish();
					return false;
				}
			}
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			progress.setVisibility(View.GONE);
			WebViewActivity.this.progress.setProgress(100);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			progress.setVisibility(View.VISIBLE);
			WebViewActivity.this.progress.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}
	}
}
