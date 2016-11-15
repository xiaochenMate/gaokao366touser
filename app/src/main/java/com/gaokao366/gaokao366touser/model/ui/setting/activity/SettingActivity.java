package com.gaokao366.gaokao366touser.model.ui.setting.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.framework.util.DataClearUtils;
import com.gaokao366.gaokao366touser.model.ui.manager.UIManager;
import com.gaokao366.gaokao366touser.model.widget.MySettingView;
import com.gaokao366.gaokao366touser.model.widget.TitleBar;


/**
 * 首页
 *
 * @author Administrator
 */
public class SettingActivity extends BaseActivity {


    // Content View Elements

    private TitleBar mTitleBar;
    private  MySettingView s_help;
    private  MySettingView s_about;
    private  MySettingView s_feedBack;
    private  MySettingView s_clear;
    private MySettingView s_update;
    private Button s_quit;

    // End Of Content View Elements

    private void bindViews() {

        mTitleBar = ( TitleBar) findViewById(R.id.mTitleBar);
        s_help = ( MySettingView) findViewById(R.id.s_help);
        s_about = ( MySettingView) findViewById(R.id.s_about);
        s_feedBack = ( MySettingView) findViewById(R.id.s_feedBack);
        s_clear = ( MySettingView) findViewById(R.id.s_clear);
        s_update = ( MySettingView) findViewById(R.id.s_update);
        s_quit = (Button) findViewById(R.id.s_quit);

        s_help.setOnClickListener(this);
        s_about.setOnClickListener(this);
        s_feedBack.setOnClickListener(this);
        s_clear.setOnClickListener(this);
        s_update.setOnClickListener(this);
        s_quit.setOnClickListener(this);
    }


    @Override
    public void setContentLayout() {
        setContentView(R.layout.s_setting);
        bindViews();
    }


    @Override
    public void initView() {
        mTitleBar.setTitle(R.string.setting);
        mTitleBar.setBack(true);
        s_help.setResInit(R.string.help, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_feedBack.setResInit(R.string.feedBack, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_clear.setResInit(R.string.clear, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_about.setResInit(R.string.about, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        s_update.setResInit(R.string.update, -1, -1, R.mipmap.arrow_right, R.color.tv_black);
        setCacheSize();
    }

    /**
     * 设置缓存大小
     */
    private void setCacheSize() {
        try {
            s_clear.setRight(DataClearUtils.getAllCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 常见问题(帮助)
     *
     */
    public void turnToHelpActivity() {
        UIManager.turnToAct(this, HelpActivity.class);
    }

    /**
     * 关于
     *
     */
    public void turnToAboutUsActivity() {
        UIManager.turnToAct(this, AboutusActivity.class);
    }

    /**
     * 默认城市
     *
     */
    public void turnToFeedBackActivity() {
        UIManager.turnToAct(this, FeedBackActivity.class);
    }

    /**
     * 清理缓存
     *
     */
    public void clearCache() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("是否清理缓存数据").setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {
                showProgressDialog("正在清理,请稍后");
                DataClearUtils.cleanApplicationData(SettingActivity.this, new String[]{null});
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        setCacheSize();
                    }
                }, 1000);
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 版本更新
     *
     */
    public void turnToVersionUpdate() {
        UIManager.turnToAct(this, VersionUpdateActivity.class);
    }

    /**
     * 安全退出
     *
     */
    public void doQuit() {
        softApplication.quit();
//		DemoHXSDKHelper.getInstance().logout(true, null);
//		UIManager.turnToAct(this, LoginActivity.class);
    }


    @Override
    public void onNetChanged(boolean oldStatus, boolean newStatus) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.s_help:
                turnToHelpActivity();
                break;
            case R.id.s_about:
                turnToAboutUsActivity();
                break;
            case R.id.s_feedBack:
                turnToFeedBackActivity();
                break;
            case R.id.s_clear:
                clearCache();
                break;
            case R.id.s_update:
                turnToVersionUpdate();
                break;
            case R.id.s_quit:
                doQuit();
                break;
        }
    }
}
