package com.jkpg.ruchu.view.activity.train;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by qindi on 2017/5/22.
 */

public class NewVideoDetailActivity extends AppCompatActivity {
    @BindView(R.id.new_video_player)
    JCVideoPlayerStandard mNewVideoPlayer;
    @BindView(R.id.new_video_tv_title)
    TextView mNewVideoTvTitle;
    @BindView(R.id.new_video_tv_detail)
    TextView mNewVideoTvDetail;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_video_detail);
        ButterKnife.bind(this);

        initVideo();
        initHeader();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("入门指导");
    }

    private void initVideo() {
        mNewVideoPlayer.setUp("https://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "哈哈哈哈");
        Glide.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/11/23/00b026e7-b830-4994-bc87-38f4033806a6.jpg@!640_360")
                .into(mNewVideoPlayer.thumbImageView);
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
