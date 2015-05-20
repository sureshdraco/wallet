package com.token.util;

public class GlobalConstants {
	public static final String PREFS_NAME = "pref";
	public static String ISLOGIN = null;
	public static String LOGIN_ACCOUNTNUMBER = null;
	public static String LOGIN_EMAIL = null;
	public static String LOGIN_FULLNAME = null;
	public static String LOGIN_ID = null;
	public static String LOGIN_MOBILE = null;
	public static String LOGIN_MONEY_TYPE = null;
	public static String LOGIN_PASSWORD = null;
	public static String LOGIN_URL = null;
	public static String LOGIN_USERNAME = null;
	public static String PREF = null;
	public static String PREF_DEVICEID;
	public static String PREF_PASSWORD;
	public static String PREF_USERNAME;
	public static String SIGNUP_CIVILID;
	public static String SIGNUP_EMAIL;
	public static String SIGNUP_FIRSTNAME;
	public static String SIGNUP_LASTNAME;
	public static String SIGNUP_NUMBER;
	public static String SIGNUP_PASSWORD;
	public static String SIGNUP_URL;
	public static String TOKEN_AMOUNT;
	public static String TOKEN_COMPANYNAME;
	public static String TOKEN_CREATIONDATE;
	public static String TOKEN_CREATOR;
	public static String TOKEN_DATE;
	public static String TOKEN_LOCATION;

	static {
		LOGIN_USERNAME = "email";
		LOGIN_PASSWORD = "password";
		LOGIN_URL = "http://walletgcc.com/wallet/public/usersmaster/signin";
		LOGIN_ID = "id";
		LOGIN_FULLNAME = "full_name_master";
		LOGIN_EMAIL = "email";
		LOGIN_MOBILE = "mobile";
		LOGIN_MONEY_TYPE = "send_money_type";
		LOGIN_ACCOUNTNUMBER = "account_number";
		SIGNUP_URL = "http://walletgcc.com/wallet/public/usersmaster/saveapi";
		SIGNUP_FIRSTNAME = "first_name";
		SIGNUP_LASTNAME = "last_name";
		SIGNUP_EMAIL = "email";
		SIGNUP_PASSWORD = "password";
		SIGNUP_CIVILID = "cpr_number";
		SIGNUP_NUMBER = "mobile";
		TOKEN_CREATOR = "creator";
		TOKEN_AMOUNT = "amount";
		TOKEN_COMPANYNAME = "company_name";
		TOKEN_LOCATION = "company_location";
		TOKEN_CREATIONDATE = "creation_date";
		TOKEN_DATE = "date";
		PREF = PREFS_NAME;
		ISLOGIN = "islogin";
		PREF_USERNAME = "username";
		PREF_DEVICEID = "deviceid";
		PREF_PASSWORD = "password";
	}
}
