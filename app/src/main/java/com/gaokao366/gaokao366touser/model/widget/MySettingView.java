package com.gaokao366.gaokao366touser.model.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.application.SoftApplication;


public class MySettingView extends RelativeLayout {

	private Context ct;
	private String tv_left;
	private String iv_left;
	private String tv_right;
	private String iv_right;
	private ImageView s_iv_left;
	private TextView s_tv_left;
	private ImageView s_iv_right;
	private TextView s_tv_right;

	public MySettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ct = context;
		initView(attrs);
	}

	private void initView(AttributeSet attrs) {
		isInEditMode();
		View view = View.inflate(ct, R.layout.setting_view, this);
		s_iv_left = (ImageView) view.findViewById(R.id.s_iv_left);
		s_tv_left = (TextView) view.findViewById(R.id.s_tv_left);
		s_iv_right = (ImageView) view.findViewById(R.id.s_iv_right);
		s_tv_right = (TextView) view.findViewById(R.id.s_tv_right);

		/*TypedArray typedArray = ct.obtainStyledAttributes(attrs, R.styleable.setting);
		tv_left = typedArray.getString(R.styleable.setting_tv_left);
		iv_left = typedArray.getString(R.styleable.setting_iv_left);
		tv_right = typedArray.getString(R.styleable.setting_tv_right);
		iv_right = typedArray.getString(R.styleable.setting_iv_right);*/
	}

	public void setResInit(int tv_left, int iv_left, int tv_right, int iv_right, int colorResId) {
		if (tv_left != -1) {
			s_tv_left.setText(getResources().getString(tv_left));
		} else {
			s_tv_left.setVisibility(View.GONE);
		}
		if (tv_right != -1) {
			s_tv_right.setText(getResources().getString(tv_right));
		} else {
			s_tv_right.setVisibility(View.GONE);
		}
		if (iv_left != -1) {
			s_iv_left.setImageResource(iv_left);
		} else {
			s_iv_left.setVisibility(View.GONE);
		}
		if (iv_right != -1) {
			s_iv_right.setImageResource(iv_right);
		} else {
			s_iv_right.setVisibility(View.GONE);
		}

		setColor(colorResId);
	}

	public void setRight(String tv_right) {
		s_tv_right.setVisibility(View.VISIBLE);
		s_iv_right.setVisibility(View.GONE);
		s_tv_right.setText(tv_right);
	}

	public String getRightText() {
		return s_tv_right.getText().toString().trim();
	}

	public void setLeft(String tv_left) {
		s_tv_left.setText(tv_left);
	}

	public void setLeftImg(String url) {
		SoftApplication.softApplication.loadImgUrlNyImgLoaderForHead(url, s_iv_left);
	}

	public void setLeftIvWidth(int width) {
		LayoutParams params = (LayoutParams)s_iv_left.getLayoutParams() ;
		params.height = width;
		params.width = width;
		s_iv_left.setLayoutParams(params);
	}

	public void setColor(int colorResId) {
		s_tv_left.setTextColor(getResources().getColor(colorResId));
		s_tv_right.setTextColor(getResources().getColor(colorResId));
	}
}
