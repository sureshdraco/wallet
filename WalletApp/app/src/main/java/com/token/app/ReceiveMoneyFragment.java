package com.token.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.token.util.GlobalConstants;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class ReceiveMoneyFragment extends Fragment {
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

    public ReceiveMoneyFragment() {
        this.result = "";
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
                message.obj.toString();
                ReceiveMoneyFragment.this.layout.setVisibility(View.VISIBLE);
                ReceiveMoneyFragment.this.amount_et.getText().clear();
                ((InputMethodManager) ReceiveMoneyFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ReceiveMoneyFragment.this
                        .getActivity().getCurrentFocus().getWindowToken(), 2);
                Log.e("global.tokencode", ReceiveMoneyFragment.this.global.getTokencode());
                ReceiveMoneyFragment.this.tokentext.setText(ReceiveMoneyFragment.this.global.tokencode);
            }
        };
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.invoice, viewGroup, false);
        super.onCreate(bundle);
        this.global = (Global) getActivity().getApplicationContext();
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
