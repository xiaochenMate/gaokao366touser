package com.gaokao366.gaokao366touser.model.ui.setting.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.framework.bean.SubBaseResponse;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.network.RequestMaker;
import com.gaokao366.gaokao366touser.model.framework.parser.SubBaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.StringUtil;
import com.gaokao366.gaokao366touser.model.widget.TitleBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 意见反馈
 * @author Administrator
 *
 */
public class FeedBackActivity extends BaseActivity {

	
	TitleBar mTitleBar ;
	EditText et_content ;
	TextView tv_num ;

	private void bindViews() {
		mTitleBar = (TitleBar) findViewById(R.id.mTitleBar);
		et_content = (EditText) findViewById(R.id.et_content);
		tv_num = (TextView) findViewById(R.id.tv_num);
	}

	@Override
	public void setContentLayout() {
		setContentView(R.layout.s_feedback);
		ViewUtils.inject(this);
	}


	@Override
	public void initView() {
		bindViews();
		mTitleBar.setTitleRight("反馈");
		mTitleBar.setBack(true);
		mTitleBar.setOnRightclickListener(onClickRightListener);
		et_content.addTextChangedListener(watcher);
	}


	View.OnClickListener onClickRightListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			doCommint();;
		}
	};


	TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if( s.length()>200){
				showToast("超出最大数字限制");
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			tv_num.setText(s.length()+"/200") ; 
		}
	};

	/**
	 *  提交
	 */
	public void doCommint(){
		String content = et_content.getText().toString().trim();
		if(StringUtil.isNullOrEmpty(content)){
			showToast("请填写内容");
			return ; 
		}
		doCommit(content );
	}

	
	private void doCommit(String content ) {
		showProgressDialog();
		String uid ="";
		Request request = RequestMaker.getInstance().getCommitRequest(uid ,content );
		getNetWorkDate(request, new SubBaseParser(SubBaseResponse.class), new OnCompleteListener<SubBaseResponse>(this) {

			@Override
			public void onSuccessed(SubBaseResponse result, String resultString) {

			}

			@Override
			public void onCompleted(SubBaseResponse result, String resultString) {
				showToast("提交成功");
				finish() ;
			}

		});
	}


	@Override
	public void onNetChanged(boolean oldStatus, boolean newStatus) {
		
	}

	@Override
	public void onClick(View view) {

	}
}
