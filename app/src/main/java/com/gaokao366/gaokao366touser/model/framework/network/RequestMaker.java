package com.gaokao366.gaokao366touser.model.framework.network;


import com.alibaba.fastjson.JSONObject;
import com.gaokao366.gaokao366touser.model.framework.application.SoftApplication;

import java.util.HashMap;
import java.util.Map;

public class RequestMaker {

	private static final String AUTH = "auth";
	private static final String INFO = "info";
	private SoftApplication softApplication;

	private RequestMaker() {
		softApplication = SoftApplication.softApplication;
	}

	private static RequestMaker requestMaker = null;

	/**
	 * 得到JsonMaker的实例
	 * 
	 * @return
	 */
	public static RequestMaker getInstance() {
		if (requestMaker == null) {
			requestMaker = new RequestMaker();
			return requestMaker;
		} else {
			return requestMaker;
		}
	}



	/**
	 * 登录
	 * 
	 * @param type
	 * @return
	 */
	public Request getVersionRequest(int type ) {
		Request request = null;
		try {

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("type", type+"");

			request = new Request(ServerInterfaceDefinition.OPT_version, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}


	public Request getCodeForgetPwd(String mobile) {
		Request request = null;
		try {
			JSONObject object = new JSONObject();
			object.put("mobile", mobile);
			String info = object.toJSONString();
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put(INFO, info);
			paramsMap.put(AUTH, SoftApplication.softApplication.getAuthJsonObject(info));

			request = new Request(ServerInterfaceDefinition.OPT_GETCODE_FORGETPWD, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}
	public Request getNextRequest(String mobile, String captcha) {
		Request request = null;
		try {
			JSONObject object = new JSONObject();
			object.put("mobile", mobile);
			object.put("capacha", captcha);
			String info = object.toJSONString();
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put(INFO, info);
			paramsMap.put(AUTH, SoftApplication.softApplication.getAuthJsonObject(info));

			request = new Request(ServerInterfaceDefinition.OPT_NEXT, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	public Request getForgetRequest(String mobile, String pwd1, String pwd2) {
		Request request = null;
		try {
			JSONObject object = new JSONObject();
			object.put("mobile", mobile);
			object.put("pwd1", pwd1);
			object.put("pwd2", pwd2);
			String info = object.toJSONString();
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put(INFO, info);
			paramsMap.put(AUTH, SoftApplication.softApplication.getAuthJsonObject(info));

			request = new Request(ServerInterfaceDefinition.OPT_SET_PWD, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * 登录
	 *
	 * @param mobile
	 * @param pwd
	 * @return
	 */
	public Request getLoginRequest(String mobile, String pwd) {
		Request request = null;
		try {
			JSONObject object = new JSONObject();
			object.put("mobile", mobile);
			object.put("pwd", pwd);
			String info = object.toJSONString();

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put(INFO, info);
			paramsMap.put(AUTH, SoftApplication.softApplication.getAuthJsonObject(info));

			request = new Request(ServerInterfaceDefinition.OPT_LOGIN, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	public Request getCodeRequest(String mobile) {
		Request request = null;
		try {
			JSONObject object = new JSONObject();
			object.put("mobile", mobile);
			String info = object.toJSONString();

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put(INFO, info);
			paramsMap.put(AUTH, SoftApplication.softApplication.getAuthJsonObject(info));

			request = new Request(ServerInterfaceDefinition.OPT_GETCODE, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * 注册
	 * @param mobile
	 * @param captcha
	 * @param pwd
     * @return
     */
	public Request getRegistRequest(String mobile, String captcha, String pwd) {
		Request request = null;
		try {
			JSONObject object = new JSONObject();
			object.put("mobile", mobile);
			object.put("capacha", captcha);
			object.put("pwd", pwd);
			String info = object.toJSONString();

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put(INFO, info);
			paramsMap.put(AUTH, SoftApplication.softApplication.getAuthJsonObject(info));

			request = new Request(ServerInterfaceDefinition.OPT_REGIST, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	public Request getUpdateRequest(int type) {
		Request request = null;
		try {

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("type", type + "");
			request = new Request(ServerInterfaceDefinition.OPT_VERSION, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * 提交反馈
	 * @param uid
	 * @param content
     * @return
     */
	public Request getCommitRequest(String uid, String content) {
		Request request = null;
		try {

			Map<String, String> paramsMap = new HashMap<String, String>();
			request = new Request(ServerInterfaceDefinition.OPT_VERSION, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}
}
