package com.gaokao366.gaokao366touser.model.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.flyco.banner.anim.select.ZoomInEnter;
import com.flyco.banner.transform.ZoomOutSlideTransformer;
import com.gaokao366.gaokao366touser.DataProvider;
import com.gaokao366.gaokao366touser.R;
import com.gaokao366.gaokao366touser.SimpleImageBanner;
import com.gaokao366.gaokao366touser.model.framework.activity.BaseFragment;
import com.gaokao366.gaokao366touser.model.ui.main.adapter.ExamPaperAdapter;
import com.gaokao366.gaokao366touser.model.ui.main.adapter.SubjectAdapter;
import com.gaokao366.gaokao366touser.model.ui.main.adapter.WatchAdapter;
import com.gaokao366.gaokao366touser.model.ui.main.bean.ExamPaperBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by hh on 2016/5/18.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.sib_the_most_comlex_usage)
    SimpleImageBanner sib;
    @ViewInject(R.id.gridView)
    GridView gridView;
    @ViewInject(R.id.lv_watch)
    ListView lv_watch;
    @ViewInject(R.id.gv_subject)
    GridView gv_subject;


    ExamPaperAdapter examPaperAdapter;
    WatchAdapter watchAdapter;
    SubjectAdapter subjectAdapter;

    Context mContext;

    @Override
    public void setContentLayout(Bundle savedInstanceState) {
        setContentView(R.layout.f_home);
        mContext = getActivity();
    }


    @Override
    public void initView(View v) {
        ViewUtils.inject(this, v);
        sib.setSource(DataProvider.getList()).startScroll();


        examPaperAdapter = new ExamPaperAdapter(mContext);
        examPaperAdapter.setItemList(DataProvider.getExamPaperBean());
        gridView.setAdapter(examPaperAdapter);

        watchAdapter = new WatchAdapter(mContext);
        watchAdapter.setItemList(DataProvider.getWatchBean());
        lv_watch.setAdapter(watchAdapter);

        subjectAdapter = new SubjectAdapter(mContext);
        subjectAdapter.setItemList(DataProvider.getSubjecyBean());
        gv_subject.setAdapter(subjectAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onNetChanged(boolean oldStatus, boolean newStatus) {

    }

    @Override
    public void onClick(View v) {
    }

}
