package com.token.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by suresh on 13/10/14.
 */
public class PreferenceUtil {

    public static final String PREF_STORAGE_LOCATION = "WalletClientApp";
    private static final String TAG = PreferenceUtil.class.getSimpleName();
    private static final String NOTIFICATION_LIST = "notificationList";
    private static final String NOTIFICATION_COUNT = "notificationCount";
    private static final String WALLET_UNPAID_TRANS = "wallet_unpaid_trans";
    private static final String WALLET_PAID_TRANS = "wallet_paid_trans";
    private static final String WALLET_EXPIRED_TRANS = "wallet_expired_trans";
    private static final String BALANCE = "balance";
    private static final String WALLET_INFO_LIST = "login_list";

    // Preference backend access.
    private static SharedPreferences.Editor prefEditor;

    /**
     * Gets the shared preferences to use
     */
    public static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(PREF_STORAGE_LOCATION, 0);
    }

    private static SharedPreferences.Editor getPrefEditor(Context context) {
        return getPref(context).edit();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    public static String getNotificationList(Context context) {
        return getPref(context).getString("notification", "[]");
    }

    public static void saveNotificationsList(Context context, String notificationsLsit) {
        getPrefEditor(context).putString("notification", notificationsLsit).commit();
    }

    public static int getIncrementedNotificationCount(Context context) {
        int count = getPref(context).getInt(NOTIFICATION_COUNT, 0);
        getPrefEditor(context).putInt(NOTIFICATION_COUNT, ++count).commit();
        return count;
    }

    public static String getBalance(Context context) {
        return getPref(context).getString(BALANCE, "");
    }

    public static void setBalance(Context context, String balance) {
        getPrefEditor(context).putString(BALANCE, balance).commit();
    }

    public static void resetNotificationCount(Context context) {
        getPrefEditor(context).putInt(NOTIFICATION_COUNT, 0).commit();
    }

    public static ArrayList<HashMap<String, String>> getUnpaidTrans(Context context) {
        String orders = getPref(context).getString(WALLET_UNPAID_TRANS, "[]");
        Type listType = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();
        return new Gson().fromJson(orders, listType);
    }

    public static void saveUnpaidTrans(Context context, ArrayList<HashMap<String, String>> unpaid) {
        getPrefEditor(context).putString(WALLET_UNPAID_TRANS, new Gson().toJson(unpaid)).commit();
    }

    public static ArrayList<HashMap<String, String>> getPaidTrans(Context context) {
        String orders = getPref(context).getString(WALLET_PAID_TRANS, "[]");
        Type listType = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();
        return new Gson().fromJson(orders, listType);
    }

    public static void savePaidTrans(Context context, ArrayList<HashMap<String, String>> paid) {
        getPrefEditor(context).putString(WALLET_PAID_TRANS, new Gson().toJson(paid)).commit();
    }

    public static ArrayList<HashMap<String, String>> getInfoList(Context context) {
        String orders = getPref(context).getString(WALLET_INFO_LIST, "[]");
        Type listType = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();
        return new Gson().fromJson(orders, listType);
    }

    public static void saveInfoList(Context context, ArrayList<HashMap<String, String>> paid) {
        getPrefEditor(context).putString(WALLET_INFO_LIST, new Gson().toJson(paid)).commit();
    }

    public static ArrayList<HashMap<String, String>> getExpiredTrans(Context context) {
        String orders = getPref(context).getString(WALLET_EXPIRED_TRANS, "[]");
        Type listType = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();
        return new Gson().fromJson(orders, listType);
    }

    public static void saveExpiredTrans(Context context, ArrayList<HashMap<String, String>> paid) {
        getPrefEditor(context).putString(WALLET_EXPIRED_TRANS, new Gson().toJson(paid)).commit();
    }
}
