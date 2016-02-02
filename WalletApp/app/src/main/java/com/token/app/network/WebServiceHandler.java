package com.token.app.network;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;

import com.google.android.gms.plus.PlusShare;
import com.token.app.BuildConfig;
import com.token.app.WalletApplication;
import com.token.util.GlobalConstants;

public class WebServiceHandler {
	public static final int TIMEOUT = 9000;
	static ArrayList<HashMap<String, String>> expireList;
	static HashMap<String, String> expireMap;
	static WalletApplication global;
	static ArrayList<HashMap<String, String>> infoList;
	static HashMap<String, String> infoMap;
	static ArrayList<HashMap<String, String>> loginList;
	static HashMap<String, String> loginMap;
	static ArrayList<HashMap<String, String>> paidList;
	static HashMap<String, String> paidMap;
	static ArrayList<HashMap<String, String>> tokenDetalList;
	static HashMap<String, String> tokendetailmap;
	static ArrayList<HashMap<String, String>> transactionInfoList;
	static HashMap<String, String> transactionInfoMap;
	static ArrayList<HashMap<String, String>> unpaidList;
	static HashMap<String, String> unpaidMap;

	private static DefaultHttpClient getHttpsParams() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		try {
			schemeRegistry.register(new Scheme("https",
					new MySSLSocketFactory(KeyStore.getInstance(KeyStore.getDefaultType())), 443));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		HttpParams my_httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(my_httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(my_httpParams, TIMEOUT);
		SingleClientConnManager mgr = new SingleClientConnManager(my_httpParams, schemeRegistry);
		return new DefaultHttpClient(mgr, my_httpParams);
	}

	private static DefaultHttpClient getHttpParams() {
		HttpParams my_httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(my_httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(my_httpParams, TIMEOUT);
		return new DefaultHttpClient(my_httpParams);
	}

	public static String accountBalanceservice(Context context, String str, String password) {
		String str2;
		Exception e;
		String str3 = "";
		global = (WalletApplication) context.getApplicationContext();

		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/usersmaster/balanceinfo");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("email", str));
		arrayList.add(new BasicNameValuePair("password", password));
		Log.v("nv of AccountData", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			Log.e("Before response", ":::::::::::" + "");
			str2 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("result of balance API", str2);
				global.setAccountbalance(new JSONObject(str2).getString("credits"));
				return "true";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return str2;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str2 = str3;
			e = exception;
			e.printStackTrace();
			return str2;
		}
	}

	public static String buyAuthenticateService(Context context, String str, String str2) {
		String str3;
		Exception e;
		String str4 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/buycredit/authenticate");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("email", str));
		arrayList.add(new BasicNameValuePair("password", str2));
		Log.e("buyAuthenticateService", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			str3 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("buyAuthenticateService ", str3);
				JSONObject jSONObject = new JSONObject(str3);
				if (!jSONObject.getString("status").equalsIgnoreCase("true")) {
					return "false";
				}
				global.setAuthenticateid_gString(jSONObject.getString("user_id"));
				return "true";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return str3;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str3 = str4;
			e = exception;
			e.printStackTrace();
			return str3;
		}
	}

	public static String buycreditService(Context context, String str, String str2, String str3, String str4) {
		Exception e;
		String str5 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/buycredit/create");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("amount", str2));
		arrayList.add(new BasicNameValuePair("option", str4));
		arrayList.add(new BasicNameValuePair("user_id", str));
		arrayList.add(new BasicNameValuePair("auth", "1"));
		Log.e("nv of buycredit Service", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		String str6;
		try {
			str6 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("buyCredit Service ", str6);
				JSONObject jSONObject = new JSONObject(str6);
				if (!jSONObject.getString("status").equalsIgnoreCase("true")) {
					return "false";
				}
				global.setCrediturl_mString(jSONObject.getString(PlusShare.KEY_CALL_TO_ACTION_URL));
				return "true";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return str6;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str6 = str5;
			e = exception;
			e.printStackTrace();
			return str6;
		}
	}

	public static String forgotpasswordService(Context context, String str, String str2) {
		String str3;
		Exception e;
		String str4 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/usersmaster/forgetpassmethod");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("email", str));
		arrayList.add(new BasicNameValuePair("mobile", str2));
		Log.e("nv of forgot pwd", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			str3 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("result of forgot pwd", str3);
				str4 = new JSONObject(str3).getString("status");
				infoList = new ArrayList();
				return str4.equalsIgnoreCase("success") ? "true" : "false";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return str3;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str3 = str4;
			e = exception;
			e.printStackTrace();
			return str3;
		}
	}

	public static Response generatetokenservice(Context context, String str, String str2) {
		String str3;
		Exception e;
		String str4 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://walletgcc.com/wallet/public/transactionapi/create");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("creator", str));
		arrayList.add(new BasicNameValuePair("amount", str2));
		Log.v("nv of generate token", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			str3 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.v("result of gen Token", str3);
				global.setTokencode(new JSONObject(str3).getString("transaction_code"));
				global.setTransactionUrl(new JSONObject(str3).getString("transaction_url"));
				return new Response("", "true");
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return new Response(new JSONObject(str3).getString("error"), "false");
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str3 = str4;
			e = exception;
			e.printStackTrace();
			return new Response("", "false");
		}
	}

	public static String infoService(Context context, String str) {
		String str2;
		Exception e;
		String str3 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/usersmaster/companyinfo");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("email", str));
		Log.e("nv of company info", ":::::::::::::::::" + arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			str2 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("result of Company Info", str2);
				JSONObject jSONObject = new JSONObject(str2);
				String string = jSONObject.getString("status");
				infoList = new ArrayList();
				if (string.equalsIgnoreCase("success")) {
					infoMap = new HashMap();
					string = jSONObject.getString("company_name");
					String string2 = jSONObject.getString("location");
					String string3 = jSONObject.getString("phone");
					String string4 = jSONObject.getString("mobile");
					String string5 = jSONObject.getString("fax_no");
					String string6 = jSONObject.getString("email");
					String string7 = jSONObject.getString("country");
					str3 = jSONObject.getString("phone2");
					infoMap.put("company_name", string);
					infoMap.put("location", string2);
					infoMap.put("phone", string3);
					infoMap.put("mobile", string4);
					infoMap.put("fax_no", string5);
					infoMap.put("email", string6);
					infoMap.put("country", string7);
					infoMap.put("phone2", str3);
					infoList.add(infoMap);
				}
				global.setInfoList(infoList);
				return "true";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				Log.e("\u00cfnside catch block", "called" + e.toString());
				return str2;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str2 = str3;
			e = exception;
			e.printStackTrace();
			Log.e("\u00cfnside catch block", "called" + e.toString());
			return str2;
		}
	}

	public static String loginservice(Context context, String str, String str2) {
		Exception exception;
		String str3 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost(GlobalConstants.LOGIN_URL);
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair(GlobalConstants.LOGIN_USERNAME, str));
		arrayList.add(new BasicNameValuePair(GlobalConstants.LOGIN_PASSWORD, str2));
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		Log.e("namevalue of login data", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			String str4 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("result", str4);
				try {
					loginList = new ArrayList();
					try {
						JSONObject jSONObject = new JSONArray(str4).getJSONObject(0);
						loginMap = new HashMap();
						str3 = jSONObject.getString(GlobalConstants.LOGIN_ID);
						String string = jSONObject.getString(GlobalConstants.LOGIN_EMAIL);
						String string2 = jSONObject.getString(GlobalConstants.LOGIN_PASSWORD);
						str4 = jSONObject.getString(GlobalConstants.LOGIN_ACCOUNTNUMBER);
						loginMap.put(GlobalConstants.LOGIN_ID, str3);
						loginMap.put(GlobalConstants.LOGIN_EMAIL, string);
						loginMap.put(GlobalConstants.LOGIN_PASSWORD, string2);
						loginMap.put(GlobalConstants.LOGIN_ACCOUNTNUMBER, str4);
						loginList.add(loginMap);
						global.setLoginList(loginList);
						return "true";
					} catch (Exception e2) {
						str3 = "false";
						try {
							Log.e("Inside Catch", e2.toString());
							return "false";
						} catch (Exception e22) {
							Exception exception2 = e22;
							str4 = str3;
							exception = exception2;
							exception.printStackTrace();
							return str4;
						}
					}
				} catch (Exception e3) {
					exception = e3;
					exception.printStackTrace();
					return str4;
				}
			} catch (Exception e4) {
				return str4;
			}
		} catch (Exception e5) {
			return str3;
		}
	}

	public static String paymentservice(Context context, String str, String str2, String str3) {
		String str4;
		Exception e;
		String str5 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://walletgcc.com/wallet/public/transactionapi/payment");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("payer", str3));
		arrayList.add(new BasicNameValuePair("status", str2));
		arrayList.add(new BasicNameValuePair("code", str));
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		Log.v("nv of payment info", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			str4 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.v("result", str4);
				JSONObject jSONObject = new JSONObject(str4);
				if (!jSONObject.getString("status").equals("false")) {
					return "true";
				}
				global.setPaymessage(jSONObject.getString("error"));
				return "false";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return str4;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str4 = str5;
			e = exception;
			e.printStackTrace();
			return str4;
		}
	}

	public static String registerOnServer(Context context, String email, String password, String deviceId, String model, String option) {
		String str6;
		Exception e;
		String str7 = "";
		WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/notificationapi/create");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("email", email));
		arrayList.add(new BasicNameValuePair("password", password));
		arrayList.add(new BasicNameValuePair("device_id", deviceId));
		arrayList.add(new BasicNameValuePair("device_ip", ip));
		arrayList.add(new BasicNameValuePair("device_model", model));
		arrayList.add(new BasicNameValuePair("note", ""));
		arrayList.add(new BasicNameValuePair("option", option));
		Log.e("nv of register device", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			str6 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("result of Register dev", str6);
				JSONObject jSONObject = new JSONObject(str6);
				return "true";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return str6;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str6 = str7;
			e = exception;
			e.printStackTrace();
			return str6;
		}
	}

	public static String signupservice(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
		Exception e;
		String str7 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost(GlobalConstants.SIGNUP_URL);
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair(GlobalConstants.SIGNUP_FIRSTNAME, str));
		arrayList.add(new BasicNameValuePair(GlobalConstants.SIGNUP_LASTNAME, str2));
		arrayList.add(new BasicNameValuePair(GlobalConstants.SIGNUP_EMAIL, str4));
		arrayList.add(new BasicNameValuePair(GlobalConstants.SIGNUP_NUMBER, str5));
		arrayList.add(new BasicNameValuePair(GlobalConstants.SIGNUP_CIVILID, str6));
		arrayList.add(new BasicNameValuePair(GlobalConstants.SIGNUP_PASSWORD, str3));
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		Log.e("nv of Signup data", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		String str8;
		try {
			Log.v("Response", basicResponseHandler + "postMethod" + httpPost);
			str8 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.v("Response of Signup", str8);
				if (new JSONObject(str8).getString("success").equalsIgnoreCase("user information saved successfully")) {
					return "true";
				}
				Log.v("inside for", "else....");
				return "false";
			} catch (Exception e3) {
				e = e3;
				Log.v("Catch", e.toString());
				e.printStackTrace();
				return str8;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str8 = str7;
			e = exception;
			Log.v("Catch", e.toString());
			e.printStackTrace();
			return str8;
		}
	}

	private static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
				KeyManagementException, KeyStoreException,
				UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	public static Response tokenservice(Context context, String str) {
		String str2;
		Exception e;
		String str3 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://walletgcc.com/wallet/public/transactionapi/info");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("code", str));
		Log.v("namevalue of tokendata", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			str2 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.v("result", str2);
				JSONObject jSONObject = new JSONObject(str2);
				if (jSONObject.getString("status").equalsIgnoreCase("false")) {
					return new Response(jSONObject.getString("error"), "false");
				}
				tokenDetalList = new ArrayList();
				tokendetailmap = new HashMap();
				String string = jSONObject.getString(GlobalConstants.TOKEN_CREATOR);
				String string2 = jSONObject.getString(GlobalConstants.TOKEN_AMOUNT);
				String obj = "";
				JSONObject jSONObject2 = jSONObject.getJSONObject(GlobalConstants.TOKEN_CREATIONDATE);
				try {
					Log.v("creationdate", jSONObject2.toString());
					if (jSONObject2.has(GlobalConstants.TOKEN_DATE)) {
						obj = jSONObject2.getString(GlobalConstants.TOKEN_DATE);
						Log.v("date", obj);
					}
				} catch (Exception e3) {
					e3.printStackTrace();
				}
				String string3 = jSONObject.getString(GlobalConstants.TOKEN_COMPANYNAME);
				String string4 = jSONObject.getString(GlobalConstants.TOKEN_LOCATION);
				tokendetailmap.put(GlobalConstants.TOKEN_CREATOR, string);
				tokendetailmap.put(GlobalConstants.TOKEN_AMOUNT, string2);
				tokendetailmap.put(GlobalConstants.TOKEN_DATE, obj);
				tokendetailmap.put(GlobalConstants.TOKEN_COMPANYNAME, string3);
				tokendetailmap.put(GlobalConstants.TOKEN_LOCATION, string4);
				tokenDetalList.add(tokendetailmap);
				global.setTokenList(tokenDetalList);
				return new Response("", "true");
			} catch (Exception e4) {
				e = e4;
				e.printStackTrace();
				return new Response("", "false");
			}
		} catch (Exception e5) {
			Exception exception = e5;
			str2 = str3;
			e = exception;
			e.printStackTrace();
			return new Response("", "false");
		}
	}

