package com.token.app;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.token.util.GlobalConstants;

public class InformationActivity extends Activity {
	TextView cmpny_txt;
	TextView country_txt;
	String deviceid_mString;
	TextView email_txt;
	TextView fax_txt;
	Global global;
	Handler handler;
	Runnable infoRunnable;
	TextView location_txt;
	Runnable logoutRunnable;
	ProgressDialog logout_ProgressDialog;
	Button logout_mBtn;
	Handler logouthandler;
	MainTabActivity mainTabActivity;
	TextView mobile_txt;
	ProgressDialog pd;
	TextView phone1_txt;
	TextView phone2_txt;
	String res;
	SharedPreferences sp;

	public InformationActivity() {
		this.res = "";
		this.deviceid_mString = "";
		this.infoRunnable = new Runnable() {
			public void run() {
				try {
					String string = InformationActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					InformationActivity.this.res = WebServiceHandler.infoService(InformationActivity.this, string);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
				message.obj = InformationActivity.this.res;
				InformationActivity.this.handler.sendMessage(message);
			}
		};
		this.handler = new Handler() {
			public void handleMessage(Message message) {
				InformationActivity.this.pd.dismiss();
				if (message.obj.toString().equalsIgnoreCase("true")) {
					InformationActivity.this.cmpny_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("company_name"));
					InformationActivity.this.location_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("location"));
					InformationActivity.this.phone1_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("phone"));
					InformationActivity.this.mobile_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("mobile"));
					InformationActivity.this.fax_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("fax_no"));
					InformationActivity.this.email_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("email"));
					InformationActivity.this.country_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("country"));
					InformationActivity.this.phone2_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("phone2"));
					return;
				}
				Toast.makeText(InformationActivity.this, "Error Occured due to some server problem!!", Toast.LENGTH_LONG).show();
			}
		};
		this.logoutRunnable = new Runnable() {
			public void run() {
				Object registerOnServer = null;
				String str = "";
				try {
					String string = InformationActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					String string2 = InformationActivity.this.sp.getString(GlobalConstants.PREF_PASSWORD, "");
					String str2 = Build.MODEL;
					Log.e("model number", str2);
					registerOnServer = WebServiceHandler.registerOnServer(InformationActivity.this, string, string2, InformationActivity.this.deviceid_mString, str2, "1");
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("Exception", ":::" + e.toString());
					String str3 = str;
				}
				Message message = new Message();
				message.obj = registerOnServer;
				InformationActivity.this.logouthandler.sendMessage(message);
			}
		};
		this.logouthandler = new Handler() {
			public void handleMessage(Message message) {
				InformationActivity.this.logout_ProgressDialog.dismiss();
				Editor edit = InformationActivity.this.sp.edit();
				edit.putBoolean(GlobalConstants.ISLOGIN, false);
				edit.commit();
				InformationActivity.this.startActivity(new Intent(InformationActivity.this, MainActivity.class));
				InformationActivity.this.finishFromChild(InformationActivity.this.mainTabActivity);
			}
		};
	}

	public void GetCompanyInfo() {
		this.pd = ProgressDialog.show(this, "", "Getting Information..Please wait");
		new Thread(null, this.infoRunnable, "").start();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.information);
		this.sp = getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
		this.deviceid_mString = this.sp.getString(GlobalConstants.PREF_DEVICEID, "");
		this.global = (Global) getApplicationContext();
		this.logout_mBtn = (Button) findViewById(R.id.logout_btn);
		this.cmpny_txt = (TextView) findViewById(R.id.info_cmpyname_txt);
		this.phone1_txt = (TextView) findViewById(R.id.info_phone1_txt);
		this.phone2_txt = (TextView) findViewById(R.id.info_phone2_txt);
		this.location_txt = (TextView) findViewById(R.id.info_location_txt);
		this.fax_txt = (TextView) findViewById(R.id.info_faxno_txt);
		this.email_txt = (TextView) findViewById(R.id.info_email_txt);
		this.country_txt = (TextView) findViewById(R.id.info_country_txt);
		this.mobile_txt = (TextView) findViewById(R.id.info_mobile_txt);
		this.logout_mBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				InformationActivity.this.logout_ProgressDialog = ProgressDialog.show(InformationActivity.this, "", "Logout is in progess..Please wait");
				new Thread(null, InformationActivity.this.logoutRunnable, "").start();
			}
		});
		GetCompanyInfo();
	}
}
