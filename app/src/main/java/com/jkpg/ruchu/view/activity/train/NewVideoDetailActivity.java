package com.jkpg.ruchu.view.activity.train;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.widget.JCVideoPlayerStandard;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by qindi on 2017/5/22.
 */

public class NewVideoDetailActivity extends BaseActivity {
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
        initHeader();

    }

    private void initHeader() {
        mHeaderTvTitle.setText("入门指导");
    }

//    private void initVideo(VideoBean.VideoMSBean.VideomessBean vediomessBean) {
//        mNewVideoPlayer.setUp(AppUrl.BASEURL + vediomessBean.videourl
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, vediomessBean.title);
//        LogUtils.i(AppUrl.BASEURL + vediomessBean.image);
//        Glide.with(this)
//                .load(AppUrl.BASEURL + vediomessBean.image)
//                .into(mNewVideoPlayer.thumbImageView);
//        mNewVideoTvTitle.setText(vediomessBean.title);
//        mNewVideoTvDetail.setText(vediomessBean.content);


//    }

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
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
