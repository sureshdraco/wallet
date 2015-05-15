package com.token.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActionBar extends RelativeLayout {
    private TextView centerText;
    private ImageView left_mBtn;
    private Context mContext;
    private ImageView right_mBtn;
    private ImageView search_mBtn;
    private ImageView share_mBtn;

    public ActionBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        RelativeLayout relativeLayout = (RelativeLayout) ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.actionbar, null);
        this.centerText = (TextView) relativeLayout.findViewById(R.id.text_tv);
        addView(relativeLayout);
    }

    public void setActionText(String str) {
        this.centerText.setText(str);
    }
}
