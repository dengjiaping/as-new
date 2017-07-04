package com.jkpg.ruchu.view.activity.train;

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
import com.jkpg.ruchu.bean.VideoBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.OtherRLAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/18.
 */

public class OtherTrainActivity extends AppCompatActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.other_recycler_view)
    RecyclerView mOtherRecyclerView;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;

    private List<VideoBean> videos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_train);
        ButterKnife.bind(this);
        initHeader();
        initRecycleView();
    }

    private void initData() {
        videos = new ArrayList<>();
        videos.add(new VideoBean("核心肌群初级训练", "2:30", ""));
        videos.add(new VideoBean("核心肌群中级训练", "2:30", ""));
        videos.add(new VideoBean("核心肌群高级训练", "2:30", ""));
    }

    private void initRecycleView() {
        initData();
        mOtherRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mOtherRecyclerView.setHasFixedSize(true);
        OtherRLAdapter otherRLAdapter = new OtherRLAdapter(videos);
        mOtherRecyclerView.setAdapter(otherRLAdapter);
        otherRLAdapter.setOnItemClickListener(new OtherRLAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, VideoBean data) {
                startActivity(new Intent(OtherTrainActivity.this, VideoDetailActivity.class));
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("拓展训练");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
