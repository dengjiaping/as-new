package com.jkpg.ruchu.view.fragment;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.IsVipBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SmsEvent;
import com.jkpg.ruchu.bean.TrainMainBean2;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.HtmlActivity;
import com.jkpg.ruchu.view.activity.ShopActivity;
import com.jkpg.ruchu.view.activity.community.FineNoteDetailWebFixActivity;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.activity.my.MySMSActivity;
import com.jkpg.ruchu.view.activity.my.OpenVipActivity;
import com.jkpg.ruchu.view.activity.train.LookRewardDetailsActivity;
import com.jkpg.ruchu.view.activity.train.MyTrainActivity;
import com.jkpg.ruchu.view.activity.train.NewTrainActivity;
import com.jkpg.ruchu.view.activity.train.TestTrainActivity;
import com.jkpg.ruchu.view.activity.train.TrainPrepareActivity;
import com.jkpg.ruchu.view.activity.train.VideoDetailActivity;
import com.jkpg.ruchu.view.adapter.TrainVedioAdapter;
import com.jkpg.ruchu.widget.TrainCircle.Circle2Progress;
import com.jkpg.ruchu.widget.TrainCircle.CircleProgress;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.umeng.message.PushAgent;

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

public class TrainFragmentV2 extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.train_tv_now_time)
    TextView mTrainTvNowTime;
    @BindView(R.id.train_tv_count_time)
    TextView mTrainTvCountTime;
    @BindView(R.id.train_tv_target)
    TextView mTrainTvTarget;
    @BindView(R.id.train_tv_info)
    TextView mTrainTvInfo;
    @BindView(R.id.train_recycler_other)
    RecyclerView mTrainRecyclerOther;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.include2)
    RelativeLayout mHeaderView;
    @BindView(R.id.train_tv_new)
    TextView mTrainTvNew;
    @BindView(R.id.train_view)
    View mTrainView;
    @BindView(R.id.train_circle_week)
    Circle2Progress mTrainCircleWeek;
    @BindView(R.id.train_circle_day)
    CircleProgress mTrainCircleDay;
    @BindView(R.id.train_tv_prize)
    TextView mTrainTvPrize;
    @BindView(R.id.train_btn_other)
    TextView mTrainBtnOther;
    @BindView(R.id.train_start)
    LinearLayout mTrainStart;
    @BindView(R.id.train_test)
    LinearLayout mTrainTest;
    @BindView(R.id.train_img_new)
    ImageView mTrainImgNew;
    @BindView(R.id.train_test_text)
    TextView mTrainTestText;
    @BindView(R.id.errorStateRelativeLayout)
    RelativeLayout mBaseRetry;
    @BindView(R.id.id_loading_and_retry)
    FrameLayout mBaseLoading;
    @BindView(R.id.train_article_title)
    TextView mTrainArticleTitle;
    @BindView(R.id.train_article_image)
    ImageView mTrainArticleImage;
    @BindView(R.id.train_ll_article)
    LinearLayout mTrainLlArticle;
    @BindView(R.id.train_shop_title)
    TextView mTrainShopTitle;
    @BindView(R.id.train_shop_image)
    ImageView mTrainShopImage;
    @BindView(R.id.train_ll_shop)
    LinearLayout mTrainLlShop;

    //一周训练目标
    private int date = 3;
    //一天训练目标
    private int dateNum = 2;
    //当前训练
    private int nowDateWeek = 1;

    private int nowDateDay = 0;

    private String nowTime;

    private String countTime;
    private List<TrainMainBean2.TuozhanxlBean> mVideoList;

    private boolean isVideoMore;
    private TrainMainBean2 mTrainMainBean2;

    private String flagTime = "分钟";
    private boolean isShow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_v2, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeaderIvLeft.setVisibility(View.GONE);
        mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
        mHeaderTvTitle.setText("盆底肌训练");
        mTrainTvNowTime.setText(Html.fromHtml("今日已训练  <font ><big>" + "000" + "</big></font>  分钟"));
        mTrainTvCountTime.setText(Html.fromHtml("累计训练  <font ><big>" + "000" + "</big></font>  分钟"));
        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.KANGFUINDEX)
                .cacheKey("KANGFUINDEX" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("devicetoken", PushAgent.getInstance(getActivity()).getRegistrationId())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mBaseLoading.setVisibility(View.GONE);
                        mBaseRetry.setVisibility(View.GONE);
                        mTrainMainBean2 = new Gson().fromJson(s, TrainMainBean2.class);
                        if (mTrainMainBean2.alltime / 60 >= 1000) {
                            String s1 = mTrainMainBean2.alltime / 60 % 60 + "";
                            s1 = s1.substring(0, 1);
                            countTime = mTrainMainBean2.alltime / 60 / 60 + "." + s1 + "";
                            flagTime = "小时";
                        } else {
                            countTime = mTrainMainBean2.alltime / 60 + "";
                        }
                        nowTime = mTrainMainBean2.currDaytime / 60 + "";
                        date = mTrainMainBean2.everyweektimes;
                        dateNum = mTrainMainBean2.everytimes;
                        nowDateDay = mTrainMainBean2.todayXltimes;
                        if (date * dateNum < mTrainMainBean2.weekxltimes) {
                            nowDateWeek = date * dateNum;
                        } else {
                            nowDateWeek = mTrainMainBean2.weekxltimes;
                        }
                        init();
                        initInfo();
                        initRecyclerView(mTrainMainBean2.tuozhanxl);

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        mBaseLoading.setVisibility(View.GONE);
                        mBaseRetry.setVisibility(View.GONE);
                        mTrainMainBean2 = new Gson().fromJson(s, TrainMainBean2.class);
                        if (mTrainMainBean2.alltime / 60 >= 1000) {
                            String s1 = mTrainMainBean2.alltime / 60 % 60 + "";
                            s1 = s1.substring(0, 1);
                            countTime = mTrainMainBean2.alltime / 60 / 60 + "." + s1 + "";
                            flagTime = "小时";
                        } else {
                            countTime = mTrainMainBean2.alltime / 60 + "";
                        }
                        nowTime = mTrainMainBean2.currDaytime / 60 + "";
                        date = mTrainMainBean2.everyweektimes;
                        dateNum = mTrainMainBean2.everytimes;
//                        nowDateWeek = mTrainMainBean2.weekxltimes;
                        if (date * dateNum < mTrainMainBean2.weekxltimes) {
                            nowDateWeek = date * dateNum;
                        } else {
                            nowDateWeek = mTrainMainBean2.weekxltimes;
                        }
                        nowDateDay = mTrainMainBean2.todayXltimes;
                        mTrainMainBean2.windowflag = "1";
                        init();
                        initInfo();
                        initRecyclerView(mTrainMainBean2.tuozhanxl);

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
                        mBaseLoading.setVisibility(View.VISIBLE);
                    }
                });

    }

    private void initInfo() {
        if (mTrainMainBean2.listArticle != null && mTrainMainBean2.listArticle.size() > 0) {
            mTrainLlArticle.setVisibility(View.VISIBLE);
            final TrainMainBean2.ListArticleBean listArticleBean = mTrainMainBean2.listArticle.get(0);
            mTrainArticleTitle.setText("康复干货");
            Glide
                    .with(UIUtils.getContext())
                    .load(AppUrl.BASEURL + listArticleBean.tuijianimg)
                    .crossFade()
                    .error(R.color.colorXmlBg)
                    .placeholder(R.color.colorXmlBg)
                    .into(mTrainArticleImage);
            mTrainLlArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FineNoteDetailWebFixActivity.class);
                    intent.putExtra("art_id", listArticleBean.ID + "");
                    startActivity(intent);
                }
            });


        } else {
            mTrainLlArticle.setVisibility(View.GONE);
        }
        if (mTrainMainBean2.listShopping != null && mTrainMainBean2.listShopping.size() > 0) {
            mTrainLlShop.setVisibility(View.VISIBLE);
            final TrainMainBean2.ListShoppingBean listShoppingBean = mTrainMainBean2.listShopping.get(0);
            mTrainShopTitle.setText("优选推荐");
            Glide
                    .with(UIUtils.getContext())
                    .load(AppUrl.BASEURL + listShoppingBean.tuijianimg)
                    .crossFade()
                    .error(R.color.colorXmlBg)
                    .placeholder(R.color.colorXmlBg)
                    .into(mTrainShopImage);

            mTrainLlShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("url", listShoppingBean.shoppingurl);
                    startActivity(intent);
                }
            });


        } else {
            mTrainLlShop.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView(final List<TrainMainBean2.TuozhanxlBean> tuozhanxl) {
        mVideoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mVideoList.add(tuozhanxl.get(i));
        }
        mTrainRecyclerOther.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        final TrainVedioAdapter trainVedioAdapter = new TrainVedioAdapter(R.layout.item_train_vedio, mVideoList);
        mTrainRecyclerOther.setAdapter(trainVedioAdapter);
        trainVedioAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                    intent.putExtra("tid", tuozhanxl.get(position).videoid);
                    intent.putExtra("position", position + "");
                    startActivity(intent);
                } else {
                    OkGo
                            .post(AppUrl.SELECTISVIP)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .execute(new StringDialogCallback(getActivity()) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    IsVipBean isVipBean = new Gson().fromJson(s, IsVipBean.class);

                                    if (isVipBean.isVIP) {
                                        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                                        intent.putExtra("tid", tuozhanxl.get(position).videoid);
                                        intent.putExtra("postion", position + "");
                                        startActivity(intent);
                                    } else {
                                        new AlertDialog.Builder(getActivity())
                                                .setMessage("观看更多拓展训练，需要开通如初会员才可以哦~")
                                                .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(getActivity(), OpenVipActivity.class));
                                                    }
                                                })
                                                .setNegativeButton("放弃观看", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            });
                }
            }
        });

        mTrainBtnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoList.clear();
                if (isVideoMore) {
                    for (int i = 0; i < 3; i++) {
                        mVideoList.add(tuozhanxl.get(i));
                    }
                    isVideoMore = false;
                    mTrainBtnOther.setText("更多精彩训练");
                } else {
                    mVideoList.addAll(tuozhanxl);
                    isVideoMore = true;
                    mTrainBtnOther.setText("收起视频");

                }
                trainVedioAdapter.notifyDataSetChanged();

            }
        });
    }

    @SuppressWarnings("deprecation")
    private void init() {
        if (mTrainMainBean2.uIsstest != null && !mTrainMainBean2.uIsstest.equals("1")) {
            mTrainTest.setVisibility(View.VISIBLE);
            mTrainStart.setVisibility(View.GONE);
            mTrainTestText.setVisibility(View.VISIBLE);
            mTrainTestText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), TestTrainActivity.class));
                }
            });
            mTrainCircleDay.setText("");
            mTrainCircleDay.setHint("");
            mTrainCircleDay.setUnit("");
            mTrainCircleWeek.setDate(date);
            mTrainCircleWeek.setDateNum(2);
            mTrainCircleWeek.setNowDate(0);
            mTrainCircleWeek.setYellow(false);
            mTrainCircleDay.setMaxValue(5);
            mTrainCircleDay.setValue(0);
            mTrainTvNowTime.setText(Html.fromHtml("今日已训练  <font ><big>" + "0" + "</big></font>  " + "分钟"));
            mTrainTvCountTime.setText(Html.fromHtml("累计训练  <font ><big>" + "0" + "</big></font>  " + flagTime));

            mTrainTvTarget.setText("系统匹配的锻炼方案可以更快的提升盆底肌力,\n先完成健康测试再进行锻炼哦！");
        } else {
            mTrainTestText.setVisibility(View.GONE);
            mTrainCircleWeek.setYellow(true);
            mTrainTest.setVisibility(View.GONE);
            mTrainStart.setVisibility(View.VISIBLE);

            mTrainTvNowTime.setText(Html.fromHtml("今日已训练  <font ><big>" + nowTime + "</big></font>  " + "分钟"));
            mTrainTvCountTime.setText(Html.fromHtml("累计训练  <font ><big>" + countTime + "</big></font>  " + flagTime));
            if (StringUtils.isEmpty(mTrainMainBean2.statime)) {
                mTrainTvTarget.setText(Html.fromHtml("目标周期为开始训练第一天到第七天" +
                        "<br />" +
                        "<font color='#fbe12f'>查看详情 ></font>"));
            } else {
                mTrainTvTarget.setText(Html.fromHtml("目标周期 : " + mTrainMainBean2.statime + " 到 " + mTrainMainBean2.endtime +
                        "<br />" +
                        "<font color='#fbe12f'>查看详情 ></font>"));
            }
            mTrainCircleWeek.setDate(date);
            mTrainCircleWeek.setDateNum(dateNum);
            mTrainCircleWeek.setNowDate(nowDateWeek);
            mTrainCircleDay.setMaxValue(dateNum);
            mTrainCircleDay.setValue(nowDateDay);
            mTrainCircleDay.setText(nowDateDay + "/" + dateNum);
            mTrainCircleDay.setHint("今日训练目标");
            mTrainCircleDay.setUnit("( 次 )");


            if (date == nowDateWeek / dateNum) {
                Drawable drawable = UIUtils.getDrawable(R.drawable.icon_star_end2);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mTrainTvPrize.setCompoundDrawables(null, drawable, null, null);
//                new AlertDialog.Builder(getActivity())
//                        .setMessage("经过一周的锻炼盆底肌力有了一定的提升，这份奖励送给你，相信自律的你运气不会太差!")
//                        .setPositiveButton("领取奖励", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .setNegativeButton("放弃机会", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
                if (mTrainMainBean2.windowflag.equals("0")) {
                    showFinish();
                }

            } else {
                Drawable drawable = UIUtils.getDrawable(R.drawable.icon_star_end1);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                mTrainTvPrize.setCompoundDrawables(null, drawable, null, null);
            }

            mTrainTvInfo.setText(mTrainMainBean2.uLevel + ":" + mTrainMainBean2.levelname + "  第"
                    + mTrainMainBean2.excisedays + "天  " + mTrainMainBean2.uzidifficulty + "级");
        }


        if (mTrainMainBean2.uIsfirst != null && !mTrainMainBean2.uIsfirst.equals("1") && mTrainMainBean2.uIsstest.equals("1")) {
            mTrainImgNew.setVisibility(View.VISIBLE);
        } else {
            mTrainImgNew.setVisibility(View.GONE);
        }


    }

    private void showFinish() {
        if (isShow) {
            return;
        }
        View inflate = View.inflate(getActivity(), R.layout.view_train_finish, null);
        final AlertDialog mShow = new AlertDialog.Builder(getActivity(), R.style.dialog_invitation)
                .setView(inflate)
                .show();
        isShow = true;
        inflate.findViewById(R.id.train_finish_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShow.dismiss();
            }
        });
        inflate.findViewById(R.id.train_finish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), HtmlActivity.class);
                intent1.putExtra("URL", mTrainMainBean2.getgifturl);
                startActivity(intent1);
                mShow.dismiss();
            }
        });
        mShow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                OkGo
                        .post(AppUrl.CLOSEWINDOW)
                        .params("tid", mTrainMainBean2.tid)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                            }
                        });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.train_tv_new, R.id.train_tv_prize, R.id.train_tv_target, R.id.train_btn_start,
            R.id.header_iv_right, R.id.train_tv_change, R.id.train_img_new, R.id.train_test, R.id.id_btn_retry})
    public void onViewClicked(View view) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        switch (view.getId()) {
            case R.id.train_img_new:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(getActivity(), NewTrainActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity(), mTrainView, "share").toBundle());
                } else {
                    startActivity(new Intent(getActivity(), NewTrainActivity.class));
                }
                break;
            case R.id.id_btn_retry:
                initData();
                break;
            case R.id.train_tv_new:
            case R.id.train_test:
                startActivity(new Intent(getActivity(), NewTrainActivity.class));
                break;
            case R.id.train_tv_change:
                if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                }
                startActivity(new Intent(getActivity(), MyTrainActivity.class));
                break;
            case R.id.header_iv_right:
                if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MySMSActivity.class));
                break;
            case R.id.train_tv_target:
                startActivity(new Intent(getActivity(), LookRewardDetailsActivity.class));
                break;
            case R.id.train_btn_start:
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
                                        final String[] array;
                                        if (mTrainMainBean2.levelid == 5) {
                                            array = new String[]{"循环当前难度训练"};

                                        } else {
                                            array = new String[]{"循环当前难度训练", "进入下一难度训练"};
                                        }
                                        final int[] selectedIndex = {0};
                                        new AlertDialog.Builder(getContext())
                                                .setTitle("您已完成" + mTrainMainBean2.uLevel + "全部天数的训练,接下来你可以选择进行:")
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
                                                                    .params("level", ((mTrainMainBean2.levelid + 1) + ""))
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
            case R.id.train_tv_prize:
//                if (!StringUtils.isEmpty(mTrainMainBean2.getgifturl)) {
//                    Intent intent1 = new Intent(getActivity(), HtmlActivity.class);
//                    intent1.putExtra("URL", mTrainMainBean2.getgifturl);
//                    startActivity(intent1);
//                }
//                showFinish();
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

    //
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

    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Mess(String s) {
        if (s.equals("TrainFragment")) {
            initData();
        }
    }

    //
//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMess(SmsEvent sms) {
        if (sms.sms) {
            mHeaderIvRight.setImageResource(R.drawable.icon_sms);
        } else {
            mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
        }
    }
}
