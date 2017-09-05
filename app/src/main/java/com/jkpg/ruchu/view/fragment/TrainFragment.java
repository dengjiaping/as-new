package com.jkpg.ruchu.view.fragment;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.IsVipBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SmsEvent;
import com.jkpg.ruchu.bean.TrainMainBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.WebViewActivity;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.activity.my.MySMSActivity;
import com.jkpg.ruchu.view.activity.my.OpenVipActivity;
import com.jkpg.ruchu.view.activity.train.FullActivity;
import com.jkpg.ruchu.view.activity.train.MyTrainActivity;
import com.jkpg.ruchu.view.activity.train.NewTrainActivity;
import com.jkpg.ruchu.view.activity.train.OtherTrainActivity;
import com.jkpg.ruchu.view.activity.train.TestTrainActivity;
import com.jkpg.ruchu.view.activity.train.TrainPrepareActivity;
import com.jkpg.ruchu.widget.banner.Banner;
import com.jkpg.ruchu.widget.banner.BannerConfig;
import com.jkpg.ruchu.widget.banner.Transformer;
import com.jkpg.ruchu.widget.banner.listener.OnBannerListener;
import com.jkpg.ruchu.widget.banner.loader.GlideImageLoader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/16.
 */

public class TrainFragment extends Fragment {
    @BindView(R.id.train_banner)
    Banner mTrainBanner;
    Unbinder unbinder;
    List<String> images;
    List<String> titles;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.train_ll_new)
    LinearLayout mTrainLlNew;
    @BindView(R.id.train_iv_new_hand)
    ImageView mTrainIvNewHand;
    @BindView(R.id.train_ll_my)
    LinearLayout mTrainLlMy;
    @BindView(R.id.train_ll_other)
    LinearLayout mTrainLlOther;
    @BindView(R.id.train_tv_tip)
    TextView mTrainTvTip;
    @BindView(R.id.train_bt_test)
    Button mTrainBtTest;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.train_tv_test)
    TextView mTrainTvTest;
    @BindView(R.id.train_ll_test)
    LinearLayout mTrainLlTest;
    @BindView(R.id.train_bt_train)
    Button mTrainBtTrain;
    @BindView(R.id.train_ll_train)
    LinearLayout mTrainLlTrain;
    @BindView(R.id.errorStateRelativeLayout)
    RelativeLayout mBaseRetry;
    @BindView(R.id.id_loading_and_retry)
    FrameLayout mBaseLoading;
    @BindView(R.id.train_tv_level)
    TextView mTrainTvLevel;
    @BindView(R.id.train_ll)
    LinearLayout mTrainLl;
    @BindView(R.id.train_image)
    ImageView mTrainImage;@BindView(R.id.train_image1)
    ImageView mTrainImage1;

    private TrainMainBean mTrainMainBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initHeader();
        mTrainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(getActivity(), FullActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity(), mTrainImage1, "sharedView").toBundle());
                } else {
                    startActivity(new Intent(getActivity(), FullActivity.class));
                }
            }
        });
    }


    private void initData() {
        OkGo
                .post(AppUrl.HEADERLUNBOIMAGE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .cacheKey("HEADERLUNBOIMAGE")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mTrainMainBean = new Gson().fromJson(s, TrainMainBean.class);
                        initContent();
                        mBaseLoading.setVisibility(View.GONE);
                        mBaseRetry.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        mTrainMainBean = new Gson().fromJson(s, TrainMainBean.class);
                        initContent();
                        mBaseLoading.setVisibility(View.GONE);
                        mBaseRetry.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        mBaseLoading.setVisibility(View.GONE);
                        mBaseRetry.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);

                        mTrainIvNewHand.setVisibility(View.VISIBLE);
                        mTrainTvTip.setVisibility(View.VISIBLE);
                        mTrainLlTest.setVisibility(View.VISIBLE);
                        mTrainLl.setVisibility(View.GONE);
                        mTrainLlTrain.setVisibility(View.GONE);

                        mBaseLoading.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initContent() {

        if (mTrainMainBean.uIsfirst != null && mTrainMainBean.uIsfirst.equals("1")) {
            mTrainIvNewHand.setVisibility(View.GONE);
        }
        if (mTrainMainBean.uIsstest != null && mTrainMainBean.uIsstest.equals("1")) {
            mTrainTvLevel.setText("当前训练等级: 难度 " + mTrainMainBean.uLevel + "        第" + mTrainMainBean.excisedays + "天");
            mTrainLl.setVisibility(View.VISIBLE);
            initTestBtn();
        } else {
            mTrainLl.setVisibility(View.GONE);
            mTrainTvTip.setVisibility(View.VISIBLE);
            mTrainLlTest.setVisibility(View.VISIBLE);
            mTrainLlTrain.setVisibility(View.GONE);
        }
        initBanner();
    }

    //初始化训练按钮状态 + 新手提示
    private void initTestBtn() {
        mTrainTvTip.setVisibility(View.GONE);
        mTrainLlTest.setVisibility(View.GONE);
        mTrainLlTrain.setVisibility(View.VISIBLE);
    }

    private void initHeader() {
        mHeaderIvLeft.setVisibility(View.GONE);
        mHeaderTvTitle.setText("如初康复");
        mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
    }

    private void initBanner() {
        images = new ArrayList<>();
        titles = new ArrayList<>();
        final List<TrainMainBean.HeaderLunBoImageBean> headerLunBoImage = mTrainMainBean.headerLunBoImage;
        if (headerLunBoImage == null) {
            return;
        }
        for (int i = 0; i < headerLunBoImage.size(); i++) {
            images.add(AppUrl.BASEURL + headerLunBoImage.get(i).imageUrl);
            titles.add(headerLunBoImage.get(i).title);
        }
        //设置图片加载器
        mTrainBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mTrainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mTrainBanner.setImages(images);
        //设置banner动画效果
        mTrainBanner.setBannerAnimation(Transformer.CubeOut);
        //设置标题集合（当banner样式有显示title时）
        mTrainBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mTrainBanner.isAutoPlay(true);
        //设置轮播时间
        mTrainBanner.setDelayTime(3000);
        mTrainBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String allcontent = headerLunBoImage.get(position).allcontent;
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("URL", allcontent);
                startActivity(intent);
            }
        });
        //设置指示器位置（当banner模式中有指示器时）
