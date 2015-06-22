package com.token.app.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.token.app.R;
import com.token.app.WalletApplication;
import com.token.app.network.WebServiceHandler;
import com.token.util.GlobalConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class BalanceActivity extends Activity implements OnClickListener {
    private final Handler reportHandler;
    Runnable accountRunnable;
    String amount_mString;
    Handler amounthandler;
    Runnable authenticateRunnable;
    Handler authenticatehandler;
    RelativeLayout balance_mRelativeLayout;
    ProgressDialog buyauthenticateProgressDialog;
    Dialog buycreditDialog;
    ProgressDialog buycreditProgressDialog;
    Runnable buycreditRunnable;
    Button buycredit_mBtn;
    String buycredit_mString;
    Handler buycredithandler;
    Dialog dialog;
    String email_mString;
    WalletApplication global;
    Handler handler;
    Runnable withdrawRunnable;
    TextView mybalancetext;
    String password_mString;
    ProgressDialog pd;
    String res;
    String res_mString;
    SharedPreferences sp;
    Button withdrawl_mBtn;
    private SwipeRefreshLayout swipeRefresh;
    private Runnable transactionReportRunnable;

    public BalanceActivity() {
        this.res = "";
        this.amount_mString = "";
        this.res_mString = "";
        this.transactionReportRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String email = BalanceActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
                    String password = BalanceActivity.this.sp.getString(GlobalConstants.PREF_PASSWORD, "");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    BalanceActivity.this.res_mString = WebServiceHandler.transactionReport(BalanceActivity.this, email, password, "2014-05-01", dateFormat.format(new Date()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = BalanceActivity.this.res_mString;
                BalanceActivity.this.reportHandler.sendMessage(message);
            }
        };
        this.accountRunnable = new Runnable() {
            public void run() {
                try {
                    String string = BalanceActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
                    BalanceActivity.this.res = WebServiceHandler.accountBalanceservice(BalanceActivity.this, string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = BalanceActivity.this.res;
                BalanceActivity.this.handler.sendMessage(message);
            }
        };
        this.handler = new Handler() {
            public void handleMessage(Message message) {
                BalanceActivity.this.pd.dismiss();
                String obj = message.obj.toString();
                if (BalanceActivity.this.pd.isShowing()) {
                    BalanceActivity.this.pd.dismiss();
                }
                if (!obj.equalsIgnoreCase("true")) {
                    Crouton.showText(BalanceActivity.this, "Error Occured due to some server problem!!", Style.ALERT);
                } else {
                    updateBalance();
                }
                swipeRefresh.setRefreshing(false);
            }
        };
        this.reportHandler = new Handler() {
            public void handleMessage(Message message) {
                BalanceActivity.this.buyauthenticateProgressDialog.dismiss();
                String obj = message.obj.toString();
                if (BalanceActivity.this.pd.isShowing()) {
                    BalanceActivity.this.pd.dismiss();
                }
                if (!obj.equalsIgnoreCase("true")) {
                    Crouton.showText(BalanceActivity.this, "Error Occured due to some server problem!!", Style.ALERT);
                    return;
                }
                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/wallet_transaction_report.pdf");  // -> filename = maven.pdf
                Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(BalanceActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                }
            }
        };
        this.withdrawRunnable = new Runnable() {
            public void run() {
                try {
                    String string = BalanceActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
                    BalanceActivity.this.res_mString = WebServiceHandler.withdrawalService(BalanceActivity.this, string, BalanceActivity.this.amount_mString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = BalanceActivity.this.res_mString;
                BalanceActivity.this.amounthandler.sendMessage(message);
            }
        };
        this.amounthandler = new Handler() {
            public void handleMessage(Message message) {
                BalanceActivity.this.pd.dismiss();
                BalanceActivity.this.dialog.dismiss();
                if (message.obj.toString().equalsIgnoreCase("true")) {
                    Crouton.showText(BalanceActivity.this, "WithDrawal Done Successfully!!", Style.CONFIRM);
                    BalanceActivity.this.pd = ProgressDialog.show(BalanceActivity.this, "", "Loading Balance..Please wait");
                    new Thread(null, BalanceActivity.this.accountRunnable, "").start();
                    return;
                }
                Crouton.showText(BalanceActivity.this, "Please contact the company to confirm the account to be able to withdraw", Style.ALERT);
            }
        };
        this.authenticateRunnable = new Runnable() {
            public void run() {
                try {
                    BalanceActivity.this.res_mString = WebServiceHandler.buyAuthenticateService(BalanceActivity.this, BalanceActivity.this.email_mString,
                            BalanceActivity.this.password_mString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = BalanceActivity.this.res_mString;
                BalanceActivity.this.authenticatehandler.sendMessage(message);
            }
        };
        this.authenticatehandler = new Handler() {
            public void handleMessage(Message message) {
                BalanceActivity.this.buyauthenticateProgressDialog.dismiss();
                if (message.obj.toString().equalsIgnoreCase("true")) {
                    BalanceActivity.this.openBuyCredit();
                } else {
                    Crouton.showText(BalanceActivity.this, "Error Occured  to authenticate credentials!!", Style.ALERT);
                }
            }
        };
        this.buycreditRunnable = new Runnable() {
            public void run() {
                // get selected radio button from radioGroup
                int selectedId = ((RadioGroup) buycreditDialog.findViewById(R.id.radioGroup)).getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) buycreditDialog.findViewById(selectedId);
                String option;
                if (radioButton.getText().equals("Credit Card")) {
                    option = "credimax";
                } else {
                    option = "debitcard";
                }

                try {
                    String authenticateid_gString = BalanceActivity.this.global.getAuthenticateid_gString();
                    Log.e("User Id", authenticateid_gString);
                    BalanceActivity.this.res_mString = WebServiceHandler.buycreditService(BalanceActivity.this, authenticateid_gString, BalanceActivity.this.amount_mString, "1",
                            option);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = BalanceActivity.this.res_mString;
                BalanceActivity.this.buycredithandler.sendMessage(message);
            }
        };
        this.buycredithandler = new Handler() {
            public void handleMessage(Message message) {
                BalanceActivity.this.buycreditProgressDialog.dismiss();
                if (message.obj.toString().equalsIgnoreCase("true")) {
                    BalanceActivity.this.startActivityForResult(new Intent(BalanceActivity.this, BuyCreditViewActivity.class), 1);
                } else {
                    Crouton.showText(BalanceActivity.this, "Error Occured  to authenticate credentials!!", Style.ALERT);
                }
            }
        };
    }

    private void updateBalance() {
        if (BalanceActivity.this.global.getAccountbalance().equalsIgnoreCase("")) {
            BalanceActivity.this.mybalancetext.setText("BD 0");
        } else {
            BalanceActivity.this.mybalancetext.setText("BD " + BalanceActivity.this.global.getAccountbalance());
            BalanceActivity.this.mybalancetext.setTextColor(SupportMenu.CATEGORY_MASK);
        }
    }

    public void BuyCreditAuthenticate() {
        this.buyauthenticateProgressDialog = ProgressDialog.show(this, "", "Please wait");
        new Thread(null, this.authenticateRunnable, "").start();
    }

    public void generateTransactionReport() {
        this.buyauthenticateProgressDialog = ProgressDialog.show(this, "", "Please wait");
        new Thread(null, this.transactionReportRunnable, "").start();
    }

    public void withdrawlDialog() {
        this.dialog = new Dialog(this, 16973840);
        this.dialog.setContentView(R.layout.withdrawl_dialog);
        Button button = (Button) this.dialog.findViewById(R.id.withdraw_send);
        EditText editText = (EditText) this.dialog.findViewById(R.id.amount_et);
        ((Button) this.dialog.findViewById(R.id.withdrawl_cancel)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BalanceActivity.this.dialog.dismiss();
            }
        });
        button.setOnClickListener(new AnonymousClass10(editText));
        this.dialog.show();
    }

    public void buyCreditAPI() {
        this.buycreditProgressDialog = ProgressDialog.show(this, "", "Please wait");
        new Thread(null, this.buycreditRunnable, "").start();
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case R.styleable.MapAttrs_mapType /* 0 */:
                new Thread(null, this.accountRunnable, "").start();
                break;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.balance_witdrawl_btn /* 2131034134 */:
                withdrawlDialog();
                break;
            case R.id.balance_buycrdit_btn /* 2131034135 */:
                BuyCreditAuthenticate();
                break;
            case R.id.transactionReport /* 2131034135 */:
                generateTransactionReport();
                break;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.balance);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        this.global = (WalletApplication) getApplicationContext();
        this.sp = getSharedPreferences(GlobalConstants.PREF, 0);
        this.email_mString = this.sp.getString(GlobalConstants.PREF_USERNAME, "");
        this.password_mString = this.sp.getString(GlobalConstants.PREF_PASSWORD, "");
        this.mybalancetext = (TextView) findViewById(R.id.mybalance_text);
        this.withdrawl_mBtn = (Button) findViewById(R.id.balance_witdrawl_btn);
        this.buycredit_mBtn = (Button) findViewById(R.id.balance_buycrdit_btn);
        this.balance_mRelativeLayout = (RelativeLayout) findViewById(R.id.balane_layout);
        this.pd = ProgressDialog.show(this, "", "Loading data..Please wait");
        new Thread(null, this.accountRunnable, "").start();
        this.withdrawl_mBtn.setOnClickListener(this);
        this.buycredit_mBtn.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(null, BalanceActivity.this.accountRunnable, "").start();
            }
        });
        updateBalance();
        findViewById(R.id.transactionReport).setOnClickListener(this);
    }

    public void openBuyCredit() {
        this.buycreditDialog = new Dialog(this, 16973840);
        this.buycreditDialog.setContentView(R.layout.buycredit);
        Button button = (Button) this.buycreditDialog.findViewById(R.id.buycredit_send);
        EditText editText = (EditText) this.buycreditDialog.findViewById(R.id.buycredit_amount_et);
        ((Button) this.buycreditDialog.findViewById(R.id.buycredit_cancel)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BalanceActivity.this.buycreditDialog.dismiss();
            }
        });
        button.setOnClickListener(new AnonymousClass14(editText));
        this.buycreditDialog.show();
    }

    public void transactionInfoDialog() {
        this.dialog = new Dialog(this, 16973840);
        this.dialog.setContentView(R.layout.withdrawl_dialog);
        Button button = (Button) this.dialog.findViewById(R.id.withdraw_send);
        EditText editText = (EditText) this.dialog.findViewById(R.id.amount_et);
        ((Button) this.dialog.findViewById(R.id.withdrawl_cancel)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BalanceActivity.this.dialog.dismiss();
            }
        });
        button.setOnClickListener(new AnonymousClass12(editText));
        this.dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void withDraw() {
        this.pd = ProgressDialog.show(this, "", "Withdrawal is in process..Please wait");
        new Thread(null, this.withdrawRunnable, "").start();
    }

    /* renamed from: com.token.app.view.BalanceActivity.10 */
    class AnonymousClass10 implements OnClickListener {
        private final/* synthetic */ EditText val$amount_mEditBox;

        AnonymousClass10(EditText editText) {
            this.val$amount_mEditBox = editText;
        }

        public void onClick(View view) {
            BalanceActivity.this.amount_mString = this.val$amount_mEditBox.getText().toString();
            BalanceActivity.this.withDraw();
        }
    }

    /* renamed from: com.token.app.view.BalanceActivity.12 */
    class AnonymousClass12 implements OnClickListener {
        private final/* synthetic */ EditText val$amount_mEditBox;

        AnonymousClass12(EditText editText) {
            this.val$amount_mEditBox = editText;
        }

        public void onClick(View view) {
            if (amount_mString.equals("0")) {
                Crouton.showText(BalanceActivity.this, "0 Credit not possible!", Style.ALERT);
                return;
            }
            BalanceActivity.this.amount_mString = this.val$amount_mEditBox.getText().toString();
            BalanceActivity.this.withDraw();
        }
    }

    /* renamed from: com.token.app.view.BalanceActivity.14 */
    class AnonymousClass14 implements OnClickListener {
        private final/* synthetic */ EditText val$amount_mEditBox;

        AnonymousClass14(EditText editText) {
            this.val$amount_mEditBox = editText;
        }

        public void onClick(View view) {
            if (amount_mString.equals("0")) {
                Crouton.showText(BalanceActivity.this, "0 Credit not possible!", Style.ALERT);
                return;
            }
            BalanceActivity.this.amount_mString = this.val$amount_mEditBox.getText().toString();

            BalanceActivity.this.buyCreditAPI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new Thread(accountRunnable).start();
    }
}