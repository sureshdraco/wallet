package com.token.app;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import com.token.util.GlobalConstants;

public class MainTabActivity extends TabActivity {
    static TabHost host;
    boolean chatcheck;
    Editor editor;
    boolean gcmcheck;
    Global global;
    ImageView img;
    Intent mIntent;
    boolean recommendcheck;
    SharedPreferences sp;

    public void changeTheTab(int i) {
        host.setCurrentTab(i);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tab);
        this.sp = getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
        host = getTabHost();
        Font.setDefaultFont(this, "DEFAULT", "regular.ttf");
        TabSpec newTabSpec = host.newTabSpec("Invoice");
        newTabSpec.setIndicator("", getResources().getDrawable(R.drawable.invoice_selector));
        this.mIntent = new Intent(this, AllTransactionFragementActivity.class);
        newTabSpec.setContent(this.mIntent);
        TabSpec newTabSpec2 = host.newTabSpec("Notification");
        newTabSpec2.setIndicator("", getResources().getDrawable(R.drawable.notification_selector));
        this.mIntent = new Intent(this, NotificationPageActivity.class);
        newTabSpec2.setContent(this.mIntent);
        TabSpec newTabSpec3 = host.newTabSpec("Pay");
        newTabSpec3.setIndicator("", getResources().getDrawable(R.drawable.pay_selector));
        this.mIntent = new Intent(this, PaymentActivity.class);
        newTabSpec3.setContent(this.mIntent);
        TabSpec newTabSpec4 = host.newTabSpec("Balance");
        newTabSpec4.setIndicator("", getResources().getDrawable(R.drawable.balance_selector));
        this.mIntent = new Intent(this, BalanceActivity.class);
        newTabSpec4.setContent(this.mIntent);
        TabSpec newTabSpec5 = host.newTabSpec("Support");
        newTabSpec5.setIndicator("", getResources().getDrawable(R.drawable.support_selector));
        this.mIntent = new Intent(this, InformationActivity.class);
        newTabSpec5.setContent(this.mIntent);
        host.addTab(newTabSpec);
        host.addTab(newTabSpec2);
        host.addTab(newTabSpec3);
        host.addTab(newTabSpec4);
        host.addTab(newTabSpec5);
        if (this.sp.getBoolean("notify", false)) {
            changeTheTab(1);
            this.editor = this.sp.edit();
            this.editor.putBoolean("notify", false);
            this.editor.commit();
        } else {
            changeTheTab(2);
        }
        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            int currentTab = host.getCurrentTab();
            for (int i2 = 0; i2 < host.getTabWidget().getChildCount(); i2++) {
                if (currentTab == i2) {
                    host.getTabWidget().getChildAt(i2).setBackgroundColor(Color.parseColor("#F44336"));
                } else {
                    host.getTabWidget().getChildAt(i2).setBackgroundColor(Color.parseColor("#25292C"));
                }
            }
        }
        host.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String str) {
                int currentTab = MainTabActivity.host.getCurrentTab();
                for (int i = 0; i < MainTabActivity.host.getTabWidget().getChildCount(); i++) {
                    if (currentTab == i) {
                        MainTabActivity.host.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#F44336"));
                    } else {
                        MainTabActivity.host.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#25292C"));
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        try {
            if (intent.getExtras().getBoolean("notifications", false)) {
                host.setCurrentTabByTag("Notification");
            }
        } catch (Exception ignored) {
        }
    }

}
