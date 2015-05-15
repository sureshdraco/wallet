package com.token.app;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AllTransactionFragementActivity extends ActivityInTab implements TabListener {
	static TextView expired_mTextView;
	static View expired_mView;
	static TextView paid_mTextView;
	static View paid_mView;
	static TextView unpaid_mTextView;
	static View unpaid_mView;
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
		public AppSectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		public int getCount() {
			return 3;
		}

		public Fragment getItem(int i) {
			switch (i) {
			case R.styleable.MapAttrs_mapType /* 0 */:
				AllTransactionFragementActivity.paid_mView.setBackgroundColor(Color.parseColor("#4CAF50"));
				AllTransactionFragementActivity.unpaid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.expired_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.paid_mTextView.setTextColor(Color.parseColor("#4CAF50"));
				AllTransactionFragementActivity.unpaid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.expired_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				return new PaidFragment();
			case R.styleable.MapAttrs_cameraBearing /* 1 */:
				return new UnpaidFragment();
			case R.styleable.MapAttrs_cameraTargetLat /* 2 */:
				return new ExpiredFragment();
			default:
				return null;
			}
		}
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_main);
		this.mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		paid_mView = findViewById(R.id.paidView);
		unpaid_mView = findViewById(R.id.unpaidView);
		expired_mView = findViewById(R.id.expiredView);
		paid_mTextView = (TextView) findViewById(R.id.paid_textview);
		unpaid_mTextView = (TextView) findViewById(R.id.unpaid_textview);
		expired_mTextView = (TextView) findViewById(R.id.expire_textview);
		paid_mTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				AllTransactionFragementActivity.paid_mView.setBackgroundColor(Color.parseColor("#4CAF50"));
				AllTransactionFragementActivity.unpaid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.expired_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.paid_mTextView.setTextColor(Color.parseColor("#4CAF50"));
				AllTransactionFragementActivity.unpaid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.expired_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.this.mViewPager.setCurrentItem(0);
			}
		});
		unpaid_mTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				AllTransactionFragementActivity.paid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.unpaid_mView.setBackgroundColor(Color.parseColor("#F44336"));
				AllTransactionFragementActivity.expired_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.paid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.unpaid_mTextView.setTextColor(Color.parseColor("#F44336"));
				AllTransactionFragementActivity.expired_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.this.mViewPager.setCurrentItem(1);
			}
		});
		expired_mTextView.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				AllTransactionFragementActivity.paid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.unpaid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.expired_mView.setBackgroundColor(Color.parseColor("#000000"));
				AllTransactionFragementActivity.paid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.unpaid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				AllTransactionFragementActivity.expired_mTextView.setTextColor(Color.parseColor("#000000"));
				AllTransactionFragementActivity.this.mViewPager.setCurrentItem(2);
			}
		});
		this.mViewPager = (ViewPager) findViewById(R.id.pager);
		this.mViewPager.setAdapter(this.mAppSectionsPagerAdapter);
		this.mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			public void onPageSelected(int i) {
				Log.e("View OPager Position", String.valueOf(i));
				if (i == 0) {
					AllTransactionFragementActivity.paid_mView.setBackgroundColor(Color.parseColor("#4CAF50"));
					AllTransactionFragementActivity.unpaid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.expired_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.paid_mTextView.setTextColor(Color.parseColor("#4CAF50"));
					AllTransactionFragementActivity.unpaid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.expired_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				} else if (i == 1) {
					AllTransactionFragementActivity.paid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.unpaid_mView.setBackgroundColor(Color.parseColor("#F44336"));
					AllTransactionFragementActivity.expired_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.paid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.unpaid_mTextView.setTextColor(Color.parseColor("#F44336"));
					AllTransactionFragementActivity.expired_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
				} else if (i == 2) {
					AllTransactionFragementActivity.paid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.unpaid_mView.setBackgroundColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.expired_mView.setBackgroundColor(Color.parseColor("#000000"));
					AllTransactionFragementActivity.paid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.unpaid_mTextView.setTextColor(Color.parseColor("#9E9E9E"));
					AllTransactionFragementActivity.expired_mTextView.setTextColor(Color.parseColor("#000000"));
				}
			}
		});
		for (int i = 0; i < 3; i++) {
		}
	}

	public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
		Log.e("onTabSelected", String.valueOf(tab.getPosition()));
		this.mViewPager.setCurrentItem(tab.getPosition());
	}

	public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
	}
}
