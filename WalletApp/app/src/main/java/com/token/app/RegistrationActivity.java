package com.token.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.token.util.GlobalConstants;
import com.token.util.Validations;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class RegistrationActivity extends Activity implements OnClickListener {
    public Runnable registeronserver;
    Handler reg_serverHandler;
    GoogleCloudMessaging gcm;
    String regid;
    String civilID;
    EditText civilid_et;
    String confirmPwd_mString;
    EditText confirm_password_mEdittext;
    String email;
    EditText email_et;
    EditText first_name_et;
    String fname;
    Global global;
    Handler handler;
    EditText last_name_et;
    String lname;
    String number;
    EditText number_et;
    String password;
    EditText password_et;
    ProgressDialog pd;
    Button read_btn;
    Button register_btn;
    String res;
    Runnable signupRunnable;
    SharedPreferences sp;

    public RegistrationActivity() {
        this.res = "";
        this.regid = "";
        this.confirmPwd_mString = "";
        this.signupRunnable = new Runnable() {
            public void run() {
                try {
                    RegistrationActivity.this.res = WebServiceHandler.signupservice(RegistrationActivity.this, RegistrationActivity.this.fname, RegistrationActivity.this.lname,
                            RegistrationActivity.this.password, RegistrationActivity.this.email, RegistrationActivity.this.number, RegistrationActivity.this.civilID);
                } catch (Exception e) {
                    Log.e("Catch Exception", e.toString());
                }
                Message message = new Message();
                message.obj = RegistrationActivity.this.res;
                RegistrationActivity.this.handler.sendMessage(message);
            }
        };
        this.handler = new Handler() {
            public void handleMessage(Message message) {
                RegistrationActivity.this.pd.dismiss();
                if (message.obj.toString().equalsIgnoreCase("true")) {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putBoolean(GlobalConstants.ISLOGIN, true);
                    edit.putString(GlobalConstants.PREF_USERNAME, email);
                    edit.putString(GlobalConstants.PREF_PASSWORD, password);
                    edit.commit();
                    registerDeviceOnserver();
                    Toast.makeText(RegistrationActivity.this, "Signed Up Successfully", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(RegistrationActivity.this, MainTabActivity.class));
                    return;
                }
                Crouton.showText(RegistrationActivity.this, "Registeration failed!", Style.ALERT);
            }
        };
        this.registeronserver = new Runnable() {
            @SuppressLint({"NewApi"})
            public void run() {
                try {
                    if (regid.isEmpty()) {
                        registerInBackground();
                    }
                    sendRegistrationIdToBackend();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reg_serverHandler.sendMessage(new Message());
            }
        };
        reg_serverHandler = new Handler() {
            public void handleMessage(Message message) {
                Log.i("registered on server", "true");
            }
        };
    }

    String userid_pref;

    protected String sendRegistrationIdToBackend() {
        Log.e("device id", this.regid);
        String str = Build.MODEL;
        Log.e("model number", str);
        return WebServiceHandler.registerOnServer(this, this.userid_pref, this.password, this.regid, str, "0");
    }

    protected void storeRegistrationId(Context context, String str) {
        getGCMPreferences(context);
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(MainActivity.class.getSimpleName(), 0);
    }

    private void registerInBackground() {
        try {
            if (this.gcm == null) {
                this.gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            }
            this.regid = this.gcm.register("924054999022");
            Log.e("regid id", this.regid);
            SharedPreferences.Editor edit = this.sp.edit();
            edit.putString(GlobalConstants.PREF_DEVICEID, this.regid);
            edit.apply();
            storeRegistrationId(getApplicationContext(), this.regid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
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

    private void registerDeviceOnserver() {
        this.gcm = GoogleCloudMessaging.getInstance(this);
        this.regid = getRegistrationId(getApplicationContext());
        new Thread(null, this.registeronserver, "").start();
    }

    private void initializations() {
        this.first_name_et = (EditText) findViewById(R.id.signup_firstname_et);
        this.last_name_et = (EditText) findViewById(R.id.signup_lastname_et);
        this.number_et = (EditText) findViewById(R.id.signup_number_et);
        this.email_et = (EditText) findViewById(R.id.signup_email_et);
        this.password_et = (EditText) findViewById(R.id.signup_password_et);
        this.confirm_password_mEdittext = (EditText) findViewById(R.id.signup_confirmpassword_et);
        this.civilid_et = (EditText) findViewById(R.id.signup_civilid_et);
        this.read_btn = (Button) findViewById(R.id.read_terms);
        this.register_btn = (Button) findViewById(R.id.registerBtn);
        this.register_btn.setOnClickListener(this);
        this.email_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 0) {
                    RegistrationActivity.this.email_et.setError(Html.fromHtml("<font color='red'>Please Enter Your Email id</font>"));
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
        this.password_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 0) {
                    RegistrationActivity.this.password_et.setError(Html.fromHtml("<font color='red'>Please Enter Password</font>"));
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
        this.confirm_password_mEdittext.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 0) {
                    RegistrationActivity.this.confirm_password_mEdittext.setError(Html.fromHtml("<font color='red'>Please Enter Confirm Password</font>"));
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
        this.first_name_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 0) {
                    RegistrationActivity.this.first_name_et.setError(Html.fromHtml("<font color='red'>Please Enter your First Name</font>"));
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
        this.last_name_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 0) {
                    RegistrationActivity.this.last_name_et.setError(Html.fromHtml("<font color='red'>Please Enter your Last Name</font>"));
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
        this.civilid_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 0) {
                    RegistrationActivity.this.civilid_et.setError(Html.fromHtml("<font color='red'>Please Enter CPR Number</font>"));
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
        this.number_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() <= 0) {
                    RegistrationActivity.this.number_et.setError(Html.fromHtml("<font color='red'>Please Enter your Phone Number</font>"));
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn /* 2131034182 */:
                this.fname = this.first_name_et.getText().toString();
                this.password = this.password_et.getText().toString();
                this.confirmPwd_mString = this.confirm_password_mEdittext.getText().toString();
                this.lname = this.last_name_et.getText().toString();
                this.email = this.email_et.getText().toString();
                this.number = this.number_et.getText().toString();
                this.civilID = this.civilid_et.getText().toString();
                if (this.email.length() == 0) {
                    this.email_et.setError(Html.fromHtml("<font color='red'>Please Enter Your Email id</font>"));
                } else if (!Validations.checkEmail(this.email_et.getText().toString())) {
                    this.email_et.setError(Html.fromHtml("<font color='red'>Please Enter Correct Email Id</font>"));
                }
                if (this.password.length() == 0) {
                    this.password_et.setError(Html.fromHtml("<font color='red'>Please Enter Password</font>"));
                }
                if (this.confirmPwd_mString.length() == 0) {
                    this.confirm_password_mEdittext.setError(Html.fromHtml("<font color='red'>Please Enter Password for confirmation</font>"));
                }
                if (this.fname.length() == 0) {
                    this.first_name_et.setError(Html.fromHtml("<font color='red'>Please Enter your First Name</font>"));
                }
                if (this.lname.length() == 0) {
                    this.last_name_et.setError(Html.fromHtml("<font color='red'>Please Enter your Last Name</font>"));
                }
                if (this.civilID.length() == 0) {
                    this.civilid_et.setError(Html.fromHtml("<font color='red'>Please Enter CPR Number</font>"));
                }
                if (this.number.length() == 0) {
                    this.number_et.setError(Html.fromHtml("<font color='red'>Please Enter your Phone Number</font>"));
                    return;
                }
                this.pd = ProgressDialog.show(this, "", "Signup is in progress..Please wait");
                new Thread(null, this.signupRunnable, "").start();
            default:
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.registration);
        this.sp = getSharedPreferences(GlobalConstants.PREF, 0);
        this.global = (Global) getApplicationContext();
        initializations();
    }
}
