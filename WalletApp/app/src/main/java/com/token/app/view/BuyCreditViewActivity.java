package com.token.app.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.token.app.R;
import com.token.app.WalletApplication;

import java.io.File;

public class BuyCreditViewActivity extends Activity {
    public static final int progress_bar_type = 0;
    public String FILEDIRECTORY;
    String Webview_url;
    WebView browser;
    File f;
    WalletApplication global;
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
        this.global = (WalletApplication) getApplicationContext();
        this.browser = (WebView) findViewById(R.id.web_view);
        this.progressBar = (ProgressBar) findViewById(R.id.pb);
        this.browser.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            this.browser.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        this.Webview_url = this.global.getCrediturl_mString();
        this.progressBar.setVisibility(View.VISIBLE);
        Log.e("linkname", "else....." + this.Webview_url);
        this.progressBar = (ProgressBar) findViewById(R.id.pb);
        this.browser.setWebViewClient(new MyBrowser());
        this.browser.setWebChromeClient(new WebChromeClient());
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

    private class MyBrowser extends WebViewClient {
        private final String TAG = MyBrowser.class.getSimpleName();

        private MyBrowser() {
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            Log.d(TAG, str);
            BuyCreditViewActivity.this.progressBar.setVisibility(View.GONE);
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            BuyCreditViewActivity.this.progressBar.setVisibility(View.VISIBLE);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            Log.d(TAG, str);
            BuyCreditViewActivity.this.Webview_url = str;
            return true;
        }
    }
}