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
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.token.app.R;
import com.token.app.WalletApplication;
import com.token.app.network.WebServiceHandler;
import com.token.util.GlobalConstants;
import com.token.util.Utils;

import java.util.HashMap;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressLint({"HandlerLeak"})
public class TokenInfoDialog extends Fragment implements OnTouchListener {
    Runnable accountRunnable;
    String account_balance;
    TextView account_txt;
    Dialog confirmationDialog;
    Dialog container;
    Dialog dialog;
    WalletApplication global;
    Handler handler;
    LinearLayout layout;
    Runnable paymentRunnable;
    Handler paymenthandler;
    String paymettype;
    ProgressDialog pd;
    String res;
    SharedPreferences sp;
    MainTabActivity tab;
    String token;
    EditText token_et;
    Utils utils;

    public TokenInfoDialog() {
        this.res = "";
        this.token = "";
        this.paymettype = "";
        this.paymentRunnable = new Runnable() {
            public void run() {
                try {
                    TokenInfoDialog.this.token = TokenInfoDialog.this.global.getToken();
                    Log.e("Token", TokenInfoDialog.this.global.getToken());
                    String string = TokenInfoDialog.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
                    TokenInfoDialog.this.res = WebServiceHandler.paymentservice(TokenInfoDialog.this.getActivity(), TokenInfoDialog.this.token, TokenInfoDialog.this.paymettype,
                            string);
                } catch (Exception e) {
                }
                Message message = new Message();
                message.obj = TokenInfoDialog.this.res;
                TokenInfoDialog.this.paymenthandler.sendMessage(message);
            }
        };
        this.paymenthandler = new Handler() {
            public void handleMessage(Message message) {
                TokenInfoDialog.this.pd.dismiss();
                if (message.obj.toString().equalsIgnoreCase("true")) {
                    new Thread(null, TokenInfoDialog.this.accountRunnable, "").start();
                    utils = new Utils();
                    TokenInfoDialog.this.utils.getAccountBalance(TokenInfoDialog.this.getActivity(), "");
                    TokenInfoDialog.this.startActivity(new Intent(TokenInfoDialog.this.getActivity(), ConfirmationActivity.class));
                    TokenInfoDialog.this.getActivity().finish();
                    return;
                }
                Toast.makeText(TokenInfoDialog.this.getActivity(), " " + TokenInfoDialog.this.global.getPaymessage(), Toast.LENGTH_LONG).show();
                TokenInfoDialog.this.getActivity().finish();
                TokenInfoDialog.this.tab.changeTheTab(2);
            }
        };
        this.accountRunnable = new Runnable() {
            public void run() {
                try {
                    String string = TokenInfoDialog.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					String pwd = TokenInfoDialog.this.sp.getString(GlobalConstants.PREF_PASSWORD, "");
					TokenInfoDialog.this.res = WebServiceHandler.accountBalanceservice(TokenInfoDialog.this.getActivity(), string, pwd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = TokenInfoDialog.this.res;
                TokenInfoDialog.this.handler.sendMessage(message);
            }
        };
        this.handler = new Handler() {
            public void handleMessage(Message message) {
                if (!message.obj.toString().equalsIgnoreCase("true")) {
                    Crouton.showText(getActivity(), "Error Occured due to some server problem!!", Style.ALERT);
                } else if (TokenInfoDialog.this.global.getAccountbalance().equalsIgnoreCase("")) {
                    TokenInfoDialog.this.account_txt.setText("0 BD");
                    Crouton.showText(getActivity(), "You don't have account balance", Style.ALERT);
                } else {
                    TokenInfoDialog.this.account_txt.setText(new StringBuilder(String.valueOf(TokenInfoDialog.this.global.getAccountbalance())).append(" BD").toString());
                }
            }
        };
    }

    public void PaymentService() {
        this.pd = ProgressDialog.show(getActivity(), "", "Please wait..");
        new Thread(null, this.paymentRunnable, "").start();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.pay_dialog, viewGroup, false);
        this.global = (WalletApplication) getActivity().getApplicationContext();
        this.sp = getActivity().getSharedPreferences(GlobalConstants.PREF, 0);
        this.tab = new MainTabActivity();
        this.layout = (LinearLayout) linearLayout.findViewById(R.id.paymain_layout);
        this.layout.setOnTouchListener(this);
        this.account_txt = (TextView) linearLayout.findViewById(R.id.account_balance);
        ((Button) linearLayout.findViewById(R.id.pay_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TokenInfoDialog.this.paymettype = "Paid";
                TokenInfoDialog.this.PaymentService();
            }
        });
        ((Button) linearLayout.findViewById(R.id.cancel_btn)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        TextView textView = (TextView) linearLayout.findViewById(R.id.token_amount_txt);
        TextView textView2 = (TextView) linearLayout.findViewById(R.id.token_code);
        TextView textView3 = (TextView) linearLayout.findViewById(R.id.token_location);
        TextView textView4 = (TextView) linearLayout.findViewById(R.id.token_creationdate);
        try {
            ((TextView) linearLayout.findViewById(R.id.token_creator)).setText((CharSequence) ((HashMap) this.global.getTokenList().get(0)).get(GlobalConstants.TOKEN_CREATOR));
            textView.setText((CharSequence) ((HashMap) this.global.getTokenList().get(0)).get(GlobalConstants.TOKEN_AMOUNT));
            textView2.setText(this.token);
            textView3.setText((CharSequence) ((HashMap) this.global.getTokenList().get(0)).get(GlobalConstants.TOKEN_LOCATION));
            textView4.setText((CharSequence) ((HashMap) this.global.getTokenList().get(0)).get(GlobalConstants.TOKEN_DATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.account_txt.setText(new StringBuilder(String.valueOf(this.global.getAccountbalance())).append(" BD").toString());
        return linearLayout;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 2);
        return false;
    }
}
