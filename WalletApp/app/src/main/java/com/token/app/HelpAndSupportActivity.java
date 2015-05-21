package com.token.app;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.token.util.GlobalConstants;

import java.io.IOException;

public class HelpAndSupportActivity extends Activity implements OnClickListener {
	private static final String TAG = HelpAndSupportActivity.class.getSimpleName();
	String amount_mString;
	String deviceid_mString;
	Dialog dialog;
	Global global;
	Handler handler;
	Runnable infoRunnable;
	Button info_mBtn;
	Runnable logoutRunnable;
	ProgressDialog logout_ProgressDialog;
	Button logout_mBtn;
	Handler logouthandler;
	MainTabActivity mainTabActivity;
	ProgressDialog pd;
	String res_mString;
	SharedPreferences sp;
	Button withdrawl_mBtn;

	public HelpAndSupportActivity() {
		this.deviceid_mString = "";
		this.infoRunnable = new Runnable() {
			public void run() {
				try {
					String string = HelpAndSupportActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					HelpAndSupportActivity.this.res_mString = WebServiceHandler.withdrawalService(HelpAndSupportActivity.this, string, HelpAndSupportActivity.this.amount_mString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
				message.obj = HelpAndSupportActivity.this.res_mString;
				HelpAndSupportActivity.this.handler.sendMessage(message);
			}
		};
		this.handler = new Handler() {
			public void handleMessage(Message message) {
				HelpAndSupportActivity.this.pd.dismiss();
				HelpAndSupportActivity.this.dialog.dismiss();
				if (message.obj.toString().equalsIgnoreCase("true")) {
					Toast.makeText(HelpAndSupportActivity.this, "WithDrawal Done Successfully!!", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(HelpAndSupportActivity.this, "Error Occured due to some server problem!!", Toast.LENGTH_LONG).show();
				}
			}
		};
		this.logoutRunnable = new Runnable() {
			public void run() {
				Object registerOnServer = null;
				String str = "";
				try {
					String string = HelpAndSupportActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					String string2 = HelpAndSupportActivity.this.sp.getString(GlobalConstants.PREF_PASSWORD, "");
					String note = Build.MODEL;
					Log.e("model number", note);
					registerOnServer = WebServiceHandler.registerOnServer(HelpAndSupportActivity.this, string, string2, HelpAndSupportActivity.this.deviceid_mString, note, "1");
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("Exception", ":::" + e.toString());
					String str3 = str;
				}
				Message message = new Message();
				message.obj = registerOnServer;
				HelpAndSupportActivity.this.logouthandler.sendMessage(message);
			}
		};
		this.logouthandler = new Handler() {
			public void handleMessage(Message message) {
				HelpAndSupportActivity.this.logout_ProgressDialog.dismiss();
				Editor edit = HelpAndSupportActivity.this.sp.edit();
				edit.putBoolean(GlobalConstants.ISLOGIN, false);
				edit.commit();
				try {
					GoogleCloudMessaging.getInstance(getApplicationContext()).unregister();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
				HelpAndSupportActivity.this.startActivity(new Intent(HelpAndSupportActivity.this, MainActivity.class));
				HelpAndSupportActivity.this.finishFromChild(HelpAndSupportActivity.this.mainTabActivity);
			}
		};
	}

	public void WithdrawlDialog() {
		this.dialog = new Dialog(this, 16973840);
		this.dialog.setContentView(R.layout.withdrawl_dialog);
		Button button = (Button) this.dialog.findViewById(R.id.withdraw_send);
		EditText editText = (EditText) this.dialog.findViewById(R.id.amount_et);
		((Button) this.dialog.findViewById(R.id.withdrawl_cancel)).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				HelpAndSupportActivity.this.dialog.dismiss();
			}
		});
		button.setOnClickListener(new AnonymousClass6(editText));
		this.dialog.show();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.witdrawl_btn /* 2131034159 */:
			WithdrawlDialog();
		case R.id.companyinfo_btn /* 2131034160 */:
			startActivity(new Intent(this, InformationActivity.class));
		case R.id.logout_btn /* 2131034170 */:
			this.logout_ProgressDialog = ProgressDialog.show(this, "", "Logout is in process..Please wait");
			new Thread(null, this.logoutRunnable, "").start();
		default:
		}
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.help_and_support);
		this.sp = getSharedPreferences(GlobalConstants.PREF, 0);
		this.deviceid_mString = this.sp.getString(GlobalConstants.PREF_DEVICEID, "");
		this.global = (Global) getApplicationContext();
		this.withdrawl_mBtn = (Button) findViewById(R.id.witdrawl_btn);
		this.logout_mBtn = (Button) findViewById(R.id.helplogout_btn);
		this.info_mBtn = (Button) findViewById(R.id.companyinfo_btn);
		this.withdrawl_mBtn.setOnClickListener(this);
		this.logout_mBtn.setOnClickListener(this);
		this.info_mBtn.setOnClickListener(this);
	}

	public void withDraw() {
		this.pd = ProgressDialog.show(this, "", "Withdrawal is in process..Please wait");
		new Thread(null, this.infoRunnable, "").start();
	}

	/* renamed from: com.token.app.HelpAndSupportActivity.6 */
	class AnonymousClass6 implements OnClickListener {
		private final/* synthetic */EditText val$amount_mEditBox;

		AnonymousClass6(EditText editText) {
			this.val$amount_mEditBox = editText;
		}

		public void onClick(View view) {
			HelpAndSupportActivity.this.amount_mString = this.val$amount_mEditBox.getText().toString();
			HelpAndSupportActivity.this.withDraw();
		}
	}
}
