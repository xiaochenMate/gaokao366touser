package com.gaokao366.gaokao366touser.model.framework.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.application.SoftApplication;
import com.gaokao366.gaokao366touser.model.framework.bean.BaseResponse;
import com.gaokao366.gaokao366touser.model.framework.manager.INetChangedListener;
import com.gaokao366.gaokao366touser.model.framework.manager.NetChangeManager;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.parser.BaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.LogUtil;
import com.gaokao366.gaokao366touser.model.framework.util.NetUtil;

/**
 * Created by hh on 2016/5/18.
 */
public abstract class BaseFragment extends Fragment implements INetChangedListener {


    protected SoftApplication softApplication;
    private CustomerDialog progressDialog;
    private View inflate;
    private int contentViewRes=-1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        softApplication=SoftApplication.softApplication;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(inflate==null) {
            LogUtil.log(getClass().getName()+ "初始化" );
            setContentLayout(savedInstanceState);
            if(contentViewRes==-1){
                LogUtil.log("未设置布局");
                return null;
            }
            inflate = inflater.inflate(contentViewRes, null);
            if (inflate != null)
                initView(inflate);
        }else{
            LogUtil.log( getClass().getName()+ "再次加载,无需初始化" );
        }
        return inflate;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.log(getClass().getName()+"[onDestroy]");
        NetChangeManager.newInstance(softApplication).removeMinitor(this);
    }

    public abstract void setContentLayout(Bundle savedInstanceState);

    public abstract void initView(View v);

    public void setContentView(int resId){
        this.contentViewRes = resId;
    }
    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public void showToast(String info) {
        Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param info 显示的内容
     */
    public void showToastLong(String info) {
        Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示Toast
     * <p>
     * 显示的内容
     */
    public void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     * <p>
     * 显示的内容
     */
    public void showToastLong(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show();
    }

    /**
     * onClick方法的封装，在此方法中处理点击
     * <p>
     * 被点击的View对象
     */
    // abstract public void onClickEvent(View view);
    public String getResStrById(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 普通联网获取数据
     *
     * @param <T>
     * @param request
     * @param onCompleteListener
     */
    @SuppressWarnings("rawtypes")
    public <T extends BaseResponse> void getNetWorkDate(Request request, BaseParser<T> parser, OnCompleteListener<T> onCompleteListener) {
        if (NetUtil.isNetDeviceAvailable(softApplication)) {
            softApplication.requestNetWork(request, onCompleteListener, parser);
        } else {
            showToast(R.string.network_is_not_available);
        }
    }


    /**
     * 显示正在加载的进度条
     */
    public void showProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new CustomerDialog(getActivity(), R.style.MyDialog);
        progressDialog.setMessage("加载中...");
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new CustomerDialog(getActivity(), R.style.MyDialog);
        progressDialog.setMessage(msg);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void showProgressDialog2() {
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        };
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new CustomerDialog(getActivity(), R.style.MyDialog);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setOnKeyListener(keyListener);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialog_loading_process);
    }

    public ProgressDialog createProgressDialog(String msg) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(msg);
        return progressDialog;
    }

    /**
     * 隐藏正在加载的进度条
     */
    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing() == true) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
