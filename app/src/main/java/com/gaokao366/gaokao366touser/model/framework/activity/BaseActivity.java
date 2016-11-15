package com.gaokao366.gaokao366touser.model.framework.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.application.SoftApplication;
import com.gaokao366.gaokao366touser.model.framework.bean.BaseResponse;
import com.gaokao366.gaokao366touser.model.framework.manager.INetChangedListener;
import com.gaokao366.gaokao366touser.model.framework.manager.NetChangeManager;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.parser.BaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.NetUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public abstract class BaseActivity extends AutoLayoutActivity implements View.OnClickListener,INetChangedListener {
    protected SoftApplication softApplication;
    public boolean isAllowFullScreen;// 是否允许全屏
    public boolean isAllowOneScreen = true;// 是否允许一體化
    public boolean hasMenu;// 是否有菜单显示
    private CustomerDialog progressDialog;
    protected Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resources = getResources();
        softApplication = (SoftApplication) getApplicationContext();
        NetChangeManager.newInstance(softApplication).addMinitor(this);
        SoftApplication.unDestroyActivityList.add(this);
        if (isAllowFullScreen) {
            setFullScreen(true);
        } else {
            setFullScreen(false);
        }
        if (isAllowOneScreen) {
            setTranslucentStatus(R.color.title_color);
        } else {
            setTranslucentStatus(R.color.transparent);
        }
        setContentLayout();
        initView();

        // 获取屏幕宽高
        if (softApplication.getScreenWidth() == 0) {
            softApplication.setScreenWidth(getScreenWidth());
        }
        if (softApplication.getScreenHeight() == 0) {
            softApplication.setScreenHeight(getScreenHeight());
        }
    }

    /**
     * 设置布局文件
     */
    public abstract void setContentLayout();

    /**
     * 实例化布局文件/组件
     */
    public abstract void initView();

    /**
     * 得到屏幕宽度
     *
     * @return 宽度
     */
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 得到屏幕高度
     *
     * @return 高度
     */
    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    /**
     * 是否全屏和显示标题，true为全屏和无标题，false为无标题，请在setContentView()方法前调用
     *
     * @param fullScreen
     */
    public void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }

    /**
     * 设置状态栏背景状态
     */
    public void setTranslucentStatus(int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(colorResId);// 状态栏无背景
    }

    public static void setStatusBarTextColor(Activity context, int type) {
        Window window = context.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (type == 0) {
                extraFlagField.invoke(window, tranceFlag, tranceFlag);// 只需要状态栏透明
            } else if (type == 1) {
                extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);// 状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);// 清除黑色字体
            }
        } catch (Exception e) {

        }
    }

    /**
     * 短时间显示Toast
     *
     * @param info 显示的内容
     */
    public void showToast(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param info 显示的内容
     */
    public void showToastLong(String info) {
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示Toast
     * <p>
     * 显示的内容
     */
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     * <p>
     * 显示的内容
     */
    public void showToastLong(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SoftApplication.unDestroyActivityList.remove(this);
        NetChangeManager.newInstance(softApplication).removeMinitor(this);
    }

    /**
     * 显示正在加载的进度条
     */
    public void showProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new CustomerDialog(BaseActivity.this, R.style.MyDialog);
        progressDialog.setMessage("加载中...");
        try {
            progressDialog.show();
        } catch (BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new CustomerDialog(BaseActivity.this, R.style.MyDialog);
        progressDialog.setMessage(msg);
        try {
            progressDialog.show();
        } catch (BadTokenException exception) {
            exception.printStackTrace();
        }
    }

    public void showProgressDialog2() {
        OnKeyListener keyListener = new OnKeyListener() {
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
        progressDialog = new CustomerDialog(BaseActivity.this, R.style.MyDialog);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setOnKeyListener(keyListener);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialog_loading_process);
    }

    public ProgressDialog createProgressDialog(String msg) {
        ProgressDialog progressDialog = new ProgressDialog(BaseActivity.this);
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

    /**
     * 1.有列表
     *
     */
    /*public void doDealWithResult(IOnDealResult onDeal, List<?> list, XListView xListView, int pageNo) {
		if (list == null || list.size() == 0) {
			xListView.setPullLoadEnable(false);
			if (pageNo > 1) {// 不是第一页,但没有信息,代表加载完毕
				showToast("数据加载完毕");
				return;
			}
			showToast("暂无数据！");// 第一页,提示无信息
			return;
		}
		if (list.size() < Constants.PAGESIZE) {// 没有加载到10条数据,也证明加载完毕
			if (pageNo == 1) {
				xListView.setPullLoadEnable(false);
			} else {
				// showToast("数据加载完毕");
				xListView.setPullLoadEnable(false);
			}
		} else {
			xListView.setPullLoadEnable(true);
		}
		// 处理结果
		if (onDeal != null) {
			onDeal.doResult();
		}
	}*/

    public interface IOnDealResult {
        public void doResult();
    }

}
