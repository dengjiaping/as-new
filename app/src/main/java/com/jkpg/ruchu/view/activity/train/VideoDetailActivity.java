package com.jkpg.ruchu.view.activity.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.FeedBackRLAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by qindi on 2017/5/20.
 */

public class VideoDetailActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.video_player)
    JCVideoPlayerStandard mVideoPlayer;
    @BindView(R.id.video_recycler_view)
    RecyclerView mVideoRecyclerView;
    @BindView(R.id.video_et_feedback_body)
    EditText mVideoEtFeedbackBody;
    @BindView(R.id.video_btn_feedback)
    Button mVideoBtnFeedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);
        initHeader();
        initRecyclerView();
        initVideoPlayer();
    }

    private void initVideoPlayer() {
        mVideoPlayer.setUp("http://10.60.7.92:8080/AppServer/videos/456.MOV"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "哈哈哈哈");
        Glide.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/11/23/00b026e7-b830-4994-bc87-38f4033806a6.jpg@!640_360")
                .into(mVideoPlayer.thumbImageView);
    }

    private void initRecyclerView() {
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mVideoRecyclerView.setAdapter(new FeedBackRLAdapter());
    }

    private void initHeader() {
        // TODO: 2017/5/20    动态设置
//        mHeaderTvTitle.setText("");
        mHeaderTvRight.setText("动作详解");
    }

    @OnClick({R.id.header_iv_left, R.id.header_tv_right, R.id.video_btn_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_tv_right:
                startActivity(new Intent(VideoDetailActivity.this, MotionDetailActivity.class));

                break;
            case R.id.video_btn_feedback:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
