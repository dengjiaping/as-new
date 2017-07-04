package com.jkpg.ruchu.view.activity.train;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.MotionDetailRLAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/20.
 */

public class MotionDetailActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.motion_detail_recycler_view)
    RecyclerView mMotionDetailRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_detail);
        ButterKnife.bind(this);
        initHeader();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mMotionDetailRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mMotionDetailRecyclerView.setAdapter(new MotionDetailRLAdapter());
    }

    private void initHeader() {
        mHeaderTvTitle.setText("动作详解");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
