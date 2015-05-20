package com.token.app;

import android.os.Bundle;
import android.view.View;

public class NotificationPageActivity extends ActivityInTab {
	private View clearNotifBtn;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		navigateTo(new NotificationFragment());
	}
}
