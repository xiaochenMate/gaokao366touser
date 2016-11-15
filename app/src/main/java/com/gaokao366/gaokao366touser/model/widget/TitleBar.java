package com.gaokao366.gaokao366touser.model.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.util.LogUtil;


/**
 * 通用标题栏
 * Created by hh on 2016/5/17.
 */
public class TitleBar extends RelativeLayout implements View.OnClickListener {

    private Context ct;

    // Content View Elements

    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_right;


    public TitleBar(Context context) {
        this(context, null, -1);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ct = context;
        init();
    }

    private void init() {
        LogUtil.log("init");
        View view = View.inflate(ct, R.layout.m_title, this);
        bindViews(view);
    }

    // End Of Content View Elements

    private void bindViews(View view) {

        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_right = (TextView) view.findViewById(R.id.tv_right);
        iv_back.setOnClickListener(this);
        tv_title.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                doBack();
                break;
            case R.id.tv_right:
                break;
        }
    }

    private void doBack() {
        if (ct instanceof Activity)
            ((Activity) ct).finish();
    }

    public void setBack(boolean isShow) {
        if (isShow)
            iv_back.setVisibility(View.VISIBLE);
        else
            iv_back.setVisibility(View.GONE);

    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setTitle(int resId) {
        tv_title.setText(ct.getResources().getString(resId));
    }

    public void setTitleRight(String title) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(title);
    }

    public void setTitleRight(int resId) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(ct.getResources().getString(resId));
    }

    public void setOnRightclickListener(OnClickListener l){
        tv_right.setOnClickListener(l);
    }
}
