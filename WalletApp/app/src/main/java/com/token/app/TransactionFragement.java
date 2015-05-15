package com.token.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.token.util.GlobalConstants;

@SuppressLint({ "HandlerLeak" })
public class TransactionFragement extends Fragment implements OnTouchListener {
	String account_balance;
	TextView account_txt;
	Dialog cashierDialog;
	Dialog confirmationDialog;
	Dialog dialog;
	Global global;
	RelativeLayout next_mLayout;
	String paymettype;
	ProgressDialog pd;
	String res;
	SharedPreferences sp;
	String token;
	Runnable tokenRunnable;
	EditText token_et;
	Handler tokenhandler;

	public TransactionFragement() {
		this.res = "";
		this.token = "";
		this.paymettype = "";
		this.tokenRunnable = new Runnable() {
			public void run() {
				try {
					TransactionFragement.this.res = WebServiceHandler.tokenservice(TransactionFragement.this.getActivity(), TransactionFragement.this.token);
				} catch (Exception e) {
				}
				Message message = new Message();
				message.obj = TransactionFragement.this.res;
				TransactionFragement.this.tokenhandler.sendMessage(message);
			}
		};
		this.tokenhandler = new Handler() {
			public void handleMessage(Message message) {
				TransactionFragement.this.pd.dismiss();
				String obj = message.obj.toString();
				TransactionFragement.this.token_et.getText().clear();
				if (obj.equalsIgnoreCase("true")) {
					TransactionFragement.this.global.setToken(TransactionFragement.this.token);
					TransactionFragement.this.startActivity(new Intent(TransactionFragement.this.getActivity(), TokenInfoActivity.class));
					return;
				}
				Toast.makeText(TransactionFragement.this.getActivity(), "This code not valid", Toast.LENGTH_LONG).show();
			}
		};
	}

	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
		LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.token_dialog, viewGroup, false);
		this.global = (Global) getActivity().getApplicationContext();
		getActivity().getWindow().setSoftInputMode(32);
		this.sp = getActivity().getSharedPreferences(GlobalConstants.PREF, 0);
		this.account_txt = (TextView) linearLayout.findViewById(R.id.account_balance);
		this.global = (Global) getActivity().getApplicationContext();
		this.token_et = (EditText) linearLayout.findViewById(R.id.token_et);
		((Button) linearLayout.findViewById(R.id.token_ok_btn)).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				TransactionFragement.this.token = TransactionFragement.this.token_et.getText().toString();
				if (TransactionFragement.this.token.length() == 0) {
					Toast.makeText(TransactionFragement.this.getActivity(), "Please Enter 4 digit Token first", Toast.LENGTH_LONG).show();
				} else if (TransactionFragement.this.token.length() < 4) {
					Toast.makeText(TransactionFragement.this.getActivity(), "Please Enter 4 digit Token", Toast.LENGTH_LONG).show();
				} else {
					TransactionFragement.this.tokenwebservice();
				}
			}
		});
		return linearLayout;
	}

	public boolean onTouch(View view, MotionEvent motionEvent) {
		((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 2);
		return false;
	}

	public void tokenwebservice() {
		this.pd = ProgressDialog.show(getActivity(), "", "Loading data..Please wait");
		new Thread(null, this.tokenRunnable, "").start();
	}
}
