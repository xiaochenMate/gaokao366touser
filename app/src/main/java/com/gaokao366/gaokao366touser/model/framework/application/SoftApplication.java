package com.gaokao366.gaokao366touser.model.framework.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;
import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.bean.BaseResponse;
import com.gaokao366.gaokao366touser.model.framework.bean.UserInfo;
import com.gaokao366.gaokao366touser.model.framework.config.AppConfig;
import com.gaokao366.gaokao366touser.model.framework.config.AppInfo;
import com.gaokao366.gaokao366touser.model.framework.network.HttpRequestAsyncTask;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.parser.BaseParser;
import com.gaokao366.gaokao366touser.model.framework.spfs.SharedPrefHelper;
import com.gaokao366.gaokao366touser.model.framework.util.CrcUtil;
import com.gaokao366.gaokao366touser.model.framework.util.DateUtil;
import com.gaokao366.gaokao366touser.model.framework.util.NetUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;


public class SoftApplication extends Application {
	/**
	 * 存放活动状态的(未被销毁)的Activity列表
	 */
	public static List<Activity> unDestroyActivityList = new ArrayList<Activity>();
	public static SoftApplication softApplication;
	private static AppInfo appInfo;
	private static UserInfo userInfo;
	private static boolean isLogin;// 判断是否已经登录
	private static String passwordWithMd5 = "";
	private int screenWidth = 0;
	private int screenHeight = 0;
	private DisplayImageOptions imgLoaderOptions;
	private DisplayImageOptions imgLoaderOptions_bg;
	private DisplayImageOptions imgLoaderOptions_head;

	@Override
	public void onCreate() {
		softApplication = this;

		super.onCreate();

		appInfo = initAppInfo();

		initImageLoader() ;


//		CrashHandler catchHandler = CrashHandler.getInstance();  
//      catchHandler.init(getApplicationContext());  
	}

