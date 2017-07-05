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
import com.jkpg.ruchu.view.adapter.MyCommentAdapter;
import com.jkpg.ruchu.view.adapter.MySMSAdapter;
import com.jkpg.ruchu.view.adapter.VipManageVPAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/27.
 */

public class MySMSActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_speak_tab_layout)
    TabLayout mMySpeakTabLayout;
    @BindView(R.id.my_speak_view_pager)
    ViewPager mMySpeakViewPager;

    private List<View> mViews;
    private List<String> mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_speak);
        ButterKnife.bind(this);
        initHeader();
        initData();
        init();

    }

    private void init() {
        mMySpeakTabLayout.setupWithViewPager(mMySpeakViewPager);
        mMySpeakViewPager.setAdapter(new VipManageVPAdapter(mViews,mTitle));
    }

    private void initData() {
        mViews = new ArrayList<>();
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MySMSAdapter());
        RecyclerView recyclerView2 = new RecyclerView(this);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(new MyCommentAdapter());
        mViews.add(recyclerView2);
        mViews.add(new View(this));
        mViews.add(new View(this));
        mTitle = new ArrayList<>();
        mTitle.add("私信");
        mTitle.add("评论");
        mTitle.add("赞");
    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的消息");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
