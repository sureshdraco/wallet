package com.token.app.view;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.token.app.WalletApplication;
import com.token.app.R;
import com.token.app.network.WebServiceHandler;
import com.token.util.GlobalConstants;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressLint({"HandlerLeak"})
public class TransactionFragement extends Fragment implements OnTouchListener {
    String account_balance;
    TextView account_txt;
    Dialog cashierDialog;
    Dialog confirmationDialog;
    Dialog dialog;
    WalletApplication global;
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
                pd.dismiss();
                String obj = message.obj.toString();
                token_et.getText().clear();
                if (obj.equalsIgnoreCase("true")) {
                    TransactionFragement.this.global.setToken(TransactionFragement.this.token);
                    TransactionFragement.this.startActivity(new Intent(TransactionFragement.this.getActivity(), TokenInfoActivity.class));
                    return;
                }
                Crouton.showText(getActivity(), "This code not valid", Style.ALERT);
            }
        };
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View linearLayout = layoutInflater.inflate(R.layout.token_dialog, viewGroup, false);
        this.global = (WalletApplication) getActivity().getApplicationContext();
        getActivity().getWindow().setSoftInputMode(32);
        this.sp = getActivity().getSharedPreferences(GlobalConstants.PREF, 0);
        this.account_txt = (TextView) linearLayout.findViewById(R.id.account_balance);
        this.global = (WalletApplication) getActivity().getApplicationContext();
        this.token_et = (EditText) linearLayout.findViewById(R.id.token_et);
        token_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    tokenOk();
                    return true;
                }
                return false;
            }
        });
        linearLayout.findViewById(R.id.token_ok_btn).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                tokenOk();
            }
        });
        return linearLayout;
    }

    private void tokenOk() {
        TransactionFragement.this.token = TransactionFragement.this.token_et.getText().toString();
        if (TransactionFragement.this.token.length() == 0) {
            Crouton.showText(getActivity(), "Please Enter 4 digit Token first", Style.ALERT);
        } else if (TransactionFragement.this.token.length() < 4) {
            Crouton.showText(getActivity(), "Please Enter 4 digit Token", Style.ALERT);
        } else {
            TransactionFragement.this.tokenwebservice();
        }
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
