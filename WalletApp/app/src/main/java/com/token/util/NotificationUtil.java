package com.token.util;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.token.app.NotificationItem;
import com.token.app.R;
import com.token.app.view.MainTabActivity;

/**
 * Created by suresh on 19/10/14.
 */
public class NotificationUtil {
	private static final int NOTIFICATION_ID = 1;
	private static Gson gson = new Gson();

	public static void cacheNotification(Context context, String title, String message, String imageUrl, String fullMessage) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TimestampUtil.FAST_BIRD_DATE_FORMAT);
		Date date = new Date();
		NotificationItem notificationItem = new NotificationItem(message, simpleDateFormat.format(date), title, imageUrl, fullMessage);
		Type listType = new TypeToken<ArrayList<NotificationItem>>() {
		}.getType();
		ArrayList<NotificationItem> notificationItemArrayList = gson.fromJson(PreferenceUtil.getNotificationList(context), listType);
		CircularFifoQueue<NotificationItem> queue = new CircularFifoQueue<NotificationItem>(10);
		Collections.reverse(notificationItemArrayList);
		notificationItemArrayList.add(notificationItem);
		queue.addAll(notificationItemArrayList);
		notificationItemArrayList = new ArrayList<>(queue);
		Collections.reverse(notificationItemArrayList);
		PreferenceUtil.saveNotificationsList(context, gson.toJson(notificationItemArrayList));
		LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("NOTIFICATIONS_UPDATED_BROADCAST"));
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	public static void sendNotification(Context context, String title, String msg, Bitmap icon) {
		Uri defaultUri = RingtoneManager.getDefaultUri(2);

		NotificationManager mNotificationManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent mainActivityIntent = new Intent(context, MainTabActivity.class);
		mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mainActivityIntent.putExtra("notifications", true);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(context)
						.setContentIntent(contentIntent)
						.setContentTitle(title)
						.setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
						.setSmallIcon(R.drawable.icon)
						.setAutoCancel(true).setSound(defaultUri)
						.setContentText(msg);
		if (icon != null) {
			mBuilder.setLargeIcon(icon);
		}
		int notifCount = PreferenceUtil.getIncrementedNotificationCount(context);
		if (notifCount > 1) {
			mBuilder.setNumber(notifCount);
		}
		LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("NOTIFICATIONS_UPDATED_BROADCAST"));

		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	public static void clearNotifications(Context context) {
		NotificationManager mNotificationManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
	}

	public static Bitmap getBitmapFromURL(String str) {
		try {
			HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.connect();
			return BitmapFactory.decodeStream(httpURLConnection.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}