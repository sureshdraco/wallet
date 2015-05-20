package com.token.app;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by suresh on 13/10/14.
 */
public class PreferenceUtil {

	public static final String PREF_STORAGE_LOCATION = "WalletClientApp";
	private static final String TAG = PreferenceUtil.class.getSimpleName();
	private static final String NOTIFICATION_LIST = "notificationList";
	private static final String NOTIFICATION_COUNT = "notificationCount";

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

	public static void resetNotificationCount(Context context) {
		getPrefEditor(context).putInt(NOTIFICATION_COUNT, 0).commit();
	}
}
