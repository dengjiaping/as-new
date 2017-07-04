package com.jkpg.ruchu.view.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.view.adapter.FansRLAdapter;
import com.jkpg.ruchu.view.adapter.MySpeakVPAdapter;
import com.jkpg.ruchu.view.adapter.SenderRLAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/25.
 */

public class MySpeakActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_speak_tab_layout)
    TabLayout mMySpeakTabLayout;
    @BindView(R.id.my_speak_view_pager)
    ViewPager mMySpeakViewPager;

    private List<View> views;
    private List<String> titles;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_speak);
        ButterKnife.bind(this);
        initHeader();
        initData();
        initViewPager();
        initTabLayout();
    }

    private void initData() {
        views = new ArrayList<>();
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FansRLAdapter());
        RecyclerView recyclerView2 = new RecyclerView(this);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(new SenderRLAdapter());
        views.add(recyclerView);
        views.add(recyclerView2);
        titles = new ArrayList<>();
        titles.add("我的帖子");
        titles.add("我的回帖");
    }

    private void initTabLayout() {
        mMySpeakTabLayout.setupWithViewPager(mMySpeakViewPager);
    }

    private void initViewPager() {
        mMySpeakViewPager.setAdapter(new MySpeakVPAdapter(views, titles));

    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的发言");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
