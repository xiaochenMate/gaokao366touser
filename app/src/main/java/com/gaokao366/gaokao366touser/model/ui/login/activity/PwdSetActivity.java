package com.gaokao366.gaokao366touser.model.ui.login.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.framework.application.SoftApplication;
import com.gaokao366.gaokao366touser.model.framework.bean.SubBaseResponse;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.network.RequestMaker;
import com.gaokao366.gaokao366touser.model.framework.parser.SubBaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.CommonUtil;
import com.gaokao366.gaokao366touser.model.framework.util.CrcUtil;
import com.gaokao366.gaokao366touser.model.widget.TitleBar;

public class PwdSetActivity extends BaseActivity {


    // Content View Elements

    private TitleBar titlebar;
    private EditText et_password;
    private EditText et_repassword;
    private Button btn_ok;

    private String mobile;// 用户名,手机号


    @Override
    public void setContentLayout() {
        setContentView(R.layout.l_pwd_set);
    }

    @Override
    public void initView() {

        bindViews();
        titlebar.setTitle("设置新密码");
        titlebar.setBack(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobile = extras.getString("mobile");
        }
    }

    // End Of Content View Elements

    private void bindViews() {

        titlebar = ( TitleBar) findViewById(R.id.titlebar);
        et_password = (EditText) findViewById(R.id.et_password);
        et_repassword = (EditText) findViewById(R.id.et_repassword);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    public void doRegist() {
        doCheckPwdIsEquas();
    }

    /**
     * 校验密码
     *
     * @return
     */
    private boolean doCheckPwdIsEquas() {
        String pwd1 = et_password.getText().toString().trim();
        String pwd2 = et_repassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            showToast("请输入密码");
            return false;
        } else if (pwd1.length() < 6) {
            showToast("密码长度不能小于6位");
            return false;
        } else if (pwd1.length() > 20) {
            showToast("密码长度不能大于20位");
            return false;
        }
        if (!pwd1.equals(pwd2)) {
            showToast("密码输入不一致");
            return false;
        }
        try {
            pwd1 = CrcUtil.MD5(pwd1);
            pwd2 = CrcUtil.MD5(pwd2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        CommonUtil.closeSoftKeyboard(this, et_password);
        doOK(mobile, pwd1, pwd2);
        return true;
    }

    /**
     * 完成注册
     *
     * @param pwd2
     * @param pwd1
     * @param mobile
     */
    private void doOK(String mobile, String pwd1, String pwd2) {
        showProgressDialog();
        Request request = RequestMaker.getInstance().getForgetRequest(mobile, pwd1, pwd2);
        getNetWorkDate(request, new SubBaseParser<SubBaseResponse>(SubBaseResponse.class), new OnCompleteListener<SubBaseResponse>(this) {
            @Override
            public void onSuccessed(SubBaseResponse result, String resultString) {
                showToast("密码设置成功");
                for (Activity act : SoftApplication.unDestroyActivityList) {
                    if (!(act instanceof LoginActivity)) {
                        act.finish();
                    }
                }
            }

            @Override
            public void onCompleted(SubBaseResponse result, String resultString) {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onNetChanged(boolean oldStatus, boolean newStatus) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                doRegist();
                break;
        }
    }
}
