package com.gaokao366.gaokao366touser.model.framework.util;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;


import com.gaokao366.gaokao366touser.model.version.bean.VersionBean;

import java.io.File;
import java.util.HashMap;

public class UpdateHelper {

	private Context mContext;
	private boolean isAutoInstall;
	private OnUpdateListener updateListener;
	private NotificationManager notificationManager;
	private NotificationCompat.Builder ntfBuilder;

	private static final int UPDATE_NOTIFICATION_PROGRESS = 0x1;
	private static final int COMPLETE_DOWNLOAD_APK = 0x2;
	private static final int DOWNLOAD_NOTIFICATION_ID = 0x3;
	private static final int HAS_EXISTS = 0x4;
	private static final int DOWNLOAD_HINT = 0x5;
	private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/download";
	private static final String SUFFIX = ".apk";
	private static final String APK_PATH = "APK_PATH";
	private static final String APP_NAME = "APP_NAME";

	private HashMap<String, String> cache = new HashMap<String, String>();

	public UpdateHelper(Context context) {
		mContext = context;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HAS_EXISTS:
				Toast.makeText(mContext, "新版本已存在,可直接安装", Toast.LENGTH_SHORT).show();
				installApk(Uri.parse("file://" + cache.get(APK_PATH)));
				break;
			case DOWNLOAD_HINT:
				Toast.makeText(mContext, "正在下载,点击状态栏查看详情", Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_NOTIFICATION_PROGRESS:
				showDownloadNotificationUI((VersionBean) msg.obj, msg.arg1);
				break;
			case COMPLETE_DOWNLOAD_APK:
				if (UpdateHelper.this.isAutoInstall) {
					installApk(Uri.parse("file://" + cache.get(APK_PATH)));
				} else {
					if (ntfBuilder == null) {
						ntfBuilder = new NotificationCompat.Builder(mContext);
					}
					ntfBuilder.setSmallIcon(mContext.getApplicationInfo().icon).setContentTitle(cache.get(APP_NAME)).setContentText("下载完成，点击安装").setTicker("任务下载完成");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse("file://" + cache.get(APK_PATH)), "application/vnd.android.package-archive");
					PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
					ntfBuilder.setContentIntent(pendingIntent);
					if (notificationManager == null) {
						notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
					}
					notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, ntfBuilder.build());
				}
				break;
			}
		}

	};

	/**
	 * 2014-10-27新增流量提示框，当网络为数据流量方式时，下载就会弹出此对话框提示
	 * 
	 * @param updateInfo
	 */
	private void showNetDialog(final VersionBean updateInfo) {

		/*MyAlertDialog dialog = new MyAlertDialog(mContext);
		dialog.setSimpleMode(false);
		dialog.setOnclickOkListener(new OnclickOkListener() {

			@Override
			public void clickOk(Dialog dialog) {
				dialog.dismiss();
				AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
				asyncDownLoad.execute(updateInfo);
			}
		});
		dialog.show();*/
	}

	/**
	 * 弹出提示更新窗口
	 * 
	 * @param updateInfo
	 */
	@SuppressLint("NewApi")
	public void showUpdateUI(final VersionBean updateInfo) {

		/*MyAlertDialog dialog = new MyAlertDialog(mContext);
		dialog.setSimpleMode(false);
		dialog.setMessage("发现新版本啦");
		dialog.setButtonText("以后更新", "现在升级");
		dialog.setOnclickOkListener(new OnclickOkListener() {

			@Override
			public void clickOk(Dialog dialog) {
				dialog.dismiss();
				if (StringUtil.isNullOrEmpty(updateInfo.downloadUrl)) {
					CommonUtil.showToast(mContext, "没有下载地址");
					return;
				}
				NetWorkUtils netWorkUtils = new NetWorkUtils(mContext);
				int type = netWorkUtils.getNetType();
				if (type != 1) {
					showNetDialog(updateInfo);
				} else {
					AsyncDownLoad asyncDownLoad = new AsyncDownLoad();
					asyncDownLoad.execute(updateInfo);
				}

			}
		});
		dialog.show();*/

	}

	/**
	 * 通知栏弹出下载提示进度
	 * 
	 * @param progress
	 */
	private void showDownloadNotificationUI(VersionBean bean, final int progress) {
		if (mContext != null) {
			String contentText = new StringBuffer().append(progress).append("%").toString();
			PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
			if (notificationManager == null) {
				notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			}
			if (ntfBuilder == null) {
				ntfBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(mContext.getApplicationInfo().icon).setContentTitle("联盟校园").setTicker("开始下载...").setContentIntent(contentIntent);
			}
			ntfBuilder.setContentText(contentText);
			ntfBuilder.setProgress(100, progress, false);
			notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, ntfBuilder.build());
		}
	}

	/**
	 * 异步下载app任务
	 */
	private class AsyncDownLoad extends AsyncTask<VersionBean, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(VersionBean... params) {
			/*HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0].downloadUrl);
			try {
				HttpResponse response = httpClient.execute(httpGet);
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					CommonUtil.showToast(mContext, "APK路径出错，请检查服务端配置接口");
					return false;
				} else {
					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();
					long total = entity.getContentLength();
					String apkName = "联盟校园_v" + params[0].version + SUFFIX;
					File savePath = new File(PATH);
					cache.put(APP_NAME, "联盟校园");
					cache.put(APK_PATH, savePath + File.separator + apkName);
					if (!savePath.exists())
						savePath.mkdirs();
					File apkFile = new File(savePath + File.separator + apkName);
					if (apkFile.exists()) {
						handler.sendEmptyMessage(HAS_EXISTS);
						return true;
					}
					handler.sendEmptyMessage(DOWNLOAD_HINT);
					FileOutputStream fos = new FileOutputStream(apkFile);
					byte[] buf = new byte[1024];
					int count = 0;
					int length = -1;
					while ((length = inputStream.read(buf)) != -1) {
						fos.write(buf, 0, length);
						count += length;
						int progress = (int) ((count / (float) total) * 100);
						if (progress % 5 == 0) {
							handler.obtainMessage(UPDATE_NOTIFICATION_PROGRESS, progress, -1, params[0]).sendToTarget();
						}
						if (UpdateHelper.this.updateListener != null) {
							UpdateHelper.this.updateListener.onDownloading(progress);
						}
					}
					inputStream.close();
					fos.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}*/
			return true;
		}

		@Override
		protected void onPostExecute(Boolean flag) {
			if (flag) {
				handler.obtainMessage(COMPLETE_DOWNLOAD_APK).sendToTarget();
				if (UpdateHelper.this.updateListener != null) {
					UpdateHelper.this.updateListener.onFinshDownload();
				}
			} else {
				CommonUtil.showToast(mContext, "下载失败");
			}
		}
	}

	// 安装
	private void installApk(Uri data) {
		if (mContext != null) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(data, "application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(i);
			if (notificationManager != null) {
				notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
			}
		} else {
			CommonUtil.showToast(mContext, "找不到安装文件");
		}

	}

	// 删除文件
	public void delFile(String apk_path) {
		if (!TextUtils.isEmpty(apk_path)) {
			File myFile = new File(apk_path);
			if (myFile.exists()) {
				myFile.delete();
			}
		}
	}

	public interface OnUpdateListener {

		public void onStartDownload();

		public void onDownloading(int progress);

		public void onFinshDownload();

	}
}