package com.token.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebView;

import com.token.util.ImageCacheManager;
import com.token.util.PreferenceUtil;

public class WalletApplication extends Application {
	private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
	private static Bitmap.CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100; // PNG is lossless so quality is ignored but must be provided
	ArrayList<HashMap<String, String>> TransactioninfoList;
	String accountbalance;
	String authenticateid_gString;
	String crediturl_mString;
	String device_id;
	ArrayList<HashMap<String, String>> expireList;
	ArrayList<HashMap<String, String>> infoList;
	ArrayList<HashMap<String, String>> loginList;
	ArrayList<HashMap<String, String>> notificationSaveList;
	ArrayList<HashMap<String, String>> paidList;
	String paymessage;
	String token;
	ArrayList<HashMap<String, String>> tokenList;
	public String tokencode;
	int trans_id_mInt;
	ArrayList<HashMap<String, String>> unpaidList;

	public WalletApplication() {
		this.accountbalance = "";
		this.tokencode = "";
		this.token = "";
		this.paymessage = "";
		this.crediturl_mString = "";
		this.device_id = "";
		this.trans_id_mInt = 0;
		this.loginList = new ArrayList();
		this.tokenList = new ArrayList();
		this.infoList = new ArrayList();
		this.TransactioninfoList = new ArrayList();
		this.notificationSaveList = new ArrayList();
		this.paidList = new ArrayList();
		this.unpaidList = new ArrayList();
		this.expireList = new ArrayList();
	}

	public String getAccountbalance() {
		return PreferenceUtil.getBalance(getApplicationContext());
	}

	public void setAccountbalance(String str) {
		PreferenceUtil.setBalance(getApplicationContext(), str);
	}

	public String getAuthenticateid_gString() {
		return this.authenticateid_gString;
	}

	public void setAuthenticateid_gString(String str) {
		this.authenticateid_gString = str;
	}

	public String getCrediturl_mString() {
		return this.crediturl_mString;
	}

	public void setCrediturl_mString(String str) {
		this.crediturl_mString = str;
	}

	public String getDevice_id() {
		return this.device_id;
	}

	public void setDevice_id(String str) {
		this.device_id = str;
	}

	public ArrayList<HashMap<String, String>> getExpireList() {
		return PreferenceUtil.getExpiredTrans(getApplicationContext());
	}

	public void setExpireList(ArrayList<HashMap<String, String>> arrayList) {
		PreferenceUtil.saveExpiredTrans(getApplicationContext(), arrayList);
	}

	public ArrayList<HashMap<String, String>> getInfoList() {
		return PreferenceUtil.getInfoList(this);
	}

	public void setInfoList(ArrayList<HashMap<String, String>> arrayList) {
		PreferenceUtil.saveInfoList(this, arrayList);
	}

	public ArrayList<HashMap<String, String>> getLoginList() {
		return this.loginList;
	}

	public void setLoginList(ArrayList<HashMap<String, String>> arrayList) {
		this.loginList = arrayList;
	}

	public ArrayList<HashMap<String, String>> getPaidList() {
		return PreferenceUtil.getPaidTrans(getApplicationContext());
	}

	public void setPaidList(ArrayList<HashMap<String, String>> arrayList) {
		PreferenceUtil.savePaidTrans(getApplicationContext(), arrayList);
	}

	public String getPaymessage() {
		return this.paymessage;
	}

	public void setPaymessage(String str) {
		this.paymessage = str;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String str) {
		this.token = str;
	}

	public ArrayList<HashMap<String, String>> getTokenList() {
		return this.tokenList;
	}

	public void setTokenList(ArrayList<HashMap<String, String>> arrayList) {
		this.tokenList = arrayList;
	}

	public String getTokencode() {
		return this.tokencode;
	}

	public void setTokencode(String str) {
		this.tokencode = str;
	}

	public int getTrans_id_mInt() {
		return this.trans_id_mInt;
	}

	public void setTrans_id_mInt(int i) {
		this.trans_id_mInt = i;
	}

	public ArrayList<HashMap<String, String>> getTransactioninfoList() {
		return this.TransactioninfoList;
	}

	public void setTransactioninfoList(ArrayList<HashMap<String, String>> arrayList) {
		this.TransactioninfoList = arrayList;
	}

	public ArrayList<HashMap<String, String>> getUnpaidList() {
		return PreferenceUtil.getUnpaidTrans(getApplicationContext());
	}

	public void setUnpaidList(ArrayList<HashMap<String, String>> arrayList) {
		PreferenceUtil.saveUnpaidTrans(getApplicationContext(), arrayList);
	}

	private void createImageCache() {
		ImageCacheManager.getInstance().init(this,
				this.getPackageCodePath()
				, DISK_IMAGECACHE_SIZE
				, DISK_IMAGECACHE_COMPRESS_FORMAT
				, DISK_IMAGECACHE_QUALITY);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		createImageCache();
		setupWebviewDebugging();
	}

	private void setupWebviewDebugging() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
				WebView.setWebContentsDebuggingEnabled(true);
			}
		}
	}
}
