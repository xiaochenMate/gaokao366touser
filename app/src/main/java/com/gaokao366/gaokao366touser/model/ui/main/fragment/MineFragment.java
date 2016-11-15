package com.gaokao366.gaokao366touser.model.ui.main.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseFragment;
import com.gaokao366.gaokao366touser.model.ui.login.activity.LoginActivity;
import com.gaokao366.gaokao366touser.model.ui.manager.UIManager;
import com.gaokao366.gaokao366touser.model.ui.setting.activity.SettingActivity;
import com.gaokao366.gaokao366touser.model.widget.MySettingView;

/**
 * Created by hh on 2016/5/18.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    // Content View Elements

    private RelativeLayout m_titleBar;
    private ImageView m_iv_head;
    private TextView m_tv_name;
    private TextView m_tv_setting;
    private com.gaokao366.gaokao366touser.model.widget.MySettingView s_personInfo;
    private com.gaokao366.gaokao366touser.model.widget.MySettingView s_myOrder;
    private com.gaokao366.gaokao366touser.model.widget.MySettingView s_myChecked;
    private com.gaokao366.gaokao366touser.model.widget.MySettingView s_onLineCheckIn;
    private com.gaokao366.gaokao366touser.model.widget.MySettingView s_feedBack;
    private com.gaokao366.gaokao366touser.model.widget.MySettingView s_complain;
    private com.gaokao366.gaokao366touser.model.widget.MySettingView s_onlineCallCenter;

    // End Of Content View Elements

    private void bindViews(View v) {

        m_titleBar = (RelativeLayout) v.findViewById(R.id.m_titleBar);
        m_iv_head = (ImageView) v.findViewById(R.id.m_iv_head);
        m_tv_name = (TextView) v.findViewById(R.id.m_tv_name);
        m_tv_setting = (TextView) v.findViewById(R.id.m_tv_setting);
        s_personInfo = (MySettingView) v.findViewById(R.id.s_personInfo);
        s_myOrder = (MySettingView) v.findViewById(R.id.s_myOrder);
        s_myChecked = (MySettingView) v.findViewById(R.id.s_myChecked);
        s_onLineCheckIn = (MySettingView) v.findViewById(R.id.s_onLineCheckIn);
        s_feedBack = (MySettingView) v.findViewById(R.id.s_feedBack);
        s_complain = (MySettingView) v.findViewById(R.id.s_complain);
        s_onlineCallCenter = (MySettingView) v.findViewById(R.id.s_onlineCallCenter);

        m_iv_head.setOnClickListener(this);
        m_tv_setting.setOnClickListener(this);
        s_personInfo.setOnClickListener(this);
        s_myOrder.setOnClickListener(this);
        s_myChecked.setOnClickListener(this);
        s_onLineCheckIn.setOnClickListener(this);
        s_feedBack.setOnClickListener(this);
        s_complain.setOnClickListener(this);
        s_onlineCallCenter.setOnClickListener(this);
    }


    @Override
    public void setContentLayout(Bundle savedInstanceState) {
        setContentView(R.layout.f_mine);
    }
    @Override
    public void initView(View v) {
        bindViews(v);

        s_personInfo.setResInit(R.string.personInfo, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_feedBack.setResInit(R.string.feedBack, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_myOrder.setResInit(R.string.myOrder, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_myChecked.setResInit(R.string.myCheck, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_onLineCheckIn.setResInit(R.string.onlineCheckIn, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_complain.setResInit(R.string.complain, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_onlineCallCenter.setResInit(R.string.onlineCallCenter, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
    }


    @Override
    public void onNetChanged(boolean oldStatus, boolean newStatus) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_tv_setting:
                UIManager.turnToAct(getActivity(), SettingActivity.class);
                break;
            case R.id.m_iv_head:
                UIManager.turnToAct(getActivity(), LoginActivity.class);
                break;
            case R.id.s_personInfo:
                break;
            case R.id.s_myOrder:
                break;
            case R.id.s_myChecked:
                break;
            case R.id.s_onLineCheckIn:
                break;
            case R.id.s_feedBack:
                break;
            case R.id.s_complain:
                break;
            case R.id.s_onlineCallCenter:
                break;
        }
    }
}
