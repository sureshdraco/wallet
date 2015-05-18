package com.token.app;

import android.app.Application;
import java.util.ArrayList;
import java.util.HashMap;

public class Global extends Application {
    ArrayList<HashMap<String, String>> TransactioninfoList;
    String accountbalance;
    String authenticateid_gString;
    String crediturl_mString;
    String device_id;
    ArrayList<HashMap<String, String>> expireList;
    ArrayList<HashMap<String, String>> infoList;
    ArrayList<HashMap<String, String>> loginList;
    ArrayList<HashMap<String, String>> notificationSaveList;
    ArrayList<HashMap<String, String>> paidList;
    String paymessage;
    String token;
    ArrayList<HashMap<String, String>> tokenList;
    String tokencode;
    int trans_id_mInt;
    ArrayList<HashMap<String, String>> unpaidList;

    public Global() {
        this.accountbalance = "";
        this.tokencode = "";
        this.token = "";
        this.paymessage = "";
        this.crediturl_mString = "";
        this.device_id = "";
        this.trans_id_mInt = 0;
        this.loginList = new ArrayList();
        this.tokenList = new ArrayList();
        this.infoList = new ArrayList();
        this.TransactioninfoList = new ArrayList();
        this.notificationSaveList = new ArrayList();
        this.paidList = new ArrayList();
        this.unpaidList = new ArrayList();
        this.expireList = new ArrayList();
    }

    public String getAccountbalance() {
        return this.accountbalance;
    }

    public String getAuthenticateid_gString() {
        return this.authenticateid_gString;
    }

    public String getCrediturl_mString() {
        return this.crediturl_mString;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public ArrayList<HashMap<String, String>> getExpireList() {
        return this.expireList;
    }

    public ArrayList<HashMap<String, String>> getInfoList() {
        return this.infoList;
    }

    public ArrayList<HashMap<String, String>> getLoginList() {
        return this.loginList;
    }

    public ArrayList<HashMap<String, String>> getPaidList() {
        return this.paidList;
    }

    public String getPaymessage() {
        return this.paymessage;
    }

    public String getToken() {
        return this.token;
    }

    public ArrayList<HashMap<String, String>> getTokenList() {
        return this.tokenList;
    }

    public String getTokencode() {
        return this.tokencode;
    }

    public int getTrans_id_mInt() {
        return this.trans_id_mInt;
    }

    public ArrayList<HashMap<String, String>> getTransactioninfoList() {
        return this.TransactioninfoList;
    }

    public ArrayList<HashMap<String, String>> getUnpaidList() {
        return this.unpaidList;
    }

    public void setAccountbalance(String str) {
        this.accountbalance = str;
    }

    public void setAuthenticateid_gString(String str) {
        this.authenticateid_gString = str;
    }

    public void setCrediturl_mString(String str) {
        this.crediturl_mString = str;
    }

    public void setDevice_id(String str) {
        this.device_id = str;
    }

    public void setExpireList(ArrayList<HashMap<String, String>> arrayList) {
        this.expireList = arrayList;
    }

    public void setInfoList(ArrayList<HashMap<String, String>> arrayList) {
        this.infoList = arrayList;
    }

    public void setLoginList(ArrayList<HashMap<String, String>> arrayList) {
        this.loginList = arrayList;
    }
    public void setPaidList(ArrayList<HashMap<String, String>> arrayList) {
        this.paidList = arrayList;
    }

    public void setPaymessage(String str) {
        this.paymessage = str;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public void setTokenList(ArrayList<HashMap<String, String>> arrayList) {
        this.tokenList = arrayList;
    }

    public void setTokencode(String str) {
        this.tokencode = str;
    }

    public void setTrans_id_mInt(int i) {
        this.trans_id_mInt = i;
    }

    public void setTransactioninfoList(ArrayList<HashMap<String, String>> arrayList) {
        this.TransactioninfoList = arrayList;
    }

    public void setUnpaidList(ArrayList<HashMap<String, String>> arrayList) {
        this.unpaidList = arrayList;
    }
}
