package com.gaokao366.gaokao366touser.model.ui.setting.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.widget.TitleBar;


/**
 * 首页
 * @author Administrator
 *
 */
public class HelpActivity extends BaseActivity {


	// Content View Elements

	private LinearLayout mv;
	private TitleBar mTitleBar;
	private ImageView iv_icon;
	private TextView ab_content;

	// End Of Content View Elements

	private void bindViews() {

		mv = (LinearLayout) findViewById(R.id.mv);
		mTitleBar = (TitleBar) findViewById(R.id.mTitleBar);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		ab_content = (TextView) findViewById(R.id.ab_content);
	}



	@Override
	public void setContentLayout() {
		setContentView(R.layout.s_aboutus);
		bindViews();
	}


	@Override
	public void initView() {
		mTitleBar.setTitle("常见问题");
		mTitleBar.setBack(true);
//		ab_version.setText(getResStrById(R.string.app_name)+softApplication.getAppVersionName());
	}
	
	 
	@Override
	public void onNetChanged(boolean oldStatus, boolean newStatus) {
		
	}


	@Override
	public void onClick(View view) {

	}
}
