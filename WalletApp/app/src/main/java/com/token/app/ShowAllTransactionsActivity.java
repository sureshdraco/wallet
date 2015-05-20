package com.token.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.token.util.GlobalConstants;

public class ShowAllTransactionsActivity extends Activity {
	TransactionAdapter adapter;
	Global global;
	ProgressDialog progressDialog;
	String res_mString;
	ListView showallTrans_mList;
	SharedPreferences sp;
	Runnable transactioninfoRunnable;
	Handler transinfohandler;

	public ShowAllTransactionsActivity() {
		this.res_mString = "";
		this.transactioninfoRunnable = new Runnable() {
			public void run() {
				try {
					String string = ShowAllTransactionsActivity.this.sp.getString(GlobalConstants.PREF_USERNAME, "");
					String string2 = ShowAllTransactionsActivity.this.sp.getString(GlobalConstants.PREF_PASSWORD, "");
					ShowAllTransactionsActivity.this.res_mString = WebServiceHandler.transInfoService(ShowAllTransactionsActivity.this, string, string2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
				message.obj = ShowAllTransactionsActivity.this.res_mString;
				ShowAllTransactionsActivity.this.transinfohandler.sendMessage(message);
			}
		};
		this.transinfohandler = new Handler() {
			public void handleMessage(Message message) {
				ShowAllTransactionsActivity.this.progressDialog.dismiss();
				if (message.obj.toString().equalsIgnoreCase("true")) {
					ShowAllTransactionsActivity.this.adapter = new TransactionAdapter(ShowAllTransactionsActivity.this,
							ShowAllTransactionsActivity.this.global.getTransactioninfoList());
					ShowAllTransactionsActivity.this.showallTrans_mList.setAdapter(ShowAllTransactionsActivity.this.adapter);
					return;
				}
				Toast.makeText(ShowAllTransactionsActivity.this, "Error Occured due to some server problem!!", Toast.LENGTH_LONG).show();
			}
		};
	}

	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.balance_screenlist);
		this.sp = getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
		this.global = (Global) getApplicationContext();
		this.showallTrans_mList = (ListView) findViewById(R.id.paidlist);
		this.progressDialog = ProgressDialog.show(this, "", "Please wait...");
		new Thread(null, this.transactioninfoRunnable, "").start();
		this.showallTrans_mList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
				ShowAllTransactionsActivity.this.global.setTrans_id_mInt(i);
				ShowAllTransactionsActivity.this.startActivity(new Intent(ShowAllTransactionsActivity.this, ShowTrasactionInfoActivity.class));
			}
		});
	}

	class TransactionAdapter extends BaseAdapter {
		List<String> arrayList;
		Context mContext;
		LayoutInflater mInflater;
		ArrayList<HashMap<String, String>> transactionList;

		public TransactionAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
			this.mContext = context;
			this.mInflater = LayoutInflater.from(context);
			this.transactionList = arrayList;
		}

		public int getCount() {
			return this.transactionList.size();
		}

		public Object getItem(int i) {
			return this.transactionList;
		}

		public long getItemId(int i) {
			return 0;
		}

		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder viewHolder;
			ViewHolder viewHolder2 = new ViewHolder();
			if (view == null) {
				View view2 = (RelativeLayout) this.mInflater.inflate(R.layout.transaction_list_item, null);
				viewHolder2.code = (TextView) view2.findViewById(R.id.trans_list_code_tv);
				viewHolder2.amount = (TextView) view2.findViewById(R.id.trans_list_amount_tv);
				view2.setTag(viewHolder2);
				viewHolder = viewHolder2;
				view = view2;
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			viewHolder.code.setText("Transaction code: " + ((String) ((HashMap) this.transactionList.get(i)).get("transaction_code")));
			viewHolder.amount.setText("Transaction amount: " + ((String) ((HashMap) this.transactionList.get(i)).get("transaction_amount")));
			return view;
		}
	}
}