	public static String transInfoService(Context context, String str, String str2) {
		String str3;
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/usersmaster/transaction");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("email", str));
		arrayList.add(new BasicNameValuePair("password", str2));
		Log.e("nv of transaction info", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			String str4 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			Log.e("result of trans info ", str4);
			transactionInfoList = new ArrayList();
			paidList = new ArrayList();
			unpaidList = new ArrayList();
			expireList = new ArrayList();
			JSONArray jSONArray = new JSONObject(str4).getJSONArray("transection_info");
			int i = 0;
			while (i < jSONArray.length()) {
				transactionInfoMap = new HashMap();
				paidMap = new HashMap();
				unpaidMap = new HashMap();
				expireMap = new HashMap();
				JSONObject jSONObject = jSONArray.getJSONObject(i);
				String string = jSONObject.getString("status");
				String string2;
				String string3;
				String string4;
				String string5;
				String string6;
				String string7;
				String string8 = "";
				String string9;
				String string10;
				String string11;
				String string12;
				if (string.equalsIgnoreCase("paid")) {
					string2 = jSONObject.getString("id");
					string3 = jSONObject.getString("transaction_user_id");
					string4 = jSONObject.getString("transaction_date");
					string5 = jSONObject.getString("transaction_code");
					string6 = jSONObject.getString("transaction_creator");
					string7 = jSONObject.getString("transaction_payer");
					str3 = "";
					try {
						string8 = jSONObject.getString("transaction_payment_date");
					} catch (Exception e2) {
						e2.printStackTrace();
						str4 = str3;
					}
					str3 = jSONObject.getString("transaction_amount");
					string9 = jSONObject.getString("percentage_amount");
					string10 = jSONObject.getString("paid_amount");
					string11 = jSONObject.getString("updated_at");
					string12 = jSONObject.getString("created_at");
					paidMap.put("id", string2);
					paidMap.put("transaction_user_id", string3);
					paidMap.put("transaction_date", string4);
					paidMap.put("transaction_code", string5);
					paidMap.put("transaction_creator", string6);
					paidMap.put("transaction_payer", string7);
					paidMap.put("transaction_payment_date", string8);
					paidMap.put("transaction_amount", str3);
					paidMap.put("percentage_amount", string9);
					paidMap.put("paid_amount", string10);
					paidMap.put("status", string);
					paidMap.put("updated_at", string11);
					paidMap.put("created_at", string12);
					paidList.add(paidMap);
				} else if (string.equalsIgnoreCase("Expired")) {
					string2 = jSONObject.getString("id");
					string3 = jSONObject.getString("transaction_user_id");
					string4 = jSONObject.getString("transaction_date");
					string5 = jSONObject.getString("transaction_code");
					string6 = jSONObject.getString("transaction_creator");
					string7 = jSONObject.getString("transaction_payer");
					str3 = "";
					try {
						string8 = jSONObject.getString("transaction_payment_date");
					} catch (Exception e22) {
						e22.printStackTrace();
						str4 = str3;
					}
					str3 = jSONObject.getString("transaction_amount");
					string9 = jSONObject.getString("percentage_amount");
					string10 = jSONObject.getString("paid_amount");
					string11 = jSONObject.getString("updated_at");
					string12 = jSONObject.getString("created_at");
					expireMap.put("id", string2);
					expireMap.put("transaction_user_id", string3);
					expireMap.put("transaction_date", string4);
					expireMap.put("transaction_code", string5);
					expireMap.put("transaction_creator", string6);
					expireMap.put("transaction_payer", string7);
					expireMap.put("transaction_payment_date", string8);
					expireMap.put("transaction_amount", str3);
					expireMap.put("percentage_amount", string9);
					expireMap.put("paid_amount", string10);
					expireMap.put("status", string);
					expireMap.put("updated_at", string11);
					expireMap.put("created_at", string12);
					expireList.add(expireMap);
				} else {
					string2 = jSONObject.getString("id");
					string3 = jSONObject.getString("transaction_user_id");
					string4 = jSONObject.getString("transaction_date");
					string5 = jSONObject.getString("transaction_code");
					string6 = jSONObject.getString("transaction_creator");
					string7 = jSONObject.getString("transaction_payer");
					str3 = "";
					try {
						string8 = jSONObject.getString("transaction_payment_date");
					} catch (Exception e222) {
						e222.printStackTrace();
						str4 = str3;
					}
					str3 = jSONObject.getString("transaction_amount");
					string9 = jSONObject.getString("percentage_amount");
					string10 = jSONObject.getString("paid_amount");
					string11 = jSONObject.getString("updated_at");
					string12 = jSONObject.getString("created_at");
					unpaidMap.put("id", string2);
					unpaidMap.put("transaction_user_id", string3);
					unpaidMap.put("transaction_date", string4);
					unpaidMap.put("transaction_code", string5);
					unpaidMap.put("transaction_creator", string6);
					unpaidMap.put("transaction_payer", string7);
					unpaidMap.put("transaction_payment_date", string8);
					unpaidMap.put("transaction_amount", str3);
					unpaidMap.put("percentage_amount", string9);
					unpaidMap.put("paid_amount", string10);
					unpaidMap.put("status", string);
					unpaidMap.put("updated_at", string11);
					unpaidMap.put("created_at", string12);
					unpaidList.add(unpaidMap);
				}
				transactionInfoList.add(transactionInfoMap);
				i++;
				str4 = "true";
			}
			global.setTransactioninfoList(transactionInfoList);
			global.setPaidList(paidList);
			global.setUnpaidList(unpaidList);
			global.setExpireList(expireList);
			Log.e("result of paid trans ", "::::" + global.getPaidList());
			Log.e("result of unpaid trans ", global.getUnpaidList().toString());
			Log.e("result of expire trans ", global.getExpireList().toString());
			return "true";
		} catch (Exception e2222) {
			Log.e("getting Exception ", e2222.toString());
			e2222.printStackTrace();
			return "false";
		}
	}

