package com.token.app;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.token.util.GlobalConstants;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class PaymentActivity extends ActivityInTab implements TabListener {
    static Fragment fragment;
    static TextView receive_mTextView;
    static View receive_mView;
    static TextView send_mTextView;
    static View send_mView;
    Runnable accountRunnable;
    String account_balance;
    TextView account_txt;
    Dialog cashierDialog;
    Dialog confirmationDialog;
    Dialog dialog;
    Global global;
    Handler handler;
    TextView header_mTextView;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    RelativeLayout next_mLayout;
    String paymettype;
    ProgressDialog pd;
    String res;
    SharedPreferences sp;
    String token;
    EditText token_et;

    public PaymentActivity() {
        this.res = "";
        this.token = "";
        this.paymettype = "";
        this.accountRunnable = new Runnable() {
            public void run() {
                try {
                    String string = PaymentActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
                    PaymentActivity.this.res = WebServiceHandler.accountBalanceservice(PaymentActivity.this, string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = PaymentActivity.this.res;
                PaymentActivity.this.handler.sendMessage(message);
            }
        };
        this.handler = new Handler() {
            public void handleMessage(Message message) {
                if (PaymentActivity.this.pd.isShowing()) {
                    PaymentActivity.this.pd.dismiss();
                }
                if (!message.obj.toString().equalsIgnoreCase("true")) {
                    Crouton.showText(PaymentActivity.this, "Error Occured due to some server problem!!", Style.ALERT);
                } else if (PaymentActivity.this.global.getAccountbalance().equalsIgnoreCase("")) {
                    PaymentActivity.this.account_txt.setText("0 BD");
                    Crouton.showText(PaymentActivity.this, "You don't have account balance", Style.ALERT);
                } else {
                    PaymentActivity.this.account_txt.setText(new StringBuilder(String.valueOf(PaymentActivity.this.global.getAccountbalance())).append(" BD").toString());
                }
            }
        };
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.payment);
        this.sp = getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
        this.global = (Global) getApplicationContext();
        this.mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        send_mView = findViewById(R.id.sendMoneyView);
        receive_mView = findViewById(R.id.receiveMoneyView);
        send_mTextView = (TextView) findViewById(R.id.sendmoney_textview);
        receive_mTextView = (TextView) findViewById(R.id.receivemoney_textview);
        this.header_mTextView = (TextView) findViewById(R.id.header_mTextView);
        this.pd = ProgressDialog.show(this, "", "Loading data..Please wait");
        this.account_txt = (TextView) findViewById(R.id.account_balance);
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager.setAdapter(this.mAppSectionsPagerAdapter);
        this.mViewPager.setCurrentItem(0);
        send_mTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PaymentActivity.send_mView.setBackgroundColor(Color.parseColor("#F44336"));
                PaymentActivity.receive_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
                PaymentActivity.send_mTextView.setTextColor(Color.parseColor("#F44336"));
                PaymentActivity.receive_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
                PaymentActivity.this.mViewPager.setCurrentItem(0);
            }
        });
        receive_mTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PaymentActivity.send_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
                PaymentActivity.receive_mView.setBackgroundColor(Color.parseColor("#4CAF50"));
                PaymentActivity.send_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
                PaymentActivity.receive_mTextView.setTextColor(Color.parseColor("#4CAF50"));
                PaymentActivity.this.mViewPager.setCurrentItem(1);
            }
        });
        this.mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int i) {
                Log.e("View OPager Position", String.valueOf(i));
                if (i == 0) {
                    PaymentActivity.send_mView.setBackgroundColor(Color.parseColor("#F44336"));
                    PaymentActivity.receive_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
                    PaymentActivity.send_mTextView.setTextColor(Color.parseColor("#F44336"));
                    PaymentActivity.receive_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
                    PaymentActivity.this.header_mTextView.setText("Account Balance");
                } else if (i == 1) {
                    PaymentActivity.send_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
                    PaymentActivity.receive_mView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    PaymentActivity.send_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
                    PaymentActivity.receive_mTextView.setTextColor(Color.parseColor("#4CAF50"));
                    PaymentActivity.this.header_mTextView.setText("Create Payment Request");
                }
            }
        });
    }

    public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
        Log.e("onTabSelected", String.valueOf(tab.getPosition()));
        this.mViewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        public AppSectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public int getCount() {
            return 2;
        }

        public Fragment getItem(int i) {
            switch (i) {
                case R.styleable.MapAttrs_mapType /* 0 */:
                    PaymentActivity.fragment = new TransactionFragement();
                    PaymentActivity.send_mView.setBackgroundColor(Color.parseColor("#F44336"));
                    PaymentActivity.receive_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
                    PaymentActivity.send_mTextView.setTextColor(Color.parseColor("#F44336"));
                    PaymentActivity.receive_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
                    return PaymentActivity.fragment;
                case R.styleable.MapAttrs_cameraBearing /* 1 */:
                    PaymentActivity.fragment = new ReceiveMoneyFragment();
                    break;
            }
            return PaymentActivity.fragment;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(null, this.accountRunnable, "").start();
    }
}
