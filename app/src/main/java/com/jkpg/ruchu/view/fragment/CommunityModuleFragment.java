package com.jkpg.ruchu.view.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.jkpg.ruchu.bean.CommunityMainBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SmsEvent;
import com.jkpg.ruchu.bean.SuccessBean;
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
import com.jkpg.ruchu.view.activity.community.FineNoteActivity;
import com.jkpg.ruchu.view.activity.community.FineNoteDetailWebFixActivity;
import com.jkpg.ruchu.view.activity.community.NoticeDetailFixActivity;
import com.jkpg.ruchu.view.activity.community.PlateDetailActivity;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.activity.my.MySMSActivity;
import com.jkpg.ruchu.view.adapter.CommunityPlateRLAdapter;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.GlideImageLoader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

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
 * Created by qindi on 2017/6/5.
 */

public class CommunityModuleFragment extends Fragment {
    @BindView(R.id.community_banner)
    Banner mCommunityBanner;
    @BindView(R.id.community_ll_fine)
    LinearLayout mCommunityLlFine;
    @BindView(R.id.community_rl_plate)
    RecyclerView mCommunityRlPlate;
    Unbinder unbinder;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.id_btn_retry)
    Button mIdBtnRetry;
    @BindView(R.id.errorStateRelativeLayout)
    RelativeLayout mErrorStateRelativeLayout;
    @BindView(R.id.id_loading_and_retry)
    FrameLayout mIdLoadingAndRetry;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.expert_image0)
    CircleImageView mExpertImage0;
    @BindView(R.id.expert_name0)
    TextView mExpertName0;
    @BindView(R.id.expert_cb0)
    CheckBox mExpertCb0;
    @BindView(R.id.expert_image1)
    CircleImageView mExpertImage1;
    @BindView(R.id.expert_name1)
    TextView mExpertName1;
    @BindView(R.id.expert_cb1)
    CheckBox mExpertCb1;
    @BindView(R.id.expert_image2)
    CircleImageView mExpertImage2;
    @BindView(R.id.expert_name2)
    TextView mExpertName2;
    @BindView(R.id.expert_cb2)
    CheckBox mExpertCb2;
    @BindView(R.id.expert_image3)
    CircleImageView mExpertImage3;
    @BindView(R.id.expert_name3)
    TextView mExpertName3;
    @BindView(R.id.expert_cb3)
    CheckBox mExpertCb3;
    @BindView(R.id.ll_expert0)
    LinearLayout mLlExpert0;
    @BindView(R.id.ll_expert1)
    LinearLayout mLlExpert1;
    @BindView(R.id.ll_expert2)
    LinearLayout mLlExpert2;
    @BindView(R.id.ll_expert3)
    LinearLayout mLlExpert3;
    @BindView(R.id.tv_expert_change)
    TextView mTvExpertChange;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    private CommunityMainBean mCommunityMainBean;
    private List<CommunityMainBean.List1Bean> mList1 = new ArrayList<>();
    private List<CommunityMainBean.List2Bean> mList2 = new ArrayList<>();
    private CommunityPlateRLAdapter mCommunityPlateRLAdapter;
    private List<CommunityMainBean.DarenBean> mDaren;
    private ArrayList<View> mViewLL;
    private ArrayList<View> mViewImage;
    private ArrayList<View> mViewName;
    private ArrayList<View> mViewCB;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        mHeaderTvTitle.setText("互动");
        mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
        mHeaderIvLeft.setVisibility(View.GONE);
        mViewLL = new ArrayList<>();
        mViewLL.add(mLlExpert0);
        mViewLL.add(mLlExpert1);
        mViewLL.add(mLlExpert2);
        mViewLL.add(mLlExpert3);

        mViewImage = new ArrayList<>();
        mViewImage.add(mExpertImage0);
        mViewImage.add(mExpertImage1);
        mViewImage.add(mExpertImage2);
        mViewImage.add(mExpertImage3);
        mViewName = new ArrayList<>();
        mViewName.add(mExpertName0);
        mViewName.add(mExpertName1);
        mViewName.add(mExpertName2);
        mViewName.add(mExpertName3);
        mViewCB = new ArrayList<>();
        mViewCB.add(mExpertCb0);
        mViewCB.add(mExpertCb1);
        mViewCB.add(mExpertCb2);
        mViewCB.add(mExpertCb3);

        initRefreshLayout();


        mTvExpertChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    return;
                }
                OkGo
                        .post(AppUrl.DIANJIHUANYIH)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringDialogCallback(getActivity()) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                CommunityMainBean communityMainBean = new Gson().fromJson(s, CommunityMainBean.class);
                                mDaren.clear();
                                mDaren.addAll(communityMainBean.daren);
                                initExpert();
                            }
                        });
            }
        });


    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkGo
                        .post(AppUrl.BBS_INFOS)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                CommunityMainBean communityMainBean = new Gson().fromJson(s, CommunityMainBean.class);
                                mList2.clear();
                                mList2.addAll(communityMainBean.list2);
                                initBanner(mList1);
                                mDaren.clear();
                                mDaren.addAll(communityMainBean.daren);
                                initExpert();
                                mCommunityPlateRLAdapter.notifyDataSetChanged();
                                mRefreshLayout.setRefreshing(false);

                            }
                        });
            }
        });
    }

    private void initData() {
        OkGo
                .post(AppUrl.BBS_INFOS)
                .cacheKey("BBS_INFOS" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mCommunityMainBean = new Gson().fromJson(s, CommunityMainBean.class);
                        mList1 = mCommunityMainBean.list1;
                        mList2 = mCommunityMainBean.list2;
                        mDaren = mCommunityMainBean.daren;
                        initBanner(mList1);
                        initPlateRecyclerView(mList2);
                        initExpert();

                        mIdLoadingAndRetry.setVisibility(View.GONE);
                        mErrorStateRelativeLayout.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        mCommunityMainBean = new Gson().fromJson(s, CommunityMainBean.class);
                        List<CommunityMainBean.List1Bean> list1 = mCommunityMainBean.list1;
                        List<CommunityMainBean.List2Bean> list2 = mCommunityMainBean.list2;
                        mDaren = mCommunityMainBean.daren;
                        initBanner(list1);
                        initExpert();
                        initPlateRecyclerView(list2);

                        mIdLoadingAndRetry.setVisibility(View.GONE);
                        mErrorStateRelativeLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
//                        mLoadingAndRetryManager.showRetry();
                        mIdLoadingAndRetry.setVisibility(View.GONE);
                        mErrorStateRelativeLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mIdLoadingAndRetry.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initExpert() {
        for (int i = 0; i < 4; i++) {
            mViewLL.get(i).setVisibility(View.INVISIBLE);
        }


        for (int i = 0; i < mDaren.size(); i++) {
            if (i > 4) {
                return;
            }
            mViewLL.get(i).setVisibility(View.VISIBLE);
            Glide.with(UIUtils.getContext())
                    .load(AppUrl.BASEURL + mDaren.get(i).drimgurl)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(R.drawable.gray_bg)
                    .error(R.drawable.gray_bg)
                    .into((ImageView) mViewImage.get(i));
            ((TextView) mViewName.get(i)).setText(mDaren.get(i).drnick);
            final int finalI = i;
            mViewLL.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toUserMain(mDaren.get(finalI).druserid);
                }
            });
            if (mDaren.get(i).drisgz.equals("0")) {
                ((CheckBox) mViewCB.get(i)).setChecked(true);
                ((CheckBox) mViewCB.get(i)).setText("关注");
            } else {
                ((CheckBox) mViewCB.get(i)).setChecked(false);
                ((CheckBox) mViewCB.get(i)).setText("已关注");
            }

            final int finalI1 = i;
            final int finalI2 = i;
            (mViewCB.get(i)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final CheckBox checkBox = (CheckBox) mViewCB.get(finalI2);
                    if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals(mDaren.get(finalI2).druserid)) {
                        ToastUtils.showShort(UIUtils.getContext(), "自己不能关注自己哦");
                        checkBox.setChecked(false);
                        return;
                    }

                    if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                        checkBox.setChecked(false);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        return;
                    }

                    if (checkBox.isChecked()) {
                        OkGo
                                .post(AppUrl.CANCLEATT)
                                .params("fansid", mDaren.get(finalI1).druserid)
                                .params("myuserid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                .execute(new StringDialogCallback(getActivity()) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                        if (!successBean.success) {
                                            ToastUtils.showShort(UIUtils.getContext(), "取消关注失败");
                                            checkBox.setText("已关注");
                                        } else {
                                            checkBox.setText("关注");
                                            EventBus.getDefault().post(new MessageEvent("MyFragment"));
                                        }
                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        ToastUtils.showShort(UIUtils.getContext(), "取消关注失败");
                                        checkBox.setText("已关注");
                                        checkBox.setChecked(true);
                                    }
                                });
                    } else {
                        OkGo
                                .post(AppUrl.ATTENTION)
                                .params("followUserid", mDaren.get(finalI1).druserid)
                                .params("MyUserid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                .execute(new StringDialogCallback(getActivity()) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                        if (!successBean.success) {
                                            ToastUtils.showShort(UIUtils.getContext(), "关注失败,请重试");
                                            checkBox.setText("关注");
                                        } else {
                                            checkBox.setText("已关注");
                                            EventBus.getDefault().post(new MessageEvent("MyFragment"));
                                        }
                                    }

                                    @Override
                                    public void onError(Call call, Response response, Exception e) {
                                        super.onError(call, response, e);
                                        ToastUtils.showShort(UIUtils.getContext(), "关注失败,请重试");
                                        checkBox.setText("关注");
                                        checkBox.setChecked(false);
                                    }
                                });
                    }
                }
            });
        }

    }

    private void toUserMain(String userid) {
        if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        Intent intent = new Intent(getActivity(), FansCenterActivity.class);
        intent.putExtra("fansId", userid);
        startActivity(intent);
    }


    private void initPlateRecyclerView(final List<CommunityMainBean.List2Bean> list2) {
        final ArrayList<String> plateNameList = new ArrayList<>();
        for (int i = 0; i < list2.size(); i++) {
            plateNameList.add(list2.get(i).platename);
        }
        final ArrayList<String> plateIdList = new ArrayList<>();
        for (int i = 0; i < list2.size(); i++) {
            plateIdList.add(list2.get(i).tid + "");
        }
        mCommunityRlPlate.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mCommunityPlateRLAdapter = new CommunityPlateRLAdapter(R.layout.item_plate, list2);
        mCommunityRlPlate.setAdapter(mCommunityPlateRLAdapter);
        mCommunityPlateRLAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(getActivity(), PlateDetailActivity.class);
                intent.putExtra("plateid", list2.get(position).tid + "");
                intent.putExtra("title", list2.get(position).platename);
                intent.putStringArrayListExtra("plate", plateNameList);
                intent.putStringArrayListExtra("plateId", plateIdList);
                startActivity(intent);
            }
        });
    }


    private void initBanner(final List<CommunityMainBean.List1Bean> list1) {
        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (CommunityMainBean.List1Bean list1Bean : list1) {
            images.add(list1Bean.image);
            titles.add(list1Bean.title);
        }
        mCommunityBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //设置图片加载器
        mCommunityBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mCommunityBanner.setImages(images);
        //设置banner动画效果
        mCommunityBanner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        mCommunityBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mCommunityBanner.isAutoPlay(true);
        //设置轮播时间
        mCommunityBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
//        mCommunityBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mCommunityBanner.start();
        mCommunityBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                1文章,2帖子,3Html页面,4App内页面,5商城商品
//                if (!list1.get(position).type.equals("8")) {
//                    int tid = list1.get(position).bbsid;
//                    Intent intent = new Intent(getActivity(), FineNoteDetailWebFixActivity.class);
//                    intent.putExtra("art_id", tid + "");
//                    startActivity(intent);
//                } else {
//                    int tid = list1.get(position).bbsid;
//                    Intent intent = new Intent(getActivity(), WebActivity.class);
//                    intent.putExtra("art_id", tid + "");
//                    startActivity(intent);
//                }
                switch (list1.get(position).type) {
                    case "1":
                        Intent intent1 = new Intent(getActivity(), FineNoteDetailWebFixActivity.class);
                        intent1.putExtra("art_id", list1.get(position).bbsid);
                        startActivity(intent1);
                        break;
                    case "2":
                        Intent intent2 = new Intent(getActivity(), NoticeDetailFixActivity.class);
                        intent2.putExtra("bbsid", list1.get(position).bbsid);
                        startActivity(intent2);
                        break;
                    case "3":
                        Intent intent3 = new Intent(getActivity(), HtmlActivity.class);
                        intent3.putExtra("URL", list1.get(position).htmlurl);
                        startActivity(intent3);
                        break;
                    case "4":
                        Intent intent4 = new Intent();
                        intent4.setComponent(new ComponentName(getActivity(), list1.get(position).json.android.key));
                        startActivity(intent4);
                        break;
                    case "5":
                        Intent intent5 = new Intent(getActivity(), ShopActivity.class);
                        intent5.putExtra("url", list1.get(position).htmlurl);
                        startActivity(intent5);
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.id_btn_retry, R.id.community_ll_fine, R.id.header_iv_right})
    public void onViewClicked(View view) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        switch (view.getId()) {
            case R.id.id_btn_retry:
                initData();
                break;
            case R.id.community_ll_fine:
                startActivity(new Intent(getActivity(), FineNoteActivity.class));
                break;
            case R.id.header_iv_right:
                if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MySMSActivity.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCommunityBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCommunityBanner.stopAutoPlay();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(String mess) {
        if (mess.equals("Community")) {
            OkGo
                    .post(AppUrl.BBS_INFOS)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            CommunityMainBean communityMainBean = new Gson().fromJson(s, CommunityMainBean.class);
                            mList2.clear();
                            mList2.addAll(communityMainBean.list2);
                            mCommunityPlateRLAdapter.notifyDataSetChanged();
                            initExpert();
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.message.equals("Login") || event.message.equals("Quit")) {
            MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    initData();
                    OkGo
                            .post(AppUrl.BBS_INFOS)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    CommunityMainBean communityMainBean = new Gson().fromJson(s, CommunityMainBean.class);
                                    mList2.clear();
                                    mList2.addAll(communityMainBean.list2);
                                    mCommunityPlateRLAdapter.notifyDataSetChanged();
                                    mDaren.clear();
                                    mDaren.addAll(communityMainBean.daren);
                                    initExpert();
                                }
                            });
                }
            }, 500);
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
