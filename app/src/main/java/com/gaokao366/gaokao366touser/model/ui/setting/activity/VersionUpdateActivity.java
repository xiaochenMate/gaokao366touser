package com.gaokao366.gaokao366touser.model.ui.setting.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.network.RequestMaker;
import com.gaokao366.gaokao366touser.model.framework.parser.SubBaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.StringUtil;
import com.gaokao366.gaokao366touser.model.version.bean.VersionBean;
import com.gaokao366.gaokao366touser.model.widget.TitleBar;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;


/**
 * 首页
 *
 * @author Administrator
 */
public class VersionUpdateActivity extends BaseActivity {

    public static final int NORMAL = -1;
    public static final int LOADING = 0;
    public static final int PAUSE = 1;
    public static final int COMPLETE = 2;
    public static final int FAIL = 3;
    public static final int START = 4;
    private static final int EXISTS = 5;

    private HttpHandler<File> mHttpHandler;
    private File mFile;

    private String versionLocal = "";
    private String versionServer = "";
    /**
     * 下载地址
     */
    private String url;
    /**
     * 按钮状态
     */
    private int status = NORMAL;


    // Content View Elements

    private LinearLayout mv;
    private TitleBar mTitleBar;
    private ImageView iv_icon;
    private TextView vu_versionLocal;
    private RelativeLayout vu_rl_update;
    private TextView vu_version;
    private ProgressBar vu_pb;
    private TextView vu_downLoad;
    private ProgressBar vu_pb_circle;

    // End Of Content View Elements

    private void bindViews() {

        mv = (LinearLayout) findViewById(R.id.mv);
        mTitleBar = (TitleBar) findViewById(R.id.mTitleBar);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        vu_versionLocal = (TextView) findViewById(R.id.vu_versionLocal);
        vu_rl_update = (RelativeLayout) findViewById(R.id.vu_rl_update);
        vu_version = (TextView) findViewById(R.id.vu_version);
        vu_pb = (ProgressBar) findViewById(R.id.vu_pb);
        vu_downLoad = (TextView) findViewById(R.id.vu_downLoad);
        vu_pb_circle = (ProgressBar) findViewById(R.id.vu_pb_circle);

        vu_downLoad.setOnClickListener(this);
    }


    @Override
    public void setContentLayout() {
        setContentView(R.layout.s_versionupdate);
        bindViews();
    }

    @Override
    public void initView() {
        mTitleBar.setTitle(R.string.update);
        mTitleBar.setBack(true);
        versionLocal = softApplication.getAppVersionName();
        vu_versionLocal.setText(getResStrById(R.string.app_name));
        vu_version.setText("V" + versionLocal);
        getData();
    }

    /**
     * 关于
     *
     */
    public void doUpdate() {
        checkStatus();
    }

    /**
     * 下载
     */
    private void doDownLoad() {
        if (StringUtil.isNullOrEmpty(url)) {
            showToast("获取下载地址失败");
            return;
        }

        HttpUtils hu = new HttpUtils(15 * 1000);
        File file = new File(Environment.getExternalStorageDirectory(), "/lbox/version");
        mFile = new File(Environment.getExternalStorageDirectory(), "/lbox/version/lschool" + "_" + versionServer + ".apk");
        try {
            if (!file.exists())
                file.mkdirs();
            if (!mFile.exists()) {
                mFile.createNewFile();
            } else {
                status = EXISTS;
                handler.sendEmptyMessage(status);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        vu_downLoad.setBackgroundResource(R.color.transparent);
        mHttpHandler = hu.download(url, mFile.getAbsolutePath(), true, false, new RequestCallBack<File>() {

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                vu_pb.setMax((int) total);
                vu_pb.setProgress((int) current);
                status = LOADING;
                handler.sendEmptyMessage(status);
            }

            @Override
            public void onStart() {
                status = START;
                handler.sendEmptyMessage(status);
            }

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                status = COMPLETE;
                handler.sendEmptyMessage(status);
            }

            @Override
            public void onCancelled() {
                status = PAUSE;
                handler.sendEmptyMessage(status);
                showToast("取消");
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                status = FAIL;
                handler.sendEmptyMessage(status);
            }
        });
    }

    /**
     * 点击按钮,.根据状态做相应动作
     */
    private void checkStatus() {
        switch (status) {
            case NORMAL:
                doDownLoad();
                break;
            case LOADING:
                mHttpHandler.pause();
                break;
            case PAUSE:
                doDownLoad();
                break;
            case COMPLETE:
                installApk();
                break;
            case EXISTS:
                installApk();
                break;
            case FAIL:
                doDownLoad();
                break;
        }
    }

    /**
     * 按钮状态变化
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START:
                    vu_downLoad.setClickable(false);
                    vu_downLoad.setText(getResStrById(R.string.downLoadStart));
                    break;
                case LOADING:
                    vu_downLoad.setClickable(true);
                    vu_downLoad.setText(getResStrById(R.string.downLoading));
                    break;
                case PAUSE:
                    vu_downLoad.setClickable(true);
                    vu_downLoad.setText(getResStrById(R.string.downLoadPause));
                    break;
                case COMPLETE:
                    vu_downLoad.setClickable(true);
                    vu_downLoad.setText(getResStrById(R.string.downLoadOver));
                    break;
                case EXISTS:
                    vu_downLoad.setClickable(true);
                    vu_downLoad.setText(getResStrById(R.string.downLoadExits));
                    break;
                case FAIL:
                    vu_downLoad.setClickable(true);
                    vu_downLoad.setText(getResStrById(R.string.downLoadFail));
                    break;
            }
        }
    };


    @Override
    public void onNetChanged(boolean oldStatus, boolean newStatus) {

    }

    private void installApk() {
        Uri fileUri = Uri.fromFile(mFile);
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.setDataAndType(fileUri, "application/vnd.android.package-archive");
        startActivity(it);
    }

    /**
     * 获取版本号
     */
    private void getData() {
        Request request = RequestMaker.getInstance().getUpdateRequest(1);
        getNetWorkDate(request, new SubBaseParser(VersionBean.class), new OnCompleteListener<VersionBean>(this) {

            @Override
            public void onSuccessed(VersionBean result, String resultString) {
                versionServer = result.versionCode;
                url = result.downloadUrl;
                if (versionLocal.equals(versionServer)) {
                    vu_downLoad.setText(getResources().getString(R.string.isLastest));
                    vu_downLoad.setClickable(false);
                    return;
                }
                vu_downLoad.setText(getResources().getString(R.string.downLoad));
            }

            @Override
            public void onCompleted(VersionBean result, String resultString) {
                vu_pb_circle.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vu_downLoad:
                doUpdate();
                break;
        }
    }
}