	private void initImageLoader() {
		// 创建默认的ImageLoader配置参数
		// ImageLoaderConfiguration configuration = ImageLoaderConfiguration
		// .createDefault(this);
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(480, 800) // default
																																// =
																																// device
																																// screen
																																// dimensions
				.denyCacheImageMultipleSizesInMemory().threadPoolSize(2).memoryCacheSizePercentage(25).diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100).writeDebugLogs().build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(configuration);
		imgLoaderOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.pic_default).showImageOnLoading(R.mipmap.pic_default).showImageOnFail(R.mipmap.pic_default).cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();


		imgLoaderOptions_head = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.head_normal).showImageOnLoading(R.mipmap.head_normal).showImageOnFail(R.mipmap.head_normal).cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}


	/**
	 * 实例化AppInfo
	 */
	private AppInfo initAppInfo() {
		AppInfo appInfo = AppConfig.getAppConfigInfo(softApplication);
		appInfo.imei = NetUtil.getIMEI(getApplicationContext());
		appInfo.imsi = NetUtil.getIMSI(getApplicationContext()) == null ? "" : NetUtil.getIMSI(getApplicationContext());
		appInfo.osVersion = getOSVersion();
		appInfo.appVersionCode = getAppVersionCode();
		return appInfo;
	}

	/**
	 * 得到系统的版本号
	 * 
	 * @return
	 */
	public String getOSVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 得到应用的版本号
	 * 
	 * @return
	 */
	public int getAppVersionCode() {
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		int versionCode = 0;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 得到应用的版本号
	 * 
	 * @return
	 */
	public String getAppVersionName() {
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		String versionCode = "";
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取Assert文件夹中的配置文件信息
	 * 
	 * @return
	 */
	public AppInfo getAppInfo() {
		return appInfo;
	}

	public String getFrom() {
		return appInfo == null ? "" : appInfo.os;
	}

	public String getApiUser() {
		return appInfo == null ? "" : appInfo.api_user;
	}

	public String getApiPassword() {
		return appInfo == null ? "" : appInfo.api_pwd;
	}

	/**
	 * 得到请求头JsonObject
	 * 
	 * @return
	 */
	public String getAuthJsonObject(String jsonString) {
		try {
			String timeStamp = DateUtil.getCurrentDateTimeyyyyMMddHHmmss();
			JSONObject authJsonObject = new JSONObject();
			authJsonObject.put("app_key", appInfo.appKey);
			authJsonObject.put("imei", appInfo.imei);
			authJsonObject.put("os", appInfo.os);
			authJsonObject.put("os_version", appInfo.osVersion);
			authJsonObject.put("app_version", appInfo.appVersionCode);
			authJsonObject.put("source_id", appInfo.sourceId);
			authJsonObject.put("ver", appInfo.ver);
			authJsonObject.put("uid", isLogin ? userInfo.id : appInfo.uid);
			authJsonObject.put("time_stamp", timeStamp);
			authJsonObject.put("crc", CrcUtil.getCrc(timeStamp, appInfo.imei, (isLogin ? userInfo.id : appInfo.uid), (isLogin ? passwordWithMd5 : CrcUtil.MD5(appInfo.crc)), jsonString));
			return authJsonObject.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 退出应用
	 */
	public void quit() {
		for (Activity activity : unDestroyActivityList) {
			if (null != activity) {
				activity.finish();
			}
		}
		unDestroyActivityList.clear();
		logout();
	}

	public <T extends BaseResponse> void requestNetWork(Request request, OnCompleteListener<T> onCompleteListener, BaseParser<T> parser) {
		HttpRequestAsyncTask<T> httpRequestTask = new HttpRequestAsyncTask<T>();
		httpRequestTask.setOnCompleteListener(onCompleteListener);
		httpRequestTask.setParser(parser);
		httpRequestTask.execute(request);
	}

	/**
	 * 注销帐号
	 */
	public void logout() {
		/**
		 * 退出登录,清空数据
		 */
		userInfo = null;
		isLogin = false;
		try {
			passwordWithMd5 = CrcUtil.MD5(appInfo.crc);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 保存登录成功之后用户的信息
	 * 
	 * @param result
	 */
	public void setUserInfo(UserInfo result) {
		userInfo = result;
	}


	/**
	 * 获取用户的信息
	 * 
	 * @return
	 */
	public UserInfo getUserInfo() {
		if(userInfo==null){
			userInfo = SharedPrefHelper.getInstance().getUserInfo();
		}
		return userInfo;
	}
	
	public String getPasswordWithMd5() {
		return passwordWithMd5;
	}

	public void setPasswordWithMd5(String passwordWithMD5) {
		passwordWithMd5 = passwordWithMD5;
	}

	public boolean isLogin() {
		return isLogin;
	}


	public void setLoginStatus(boolean b) {
		isLogin = b;
	}

	/**
	 * 
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}



	public void loadImgUrlNyImgLoader(String url, ImageView v) {
		ImageLoader.getInstance().displayImage(url, v, imgLoaderOptions);
	}
	public void loadImgUrlNyImgLoader(String url, ImageView v,ImageLoadingListener l) {
		ImageLoader.getInstance().displayImage(url, v, imgLoaderOptions, l );
	}

	public void loadImgUrlNyImgLoaderByBg(String url, ImageView v) {
		ImageLoader.getInstance().displayImage(url, v, imgLoaderOptions_bg);
	}

	public void loadImgUrlNyImgLoaderForHead(String url, ImageView v) {
		/*if(StringUtil.isNullOrEmpty(url)){
			url="www.www.www";
		}*/
		ImageLoader.getInstance().displayImage(url, v, imgLoaderOptions_head);
	}
	
	public void changeFonts(ViewGroup root, Context act) {

		Typeface tf = Typeface.createFromAsset(act.getAssets(), "MNJLX.ttf");

		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, act);
			}
		}
	}
	public void changeFontsThin(ViewGroup root, Context act) {
		
		Typeface tf = Typeface.createFromAsset(act.getAssets(), "MNJLX_THIN.ttf");
		
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFontsThin((ViewGroup) v, act);
			}
		}
	}
}
