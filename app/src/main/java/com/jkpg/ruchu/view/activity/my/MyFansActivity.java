package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FansListBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.MyFansRVAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by qindi on 2017/5/25.
 */

public class MyFansActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_fans_recyclerView)
    RecyclerView mMyFansRecyclerView;

    private List<FansListBean> fansList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        ButterKnife.bind(this);
        initHeader();
        initRecyclerView();
    }

    private void initRecyclerView() {
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",false));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",false));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        fansList.add(new FansListBean("","a","b",true));
        mMyFansRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        MyFansRVAdapter fansRVAdapter = new MyFansRVAdapter(fansList);
        mMyFansRecyclerView.setAdapter(fansRVAdapter);
        fansRVAdapter.setOnItemClickListener(new MyFansRVAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, FansListBean data) {
                startActivity(new Intent(MyFansActivity.this, FansCenterActivity.class));
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText(getIntent().getStringExtra("title"));
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
