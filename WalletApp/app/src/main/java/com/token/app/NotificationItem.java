package com.token.app;

/**
 * Created by suresh on 18/10/14.
 */
public class NotificationItem {
    private String notificationMessage;
    private String date, title, imageUrl, fullMessage;

    public NotificationItem( String notificationMessage, String date, String title, String imageUrl, String fullMessage) {
        this.notificationMessage = notificationMessage;
        this.date = date;
        this.title = title;
        this.imageUrl = imageUrl;
        this.fullMessage = fullMessage;
        this.seen = false;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFullMessage() {
        return fullMessage;
    }
	private boolean seen;

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}
}
