package com.gaokao366.gaokao366touser.model.ui.login.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.framework.bean.UserInfo;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.network.RequestMaker;
import com.gaokao366.gaokao366touser.model.framework.parser.SubBaseParser;
import com.gaokao366.gaokao366touser.model.framework.spfs.SharedPrefHelper;
import com.gaokao366.gaokao366touser.model.framework.util.CommonUtil;
import com.gaokao366.gaokao366touser.model.framework.util.CrcUtil;
import com.gaokao366.gaokao366touser.model.ui.manager.UIManager;

/**
 * 登录
 *
 * @author --FY
 * @version 创建时间：2015-8-3 上午11:07:24
 */
public class LoginActivity extends BaseActivity {

    // Content View Elements

    private ImageView iv_top;
    private RelativeLayout rl_uname;
    private ImageView iv1;
    private EditText et_uname;
    private ImageView iv_uname;
    private RelativeLayout rl_psw;
    private ImageView iv2;
    private EditText et_psw;
    private ImageView iv_pwd;
    private Button bt_login;
    private TextView l_tv_findPsw;
    private TextView l_tv_register;
    private RelativeLayout rl_title;
    private RelativeLayout rl_back;
    private ImageView iv_back;
    private TextView tv_title;

    // End Of Content View Elements

    private void bindViews() {

        iv_top = (ImageView) findViewById(R.id.iv_top);
        rl_uname = (RelativeLayout) findViewById(R.id.rl_uname);
        iv1 = (ImageView) findViewById(R.id.iv1);
        et_uname = (EditText) findViewById(R.id.et_uname);
        iv_uname = (ImageView) findViewById(R.id.iv_uname);
        rl_psw = (RelativeLayout) findViewById(R.id.rl_psw);
        iv2 = (ImageView) findViewById(R.id.iv2);
        et_psw = (EditText) findViewById(R.id.et_psw);
        iv_pwd = (ImageView) findViewById(R.id.iv_pwd);
        bt_login = (Button) findViewById(R.id.bt_login);
        l_tv_findPsw = (TextView) findViewById(R.id.l_tv_findPsw);
        l_tv_register = (TextView) findViewById(R.id.l_tv_register);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_back.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        l_tv_findPsw.setOnClickListener(this);
        l_tv_register.setOnClickListener(this);
    }


    @Override
    public void setContentLayout() {
        setContentView(R.layout.l_login);
    }

    @Override
    public void initView() {
        bindViews();
    }

    /**
     * 返回
     *
     */
    public void doBack( ) {
        finish();
    }

    /**
     * 找回密码
     *
     */
    public void turnToFindPwd( ) {
        UIManager.turnToAct(this, FindPwdActivity.class);
    }

    /**
     * 注册
     *
     */
    public void turnToRegist( ) {
        UIManager.turnToAct(this, RegistActivity.class);
    }

    /**
     * 登录
     *
     */
    public void doLogin() {
        String mobile = et_uname.getText().toString().trim();
        String pwd = et_psw.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
        String md5Pwd = null;
        try {
            md5Pwd = CrcUtil.MD5(pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonUtil.closeSoftKeyboard(this,  et_uname);
        doLoginRequest(mobile, md5Pwd);
    }

    private void doLoginRequest(final String mobile, final String md5Pwd) {
        Request request = RequestMaker.getInstance().getLoginRequest(mobile, md5Pwd);
        getNetWorkDate(request, new SubBaseParser<UserInfo>(UserInfo.class), new OnCompleteListener<UserInfo>(this) {

            @Override
            public void onSuccessed(UserInfo result, String resultString) {
                //保存用户信息
                softApplication.setUserInfo(result);
                SharedPrefHelper.getInstance().setUserInfo(resultString);
                //SharedPrefHelper.getInstance().setMobile(mobile);
                //SharedPrefHelper.getInstance().setPwd(md5Pwd);
                //SoftApplication.softApplication.setAlias(result.id);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        if (resultCode == RESULT_OK) {
            String mobile = it.getStringExtra("mobile");
            String pwd = it.getStringExtra("pwd");
            et_uname.setText(mobile);
            et_psw.setText(pwd);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                doBack();
                break;
            case R.id.bt_login:
                doLogin();
                break;
            case R.id.l_tv_findPsw:
                turnToFindPwd();
                break;
            case R.id.l_tv_register:
                turnToRegist();
                break;
        }
    }

    @Override
    public void onNetChanged(boolean oldStatus, boolean newStatus) {

    }
}
