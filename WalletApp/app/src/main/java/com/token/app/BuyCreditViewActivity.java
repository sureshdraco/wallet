package com.token.app;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class BuyCreditViewActivity extends Activity {
	public static final int progress_bar_type = 0;
	public String FILEDIRECTORY;
	String Webview_url;
	WebView browser;
	File f;
	Global global;
	File pdfDir;
	ProgressBar progressBar;
	private ProgressDialog pDialog;

	public BuyCreditViewActivity() {
		this.Webview_url = "";
		this.FILEDIRECTORY = "/sdcard/TenantReports/";
	}

	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.buycredit_webview);
		Log.i("TAG", getClass().getCanonicalName());
		this.global = (Global) getApplicationContext();
		this.browser = (WebView) findViewById(R.id.web_view);
		this.progressBar = (ProgressBar) findViewById(R.id.pb);
		this.browser.getSettings().setJavaScriptEnabled(true);
		this.Webview_url = this.global.getCrediturl_mString();
		this.progressBar.setVisibility(View.VISIBLE);
		Log.e("linkname", "else....." + this.Webview_url);
		this.progressBar = (ProgressBar) findViewById(R.id.pb);
		this.browser.setWebViewClient(new MyBrowser());
		this.browser.loadUrl(this.Webview_url);
	}

	protected Dialog onCreateDialog(int i) {
		switch (i) {
		case R.styleable.MapAttrs_mapType /* 0 */:
			this.pDialog = new ProgressDialog(this);
			this.pDialog.setMessage("Loading data. Please wait...");
			this.pDialog.setIndeterminate(false);
			this.pDialog.setMax(100);
			this.pDialog.setProgressStyle(1);
			this.pDialog.setCancelable(true);
			this.pDialog.show();
			return this.pDialog;
		default:
			return null;
		}
	}

	class DownloadPdfFileFromURL extends AsyncTask<String, String, String> {
		DownloadPdfFileFromURL() {
		}

		protected String doInBackground(String... strArr) {
			try {
				URL url = new URL(strArr[0]);
				URLConnection openConnection = url.openConnection();
				openConnection.connect();
				int contentLength = openConnection.getContentLength();
				InputStream bufferedInputStream = new BufferedInputStream(url.openStream(), AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
				OutputStream fileOutputStream = new FileOutputStream("/sdcard/TenantReports/" + BuyCreditViewActivity.this.Webview_url);
				Log.v("OutputStream", fileOutputStream.toString());
				byte[] bArr = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
				long j = 0;
				while (true) {
					int read = bufferedInputStream.read(bArr);
					if (read == -1) {
						break;
					}
					j += (long) read;
					publishProgress(new String[] { String.valueOf((int) ((100 * j) / ((long) contentLength))) });
					fileOutputStream.write(bArr, 0, read);
				}
				fileOutputStream.flush();
				fileOutputStream.close();
				bufferedInputStream.close();
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
			return null;
		}

		protected void onPostExecute(String str) {
			BuyCreditViewActivity.this.dismissDialog(0);
		}

		protected void onPreExecute() {
			super.onPreExecute();
			BuyCreditViewActivity.this.showDialog(0);
		}

		protected void onProgressUpdate(String... strArr) {
			BuyCreditViewActivity.this.pDialog.setProgress(Integer.parseInt(strArr[0]));
		}
	}

	private class MyBrowser extends WebViewClient {
		private MyBrowser() {
		}

		public void onPageFinished(WebView webView, String str) {
			super.onPageFinished(webView, str);
			BuyCreditViewActivity.this.progressBar.setVisibility(View.GONE);
		}

		public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
			super.onPageStarted(webView, str, bitmap);
			BuyCreditViewActivity.this.progressBar.setVisibility(View.VISIBLE);
		}

		public boolean shouldOverrideUrlLoading(WebView webView, String str) {
			webView.loadUrl(str);
			BuyCreditViewActivity.this.Webview_url = str;
			return true;
		}
	}
}
