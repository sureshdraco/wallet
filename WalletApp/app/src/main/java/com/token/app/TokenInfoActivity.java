package com.token.app;

import android.os.Bundle;

public class TokenInfoActivity extends ActivityInTab {
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		navigateTo(new TokenInfoDialog());
	}
}
