package com.gaokao366.gaokao366touser;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.flyco.banner.anim.select.ZoomInEnter;
import com.gaokao366.gaokao366touser.model.ui.main.activity.MainUIActivity;
import com.gaokao366.gaokao366touser.model.ui.manager.UIManager;

public class GuideActivity extends AppCompatActivity {
    private Context context = this;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        decorView = getWindow().getDecorView();
        sgb();

    }

    private void sgb() {
        SimpleGuideBanner sgb = ViewFindUtils.find(decorView, R.id.sgb);

        sgb
                .setIndicatorWidth(6)
                .setIndicatorHeight(6)
                .setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
                .setTransformerClass(null)
                .barPadding(0, 10, 0, 10)
                .setSource(DataProvider.geUsertGuides())
                .startScroll();
        sgb.setOnJumpClickL(new SimpleGuideBanner.OnJumpClickL() {
            @Override
            public void onJumpClick() {
                UIManager.turnToAct(context, MainUIActivity.class);
            }
        });

    }
}
