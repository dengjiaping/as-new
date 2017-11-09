package com.jkpg.ruchu.view.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.view.adapter.MySpeakVPAdapter;
import com.jkpg.ruchu.view.fragment.MyReplyFragment;
import com.jkpg.ruchu.view.fragment.MySpeakFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/25.
 */

public class MySpeakActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_speak_tab_layout)
    TabLayout mMySpeakTabLayout;
    @BindView(R.id.my_speak_view_pager)
    ViewPager mMySpeakViewPager;

    private List<Fragment> views;
    private List<String> titles;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_speak);
        ButterKnife.bind(this);
        initHeader();

        initTabLayout();
        initViewPager();
    }


    private void initTabLayout() {
        mMySpeakTabLayout.setupWithViewPager(mMySpeakViewPager);
    }

    private void initViewPager() {
        views = new ArrayList<>();
        views.add(new MySpeakFragment());
        views.add(new MyReplyFragment());
        titles = new ArrayList<>();
        titles.add("我的帖子");
        titles.add("我的回帖");
        mMySpeakViewPager.setAdapter(new MySpeakVPAdapter(getSupportFragmentManager(),views, titles));

    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的发言");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
