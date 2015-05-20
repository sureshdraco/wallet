package com.token.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class Validations {
	static Bitmap bmImg;

	public static boolean checkEmail(String str) {
		return Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+").matcher(str).matches();
	}

	public static Boolean checkNetwork(Context context) {
		NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		return (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) ? (activeNetworkInfo == null || !(activeNetworkInfo.getState() == State.DISCONNECTED
				|| activeNetworkInfo.getState() == State.DISCONNECTING || activeNetworkInfo.getState() == State.SUSPENDED || activeNetworkInfo.getState() == State.UNKNOWN)) ? Boolean
				.valueOf(false)
				: Boolean.valueOf(false)
				: Boolean.valueOf(true);
	}

	public static Bitmap downloadFile(String str) {
		URL url;
		try {
			url = new URL(str);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			url = null;
		}
		try {
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.connect();
			Options options = new Options();
			bmImg = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
			bmImg = Bitmap.createScaledBitmap(bmImg, 210, 210, true);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return bmImg;
	}
}
