package com.gaokao366.gaokao366touser.model.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;


import com.gaokao366.gaokao366touser.model.framework.util.LogUtil;

import java.lang.reflect.Field;

/**
 * Created by hh on 2016/5/18.
 */
public class MyViewPager extends ViewPager{
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setOffscreenPageLimit(int limit) {
        try {
            Field mOffscreenPageLimit = ViewPager.class.getDeclaredField("mOffscreenPageLimit");
            mOffscreenPageLimit.setAccessible(true);
            //mOffscreenPageLimit.set(this,0);
            LogUtil.log("mOffscreenPageLimit赋值为0");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
