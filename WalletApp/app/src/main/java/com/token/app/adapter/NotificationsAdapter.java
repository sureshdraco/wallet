package com.token.app;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.token.util.ImageCacheManager;
import com.token.util.TimestampUtil;

/**
 * Created by suresh on 18/10/14.
 */
public class NotificationsAdapter extends ArrayAdapter<NotificationItem> {
	private final Context context;
	private final List<NotificationItem> menuItems;

	public NotificationsAdapter(Context context, List<NotificationItem> menuItems) {
		super(context, R.layout.notifications_row, menuItems);
		this.context = context;
		this.menuItems = menuItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null || !ViewHolder.class.isInstance(convertView.getTag())) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.notifications_row, null);
			viewHolder = new ViewHolder();
			viewHolder.notificationTitle = (TextView) convertView.findViewById(R.id.notificationTitle);
			viewHolder.notificationContent = (TextView) convertView.findViewById(R.id.notificationContent);
			viewHolder.notificationDate = (TextView) convertView.findViewById(R.id.notificationDate);
			viewHolder.notifImage = (NetworkImageView) convertView.findViewById(R.id.notifImage);
			convertView.setTag(viewHolder);
		} else {
			try {
				viewHolder = (ViewHolder) convertView.getTag();
			} catch (ClassCastException e) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.notifications_row, null);
				viewHolder = new ViewHolder();
				viewHolder.notificationTitle = (TextView) convertView.findViewById(R.id.notificationTitle);
				viewHolder.notificationContent = (TextView) convertView.findViewById(R.id.notificationContent);
				viewHolder.notificationDate = (TextView) convertView.findViewById(R.id.notificationDate);
				viewHolder.notifImage = (NetworkImageView) convertView.findViewById(R.id.notifImage);
				convertView.setTag(viewHolder);
			}
		}
		if (viewHolder.notificationContent != null) {
			NotificationItem menuItem = menuItems.get(position);
			viewHolder.notificationContent.setText(menuItem.getNotificationMessage());
			viewHolder.notificationTitle.setText(menuItem.getTitle());
			try {
				viewHolder.notificationDate.setText(TimestampUtil.getElapsedTimeForNotification(new Date(), TimestampUtil.getFastbirdDate(menuItem.getDate())));
			} catch (Exception e) {
				viewHolder.notificationDate.setText("");
			}
			viewHolder.notifImage.setDefaultImageResId(R.drawable.icon);
			viewHolder.notifImage.setImageUrl(menuItem.getImageUrl(), ImageCacheManager.getInstance().getImageLoader());
		}
		return convertView;
	}

	static class ViewHolder {
		private TextView notificationTitle, notificationContent, notificationDate;
		private NetworkImageView notifImage;
	}
}
