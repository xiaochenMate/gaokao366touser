package com.gaokao366.gaokao366touser.model.ui.login.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.framework.bean.SubBaseResponse;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.network.RequestMaker;
import com.gaokao366.gaokao366touser.model.framework.parser.SubBaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.CommonUtil;
import com.gaokao366.gaokao366touser.model.framework.util.StringUtil;
import com.gaokao366.gaokao366touser.model.framework.util.VerifyCheck;
import com.gaokao366.gaokao366touser.model.ui.manager.UIManager;
import com.gaokao366.gaokao366touser.model.widget.TitleBar;


public class FindPwdActivity extends BaseActivity {

	// Content View Elements

	private TitleBar titlebar;
	private EditText et_phone;
	private EditText et_code;
	private Button bt_code;
	private Button btn_next;

	// End Of Content View Elements
	private String mobile;// 用户名,手机号
	private String captcha;// 验证码
	private Handler handler = new Handler();
	public static final int MAX_TIME = 60;
	private int totalSecond = MAX_TIME;// 按钮 60秒内不能点击




	@Override
	public void setContentLayout() {
		setContentView(R.layout.l_forget_pwd);
	}

	@Override
	public void initView() {
		bindViews();
		titlebar.setTitle("找回密码");
		titlebar.setBack(true);
	}
	private void bindViews() {

		titlebar = (TitleBar) findViewById(R.id.titlebar);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_code = (EditText) findViewById(R.id.et_code);
		bt_code = (Button) findViewById(R.id.bt_code);
		btn_next = (Button) findViewById(R.id.btn_next);
		bt_code.setOnClickListener(this);
		btn_next.setOnClickListener(this);
	}

	public void doGetCode() {
		mobile = et_phone.getText().toString();
		if (!isPhoneChecked(mobile)) {
			return;
		}
		doGetCodeRequest(mobile);
	}

	public void doNext() {
		captcha = et_code.getText().toString().trim();
		if(!isCodeChecked(captcha)) return ; 
		CommonUtil.closeSoftKeyboard(this, et_phone);
		doNextRequest(mobile, captcha);
	}

	private void doTurnToNext() {
		Bundle bundle = new Bundle();
		bundle.putString("mobile", mobile);
		bundle.putString("captcha", captcha);
		UIManager.turnToAct(this, PwdSetActivity.class, bundle);
	}

	/**
	 * 手机号校验
	 * 
	 * @param mobile
	 * @return
	 */
	private boolean isPhoneChecked(String mobile) {
		if (StringUtil.isNullOrEmpty(mobile)) {
			showToast("请输入手机号");
			return false;
		}
		if (!VerifyCheck.isMobilePhoneVerify(mobile)) {
			showToast("请输入正确的手机号码");
			return false;
		}
		return true;
	}

	/**
	 * 验证码校验
	 * 
	 * @param code
	 * @return
	 */
	private boolean isCodeChecked(String code) {
		if (StringUtil.isNullOrEmpty(code)) {
			showToast("请输入验证码");
			return false;
		}
		return true;
	}

	/**
	 * 获取验证码
	 * 
	 * @param mobile
	 */
	private void doGetCodeRequest(String mobile) {
		bt_code.setClickable(false);
		Request request = RequestMaker.getInstance().getCodeForgetPwd(mobile);
		getNetWorkDate(request, new SubBaseParser<SubBaseResponse>(SubBaseResponse.class), new OnCompleteListener<SubBaseResponse>(this) {
			@Override
			public void onSuccessed(SubBaseResponse result, String resultString) {
				doTimer() ; 
			}

			@Override
			public void onCompleted( SubBaseResponse result, String resultString) {
				bt_code.setClickable(true);
				dismissProgressDialog() ; 
			}
		});
	}

	/**
	 * 下一步
	 * 
	 * @param mobile
	 */
	private void doNextRequest(String mobile, String captcha) {
		showProgressDialog();
		Request request = RequestMaker.getInstance().getNextRequest(mobile, captcha);
		getNetWorkDate(request, new SubBaseParser<SubBaseResponse>(SubBaseResponse.class), new OnCompleteListener<SubBaseResponse>(this) {

			@Override
			public void onSuccessed(SubBaseResponse result, String resultString) {
				doTurnToNext();
			}

			@Override
			public void onCompleted( SubBaseResponse result, String resultString) {
				dismissProgressDialog() ;
			}

		});
	}

	private void doTimer() {
		if (runnable == null) {
			runnable = new MyRunnable();
		}
		handler.post(runnable);
		bt_code.setEnabled(false);
		bt_code.setClickable(false);
	}

	void stopTimmer(){
		if(runnable!=null){
			handler.removeCallbacks(runnable);
			runnable=null;
		}
	}
	
	
	public MyRunnable runnable;
	private String brithday;
	private String weightString;


	public class MyRunnable implements Runnable {

		@SuppressLint("NewApi")
		@Override
		public void run() {
			handler.postDelayed(runnable, 1000);
			bt_code.setText( totalSecond + "s"  );
			totalSecond--;
			if (totalSecond < 0) {
				totalSecond = MAX_TIME;
				handler.removeCallbacks(runnable);
				// 倒计时完成后让按钮可点击
				bt_code.setEnabled(true);
				bt_code.setClickable(true);
				bt_code.setText("重新获取");
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.bt_code:
				doGetCode();
				break ;
			case R.id.btn_next:
				doNext();
				break ;
		}
	}

	@Override
	public void onNetChanged(boolean oldStatus, boolean newStatus) {

	}

}
