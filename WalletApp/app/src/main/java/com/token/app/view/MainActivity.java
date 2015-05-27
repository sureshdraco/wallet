package com.token.app.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.token.app.WalletApplication;
import com.token.app.R;
import com.token.app.network.WebServiceHandler;
import com.token.util.GlobalConstants;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends Activity implements OnClickListener {
    public Runnable registeronserver;
    Context context;
    Dialog dialog;
    String email_fp_mString;
    TextView forgot_text;
    Handler fphandler;
    GoogleCloudMessaging gcm;
    WalletApplication global;
    Handler handler;
    Runnable infoRunnable;
    Runnable loginRunnable;
    Button login_btn;
    AtomicInteger msgId;
    String password;
    EditText password_et;
    String password_pref;
    ProgressDialog pd;
    Handler reg_serverHandler;
    String regid;
    String res;
    Button signup_btn;
    SharedPreferences sp;
    String userid_pref;
    String username;
    EditText username_et;

    public MainActivity() {
        this.res = "";
        this.regid = "";
        this.msgId = new AtomicInteger();
        this.userid_pref = "";
        this.password_pref = "";
        this.email_fp_mString = "";
        this.loginRunnable = new Runnable() {
            public void run() {
                try {
                    MainActivity.this.res = WebServiceHandler.loginservice(MainActivity.this, MainActivity.this.username, MainActivity.this.password);
                } catch (Exception e) {
                }
                Message message = new Message();
                message.obj = MainActivity.this.res;
                MainActivity.this.handler.sendMessage(message);
            }
        };
        this.handler = new Handler() {
            public void handleMessage(Message message) {
                MainActivity.this.pd.dismiss();
                if (message.obj.toString().equalsIgnoreCase("true")) {
                    MainActivity.this.userid_pref = (String) ((HashMap) MainActivity.this.global.getLoginList().get(0)).get(GlobalConstants.LOGIN_EMAIL);
                    MainActivity.this.password_pref = (String) ((HashMap) MainActivity.this.global.getLoginList().get(0)).get(GlobalConstants.LOGIN_PASSWORD);
                    Editor edit = MainActivity.this.sp.edit();
                    edit.putBoolean(GlobalConstants.ISLOGIN, true);
                    edit.putString(GlobalConstants.PREF_USERNAME, MainActivity.this.userid_pref);
                    edit.putString(GlobalConstants.PREF_PASSWORD, MainActivity.this.password);
                    edit.commit();
                    MainActivity.this.RegisterDeviceOnserver();
                    Toast.makeText(MainActivity.this, "Logged in  Successfully", Toast.LENGTH_LONG).show();
                    MainActivity.this.finish();
                    MainActivity.this.startActivity(new Intent(MainActivity.this, MainTabActivity.class));
                    return;
                }
                Crouton.showText(MainActivity.this, "Please Enter Correct Username and Password", Style.ALERT);
            }
        };
        this.registeronserver = new Runnable() {
            @SuppressLint({"NewApi"})
            public void run() {
                try {
                    if (MainActivity.this.regid.isEmpty()) {
                        MainActivity.this.registerInBackground();
                    }
                    MainActivity.this.sendRegistrationIdToBackend();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainActivity.this.reg_serverHandler.sendMessage(new Message());
            }
        };
        this.reg_serverHandler = new Handler() {
            public void handleMessage(Message message) {
                Log.i("registered on server", "true");
            }
        };
        this.infoRunnable = new Runnable() {
            public void run() {
                try {
                    MainActivity.this.res = WebServiceHandler.forgotpasswordService(MainActivity.this, MainActivity.this.username_et.getText().toString(),
                            MainActivity.this.email_fp_mString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = MainActivity.this.res;
                MainActivity.this.fphandler.sendMessage(message);
            }
        };
        this.fphandler = new Handler() {
            public void handleMessage(Message message) {
                MainActivity.this.pd.dismiss();
                String obj = message.obj.toString();
                if (obj.equalsIgnoreCase("true")) {
                    Crouton.showText(MainActivity.this, "Password has been sent to your Mobile !!", Style.ALERT);
                } else if (obj.equalsIgnoreCase("false")) {
                    Crouton.showText(MainActivity.this, "Your username and mobile combination was incorrect!!", Style.ALERT);
                } else {
                    Crouton.showText(MainActivity.this, "Some problem occured!! Please try again later", Style.ALERT);
                }
            }
        };
    }

    private void initializations() {
        this.username_et = (EditText) findViewById(R.id.username_et);
        this.password_et = (EditText) findViewById(R.id.password_et);
        this.forgot_text = (TextView) findViewById(R.id.forgot_password);
        this.login_btn = (Button) findViewById(R.id.loginBtn);
        this.signup_btn = (Button) findViewById(R.id.registerBtn);
        this.forgot_text.setOnClickListener(this);
        this.login_btn.setOnClickListener(this);
        this.signup_btn.setOnClickListener(this);
    }

    private void RegisterDeviceOnserver() {
        this.gcm = GoogleCloudMessaging.getInstance(this);
        this.regid = getRegistrationId(this.context);
        new Thread(null, this.registeronserver, "").start();
    }

    private int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(), 0);
    }

    @SuppressLint({"NewApi"})
    private String getRegistrationId(Context context) {
        SharedPreferences gCMPreferences = getGCMPreferences(context);
        String string = gCMPreferences.getString("regid", "");
        if (string.isEmpty()) {
            return "";
        }
        if (gCMPreferences.getInt("APP_VERSION", ExploreByTouchHelper.INVALID_ID) == getAppVersion(context)) {
            return string;
        }
        Log.i("", "App version changed.");
        return "";
    }

    private void registerInBackground() {
        try {
            if (this.gcm == null) {
                this.gcm = GoogleCloudMessaging.getInstance(this.context);
            }
            this.regid = this.gcm.register("924054999022");
            Log.e("regid id", this.regid);
            Editor edit = this.sp.edit();
            edit.putString(GlobalConstants.PREF_DEVICEID, this.regid);
            edit.apply();
            storeRegistrationId(this.context, this.regid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword() {
        this.pd = ProgressDialog.show(this, "", "Sending password to your mail id...Please wait");
        new Thread(null, this.infoRunnable, "").start();
    }

    public void onBackPressed() {
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn /* 2131034181 */:
                this.username = this.username_et.getText().toString();
                this.password = this.password_et.getText().toString();
                if (this.username.length() == 0) {
                    Crouton.showText(MainActivity.this, "Please Enter UserName", Style.ALERT);
                } else if (this.password_et.length() == 0) {
                    Crouton.showText(MainActivity.this, "Please Enter Password", Style.ALERT);
                } else {
                    this.pd = ProgressDialog.show(this, "", "Please wait...App is loading");
                    new Thread(null, this.loginRunnable, "").start();
                }
                break;
            case R.id.registerBtn /* 2131034182 */:
                finish();
                startActivity(new Intent(this, RegistrationActivity.class));
                break;
            case R.id.forgot_password /* 2131034183 */:
                this.username = this.username_et.getText().toString();
                if (this.username.length() == 0) {
                    Crouton.showText(MainActivity.this, "Pleae enter Your Email id first to get forgot Password", Style.ALERT);
                    return;
                }
                onClickForgetPassword();
                break;
            default:
        }
    }

    public void onClickForgetPassword() {
        // Creating alert Dialog with one Button
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final View changePasswordDialog = LayoutInflater.from(this).inflate(R.layout.change_password, null);
        // Setting Dialog Title
        alertDialog.setTitle("Forget Password");
        alertDialog.setView(changePasswordDialog);
        alertDialog.setPositiveButton("Get Password", null);
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        final AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = alertDialog1.getButton(AlertDialog.BUTTON_POSITIVE);
                final EditText mobileNumber = (EditText) changePasswordDialog.findViewById(R.id.mobileNumber);
                b.setOnClickListener(new AnonymousClass8(mobileNumber));
            }
        });
        alertDialog1.show();
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.sp = getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
        this.global = (WalletApplication) getApplicationContext();
        if (this.sp.getBoolean(GlobalConstants.ISLOGIN, false)) {
            finish();
            startActivity(new Intent(this, MainTabActivity.class));
            return;
        }
        setContentView(R.layout.main);
        initializations();
    }

    protected String sendRegistrationIdToBackend() {
        Log.e("device id", this.regid);
        String str = Build.MODEL;
        Log.e("model number", str);
        return WebServiceHandler.registerOnServer(this, this.userid_pref, this.password, this.regid, str, "0");
    }

    protected void storeRegistrationId(Context context, String str) {
        getGCMPreferences(context);
        Log.i("", "Saving regId on app version " + getAppVersion(context));
    }

    /* renamed from: com.token.app.view.MainActivity.8 */
    class AnonymousClass8 implements OnClickListener {
        private final/* synthetic */ EditText val$amount_mEditBox;

        AnonymousClass8(EditText editText) {
            this.val$amount_mEditBox = editText;
        }

        public void onClick(View view) {
            MainActivity.this.email_fp_mString = this.val$amount_mEditBox.getText().toString();
            MainActivity.this.forgotPassword();
        }
    }

}
