package com.gaokao366.gaokao366touser.model.ui.main.fragment;

import android.os.Bundle;
import android.view.View;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseFragment;
import com.gaokao366.gaokao366touser.model.widget.TitleBar;


/**
 * Created by hh on 2016/5/18.
 */
public class TripFragment extends BaseFragment {

    private TitleBar m_titleBar;

    @Override
    public void setContentLayout(Bundle savedInstanceState) {
        setContentView(R.layout.f_trip);
    }
    @Override
    public void initView(View v) {
        m_titleBar = (TitleBar) v.findViewById(R.id.m_titleBar);
        m_titleBar.setTitle("行程");
    }

    @Override
    public void onNetChanged(boolean oldStatus, boolean newStatus) {

    }
}
