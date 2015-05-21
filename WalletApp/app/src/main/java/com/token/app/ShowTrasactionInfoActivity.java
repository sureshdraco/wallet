package com.token.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class ShowTrasactionInfoActivity extends Activity {
	public static final String TYPE = "type";
	public static final int EXPIRED = 1;
	public static final int PAID = 2;
	public static final int UNPAID = 3;
	TextView code_txt;
	TextView created_txt;
	TextView creator_txt;
	TextView date_txt;
	Global global;
	TextView paid_amount_txt;
	TextView payer_txt;
	TextView percentage_txt;
	TextView status_txt;
	TextView trans_amount_txt;
	TextView updated_txt;
	TextView userid_txt;

	public void onBackPressed() {
		this.global.setTrans_id_mInt(0);
		super.onBackPressed();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.show_trasaction_info);
		this.global = (Global) getApplicationContext();
		this.userid_txt = (TextView) findViewById(R.id.trans_userid_txt);
		this.date_txt = (TextView) findViewById(R.id.trans_date_txt);
		this.code_txt = (TextView) findViewById(R.id.trans_code_txt);
		this.creator_txt = (TextView) findViewById(R.id.trans_creator_txt);
		this.payer_txt = (TextView) findViewById(R.id.trans_payer_txt);
		this.trans_amount_txt = (TextView) findViewById(R.id.trans_amount_txt);
		this.percentage_txt = (TextView) findViewById(R.id.trans_percentage_txt);
		this.paid_amount_txt = (TextView) findViewById(R.id.trans_paid_txt);
		this.updated_txt = (TextView) findViewById(R.id.trans_updated_txt);
		this.created_txt = (TextView) findViewById(R.id.trans_created_txt);
		this.status_txt = (TextView) findViewById(R.id.trans_status_txt);
		HashMap<String, String> info = new HashMap<>();
		switch (getIntent().getIntExtra(TYPE, EXPIRED)) {
		case EXPIRED:
			info = global.getExpireList().get(global.getTrans_id_mInt());
			break;
		case UNPAID:
			info = global.getUnpaidList().get(global.getTrans_id_mInt());
			break;
		case PAID:
			info = global.getPaidList().get(global.getTrans_id_mInt());
			break;
		}
		this.userid_txt.setText(info.get("transaction_user_id"));
		this.date_txt.setText(info.get("transaction_date"));
		this.code_txt.setText(info.get("transaction_code"));
		this.creator_txt.setText(info.get("transaction_creator"));
		this.payer_txt.setText(info.get("transaction_payer"));
		this.trans_amount_txt.setText(info.get("transaction_amount"));
		this.percentage_txt.setText(info.get("percentage_amount"));
		this.paid_amount_txt.setText(info.get("paid_amount"));
		this.updated_txt.setText(info.get("updated_at"));
		this.created_txt.setText(info.get("created_at"));
		this.status_txt.setText(info.get("status"));
	}
}
