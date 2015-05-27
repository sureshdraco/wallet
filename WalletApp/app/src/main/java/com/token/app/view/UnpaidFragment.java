package com.token.app.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.token.app.R;
import com.token.app.ViewHolder;
import com.token.app.WalletApplication;
import com.token.app.network.WebServiceHandler;
import com.token.util.GlobalConstants;
import com.token.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class UnpaidFragment extends Fragment {
    TransactionAdapter adapter;
    WalletApplication global;
    ProgressDialog progressDialog;
    String res_mString;
    ListView showallTrans_mList;
    SharedPreferences sp;
    private BroadcastReceiver updateUnpaidListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
    private Runnable transactioninfoRunnable;

    public UnpaidFragment() {
        this.res_mString = "";
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.transactioninfoRunnable = new Runnable() {
            public void run() {
                try {
                    String string = sp.getString(GlobalConstants.PREF_USERNAME, "");
                    String string2 = sp.getString(GlobalConstants.PREF_PASSWORD, "");
                    res_mString = WebServiceHandler.transInfoService(getActivity(), string, string2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.reverse(global.getUnpaidList());
                        adapter = new TransactionAdapter(getActivity(), global.getUnpaidList());
                        showallTrans_mList.setAdapter(adapter);
                    }
                });
            }
        };
        View inflate = layoutInflater.inflate(R.layout.balance_screenlist, viewGroup, false);
        this.sp = getActivity().getSharedPreferences(GlobalConstants.PREFS_NAME, 0);
        this.global = (WalletApplication) getActivity().getApplicationContext();
        this.showallTrans_mList = (ListView) inflate.findViewById(R.id.paidlist);
        Collections.reverse(this.global.getUnpaidList());
        this.adapter = new TransactionAdapter(getActivity(), this.global.getUnpaidList());
        this.showallTrans_mList.setAdapter(this.adapter);
        this.showallTrans_mList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                global.setTrans_id_mInt(i);
                Intent intent = new Intent(getActivity(), ShowTrasactionInfoActivity.class);
                intent.putExtra(ShowTrasactionInfoActivity.TYPE, ShowTrasactionInfoActivity.UNPAID);
                startActivity(intent);
            }
        });
        return inflate;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(transactioninfoRunnable).start();
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
                viewHolder2.titleContainer = view2.findViewById(R.id.titleContainer);
                viewHolder2.amount = (TextView) view2.findViewById(R.id.trans_list_amount_tv);
                viewHolder2.payer = (TextView) view2.findViewById(R.id.trans_list_payername_tv);
                viewHolder2.paymentdate = (TextView) view2.findViewById(R.id.trans_list_date_tv);
                viewHolder2.datecomparator_mTextView = (TextView) view2.findViewById(R.id.trans_list_datecomparator_tv);
                view2.setTag(viewHolder2);
                viewHolder = viewHolder2;
                view = view2;
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.code.setText(((String) ((HashMap) this.transactionList.get(i)).get("transaction_code")));
            viewHolder.amount.setText("BD " + ((String) ((HashMap) this.transactionList.get(i)).get("transaction_amount")));
            viewHolder.payer.setText(((String) ((HashMap) this.transactionList.get(i)).get("transaction_payer")));
            viewHolder.paymentdate.setText(((String) ((HashMap) this.transactionList.get(i)).get("transaction_date")));
            viewHolder.titleContainer.setBackgroundColor(Color.parseColor("#F44336"));
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String str = (String) ((HashMap) this.transactionList.get(i)).get("transaction_date");
                Log.e("Date for Compare", str);
                long time = simpleDateFormat.parse(str).getTime() / 1000;
                Log.e("long outpute", String.valueOf(time));
                time = Long.parseLong(Long.toString(time)) * 1000;
                Log.e("long timestamp", String.valueOf(time));
                viewHolder.datecomparator_mTextView.setText(Utils.getFriendlyTime(new Date(time)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }
    }
}
