package com.jkpg.ruchu.view.activity.my;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MyMessageBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.PlateDetailVPAdapter;
import com.jkpg.ruchu.view.fragment.MySmsLoveFragment;
import com.jkpg.ruchu.view.fragment.MySmsNoticFragment;
import com.jkpg.ruchu.view.fragment.MySmsReplyFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/27.
 */

public class MySMSActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_speak_tab_layout)
    SlidingTabLayout mMySpeakTabLayout;
    @BindView(R.id.my_speak_view_pager)
    ViewPager mMySpeakViewPager;
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sms);
        ButterKnife.bind(this);
        initHeader();

        init();
        OkGo
                .post(AppUrl.MYMASSAGE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("flag", 0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyMessageBean myMessageBean = new Gson().fromJson(s, MyMessageBean.class);
                        if (myMessageBean.notice) {
                            mMySpeakTabLayout.showDot(0);
                            mMySpeakTabLayout.setMsgMargin(0, 30, 10);
                        } else {
                            mMySpeakTabLayout.hideMsg(0);
                        }
                        if (myMessageBean.reply) {
                            mMySpeakTabLayout.showDot(1);
                            mMySpeakTabLayout.setMsgMargin(1, 30, 10);

                        } else {
                            mMySpeakTabLayout.hideMsg(1);
                        }
                        if (myMessageBean.zan) {
                            mMySpeakTabLayout.showDot(2);
                            mMySpeakTabLayout.setMsgMargin(2, 40, 10);

                        } else {
                            mMySpeakTabLayout.hideMsg(2);
                        }
                    }
                });
        OkGo
                .post(AppUrl.MYMASSAGE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("flag", 1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mMySpeakTabLayout.hideMsg(0);
                    }
                });
    }

    private void init() {
        List<Fragment> views = new ArrayList<>();
        List<String> title = new ArrayList<>();
        title.add("通知");
        title.add("评论");
        title.add("赞");
        MySmsNoticFragment mySmsPrivateFragment = new MySmsNoticFragment();
        views.add(mySmsPrivateFragment);
        MySmsReplyFragment mySmsReplyFragment = new MySmsReplyFragment();
        views.add(mySmsReplyFragment);
        MySmsLoveFragment mySmsLoveFragment = new MySmsLoveFragment();
        views.add(mySmsLoveFragment);

        //        RecyclerView recyclerView = new RecyclerView(this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new MySMSAdapter());
//        mMySpeakTabLayout.setupWithViewPager(mMySpeakViewPager);
        mMySpeakViewPager.setAdapter(new PlateDetailVPAdapter(getSupportFragmentManager(), views, title));
        mMySpeakViewPager.setOffscreenPageLimit(3);
        mMySpeakTabLayout.setViewPager(mMySpeakViewPager);
        mMySpeakTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mMySpeakViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                OkGo
                        .post(AppUrl.MYMASSAGE)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("flag", position + 1)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                mMySpeakTabLayout.hideMsg(position);

                            }
                        });

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initHeader() {
        mHeaderTvTitle.setText("我的消息");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHeaderView.setElevation(0);
        }
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }

    public ImageView getHeaderIvLeft() {
        return mHeaderIvLeft;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post("initSms");
    }
}