//        mTrainBanner.setIndicatorGravity(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mTrainBanner.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.train_ll, R.id.id_btn_retry, R.id.header_iv_right, R.id.train_ll_new, R.id.train_ll_my, R.id.train_bt_train, R.id.train_ll_other, R.id.train_tv_tip, R.id.train_bt_test})
    public void onViewClicked(View view) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        switch (view.getId()) {
            case R.id.id_btn_retry:
                TrainFragment.this.initData();
                break;
            case R.id.train_ll_new:
              /*  if (SPUtils.getString(UIUtils.getContext(),Constants.USERID,"").equals("")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                }*/
                startActivity(new Intent(getActivity(), NewTrainActivity.class));
                break;
            case R.id.train_ll_my:
                if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                }
                startActivity(new Intent(getActivity(), MyTrainActivity.class));
                break;
            case R.id.train_ll_other:
                if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                }
                startActivity(new Intent(getActivity(), OtherTrainActivity.class));
                break;
            case R.id.train_tv_tip:
                mTrainTvTip.setVisibility(View.GONE);
                break;
            case R.id.train_bt_test:
                startActivity(new Intent(getActivity(), TestTrainActivity.class));
                break;
            case R.id.train_bt_train:
                if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                }
                OkGo
                        .post(AppUrl.SELECTISVIP)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringDialogCallback(getActivity()) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                IsVipBean isVipBean = new Gson().fromJson(s, IsVipBean.class);

                                if (isVipBean.isVIP) {
                                    if (!isVipBean.excisedays) {
                                        final String[] array = new String[]{"循环当前难度训练", "进入下一难度训练"};
                                        final int[] selectedIndex = {0};
                                        new AlertDialog.Builder(getContext())
                                                .setTitle("您已完成难度" + mTrainMainBean.uLevel + "全部天数的训练,接下来你可以选择进行:")
                                                .setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        selectedIndex[0] = which;
                                                    }
                                                })
                                                .setNeutralButton("查看全部", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(getActivity(), MyTrainActivity.class));
                                                    }
                                                })
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (selectedIndex[0] == 0) {
                                                            OkGo
                                                                    .post(AppUrl.RESTARTTHISLEVEL)
                                                                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                                                    .execute(new StringDialogCallback(getActivity()) {
                                                                        @Override
                                                                        public void onSuccess(String s, Call call, Response response) {
                                                                            initData();

                                                                            startActivity(new Intent(getActivity(), TrainPrepareActivity.class));

                                                                        }
                                                                    });
                                                        } else {
                                                            OkGo
                                                                    .post(AppUrl.OPENMYPRACTICE)
                                                                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                                                    .params("level", (mTrainMainBean.uLevel + 1 + ""))
                                                                    .execute(new StringDialogCallback(getActivity()) {
                                                                        @Override
                                                                        public void onSuccess(String s, Call call, Response response) {
                                                                            initData();
                                                                            startActivity(new Intent(getActivity(), TrainPrepareActivity.class));
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                })
                                                .show();
                                    } else {
                                        startActivity(new Intent(getActivity(), TrainPrepareActivity.class));
                                    }
                                } else {
                                    new AlertDialog.Builder(getContext())
                                            .setMessage("只有会员才能训练哦")
                                            .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(getActivity(), OpenVipActivity.class));
                                                }
                                            })
                                            .setNegativeButton("放弃训练", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }

                            }
                        });
                break;
            case R.id.header_iv_right:
                if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MySMSActivity.class));
                break;
            case R.id.train_ll:
                new AlertDialog.Builder(getActivity())
                        .setMessage(mTrainMainBean.userInfos.introduction + "\n\n建议强度: " + mTrainMainBean.advise)
                        .setTitle("您当前的训练等级: 难度" + mTrainMainBean.uLevel)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mTrainBanner.stopAutoPlay();
    }

    @Override
    public void onStart() {
        super.onStart();
        mTrainBanner.startAutoPlay();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        /* Do something */
        if (event.message.equals("Login") || event.message.equals("Quit")) {
            MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            }, 500);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Mess(String s) {
        if (s.equals("TrainFragment")) {
            initData();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMess(SmsEvent sms) {
        if (sms.sms) {
            mHeaderIvRight.setImageResource(R.drawable.icon_sms);
        } else {
            mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
        }
    }
}
