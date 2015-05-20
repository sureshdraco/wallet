package com.token.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.token.util.GlobalConstants;

@SuppressLint({ "HandlerLeak" })
public class ConfirmationFragement extends Fragment implements OnTouchListener {
	Runnable accountRunnable;
	String account_balance;
	TextView account_txt;
	Dialog confirmationDialog;
	Dialog container;
	Dialog dialog;
	Global global;
	Handler handler;
	RelativeLayout layout;
	String paymettype;
	ProgressDialog pd;
	String res;
	SharedPreferences sp;
	String token;
	EditText token_et;

	public ConfirmationFragement() {
		this.res = "";
		this.token = "";
		this.paymettype = "";
		this.accountRunnable = new Runnable() {
			public void run() {
				try {
					String string = ConfirmationFragement.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					ConfirmationFragement.this.res = WebServiceHandler.AccountBalanceservice(ConfirmationFragement.this.getActivity(), string);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
				message.obj = ConfirmationFragement.this.res;
				ConfirmationFragement.this.handler.sendMessage(message);
			}
		};
		this.handler = new Handler() {
			public void handleMessage(Message message) {
				if (!message.obj.toString().equalsIgnoreCase("true")) {
					Toast.makeText(ConfirmationFragement.this.getActivity(), "Error Occured due to some server problem!!", Toast.LENGTH_LONG).show();
				} else if (ConfirmationFragement.this.global.getAccountbalance().equalsIgnoreCase("")) {
					ConfirmationFragement.this.account_txt.setText("0 BD");
					Toast.makeText(ConfirmationFragement.this.getActivity(), "You don't have account balance", Toast.LENGTH_LONG).show();
				} else {
					ConfirmationFragement.this.account_txt.setText(new StringBuilder(String.valueOf(ConfirmationFragement.this.global.getAccountbalance())).append(" BD")
							.toString());
				}
			}
		};
	}

	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
		RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.confirmation, viewGroup, false);
		this.global = (Global) getActivity().getApplicationContext();
		this.sp = getActivity().getSharedPreferences(GlobalConstants.PREF, 0);
		this.account_txt = (TextView) relativeLayout.findViewById(R.id.account_balance_mTextView);
		this.layout = (RelativeLayout) relativeLayout.findViewById(R.id.confirmation_layout);
		this.layout.setOnTouchListener(this);
		Button button = (Button) relativeLayout.findViewById(R.id.payment_ok);
		button.setOnClickListener(new AnonymousClass3(button));
		new Thread(null, this.accountRunnable, "").start();
		this.account_txt.setText(new StringBuilder(String.valueOf(this.global.getAccountbalance())).append(" BD").toString());
		return relativeLayout;
	}

	public boolean onTouch(View view, MotionEvent motionEvent) {
		return false;
	}

	/* renamed from: com.token.app.ConfirmationFragement.3 */
	class AnonymousClass3 implements OnClickListener {
		private final/* synthetic */Button val$pay;

		AnonymousClass3(Button button) {
			this.val$pay = button;
		}

		public void onClick(View view) {
			this.val$pay.setVisibility(8);
		}
	}
}
