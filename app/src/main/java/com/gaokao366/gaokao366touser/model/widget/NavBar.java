package com.gaokao366.gaokao366touser.model.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.util.LogUtil;


//  rujingyouapp1.0
//
//  Copyright  2016年 shengmei. All rights reserved.
public class NavBar extends LinearLayout implements View.OnClickListener {

    private Context ct;

    // Content View Elements

    private LinearLayout ll_home;
    private ImageView iv_home;
    private LinearLayout ll_itinerary;
    private ImageView iv_itinerary;
    private LinearLayout ll_service;
    private ImageView iv_service;
    private LinearLayout ll_mine;
    private ImageView iv_mine;
    private ViewPager mViewPager;
    private OnItemChangedListener onItemChangedListener;
    private int mCurrentItem = 0;
    private TextView tv_home;
    private TextView tv_itinerary;
    private TextView tv_service;
    private TextView tv_mine;

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

        /*String language = SharedPrefHelper.getInstance().getLanguage().equals("") ? Locale.getDefault().toString() : SharedPrefHelper.getInstance().getLanguage();
        if(language.equals("ru_RU")){
            tv_home.setTextSize(12);
        }*/
    }

    // End Of Content View Elements
    private void bindViews(View view) {

        ll_home = (LinearLayout) view.findViewById(R.id.ll_home);
        iv_home = (ImageView) view.findViewById(R.id.iv_home);
        tv_home = (TextView) view.findViewById(R.id.tv_home);
        iv_home.setSelected(true);

        ll_itinerary = (LinearLayout) view.findViewById(R.id.ll_itinerary);
        iv_itinerary = (ImageView) view.findViewById(R.id.iv_itinerary);
        tv_itinerary = (TextView) view.findViewById(R.id.tv_itinerary);

        ll_service = (LinearLayout) view.findViewById(R.id.ll_service);
        iv_service = (ImageView) view.findViewById(R.id.iv_service);
        tv_service = (TextView) view.findViewById(R.id.tv_service);

        ll_mine = (LinearLayout) view.findViewById(R.id.ll_mine);
        iv_mine = (ImageView) view.findViewById(R.id.iv_mine);
        tv_mine = (TextView) view.findViewById(R.id.tv_mine);

        ll_home.setOnClickListener(this);
        ll_itinerary.setOnClickListener(this);
        ll_service.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickPosition = -1;
        switch (view.getId()) {
            case R.id.ll_home:
                clickPosition = 0;
                break;
            case R.id.ll_itinerary:
                clickPosition = 1;
                break;
            case R.id.ll_service:
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
                tv_home.setTextColor(getResources().getColor(R.color.red));
                tv_itinerary.setTextColor(getResources().getColor(R.color.gray));
                tv_service.setTextColor(getResources().getColor(R.color.gray));
                tv_mine.setTextColor(getResources().getColor(R.color.gray));

                iv_home.setSelected(true);
                iv_itinerary.setSelected(false);
                iv_service.setSelected(false);
                iv_mine.setSelected(false);
                break;
            case 1:
                tv_home.setTextColor(getResources().getColor(R.color.gray));
                tv_itinerary.setTextColor(getResources().getColor(R.color.red));
                tv_service.setTextColor(getResources().getColor(R.color.gray));
                tv_mine.setTextColor(getResources().getColor(R.color.gray));

                iv_home.setSelected(false);
                iv_itinerary.setSelected(true);
                iv_service.setSelected(false);
                iv_mine.setSelected(false);
                break;
            case 2:
                tv_home.setTextColor(getResources().getColor(R.color.gray));
                tv_itinerary.setTextColor(getResources().getColor(R.color.gray));
                tv_service.setTextColor(getResources().getColor(R.color.red));
                tv_mine.setTextColor(getResources().getColor(R.color.gray));

                iv_home.setSelected(false);
                iv_itinerary.setSelected(false);
                iv_service.setSelected(true);
                iv_mine.setSelected(false);
                break;
            case 3:
                tv_home.setTextColor(getResources().getColor(R.color.gray));
                tv_itinerary.setTextColor(getResources().getColor(R.color.gray));
                tv_service.setTextColor(getResources().getColor(R.color.gray));
                tv_mine.setTextColor(getResources().getColor(R.color.red));

                iv_home.setSelected(false);
                iv_itinerary.setSelected(false);
                iv_service.setSelected(false);
                iv_mine.setSelected(true);
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
