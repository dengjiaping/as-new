package com.jkpg.ruchu.view.activity.train;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.IsVipBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.bean.VideoDetailBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.WebViewActivity;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.activity.my.OpenVipActivity;
import com.jkpg.ruchu.view.adapter.FeedBackRLAdapter;
import com.jkpg.ruchu.widget.FreshDownloadView;
import com.jkpg.ruchu.widget.JCVideoPlayerStandard;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Call;
import okhttp3.Response;

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
    @BindView(R.id.download)
    FreshDownloadView mDownload;
    @BindView(R.id.download_text)
    TextView mDownloadText;
    @BindView(R.id.view_start)
    View mViewStart;
    private String mTid;
    private String mDetailsUrl;
    private FeedBackRLAdapter mFeedBackRLAdapter;
    private String mPosition;
    private AlertDialog mShow;
    private AlertDialog.Builder mBuilder;
    private MyRunnable mMyRunnable;
    private VideoDetailBean mVideoDetailBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mTid = getIntent().getStringExtra("tid");
        mPosition = getIntent().getStringExtra("position");

        initData();
        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(UIUtils.getContext(), "边缓存边播哦");
            }
        });
        mMyRunnable = new MyRunnable();
    }

    private void initData() {
        OkGo
                .post(AppUrl.NEWHANDVEDIO)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("videoid", mTid)
                .execute(new StringDialogCallback(VideoDetailActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mVideoDetailBean = new Gson().fromJson(s, VideoDetailBean.class);
                        initVideoPlayer(mVideoDetailBean.videoMS);
                        initRecyclerView(mVideoDetailBean.videoMS.discuss);
                        mDetailsUrl = mVideoDetailBean.videoMS.detailsUrl;
                        initHeader(mVideoDetailBean.videoMS.title);
                        isVip();
                    }
                });


    }

    private void isVip() {
        OkGo
                .post(AppUrl.SELECTISVIP)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(VideoDetailActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        IsVipBean isVipBean = new Gson().fromJson(s, IsVipBean.class);

                        if (!isVipBean.isVIP) {
                            if (mPosition.equals("0")) {
//                                mMyRunnable.start();
                                mMyRunnable = new MyRunnable();
                                MyApplication.getMainThreadHandler().postDelayed(mMyRunnable, 1000);
                                JCUtils.clearSavedProgress(VideoDetailActivity.this, AppUrl.BASEURLHTTP + mVideoDetailBean.videoMS.videourl);
                            } else {
                                new AlertDialog.Builder(VideoDetailActivity.this)
                                        .setMessage("只有会员才能观看哦!")
                                        .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(VideoDetailActivity.this, OpenVipActivity.class));
                                            }
                                        })
                                        .setNegativeButton("放弃开通", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                                mVideoPlayer.setUiWitStateAndScreen(JCVideoPlayer.NORMAL_ORIENTATION);
//                                                mVideoPlayer.changeUiToNormal();
                            }
                        }

                    }
                });
    }

    private class MyRunnable implements Runnable {

        @Override
        public void run() {
            LogUtils.d("currentPosition" + mVideoPlayer.getCurrentPosition());
            if (mVideoPlayer.getCurrentPosition() >= 5 * 60 * 1000) {
                if (mShow != null && mShow.isShowing()) {
                    return;
                }
                mBuilder = new AlertDialog.Builder(VideoDetailActivity.this)
                        .setMessage("开会员后继续观看哦!")
                        .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(VideoDetailActivity.this, OpenVipActivity.class));
                            }
                        })
                        .setNegativeButton("放弃开通", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
//                                MyApplication.getMainThreadHandler().removeCallbacks(mMyRunnable);
//                                mShow.dismiss();
                            }
                        })
                        .setCancelable(false);
                mShow = mBuilder.show();
