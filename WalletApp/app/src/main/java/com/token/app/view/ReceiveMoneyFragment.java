package com.token.app.view;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.token.app.R;
import com.token.app.WalletApplication;
import com.token.app.network.Response;
import com.token.app.network.WebServiceHandler;
import com.token.util.GlobalConstants;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class ReceiveMoneyFragment extends Fragment {
	String amount;
	EditText amount_et;
	WalletApplication global;
	RelativeLayout layout;
	ProgressDialog pd;
	Response result;
	SharedPreferences sp;
	Runnable tokenRunnable;
	Handler tokenhandler;
	TextView tokentext;
	private Button copyUrlBtn;

	public ReceiveMoneyFragment() {
		this.result = new Response("", "");
		this.amount = "";
		this.tokenRunnable = new Runnable() {
			public void run() {
				try {
					ReceiveMoneyFragment.this.amount = ReceiveMoneyFragment.this.amount_et.getText().toString();
					String string = ReceiveMoneyFragment.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					ReceiveMoneyFragment.this.result = WebServiceHandler.generatetokenservice(ReceiveMoneyFragment.this.getActivity(), string, ReceiveMoneyFragment.this.amount);
				} catch (Exception e) {
				}
				Message message = new Message();
				message.obj = ReceiveMoneyFragment.this.result;
				ReceiveMoneyFragment.this.tokenhandler.sendMessage(message);
			}
		};
		this.tokenhandler = new Handler() {
			public void handleMessage(Message message) {
				ReceiveMoneyFragment.this.pd.dismiss();
				Response response = (Response) message.obj;
				ReceiveMoneyFragment.this.amount_et.getText().clear();
				((InputMethodManager) ReceiveMoneyFragment.this.getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(),
						2);
				Log.e("global.tokencode", ReceiveMoneyFragment.this.global.getTokencode());
				if (response.getStatus().equalsIgnoreCase("true")) {
					ReceiveMoneyFragment.this.layout.setVisibility(View.VISIBLE);
					ReceiveMoneyFragment.this.tokentext.setText(ReceiveMoneyFragment.this.global.tokencode);
				} else {
					if (!TextUtils.isEmpty(response.getError())) {
						Crouton.makeText(ReceiveMoneyFragment.this.getActivity(), response.getError(), Style.ALERT).show();
					} else {
						Crouton.makeText(ReceiveMoneyFragment.this.getActivity(), "Unknown error!", Style.ALERT).show();
					}
				}
			}
		};
	}

	public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
		View inflate = layoutInflater.inflate(R.layout.invoice, viewGroup, false);
		super.onCreate(bundle);
		this.global = (WalletApplication) getActivity().getApplicationContext();
		this.sp = getActivity().getSharedPreferences(GlobalConstants.PREF, 0);
		this.layout = (RelativeLayout) inflate.findViewById(R.id.token_relativeLayout);
		this.tokentext = (TextView) inflate.findViewById(R.id.token_number);
		this.amount_et = (EditText) inflate.findViewById(R.id.invoice_amount_et);
		amount_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					generate();
					return true;
				}
				return false;
			}
		});
		this.copyUrlBtn = (Button) inflate.findViewById(R.id.copyUrlBtn);
		this.copyUrlBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("pay url", global.getTransactionUrl());
				clipboard.setPrimaryClip(clip);
				Toast.makeText(getActivity(), "Payment URL copied", Toast.LENGTH_SHORT).show();
			}
		});
		this.amount_et.setFocusable(true);
		inflate.findViewById(R.id.invoice_ok_btn).setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				generate();
			}
		});
		return inflate;
	}

	private void generate() {
		ReceiveMoneyFragment.this.amount = ReceiveMoneyFragment.this.amount_et.getText().toString();
		if (ReceiveMoneyFragment.this.amount.length() == 0) {
			Crouton.showText(getActivity(), "Please Enter Amount First to proceed further", Style.ALERT);
			return;
		}

		if (ReceiveMoneyFragment.this.amount.equals("0")) {
			Crouton.showText(getActivity(), "0 is not accepted.", Style.ALERT);
			return;
		}
		ReceiveMoneyFragment.this.pd = ProgressDialog.show(ReceiveMoneyFragment.this.getActivity(), "", "Loading data..Please wait");
		new Thread(null, ReceiveMoneyFragment.this.tokenRunnable, "").start();
	}
}
