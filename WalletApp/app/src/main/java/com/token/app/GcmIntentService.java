package com.token.app;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.PlusShare;
import com.token.util.GlobalConstants;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class GcmIntentService extends IntentService {
    public static int count;
    public static int i;
    String TAG;
    Builder builder;
    Context c;
    String date_mString;
    Editor editor;
    Global global;
    String item_id;
    String item_name;
    private NotificationManager mNotificationManager;
    String message_mString;
    String msg;
    String msgcount;
    ArrayList<HashMap<String, String>> notification_mArrayList;
    HashMap<String, String> notification_mHashmap;
    String notification_mIcon;
    boolean notify;
    String post_id;
    String sender_id;
    String sender_name;
    String senderid;
    String sms;
    String sound;
    String sounditem;
    SharedPreferences sp;
    String title;
    String title_mString;

    public GcmIntentService() {
        super("diesel-horizon-739");
        this.TAG = "Log";
        this.notification_mArrayList = new ArrayList();
        this.notification_mHashmap = new HashMap();
    }

    private void sendNotification(String str, String str2, String str3) {
        Log.e("Icon Url", "..." + str2);
        Uri defaultUri = RingtoneManager.getDefaultUri(2);
        String trim = str2.trim();
        Log.e("Icon Url", "after..." + trim);
        this.mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(this, MainTabActivity.class), 0);
        Builder sound = new Builder(this).setSmallIcon(R.drawable.ic_attach_money_white_48dp).setLargeIcon(getBitmapFromURL(trim)).setContentTitle(str3).setAutoCancel(true).setStyle(new BigTextStyle().bigText(str)).setContentText(str).setNumber(i).setSound(defaultUri);
        if (count == 1) {
            Log.e("count", "..." + count);
            count++;
        } else {
            i++;
        }
        Notification build = sound.build();
        build.number += i;
        Log.e("Notification count", String.valueOf(i));
        sound.setContentIntent(activity);
        this.editor = this.sp.edit();
        this.editor.putBoolean("notify", true);
        this.editor.commit();
        this.notification_mHashmap.put("image", trim);
        this.notification_mHashmap.put(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE, str3);
        this.notification_mHashmap.put("notmsg", str);
        this.notification_mHashmap.put("date", this.date_mString);
        this.notification_mArrayList.add(this.notification_mHashmap);
        this.global.setNotificationList(this.notification_mArrayList);
        Log.e("Array List count", String.valueOf(this.global.getNotificationList().size()));
        Log.e("notification_mArrayList", String.valueOf(this.notification_mArrayList.size()));
        this.mNotificationManager.notify(1, sound.build());
    }

    public Bitmap getBitmapFromURL(String str) {
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

    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        this.global = (Global) getApplicationContext();
        this.sp = getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
        Log.e("extras", "oncreate" + extras.toString());
        String messageType = GoogleCloudMessaging.getInstance(this).getMessageType(intent);
        if (!(extras.isEmpty() || GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType) || GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType) || !GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))) {
            this.date_mString = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            if (extras.containsKey("message")) {
                try {
                    this.message_mString = extras.getString("message");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    this.notification_mIcon = extras.getString("icon");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    this.title_mString = extras.getString(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE);
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            }
            sendNotification(this.message_mString, this.notification_mIcon, this.title_mString);
        }
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}
