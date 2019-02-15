package com.gaokao366.gaokao366touser.model.ui.main.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseActivity;
import com.gaokao366.gaokao366touser.model.framework.network.OnCompleteListener;
import com.gaokao366.gaokao366touser.model.framework.network.Request;
import com.gaokao366.gaokao366touser.model.framework.network.RequestMaker;
import com.gaokao366.gaokao366touser.model.framework.parser.SubBaseParser;
import com.gaokao366.gaokao366touser.model.framework.util.LogUtil;
import com.gaokao366.gaokao366touser.model.ui.main.fragment.HomeFragment;
import com.gaokao366.gaokao366touser.model.ui.main.fragment.MineFragment;
import com.gaokao366.gaokao366touser.model.ui.main.fragment.MsgFragment;
import com.gaokao366.gaokao366touser.model.ui.main.fragment.TripFragment;
import com.gaokao366.gaokao366touser.model.version.bean.VersionBean;
import com.gaokao366.gaokao366touser.model.widget.NavBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainUIActivity extends BaseActivity {

    // Content View Elements

//法规法规和法国 到岗发过火
    //1903_A的分支测试sdfsdf sdf dsf sdfsd
    private ViewPager m_viewPager;
    private NavBar m_bottom;
    private List<Fragment> fragments = new ArrayList<>();
    private HashMap<String, Fragment> fMaps = new HashMap<>();

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_main_1);
    }

    // End Of Content View Elements

    private void bindViews() {
        m_viewPager = (ViewPager ) findViewById(R.id.m_viewPager);
        m_bottom = (NavBar) findViewById(R.id.m_bottom);
        m_bottom.setOnItemChangedListener(onBottomItemClickListener);
    }


    @Override
    public void initView() {
        bindViews();
        fragments.add(new HomeFragment());
        fragments.add(new TripFragment());
        fragments.add(new MsgFragment());
        fragments.add(new MineFragment());
        m_viewPager.setAdapter(mFragmentAdapter);
        m_bottom.setViewPager(m_viewPager);
    }


    /**
     * 底部导航栏的点击
     * 未登录状态下
     */
    NavBar.OnItemChangedListener onBottomItemClickListener = new NavBar.OnItemChangedListener() {
        @Override
        public boolean onItemChecked(int position) {
//            if(position==3){
//                if(softApplication.getUserInfo()==null) {
//                    UIManager.turnToAct(MainActivity.this, LoginActivity.class);
//                    return false ;
//                }
//            }
            return false;
        }
    };


    PagerAdapter mFragmentAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            LogUtil.log("正加载position----------" + position);
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    };

    /**
     * 网络框架讲解
     */
    public void getData() {
        Request request = RequestMaker.getInstance().getVersionRequest(2);
        getNetWorkDate(request, new SubBaseParser(VersionBean.class), new OnCompleteListener<VersionBean>(this) {
            @Override
            public void onSuccessed(VersionBean result, String resultString) {
                showToast(result.errCode + "====" + result.msg);
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
