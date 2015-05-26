package com.token.app;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.google.android.gms.plus.PlusShare;
import com.token.util.GlobalConstants;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebServiceHandler {
    static ArrayList<HashMap<String, String>> expireList;
    static HashMap<String, String> expireMap;
    static Global global;
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

    public static String accountBalanceservice(Context context, String str) {
        String str2;
        Exception e;
        String str3 = "";
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://walletgcc.com/wallet/public/usersmaster/balanceinfo");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("email", str));
        Log.v("namevalue of AccountData", arrayList.toString());
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://www.walletgcc.com/wallet/public/buycredit/authenticate");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
        arrayList.add(new BasicNameValuePair("email", str));
        arrayList.add(new BasicNameValuePair("password", str2));
        Log.e("namevalue of buyAuthenticateService", arrayList.toString());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        try {
            str3 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
            try {
                Log.e("result of buyAuthenticateService ", str3);
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://www.walletgcc.com/wallet/public/buycredit/create");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
        arrayList.add(new BasicNameValuePair("amount", str2));
        arrayList.add(new BasicNameValuePair("option", str4));
        arrayList.add(new BasicNameValuePair("user_id", str));
        arrayList.add(new BasicNameValuePair("auth", "1"));
        Log.e("namevalue of buycredit Service", arrayList.toString());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        String str6;
        try {
            str6 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
            try {
                Log.e("result of buyCredit Service ", str6);
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://www.walletgcc.com/wallet/public/usersmaster/forgetpassmethod");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
        arrayList.add(new BasicNameValuePair("email", str));
        arrayList.add(new BasicNameValuePair("mobile", str2));
        Log.e("namevalue of forgot password", arrayList.toString());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        try {
            str3 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
            try {
                Log.e("result of forgot password ", str3);
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

    public static String generatetokenservice(Context context, String str, String str2) {
        String str3;
        Exception e;
        String str4 = "";
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://walletgcc.com/wallet/public/transactionapi/create");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("creator", str));
        arrayList.add(new BasicNameValuePair("amount", str2));
        Log.v("namevalue of generate token  info", arrayList.toString());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        try {
            str3 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
            try {
                Log.v("result of Generate Token", str3);
                global.setTokencode(new JSONObject(str3).getString("transaction_code"));
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

    public static String infoService(Context context, String str) {
        String str2;
        Exception e;
        String str3 = "";
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://www.walletgcc.com/wallet/public/usersmaster/companyinfo");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
        arrayList.add(new BasicNameValuePair("email", str));
        Log.e("namevalue of company  info", ":::::::::::::::::" + arrayList.toString());
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://walletgcc.com/wallet/public/transactionapi/payment");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("payer", str3));
        arrayList.add(new BasicNameValuePair("status", str2));
        arrayList.add(new BasicNameValuePair("code", str));
        Log.v("namevalue of payment info", arrayList.toString());
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://www.walletgcc.com/wallet/public/notificationapi/create");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
        arrayList.add(new BasicNameValuePair("email", email));
        arrayList.add(new BasicNameValuePair("password", password));
        arrayList.add(new BasicNameValuePair("device_id", deviceId));
        arrayList.add(new BasicNameValuePair("device_ip", ip));
        arrayList.add(new BasicNameValuePair("device_model", model));
        arrayList.add(new BasicNameValuePair("note", ""));
        arrayList.add(new BasicNameValuePair("option", option));
        Log.e("namevalue of register device", arrayList.toString());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        try {
            str6 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
            try {
                Log.e("result of Register device on server ", str6);
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
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
        Log.e("namevalue of Signup data", arrayList.toString());
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

    public static String tokenservice(Context context, String str) {
        String str2;
        Exception e;
        String str3 = "";
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://walletgcc.com/wallet/public/transactionapi/info");
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
                    return "false";
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
                return "true";
            } catch (Exception e4) {
                e = e4;
                e.printStackTrace();
                return str2;
            }
        } catch (Exception e5) {
            Exception exception = e5;
            str2 = str3;
            e = exception;
            e.printStackTrace();
            return str2;
        }
    }

    public static String transInfoService(Context context, String str, String str2) {
        String str3;
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://www.walletgcc.com/wallet/public/usersmaster/transaction");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
        arrayList.add(new BasicNameValuePair("email", str));
        arrayList.add(new BasicNameValuePair("password", str2));
        Log.e("namevalue of getting transaction info", arrayList.toString());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            String str4 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
            Log.e("result of getting transaction info ", str4);
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
            Log.e("result of paid transaction ", "::::" + global.getPaidList());
            Log.e("result of unpaid transaction ", global.getUnpaidList().toString());
            Log.e("result of expire transaction ", global.getExpireList().toString());
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://www.walletgcc.com/wallet/public/withdrawapi/create");
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
                Log.e("result of withdraw server ", str3);
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
        global = (Global) context.getApplicationContext();
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
        ResponseHandler basicResponseHandler = new BasicResponseHandler();
        HttpPost httpPost = new HttpPost("http://www.walletgcc.com/wallet/public/usersmaster/resetpassword");
        List arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("_token", "faa8cca04d3234b759203g08dc22afdb2"));
        arrayList.add(new BasicNameValuePair("email", email));
        arrayList.add(new BasicNameValuePair("oldpassword", old));
        arrayList.add(new BasicNameValuePair("newpassword", newPwd));
        arrayList.add(new BasicNameValuePair("confirmpassword", confirmPwd));
        if (BuildConfig.DEBUG) Log.d("namevalue of reset password Service", arrayList.toString());
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        String str6;
        try {
            str6 = (String) defaultHttpClient.execute(httpPost, basicResponseHandler);
            try {
                Log.e("result of reset password Service ", str6);
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
}