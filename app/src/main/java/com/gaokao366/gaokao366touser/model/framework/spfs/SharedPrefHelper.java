package com.gaokao366.gaokao366touser.model.framework.spfs;


import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.gaokao366.gaokao366touser.model.framework.application.SoftApplication;
import com.gaokao366.gaokao366touser.model.framework.bean.UserInfo;


public class SharedPrefHelper {
	/**
	 * SharedPreferences的名字
	 */
	private static final String SP_FILE_NAME = "APPLICATION_SP";
	private static SharedPrefHelper sharedPrefHelper = null;
	private static SharedPreferences sharedPreferences;
	/**
	 * 经度
	 */
	private static final String LONGITUDE = "LONGITUDE";
	/**
	 * 纬度
	 */
	private static final String LATITUDE = "LATITUDE";
	private static final String USER = "user";
	private static final String HELP = "help";

	public static synchronized SharedPrefHelper getInstance() {
		if (null == sharedPrefHelper) {
			sharedPrefHelper = new SharedPrefHelper();
		}
		return sharedPrefHelper;
	}

	private SharedPrefHelper() {
		sharedPreferences = SoftApplication.softApplication.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
	}

	public void setPhoneNumber(String phoneNumber) {
		sharedPreferences.edit().putString("phoneNumber", phoneNumber).commit();
	}

	public String getPhoneNumber() {
		return sharedPreferences.getString("phoneNumber", "");
	}

	public void setPassword(String password) {
		sharedPreferences.edit().putString("password", password).commit();
	}

	public String getPassword() {
		return sharedPreferences.getString("password", "");
	}

	public void setRememberAccount(boolean bool) {
		sharedPreferences.edit().putBoolean("rememberAccount", bool).commit();
	}

	public boolean isRememberAccount() {
		return sharedPreferences.getBoolean("rememberAccount", false);
	}

	/**
	 * 设置经度
	 * 
	 * @param longitude
	 */
	public void setLongitude(String longitude) {
		sharedPreferences.edit().putString(LONGITUDE, longitude).commit();
	}

	/**
	 * 获取经度
	 * 
	 * @return
	 */
	public String getLongitude() {
		return sharedPreferences.getString(LONGITUDE, "");
	}

	/**
	 * 设置纬度
	 */
	public void setLatitude(String latitude) {
		sharedPreferences.edit().putString(LATITUDE, latitude).commit();
	}

	/**
	 * 获取纬度
	 */
	public String getLatitude() {
		return sharedPreferences.getString(LATITUDE, "");
	}
	
	/**
	 * 设置账号
	 */
	public void setLoginAccount(String account) {
		sharedPreferences.edit().putString("account", account).commit();
	}
	/**
	 * 获取账号
	 */
	public String getLoginAccount() {
		return sharedPreferences.getString("account", "");
	}
	/**
	 * 设置密码
	 */
	public void setLoginPwd(String pwd) {
		sharedPreferences.edit().putString("pwd", pwd).commit();
	}
	/**
	 * 获取密码
	 */
	public String getLoginPwd() {
		return sharedPreferences.getString("pwd", "");
	}

	public void setUserInfo(String resultString) {
		sharedPreferences.edit().putString("userInfo", resultString).commit(); 
	}
	
	public UserInfo getUserInfo( ) {
		String jsonString = sharedPreferences.getString("userInfo", "");
		UserInfo info = null ;
		try {
			 info  = JSONObject.parseObject(jsonString,UserInfo.class);
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return info ; 
	}

	public void saveSchoolInfo(String resultString) {
		sharedPreferences.edit().putString("schoolInfo", resultString).commit(); 
	}
	
}
