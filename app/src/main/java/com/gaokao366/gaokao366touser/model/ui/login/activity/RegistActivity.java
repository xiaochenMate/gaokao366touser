package com.gaokao366.gaokao366touser.model.ui.login.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.framework.bean.SubBaseResponse;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.network.RequestMaker;
import com.gaokao366.gaokao366touser.model.framework.parser.SubBaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.CommonUtil;
import com.gaokao366.gaokao366touser.model.framework.util.CrcUtil;
import com.gaokao366.gaokao366touser.model.framework.util.StringUtil;
import com.gaokao366.gaokao366touser.model.framework.util.VerifyCheck;

public class RegistActivity extends BaseActivity {


	// Content View Elements

	private RelativeLayout rl_uname;
	private ImageView iv1;
	private EditText et_phone;
	private ImageView iv_uname;
	private RelativeLayout rl_code;
	private ImageView iv_code;
	private ImageView iv2;
	private EditText et_code;
	private Button btn_getcode;
	private RelativeLayout rl_psw;
	private ImageView iv3;
	private EditText et_password;
	private ImageView iv_pwd;
	private RelativeLayout rl_repsw;
	private ImageView iv4;
	private EditText et_repassword;
	private ImageView iv_repwd;
	private Button bt_regist;
	private RelativeLayout rl_title;
	private RelativeLayout rl_back;
	private ImageView iv_back;
	private TextView tv_title;

	private Handler handler = new Handler();
	public static final  int MAX_TIME = 60;// 按钮 60秒内不能点击
	private int totalSecond = MAX_TIME;// 按钮 60秒内不能点击

	@Override
	public void setContentLayout() {
		setContentView(R.layout.l_regist);
	}


	@Override
	public void initView() {
		bindViews();
	}


	// End Of Content View Elements

	private void bindViews() {

		rl_uname = (RelativeLayout) findViewById(R.id.rl_uname);
		iv1 = (ImageView) findViewById(R.id.iv1);
		et_phone = (EditText) findViewById(R.id.et_phone);
		iv_uname = (ImageView) findViewById(R.id.iv_uname);
		rl_code = (RelativeLayout) findViewById(R.id.rl_code);
		iv_code = (ImageView) findViewById(R.id.iv_code);
		iv2 = (ImageView) findViewById(R.id.iv2);
		et_code = (EditText) findViewById(R.id.et_code);
		btn_getcode = (Button) findViewById(R.id.btn_getcode);
		rl_psw = (RelativeLayout) findViewById(R.id.rl_psw);
		iv3 = (ImageView) findViewById(R.id.iv3);
		et_password = (EditText) findViewById(R.id.et_password);
		iv_pwd = (ImageView) findViewById(R.id.iv_pwd);
		rl_repsw = (RelativeLayout) findViewById(R.id.rl_repsw);
		iv4 = (ImageView) findViewById(R.id.iv4);
		et_repassword = (EditText) findViewById(R.id.et_repassword);
		iv_repwd = (ImageView) findViewById(R.id.iv_repwd);
		bt_regist = (Button) findViewById(R.id.bt_regist);
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);

		rl_back.setOnClickListener(this);
		bt_regist.setOnClickListener(this);
		btn_getcode.setOnClickListener(this);
	}


	/**
	 * 返回
	 */
	public void doBack( ) {
		finish() ; 
	}
	/**
	 * 获取验证码
	 */
	public void doGetCode( ) {
		String mobile = et_phone.getText().toString();
		if(!isPhoneChecked(mobile)){
			return ; 
		}
		doGetCodeRequest(mobile);
	}

	/**
	 * 注册
	 */
	public void doRegist( ) {
		String mobile = et_phone.getText().toString();
		String captcha = et_code.getText().toString().trim() ; 
		String pwd1 = et_password.getText().toString().trim();
		String pwd2 = et_repassword.getText().toString().trim();
		if(!isPhoneChecked(mobile)){
			return; 
		}
		if(!isOtherChecked(captcha,pwd1,pwd2)){
			return; 
		}
		
		CommonUtil.closeSoftKeyboard(this, et_phone);
		doResistRequest(mobile,captcha,pwd1) ;
	}


	/**
	 * 手机号校验
	 * @param mobile
	 * @return
	 */
	private boolean isPhoneChecked(String mobile){
		if (StringUtil.isNullOrEmpty(mobile)) {
			showToast("请输入手机号");
			return false;
		}
		if (!VerifyCheck.isMobilePhoneVerify(mobile)) {
			showToast("请输入正确的手机号码");
			return false;
		}
		return true ; 
	}
	
	/**
	 * 其他校验
	 * @param captcha
	 * @param pwd1
	 * @param pwd2
	 * @return
	 */
	private boolean isOtherChecked(String captcha,String pwd1,String pwd2){
		if(TextUtils.isEmpty(captcha)){
			showToast("请填写验证码") ; 
			return false ; 
		}
		if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
			showToast("请输入密码");
			return false;
		} else if(pwd1.length()<6){
			showToast("密码长度不能小于6位");
			return false;
		} else if(pwd1.length()>20){
			showToast("密码长度不能大于20位");
			return false;
		}
		
		if (!pwd1.equals(pwd2)) {
			showToast("密码输入不一致");
			return false;
		}
		return true ; 
	}
	
	
	/**
	 * 获取验证码
	 * @param mobile
	 */
	private void doGetCodeRequest(String mobile) {
		btn_getcode.setClickable(false);
		Request request = RequestMaker.getInstance().getCodeRequest(mobile);
		getNetWorkDate(request, new SubBaseParser<SubBaseResponse>(SubBaseResponse.class),new OnCompleteListener<SubBaseResponse>(this){
			@Override
			public void onSuccessed(SubBaseResponse result, String resultString) {
				doTimer();
			}

			@Override
			public void onCompleted(SubBaseResponse result, String resultString) {
				btn_getcode.setClickable(true);
			}
			
		});
	}
	
	/**
	 * 完成注册
	 * @param pwd  
	 * @param captcha 
	 * @param mobile 
	 */
	private void doResistRequest(final String mobile, String captcha, final String pwd ) {
		String md5Pwd=null ; 
		try {
			md5Pwd = CrcUtil.MD5(pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Request request = RequestMaker.getInstance().getRegistRequest(mobile,captcha,md5Pwd);
		getNetWorkDate(request, new SubBaseParser<SubBaseResponse>(SubBaseResponse.class),new OnCompleteListener<SubBaseResponse>(this){
			@Override
			public void onSuccessed(SubBaseResponse result, String resultString) {
				showToast("注册成功");
				Intent it = new Intent();
				it.putExtra("mobile", mobile);
				it.putExtra("pwd", pwd);
				setResult(RESULT_OK);
				finish() ;
			}

		});
	}
	
	private void doTimer() {
		if (runnable == null) {
			runnable = new MyRunnable();
		}
		handler.post(runnable);
		btn_getcode.setEnabled(false);
		btn_getcode.setClickable(false);
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
			btn_getcode.setText(  totalSecond + "s"  );
			totalSecond--;
			if (totalSecond < 0) {
				totalSecond = MAX_TIME;
				handler.removeCallbacks(runnable);
				// 倒计时完成后让按钮可点击
				btn_getcode.setEnabled(true);
				btn_getcode.setClickable(true);
				btn_getcode.setText("重新获取");
			}
		}
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.rl_back:
				doBack();
				break;
			case R.id.btn_getcode:
				doGetCode();
				break;
			case R.id.bt_regist:
				doRegist();
				break;
		}
	}
	@Override
	public void onNetChanged(boolean oldStatus, boolean newStatus) {
		
	}

}
