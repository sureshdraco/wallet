package com.token.app;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowTrasactionInfoActivity extends Activity {
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
		this.userid_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("transaction_user_id"));
		this.date_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("transaction_date"));
		this.code_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("transaction_code"));
		this.creator_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("transaction_creator"));
		this.payer_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("transaction_payer"));
		this.trans_amount_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("transaction_amount"));
		this.percentage_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("percentage_amount"));
		this.paid_amount_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("paid_amount"));
		this.updated_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("updated_at"));
		this.created_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("created_at"));
		this.status_txt.setText((CharSequence) ((HashMap) this.global.getTransactioninfoList().get(this.global.getTrans_id_mInt())).get("status"));
	}
}
