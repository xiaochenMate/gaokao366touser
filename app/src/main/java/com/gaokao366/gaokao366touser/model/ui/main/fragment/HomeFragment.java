package com.gaokao366.gaokao366touser.model.ui.main.fragment;

import android.os.Bundle;
import android.view.View;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseFragment;

/**
 * Created by hh on 2016/5/18.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void setContentLayout(Bundle savedInstanceState) {
        setContentView(R.layout.f_home);
    }


    @Override
    public void initView(View v) {


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onNetChanged(boolean oldStatus, boolean newStatus) {

    }

    @Override
    public void onClick(View v) {
    }

}
