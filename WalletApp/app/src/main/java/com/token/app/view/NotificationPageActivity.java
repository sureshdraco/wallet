package com.token.app.view;

import android.os.Bundle;
import android.view.View;

import com.token.app.ActivityInTab;

public class NotificationPageActivity extends ActivityInTab {
	private View clearNotifBtn;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		navigateTo(new NotificationFragment());
	}
}
