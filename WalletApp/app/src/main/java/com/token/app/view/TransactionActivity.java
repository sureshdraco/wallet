package com.token.app.view;

import android.os.Bundle;

import com.token.app.ActivityInTab;

public class TransactionActivity extends ActivityInTab {
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		navigateTo(new TransactionFragement());
	}
}
