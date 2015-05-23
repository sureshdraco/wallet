package com.token.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.token.util.GlobalConstants;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class InvoiceActivity extends Activity {
	String amount;
	EditText amount_et;
	Global global;
	RelativeLayout layout;
	ProgressDialog pd;
	String result;
	SharedPreferences sp;
	Runnable tokenRunnable;
	Handler tokenhandler;
	TextView tokentext;

	public InvoiceActivity() {
		this.result = "";
		this.amount = "";
		this.tokenRunnable = new Runnable() {
			public void run() {
				try {
					InvoiceActivity.this.amount = InvoiceActivity.this.amount_et.getText().toString();
					String string = InvoiceActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					InvoiceActivity.this.result = WebServiceHandler.generatetokenservice(InvoiceActivity.this, string, InvoiceActivity.this.amount);
				} catch (Exception e) {
				}
				Message message = new Message();
				message.obj = InvoiceActivity.this.result;
				InvoiceActivity.this.tokenhandler.sendMessage(message);
			}
		};
		this.tokenhandler = new Handler() {
			public void handleMessage(Message message) {
				InvoiceActivity.this.pd.dismiss();
				message.obj.toString();
				InvoiceActivity.this.layout.setVisibility(View.VISIBLE);
				InvoiceActivity.this.amount_et.getText().clear();
				((InputMethodManager) InvoiceActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(InvoiceActivity.this.getCurrentFocus().getWindowToken(),
						2);
				Log.e("global.tokencode", InvoiceActivity.this.global.getTokencode());
				InvoiceActivity.this.tokentext.setText(InvoiceActivity.this.global.tokencode);
			}
		};
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.invoice);
		this.global = (Global) getApplicationContext();
		this.sp = getSharedPreferences(GlobalConstants.PREF, 0);
		this.layout = (RelativeLayout) findViewById(R.id.token_relativeLayout);
		this.tokentext = (TextView) findViewById(R.id.token_number);
		this.amount_et = (EditText) findViewById(R.id.invoice_amount_et);
		findViewById(R.id.invoice_ok_btn).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				InvoiceActivity.this.amount = InvoiceActivity.this.amount_et.getText().toString();
				if (InvoiceActivity.this.amount.length() == 0) {
					Crouton.showText(InvoiceActivity.this, "Please Enter Amount First to proceed further", Style.ALERT);
					return;
				}
				InvoiceActivity.this.pd = ProgressDialog.show(InvoiceActivity.this, "", "Loading data..Please wait");
				new Thread(null, InvoiceActivity.this.tokenRunnable, "").start();
			}
		});
	}
}
