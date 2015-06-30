package com.token.app.view;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.token.app.R;
import com.token.app.WalletApplication;
import com.token.app.network.WebServiceHandler;
import com.token.util.GlobalConstants;
import com.token.util.NotificationUtil;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class InformationActivity extends Activity {
	private final Runnable changePasswordRunnable;
	private final Handler resetPwdHandler;
	TextView cmpny_txt;
	TextView country_txt;
	String deviceid_mString;
	TextView email_txt;
	TextView fax_txt;
	WalletApplication global;
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
	private String oldPassword, newPwd, confirmPwd;
	private AlertDialog changePwdDialog;
	private View chatBtn;

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
		this.changePasswordRunnable = new Runnable() {
			public void run() {
				try {
					String email = InformationActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					InformationActivity.this.res = WebServiceHandler.resetPasswordService(InformationActivity.this, email, oldPassword, newPwd, confirmPwd);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
				message.obj = InformationActivity.this.res;
				InformationActivity.this.resetPwdHandler.sendMessage(message);
			}
		};

		this.resetPwdHandler = new Handler() {
			public void handleMessage(Message message) {
				InformationActivity.this.pd.dismiss();
				if (message.obj.toString().equalsIgnoreCase("true")) {
					Crouton.showText(InformationActivity.this, "Reset password success!!", Style.ALERT);
					return;
				}
				Crouton.showText(InformationActivity.this, "Reset password failed!!", Style.ALERT);
				changePwdDialog.dismiss();
			}
		};

		this.handler = new Handler() {
			public void handleMessage(Message message) {
				InformationActivity.this.pd.dismiss();
				if (message.obj.toString().equalsIgnoreCase("true")) {
					try {
						setupInfo();
					} catch (Exception ex) {
					}
					return;
				}
				Crouton.showText(InformationActivity.this, "Error Occured due to some server problem!!", Style.ALERT);
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
				NotificationUtil.clearNotifications(getApplicationContext());
				Editor edit = InformationActivity.this.sp.edit();
				edit.putBoolean(GlobalConstants.ISLOGIN, false);
				edit.commit();
				InformationActivity.this.startActivity(new Intent(InformationActivity.this, MainActivity.class));
				InformationActivity.this.finishFromChild(InformationActivity.this.mainTabActivity);
			}
		};
	}

	private void setupInfo() {
		InformationActivity.this.cmpny_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("company_name"));
		InformationActivity.this.location_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("location"));
		InformationActivity.this.phone1_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("phone"));
		InformationActivity.this.mobile_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("mobile"));
		InformationActivity.this.fax_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("fax_no"));
		InformationActivity.this.email_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("email"));
		InformationActivity.this.country_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("country"));
		InformationActivity.this.phone2_txt.setText((CharSequence) ((HashMap) InformationActivity.this.global.getInfoList().get(0)).get("phone2"));
	}

	public void getCompanyInfo() {
		this.pd = ProgressDialog.show(this, "", "Getting Information..Please wait");
		new Thread(null, this.infoRunnable, "").start();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			//disable chat button
			chatBtn.setEnabled(false);
			Toast.makeText(this, "Chat not available now!", Toast.LENGTH_LONG).show();
		} else {
			chatBtn.setEnabled(true);
			chatBtn.setOnClickListener(chatClickListener);
		}
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.information);
		chatBtn = findViewById(R.id.chatBtn);
		chatBtn.setOnClickListener(chatClickListener);
		this.sp = getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
		this.deviceid_mString = this.sp.getString(GlobalConstants.PREF_DEVICEID, "");
		this.global = (WalletApplication) getApplicationContext();
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
		findViewById(R.id.resetPwdBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changePassword();
			}
		});
		getCompanyInfo();
		try {
			setupInfo();
		} catch (Exception ex) {
		}
	}

	private void changePassword() {
		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		View promptsView = li.inflate(R.layout.change_password_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText currentPassword = (EditText) promptsView
				.findViewById(R.id.currentPassword);
		final EditText confirmPassword = (EditText) promptsView
				.findViewById(R.id.confirmPassword);
		final EditText newPassword = (EditText) promptsView
				.findViewById(R.id.newPassword);

		// set dialog message
		alertDialogBuilder
				.setCancelable(true)
				.setPositiveButton("OK", null)
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		changePwdDialog = alertDialogBuilder.create();
		changePwdDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				changePwdDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (!confirmPassword.getText().toString().equals(newPassword.getText().toString())) {
							newPassword.setError("Password does not match the confirm password.");
							return;
						}
						oldPassword = currentPassword.getText().toString();
						newPwd = newPassword.getText().toString();
						confirmPwd = confirmPassword.getText().toString();
						new Thread(changePasswordRunnable).start();
					}
				});
			}
		});
		// show it
		changePwdDialog.show();
	}

	private View.OnClickListener chatClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			String url = "http://24bh.com/livehelp/";
			Intent i = new Intent(InformationActivity.this, WebViewActivity.class);
			i.putExtra("url", url);
			startActivityForResult(i, 1);
		}
	};
}
