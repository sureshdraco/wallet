package com.token.app.view;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.token.util.Font;
import com.token.app.WalletApplication;
import com.token.app.R;
import com.token.util.GlobalConstants;

public class MainTabActivity extends TabActivity {
	static TabHost host;
	boolean chatcheck;
	Editor editor;
	boolean gcmcheck;
	WalletApplication global;
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
		Font.setDefaultFont(this, "DEFAULT", "roboto_regular.ttf");
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
		TabWidget widget = host.getTabWidget();
		for (int i = 0; i < widget.getChildCount(); i++) {
			View v = widget.getChildAt(i);
			if (i == 2) {
				v.setBackgroundResource(R.drawable.apptheme_tab_unselected_pressed_holo);
			} else {
				v.setBackgroundResource(R.drawable.apptheme_tab_indicator_holo);
			}
		}
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