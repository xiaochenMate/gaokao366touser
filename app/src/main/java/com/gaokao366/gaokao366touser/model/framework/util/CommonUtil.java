package com.gaokao366.gaokao366touser.model.framework.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

public class CommonUtil {

	public static boolean isListEmpty(List list) {
		if (list == null || list.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	public static String getDateTime(String date) {
		 String replace = date.substring(5,16).replace("T", " ");
		 return replace;
	}
	public static String getDateTimeDetail(String date) {
		String replace = date.substring(0,16).replace("T", " ");
		return replace;
	}
	public static String getDateTimeComment(String date) {
		String replace = date.substring(0,19).replace("T", " ");
		return replace;
	}
	public static void hideSoftKeyboard(Activity ct) {
		InputMethodManager imm = (InputMethodManager) ct.getSystemService(Context.INPUT_METHOD_SERVICE);
		View focus = ct.getCurrentFocus();
		if (focus != null) {
			IBinder binder = focus.getWindowToken();
			if (null != binder) {
				imm.hideSoftInputFromWindow(binder, 0);
			}
		}
	}
	public static void closeSoftKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null && imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	public static void openSoftKeyboard(Context context ) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //显示软键盘
	}
	
	
	public static void showToast(Context context,String msg){
		Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
     *判断当前应用程序处于前台还是后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
	
}
