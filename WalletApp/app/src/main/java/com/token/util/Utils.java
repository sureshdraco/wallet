package com.token.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.token.app.WalletApplication;
import com.token.app.network.WebServiceHandler;

public class Utils {
	Runnable accountRunnable;
	Context context;
	String email_mString;
	WalletApplication global;
	Handler handler;
	String res;
	SharedPreferences sharedPreferences;

	public Utils() {
		this.res = "";
		this.accountRunnable = new Runnable() {
			public void run() {
				try {
					Utils.this.email_mString = Utils.this.sharedPreferences.getString(GlobalConstants.PREF_USERNAME, "");
					String pwd = Utils.this.sharedPreferences.getString(GlobalConstants.PREF_PASSWORD, "");
					Utils.this.res = WebServiceHandler.accountBalanceservice(Utils.this.context, email_mString, pwd);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
				message.obj = Utils.this.res;
				Utils.this.handler.sendMessage(message);
			}
		};
		this.handler = new Handler() {
			public void handleMessage(Message message) {
				Log.e("Account Balance", "Utils::::" + Utils.this.global.getAccountbalance());
			}
		};
	}

	public static Bitmap getCircularBitmap(Bitmap bitmap) {
		Bitmap createBitmap = bitmap.getWidth() > bitmap.getHeight() ? Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888) : Bitmap.createBitmap(
				bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
		Canvas canvas = new Canvas(createBitmap);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		float height = bitmap.getWidth() > bitmap.getHeight() ? (float) (bitmap.getHeight() / 2) : (float) (bitmap.getWidth() / 2);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(-12434878);
		canvas.drawCircle(height, height, height, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return createBitmap;
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
					stringBuffer.append(" month");
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
			if (j4 <= 6 && j3 > 0 && j3 == 1) {
				stringBuffer.append(" and a day");
			}
		} else if (j3 > 0) {
			if (j3 == 1) {
				stringBuffer.append("a day");
			} else {
				stringBuffer.append(new StringBuilder(String.valueOf(j3)).append(" day").toString());
			}
			if (j3 <= 3 && j2 > 0) {
				if (j2 == 1) {
					stringBuffer.append(" , an hour");
				} else {
					stringBuffer.append(" ," + j2 + " h");
				}
			}
		} else if (j2 > 0) {
			if (j2 == 1) {
				stringBuffer.append("an hour");
			} else {
				stringBuffer.append(new StringBuilder(String.valueOf(j2)).append(" h").toString());
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
			stringBuffer.append(j + " seconds");
		}
		stringBuffer.append(" ago");
		Log.e("Date time format", "::::" + stringBuffer.toString());
		return stringBuffer.toString();
	}

	public static Bitmap reduceImageSize(String str) {
		int i = 1;
		try {
			File file = new File(str);
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, options);
			int i2 = options.outWidth;
			int i3 = options.outHeight;
			while (i2 / 2 >= 100 && i3 / 2 >= 100) {
				i2 /= 2;
				i3 /= 2;
				i *= 2;
			}
			Options options2 = new Options();
			options2.inSampleSize = i;
			return BitmapFactory.decodeStream(new FileInputStream(file), null, options2);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getAccountBalance(Context context, String str) {
		this.context = context;
		this.sharedPreferences = context.getSharedPreferences(GlobalConstants.PREF, 0);
		this.global = (WalletApplication) context.getApplicationContext();
		new Thread(null, this.accountRunnable, "").start();
	}
}
