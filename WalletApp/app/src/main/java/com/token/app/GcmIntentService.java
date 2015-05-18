package com.token.app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.PlusShare;
import com.token.util.GlobalConstants;

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
            NotificationUtil.sendNotification(getApplicationContext(), this.title_mString, this.message_mString, NotificationUtil.getBitmapFromURL(this.notification_mIcon));
            NotificationUtil.cacheNotification(getApplicationContext(), this.title_mString, this.message_mString, this.notification_mIcon, "");
        }
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}
