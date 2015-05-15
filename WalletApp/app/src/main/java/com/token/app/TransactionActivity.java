package com.token.app;

import android.os.Bundle;

public class TransactionActivity extends ActivityInTab {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        navigateTo(new TransactionFragement());
    }
}
