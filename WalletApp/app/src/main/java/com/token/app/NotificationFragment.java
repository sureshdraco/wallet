package com.token.app;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NotificationFragment extends Fragment {

	private ListView notificationsListView;
	private ArrayList<NotificationItem> recentNotificationItemArrayList = new ArrayList<NotificationItem>();
	private ArrayList<NotificationItem> olderNotificationItemArrayList = new ArrayList<NotificationItem>();
	private NotificationsAdapter recentNotificationsAdapter, olderNotificationsAdapter;
	private View clearNotifBtn;
	private SeparatedNotifListAdapter adapter;
	private ArrayList<NotificationItem> notificationItemArrayList;
	private Handler handler;
	BroadcastReceiver notificationsBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (getActivity() != null) {
				updateNotifications();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.notification_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		handler = new Handler();
		initview();
		updateNotifications();
	}

	private void initList() {
		notificationsListView = (ListView) getActivity().findViewById(R.id.notificationsList);
		adapter = new SeparatedNotifListAdapter(getActivity());
		recentNotificationsAdapter = new NotificationsAdapter(getActivity(), recentNotificationItemArrayList);
		olderNotificationsAdapter = new NotificationsAdapter(getActivity(), olderNotificationItemArrayList);
		notificationsListView.setAdapter(adapter);
	}

	private void initview() {
		clearNotifBtn = getActivity().findViewById(R.id.clearnotify_mTextView);
		recentNotificationItemArrayList = new ArrayList<>();
		clearNotifBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PreferenceUtil.saveNotificationsList(getActivity(), "[]");
				NotificationUtil.clearNotifications(getActivity());
				updateNotifications();
			}
		});
		initList();
	}

	private void updateNotifications() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				adapter = new SeparatedNotifListAdapter(getActivity());
				notificationsListView.setAdapter(adapter);
				recentNotificationItemArrayList.clear();
				olderNotificationItemArrayList.clear();
				Type listType = new TypeToken<ArrayList<NotificationItem>>() {
				}.getType();
				notificationItemArrayList = new Gson().fromJson(PreferenceUtil.getNotificationList(getActivity()), listType);
				recentNotificationsAdapter.clear();
				olderNotificationsAdapter.clear();

				// split notifications
				for (NotificationItem notificationItem : notificationItemArrayList) {
					if (notificationItem.isSeen()) {
						olderNotificationItemArrayList.add(notificationItem);
					} else {
						recentNotificationItemArrayList.add(notificationItem);
					}
				}
				if (recentNotificationItemArrayList != null && !recentNotificationItemArrayList.isEmpty()) {
					adapter.addSection("RECENT", recentNotificationsAdapter);
				}
				if (olderNotificationItemArrayList != null && !olderNotificationItemArrayList.isEmpty()) {
					adapter.addSection("OLDER", olderNotificationsAdapter);
				}
				recentNotificationsAdapter.notifyDataSetChanged();
				olderNotificationsAdapter.notifyDataSetChanged();
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onPause() {
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(notificationsBroadcastReceiver);
		super.onPause();
		// update all notifications as seen
		updateAllNotificationsAsSeen();
	}

	private void updateAllNotificationsAsSeen() {
		if (notificationItemArrayList != null) {
			for (NotificationItem notificationItem : notificationItemArrayList) {
				notificationItem.setSeen(true);
			}
			PreferenceUtil.saveNotificationsList(getActivity(), new Gson().toJson(notificationItemArrayList));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		PreferenceUtil.resetNotificationCount(getActivity());
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(notificationsBroadcastReceiver, new IntentFilter("NOTIFICATIONS_UPDATED_BROADCAST"));
	}
}