	public static String withdrawalService(Context context, String str, String str2) {
		String str3;
		Exception e;
		String str4 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/withdrawapi/create");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("withdrawer", str));
		arrayList.add(new BasicNameValuePair("amount", str2));
		Log.e("namevalue of witdraw", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		try {
			str3 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("result of withdraw", str3);
				str4 = new JSONObject(str3).getString("status");
				infoList = new ArrayList();
				return str4.equalsIgnoreCase("true") ? "true" : "false";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return str3;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str3 = str4;
			e = exception;
			e.printStackTrace();
			return str3;
		}
	}

	public static String resetPasswordService(Context context, String email, String old, String newPwd, String confirmPwd) {
		Exception e;
		String str5 = "";
		global = (WalletApplication) context.getApplicationContext();
		DefaultHttpClient defaultHttpClient = getHttpsParams();
		ResponseHandler basicResponseHandler = new BasicResponseHandler();
		HttpPost httpPost = new HttpPost("https://www.walletgcc.com/wallet/public/usersmaster/resetpassword");
		List arrayList = new ArrayList();
		arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
		arrayList.add(new BasicNameValuePair("email", email));
		arrayList.add(new BasicNameValuePair("oldpassword", old));
		arrayList.add(new BasicNameValuePair("newpassword", newPwd));
		arrayList.add(new BasicNameValuePair("confirmpassword", confirmPwd));
		if (BuildConfig.DEBUG) Log.d("nv of reset pwd serv", arrayList.toString());
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		String str6;
		try {
			str6 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
			try {
				Log.e("result of reset pwd ser", str6);
				JSONObject jSONObject = new JSONObject(str6);
				if (!jSONObject.getString("status").equalsIgnoreCase("true")) {
					return "false";
				}
				return "true";
			} catch (Exception e3) {
				e = e3;
				e.printStackTrace();
				return str6;
			}
		} catch (Exception e4) {
			Exception exception = e4;
			str6 = str5;
			e = exception;
			e.printStackTrace();
			return str6;
		}
	}

	public static String transactionReport(Context context, String email, String password, String startDate, String endDate) {

		try {
			URL url = new URL("https://www.walletgcc.com/wallet/public/transactionapi/report");
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			List arrayList = new ArrayList();
			arrayList.add(new BasicNameValuePair("email", email));
			arrayList.add(new BasicNameValuePair("password", password));
			arrayList.add(new BasicNameValuePair("start_date", startDate));
			arrayList.add(new BasicNameValuePair("end_date", endDate));
			Log.d("trans", arrayList.toString());
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(arrayList));
			writer.flush();
			writer.close();
			os.close();

			conn.connect();
			InputStream inputStream = conn.getInputStream();
			FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/wallet_transaction_report.pdf");
			int totalSize = conn.getContentLength();

			byte[] buffer = new byte[1024 * 1024];
			int bufferLength = 0;
			while ((bufferLength = inputStream.read(buffer)) > 0) {
				fileOutputStream.write(buffer, 0, bufferLength);
			}
			fileOutputStream.close();
			return "true";
		} catch (Exception ex) {
			Log.e("report", ex.toString());
			return "false";
		}

	}

	private static String getQuery(List<BasicNameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}
}