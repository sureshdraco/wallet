package com.token.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fouad.yaseen on 8/21/2014. Code taken and modified from: http://stackoverflow.com/questions/2201925/converting-iso-8601-compliant-string-to-java-util-date
 */
public class TimestampUtil {

	public static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String FAST_BIRD_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	private static final int DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
	private static final int HOURS_IN_MILLIS = 1000 * 60 * 60;

	public static String getFastBirdDateString(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FAST_BIRD_DATE_FORMAT);
		try {
			return simpleDateFormat.format(ISO8601.toDate(date));
		} catch (ParseException e) {
			return "";
		}
	}

	public static Date getIso8601Date(String date) throws ParseException {
		return ISO8601.toDate(date);
	}

	public static Date getFastbirdDate(String date) throws ParseException {
		return FastBirdFormat.toDate(date);
	}

	public static String getDaysBetween(Date date1, Date date2) {
		int days = (int) ((date1.getTime() - date2.getTime()) / DAY_IN_MILLIS);
		if (days <= 1) {
			return (int) ((date1.getTime() - date2.getTime()) / HOURS_IN_MILLIS) + " h";
		} else if (days >= 365) {
			return (days / 365) + " y";
		} else if (days >= 120) {
			return (days / 30) + " m";
		} else if (days >= 14) {
			return (days / 14) + " w";
		} else {
			return days + " days";
		}
	}

	public static String getElapsedTimeForNotification(Date date1, Date date2) {
		int days = (int) ((date1.getTime() - date2.getTime()) / DAY_IN_MILLIS);
		if (days <= 1) {
			int hours = (int) ((date1.getTime() - date2.getTime()) / HOURS_IN_MILLIS);
			if (hours == 0) {
				return "Just now";
			} else {
				return hours + " hour ago";
			}
		} else if (days >= 365) {
			return (days / 365) + " year ago";
		} else if (days >= 120) {
			return (days / 30) + " month ago";
		} else if (days >= 14) {
			return (days / 14) + " week ago";
		} else {
			return days + " days ago";
		}
	}

	/**
	 * Helper class for handling ISO 8601 strings of the following format: "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
	 */
	public static class ISO8601 {
		/**
		 * Transform ISO 8601 string to Date.
		 */
		public static Date toDate(final String iso8601string)
				throws ParseException {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_8601_FORMAT);
			return simpleDateFormat.parse(iso8601string);
		}
	}

	public static class FastBirdFormat {
		/**
		 * Transform FastBirdFormat string to Date.
		 */
		public static Date toDate(final String iso8601string)
				throws ParseException {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FAST_BIRD_DATE_FORMAT);
			return simpleDateFormat.parse(iso8601string);
		}
	}
}
