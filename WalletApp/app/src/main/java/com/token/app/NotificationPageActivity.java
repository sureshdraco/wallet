package com.token.app;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.plus.PlusShare;
import com.token.util.GlobalConstants;
import com.token.util.Utils;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NotificationPageActivity extends ActivityInTab {
    private static final int DATE_DIALOG_ID = 1;
    NotiifcationListAdapter adapter;
    TextView clear_mTextView;
    private OnDateSetListener datePickerListener;
    public int day;
    Global global;
    String image_mString;
    String message_mString;
    public int month;
    boolean notify_mBoolean;
    ImageView notify_mImageView;
    ListView notiifcation_mListView;
    SharedPreferences sp;
    String title_mString;
    public int year;

    public class NotiifcationListAdapter extends BaseAdapter {
        Context c;
        Global global;
        LayoutInflater mInflater;
        String[] names;
        ArrayList<HashMap<String, String>> notifylist;
        SharedPreferences sp;

        public NotiifcationListAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
            this.c = context;
            this.notifylist = arrayList;
            this.sp = context.getSharedPreferences(GlobalConstants.PREF, 0);
            this.global = (Global) context.getApplicationContext();
            this.mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return this.notifylist.size();
        }

        public Object getItem(int i) {
            return this.notifylist.get(i);
        }

        public long getItemId(int i) {
            return 0;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = this.mInflater.inflate(R.layout.notiifcation_listitem, null);
                viewHolder.notify_mImageView = (ImageView) view.findViewById(R.id.notification_imageView);
                viewHolder.date_mTextView = (TextView) view.findViewById(R.id.notify_date_tv);
                viewHolder.title_mTextView = (TextView) view.findViewById(R.id.notify_title_tv);
                viewHolder.message_mTextView = (TextView) view.findViewById(R.id.notify_message_tv);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            Calendar instance = Calendar.getInstance();
            NotificationPageActivity.this.year = instance.get(Calendar.YEAR);
            NotificationPageActivity.this.month = instance.get(Calendar.MONTH);
            NotificationPageActivity.this.day = instance.get(Calendar.DAY_OF_MONTH);
            NotificationPageActivity.this.notify_mBoolean = this.sp.getBoolean("notify", false);
            Log.e("Notifylist", ":::::::::" + this.notifylist);
            Log.e("Notiifcation data count", ":::::" + this.global.getNotificationList().size());
            if (NotificationPageActivity.this.notify_mBoolean) {
                NotificationPageActivity.this.image_mString = (String) ((HashMap) this.notifylist.get(i)).get("image");
                NotificationPageActivity.this.title_mString = (String) ((HashMap) this.notifylist.get(i)).get(PlusShare.KEY_CONTENT_DEEP_LINK_METADATA_TITLE);
                NotificationPageActivity.this.message_mString = (String) ((HashMap) this.notifylist.get(i)).get("notmsg");
                String str = (String) ((HashMap) this.notifylist.get(i)).get("date");
                String str2 = "";
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
                    Log.e("Date for Compare", str);
                    long time = simpleDateFormat.parse(str).getTime() / 1000;
                    Log.e("long outpute", String.valueOf(time));
                    time = Long.parseLong(Long.toString(time)) * 1000;
                    Log.e("long timestamp", String.valueOf(time));
                    str = Utils.getFriendlyTime(new Date(time));
                } catch (Exception e) {
                    e.printStackTrace();
                    str = str2;
                }
                viewHolder.notify_mImageView.setImageBitmap(NotificationPageActivity.this.getBitmapFromURL(NotificationPageActivity.this.image_mString));
                viewHolder.title_mTextView.setText(NotificationPageActivity.this.title_mString);
                viewHolder.message_mTextView.setText(NotificationPageActivity.this.message_mString);
                viewHolder.date_mTextView.setText(str);
            }
            return view;
        }
    }

    public NotificationPageActivity() {
        this.datePickerListener = new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                NotificationPageActivity.this.year = i;
                NotificationPageActivity.this.month = i2;
                NotificationPageActivity.this.day = i3;
                String valueOf = String.valueOf(NotificationPageActivity.this.month + NotificationPageActivity.DATE_DIALOG_ID);
                if (valueOf.length() == NotificationPageActivity.DATE_DIALOG_ID) {
//                    "0" + valueOf;
                }
            }
        };
    }

    public static String getFriendlyTime(Date date) {
        StringBuffer stringBuffer = new StringBuffer();
        long time = (Calendar.getInstance().getTime().getTime() - date.getTime()) / 1000;
        Log.e("time format 1", "::::" + time);
        long j = time >= 60 ? time % 60 : time;
        long j2 = time / 60;
        time = j2 >= 60 ? j2 % 60 : j2;
        long j3 = j2 / 60;
        j2 = j3 >= 24 ? j3 % 24 : j3;
        long j4 = j3 / 24;
        j3 = j4 >= 30 ? j4 % 30 : j4;
        long j5 = j4 / 30;
        j4 = j5 >= 12 ? j5 % 12 : j5;
        j5 /= 12;
        if (j5 > 0) {
            if (j5 == 1) {
                stringBuffer.append("a year");
            } else {
                stringBuffer.append(new StringBuilder(String.valueOf(j5)).append(" years").toString());
            }
            if (j5 <= 6 && j4 > 0) {
                if (j4 == 1) {
                    stringBuffer.append(" and a month");
                } else {
                    stringBuffer.append(" and " + j4 + " months");
                }
            }
        } else if (j4 > 0) {
            if (j4 == 1) {
                stringBuffer.append("a month");
            } else {
                stringBuffer.append(new StringBuilder(String.valueOf(j4)).append(" months").toString());
            }
            if (j4 <= 6 && j3 > 0) {
                if (j3 == 1) {
                    stringBuffer.append(" and a day");
                } else {
                    stringBuffer.append(" and " + j3 + " days");
                }
            }
        } else if (j3 > 0) {
            if (j3 == 1) {
                stringBuffer.append("a day");
            } else {
                stringBuffer.append(new StringBuilder(String.valueOf(j3)).append(" days").toString());
            }
            if (j3 <= 3 && j2 > 0) {
                if (j2 == 1) {
                    stringBuffer.append(" and an hour");
                } else {
                    stringBuffer.append(" and " + j2 + " hours");
                }
            }
        } else if (j2 > 0) {
            if (j2 == 1) {
                stringBuffer.append("an hour");
            } else {
                stringBuffer.append(new StringBuilder(String.valueOf(j2)).append(" hours").toString());
            }
            if (time > 1) {
                stringBuffer.append(" and " + time + " minutes");
            }
        } else if (time > 0) {
            if (time == 1) {
                stringBuffer.append("a minute");
            } else {
                stringBuffer.append(new StringBuilder(String.valueOf(time)).append(" minutes").toString());
            }
            if (j > 1) {
                stringBuffer.append(" and " + j + " seconds");
            }
        } else if (j <= 1) {
            stringBuffer.append("about a second");
        } else {
            stringBuffer.append("about " + j + " seconds");
        }
        stringBuffer.append(" ago");
        Log.e("Date time format", "::::" + stringBuffer.toString());
        return stringBuffer.toString();
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

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ntification_page);
        StrictMode.setThreadPolicy(new Builder().permitAll().build());
        this.sp = getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
        this.global = (Global) getApplicationContext();
        this.clear_mTextView = (TextView) findViewById(R.id.clearnotify_mTextView);
        this.notiifcation_mListView = (ListView) findViewById(R.id.notification_listitem);
        this.clear_mTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                NotificationPageActivity.this.global.getNotificationList().clear();
                NotificationPageActivity.this.adapter = new NotiifcationListAdapter(NotificationPageActivity.this, NotificationPageActivity.this.global.getNotificationList());
                NotificationPageActivity.this.notiifcation_mListView.setAdapter(NotificationPageActivity.this.adapter);
                NotificationPageActivity.this.clear_mTextView.setClickable(false);
                NotificationPageActivity.this.clear_mTextView.setEnabled(false);
            }
        });
        Log.e("Notification List", "::::" + this.global.getNotificationList());
        this.adapter = new NotiifcationListAdapter(this, this.global.getNotificationList());
        this.notiifcation_mListView.setAdapter(this.adapter);
    }

    protected Dialog onCreateDialog(int i) {
        switch (i) {
            case DATE_DIALOG_ID /*1*/:
                return new DatePickerDialog(this, this.datePickerListener, this.year, this.month, this.day);
            default:
                return null;
        }
    }
}
