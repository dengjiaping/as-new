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
import com.jkpg.ruchu.view.adapter.NewRLAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/18.
 */

public class NewTrainActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.other_recycler_view)
    RecyclerView mRecyclerView;

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
        videos.add(new VideoBean("第一步：认识盆底肌", "2:30", ""));
        videos.add(new VideoBean("第二部：正确收缩", "2:30", ""));
        videos.add(new VideoBean("第三部：常见误区", "2:30", ""));
    }

    private void initRecycleView() {
        initData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mRecyclerView.setHasFixedSize(true);
        NewRLAdapter newRLAdapter = new NewRLAdapter(videos);
        mRecyclerView.setAdapter(newRLAdapter);
        newRLAdapter.setOnItemClickListener(new NewRLAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, VideoBean data) {
                startActivity(new Intent(NewTrainActivity.this, NewVideoDetailActivity.class));
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("入门指导");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
