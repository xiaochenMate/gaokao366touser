package com.gaokao366.gaokao366touser.model.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.util.LogUtil;


/**
 * Created by hh on 2016/5/17.
 */
public class NavBar extends LinearLayout implements View.OnClickListener {

    private Context ct;

    // Content View Elements

    private LinearLayout ll_home;
    private ImageView iv_home;
    private LinearLayout ll_msg;
    private ImageView iv_msg;
    private LinearLayout ll_trip;
    private ImageView iv_trip;
    private LinearLayout ll_mine;
    private ImageView iv_mine;
    private ViewPager mViewPager;
    private OnItemChangedListener onItemChangedListener;
    private int mCurrentItem = 0;

    public NavBar(Context context) {
        this(context, null, -1);
    }

    public NavBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ct = context;
        init();
    }

    private void init() {
        View view = View.inflate(ct, R.layout.m_bottom, this);
        bindViews(view);
    }

    // End Of Content View Elements

    private void bindViews(View view) {

        ll_home = (LinearLayout) view.findViewById(R.id.ll_home);
        iv_home = (ImageView) view.findViewById(R.id.iv_home);
        ll_msg = (LinearLayout) view.findViewById(R.id.ll_msg);
        iv_msg = (ImageView) view.findViewById(R.id.iv_msg);
        ll_trip = (LinearLayout) view.findViewById(R.id.ll_trip);
        iv_trip = (ImageView) view.findViewById(R.id.iv_trip);
        ll_mine = (LinearLayout) view.findViewById(R.id.ll_mine);
        iv_mine = (ImageView) view.findViewById(R.id.iv_mine);

        ll_home.setOnClickListener(this);
        ll_msg.setOnClickListener(this);
        ll_trip.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int clickPosition = -1;
        switch (view.getId()) {
            case R.id.ll_home:
                clickPosition = 0;
                break;
            case R.id.ll_msg:
                clickPosition = 1;
                break;
            case R.id.ll_trip:
                clickPosition = 2;
                break;
            case R.id.ll_mine:
                clickPosition = 3;
                break;
        }
        setItemChecked(clickPosition);
    }


    public void setItemChecked(int position) {

        if (mCurrentItem == position) {
            return;
        }

        //如果监听了此方法,且return true , 则消费掉
        if (onItemChangedListener != null) {
            if (onItemChangedListener.onItemChecked(position)) {
                mViewPager.setCurrentItem(mCurrentItem);
                return;
            }
        }

        LogUtil.log("切换到:" + position);
        mCurrentItem = position;

        //处理一些颜色的变化
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }

        if (mViewPager != null) {
            mViewPager.setCurrentItem(position);
        }
    }

    public void setViewPager(ViewPager vp) {
        if (vp == null)
            return;
        this.mViewPager = vp;
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
    }


    OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setItemChecked(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public void setOnItemChangedListener(OnItemChangedListener l) {
        this.onItemChangedListener = l;
    }

    public interface OnItemChangedListener {
        boolean onItemChecked(int position);
    }
}
