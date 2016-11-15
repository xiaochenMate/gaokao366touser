package com.gaokao366.gaokao366touser.model.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关
 */
public class NetWorkUtils {

	private Context mContext;
	private ConnectivityManager connectivityManager;
	private NetworkInfo networkInfo;

	public NetWorkUtils(Context context) {
		this.mContext = context;
		connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = connectivityManager.getActiveNetworkInfo();
	}

	public boolean isConnected() {
		return networkInfo != null && networkInfo.isConnected();
	}

	/**
	 * 
	 * @return 0: 无网络， 1:WIFI， 2:其他（流量）
	 */
	public int getNetType() {
		if (!isConnected()) {
			return 0;
		}
		int type = networkInfo.getType();
		if (type == ConnectivityManager.TYPE_WIFI) {
			return 1;
		} else {
			return 2;
		}
	}

}