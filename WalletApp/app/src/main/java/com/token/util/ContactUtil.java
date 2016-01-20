package com.token.util;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by suresh.kumar on 19/01/16.
 */
public class ContactUtil {
	public static final String[] PHONENUMBER_PROJECTION = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
	private static final String TAG = "contactutil";

	private boolean containsPhoneNumber(ContentResolver contentResolver, String phoneNumber) {
		String[] projection = PHONENUMBER_PROJECTION;
		String where = ContactsContract.CommonDataKinds.Phone.NUMBER + "=?";
		Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, where, new String[] { phoneNumber }, null);
		if (cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}

	public String getRawContactId(ContentResolver contentResolver, String displayName) {
		String contactId = null;
		Cursor cursor = null;
		try {
			cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.DISPLAY_NAME + " = ?", new String[] { displayName },
					null);
		} catch (Exception e) {
		}

		if (cursor != null && cursor.moveToFirst()) {
			contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return contactId;
	}

	public void createContactForAccessNumber(ContentResolver contentResolver, String phoneNumber, String displayName) {
		if (!TextUtils.isEmpty(phoneNumber) && !containsPhoneNumber(contentResolver, phoneNumber)) {

			String rawContactId = getRawContactId(contentResolver, displayName);
			ArrayList<ContentProviderOperation> operations;

			if (TextUtils.isEmpty(rawContactId)) {

				operations = new ArrayList<ContentProviderOperation>();
				int rawContactInsertIndex = operations.size();

				operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
						.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
						.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
						.build());

				operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
						.build());

				operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
						.build());

			} else {

				operations = new ArrayList<ContentProviderOperation>();

				operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValue(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
						.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
						.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
						.build());
			}
			try {
				contentResolver.applyBatch(ContactsContract.AUTHORITY, operations);
			} catch (Exception e) {
				Log.e(TAG, e.toString(), e);
			}
		}
	}
}
