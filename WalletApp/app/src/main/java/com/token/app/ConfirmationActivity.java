package com.token.app;

import android.os.Bundle;

public class ConfirmationActivity extends ActivityInTab {
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		navigateTo(new ConfirmationFragement());
	}
}