//                                            mVideoPlayer.setUiWitStateAndScreen(JCVideoPlayer.NORMAL_ORIENTATION);
//                                                            mVideoPlayer.changeUiToNormal();
                mVideoPlayer.startButton.performClick();
            }
            MyApplication.getMainThreadHandler().postDelayed(this, 1000);

        }

    }

    private void initVideoPlayer(final VideoDetailBean.VideoMSBean videoMS) {
        final HttpProxyCacheServer proxy = MyApplication.getProxy(VideoDetailActivity.this);
        String proxyUrl = proxy.getProxyUrl(AppUrl.BASEURLHTTP + videoMS.videourl);
        if (proxy.isCached(AppUrl.BASEURLHTTP + videoMS.videourl)) {
            mDownloadText.setText("已缓存");
            mDownload.showDownloadOk();
            mVideoPlayer.bottomProgressBar.setSecondaryProgress(100);

        } else {
            mDownload.reset();
        }
        mVideoPlayer.setUp(proxyUrl
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, videoMS.title);
        Glide.with(this)
                .load(AppUrl.BASEURL + videoMS.image_url)
                .crossFade()
                .centerCrop()
                .into(mVideoPlayer.thumbImageView);
        proxy.registerCacheListener(new CacheListener() {
            @Override
            public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
                if (percentsAvailable == 1) {
//                    ToastUtils.showShort(UIUtils.getContext(), "边播边缓存");

                    mDownload.startDownload();
                }
                mDownload.upDateProgress(percentsAvailable);
                mVideoPlayer.bottomProgressBar.setSecondaryProgress(percentsAvailable);
                if (percentsAvailable == 100) {
                    mDownloadText.setText("已经缓存");
                }
            }
        }, AppUrl.BASEURLHTTP + videoMS.videourl);
        mViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoPlayer.startButton.performClick();
                mViewStart.setVisibility(View.GONE);
                if (proxy.isCached(AppUrl.BASEURLHTTP + videoMS.videourl)) {
                    mDownloadText.setText("已缓存");
                    mDownload.showDownloadOk();
                } else {
                    mDownload.startDownload();
                }
            }
        });


    }


    private void initRecyclerView(final List<VideoDetailBean.VideoMSBean.DiscussBean> discuss) {
        mVideoRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mFeedBackRLAdapter = new FeedBackRLAdapter(R.layout.item_feedback, discuss);
        mVideoRecyclerView.setAdapter(mFeedBackRLAdapter);
        mFeedBackRLAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(VideoDetailActivity.this, FansCenterActivity.class);
                intent.putExtra("fansId", discuss.get(position).userid);
                startActivity(intent);
            }
        });
    }

    private void initHeader(String title) {
        mHeaderTvTitle.setText(title);
        mHeaderTvRight.setText("动作详解");
    }

    @OnClick({R.id.header_iv_left, R.id.header_tv_right, R.id.video_btn_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_tv_right:
                Intent intent = new Intent(VideoDetailActivity.this, WebViewActivity.class);
                intent.putExtra("URL", mDetailsUrl);
                startActivity(intent);
                break;
            case R.id.video_btn_feedback:
                reply();
                break;
        }
    }

    private void reply() {
        if (StringUtils.isEmpty(mVideoEtFeedbackBody.getText().toString())) {
            return;
        }

        OkGo
                .post(AppUrl.PINGLUNTUOZHAN)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("videoid", mTid)
                .params("content", mVideoEtFeedbackBody.getText().toString())
                .execute(new StringDialogCallback(VideoDetailActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        closeInputMethod();
                        mVideoEtFeedbackBody.setText("");
                        OkGo
                                .post(AppUrl.NEWHANDVEDIO)
                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                .params("videoid", mTid)
                                .execute(new StringDialogCallback(VideoDetailActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                        if (successBean.state == 200) {
                                            VideoDetailBean videoDetailBean = new Gson().fromJson(s, VideoDetailBean.class);
                                            initRecyclerView(videoDetailBean.videoMS.discuss);
                                            mVideoRecyclerView.scrollToPosition(videoDetailBean.videoMS.discuss.size() - 1);
                                        } else {
                                            ToastUtils.showShort(UIUtils.getContext(), "反馈失败");
                                        }
                                    }
                                });
                    }
                });

    }

    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
            imm.hideSoftInputFromWindow(mVideoEtFeedbackBody.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        MyApplication.getMainThreadHandler().removeCallbacks(mMyRunnable);
        EventBus.getDefault().post("OtherTrain");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(MessageEvent mess) {
        if (mess.message.equals("Vip")) {
            MyApplication.getMainThreadHandler().removeCallbacks(mMyRunnable);
            if (mShow != null && mShow.isShowing()) {
                mShow.dismiss();
            }
        }
    }
}
