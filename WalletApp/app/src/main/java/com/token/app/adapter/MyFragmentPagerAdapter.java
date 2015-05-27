package com.token.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.token.app.view.ConfirmationFragement;
import com.token.app.view.TransactionFragement;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	final int PAGE_COUNT;

	public MyFragmentPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
		this.PAGE_COUNT = 2;
	}

	public int getCount() {
		return 2;
	}

	public Fragment getItem(int i) {
		Bundle bundle = new Bundle();
		Fragment transactionFragement;
		switch (i) {
		case R.styleable.MapAttrs_mapType /* 0 */:
			transactionFragement = new TransactionFragement();
			bundle.putInt("current_page", i + 1);
			transactionFragement.setArguments(bundle);
			return transactionFragement;
		case R.styleable.MapAttrs_cameraBearing /* 1 */:
			transactionFragement = new ConfirmationFragement();
			bundle.putInt("current_page", i + 1);
			transactionFragement.setArguments(bundle);
			return transactionFragement;
		default:
			return null;
		}
	}
}
