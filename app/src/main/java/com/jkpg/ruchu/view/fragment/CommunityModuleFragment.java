package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.CommunityMianBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.WebActivity;
import com.jkpg.ruchu.view.activity.community.FineNoteActivity;
import com.jkpg.ruchu.view.activity.community.FineNoteDetailWebFixActivity;
import com.jkpg.ruchu.view.activity.community.HotNoteActivity;
import com.jkpg.ruchu.view.activity.community.MyCollectEditActivity;
import com.jkpg.ruchu.view.activity.community.NoticeDetailFixActivity;
import com.jkpg.ruchu.view.activity.community.PlateDetailActivity;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.adapter.CommunityPlateRLAdapter;
import com.jkpg.ruchu.view.adapter.HotPlateRLAdapter;
import com.jkpg.ruchu.widget.GridDividerItemDecoration;
import com.jkpg.ruchu.widget.banner.BannerCommunity;
import com.jkpg.ruchu.widget.banner.BannerConfig;
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
 * Created by qindi on 2017/6/5.
 */

public class CommunityModuleFragment extends Fragment {
    @BindView(R.id.community_banner)
    BannerCommunity mCommunityBanner;
    @BindView(R.id.community_ll_fine)
    LinearLayout mCommunityLlFine;
    @BindView(R.id.community_ll_collect)
    LinearLayout mCommunityLlCollect;
    @BindView(R.id.community_rl_plate)
    RecyclerView mCommunityRlPlate;
    @BindView(R.id.community_tv_hot)
    TextView mCommunityTvHot;
    @BindView(R.id.community_rl_hot)
    RecyclerView mCommunityRlHot;
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
    private CommunityMianBean mCommunityMianBean;
    private List<CommunityMianBean.List1Bean> mList1 = new ArrayList<>();
    private List<CommunityMianBean.List2Bean> mList2 = new ArrayList<>();
    private List<CommunityMianBean.List3Bean> mList3 = new ArrayList<>();
    private CommunityPlateRLAdapter mCommunityPlateRLAdapter;
    private HotPlateRLAdapter mHotPlateRLAdapter;


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
        mHeaderIvLeft.setVisibility(View.GONE);

        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkGo
                        .post(AppUrl.BBS_INFOS)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                CommunityMianBean communityMianBean = new Gson().fromJson(s, CommunityMianBean.class);
                                mList2.clear();
                                mList3.clear();
                                mList2.addAll(communityMianBean.list2);
                                mList3.addAll(communityMianBean.list3);
                                initBanner(mList1);
                                mCommunityPlateRLAdapter.notifyDataSetChanged();
                                mHotPlateRLAdapter.notifyDataSetChanged();
                                mRefreshLayout.setRefreshing(false);
                            }
                        });
            }
        });
    }

    private void initData() {
        OkGo
                .post(AppUrl.BBS_INFOS)
                .cacheKey("BBS_INFOS")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
//                        LogUtils.i(s);
                        mCommunityMianBean = new Gson().fromJson(s, CommunityMianBean.class);
                        mList1 = mCommunityMianBean.list1;
                        mList2 = mCommunityMianBean.list2;
                        mList3 = mCommunityMianBean.list3;
                        initBanner(mList1);
                        initPlateRecyclerView(mList2);
                        initHotRecyclerView(mList3);

                        mIdLoadingAndRetry.setVisibility(View.GONE);
                        mErrorStateRelativeLayout.setVisibility(View.GONE);

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        mCommunityMianBean = new Gson().fromJson(s, CommunityMianBean.class);
                        List<CommunityMianBean.List1Bean> list1 = mCommunityMianBean.list1;
                        List<CommunityMianBean.List2Bean> list2 = mCommunityMianBean.list2;
                        List<CommunityMianBean.List3Bean> list3 = mCommunityMianBean.list3;
                        initBanner(list1);
                        initPlateRecyclerView(list2);
                        initHotRecyclerView(list3);

                        mIdLoadingAndRetry.setVisibility(View.GONE);
                        mErrorStateRelativeLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
//                        mLoadingAndRetryManager.showRetry();
                        mIdLoadingAndRetry.setVisibility(View.GONE);
                        mErrorStateRelativeLayout.setVisibility(View.VISIBLE);

                        LogUtils.i("onError");
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mIdLoadingAndRetry.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void initPlateRecyclerView(final List<CommunityMianBean.List2Bean> list2) {
        final ArrayList<String> plateNameList = new ArrayList<>();
        for (int i = 0; i < list2.size(); i++) {
            plateNameList.add(list2.get(i).platename);
        }
        final ArrayList<String> plateIdList = new ArrayList<>();
        for (int i = 0; i < list2.size(); i++) {
            plateIdList.add(list2.get(i).tid + "");
        }
        mCommunityRlPlate.setLayoutManager(new GridLayoutManager(UIUtils.getContext(), 1));
//        mCommunityRlPlate.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mCommunityPlateRLAdapter = new CommunityPlateRLAdapter(list2);
        mCommunityRlPlate.setAdapter(mCommunityPlateRLAdapter);
        mCommunityRlPlate.addItemDecoration(new GridDividerItemDecoration(1, Color.parseColor("#22000000")));
        mCommunityPlateRLAdapter.setOnItemClickListener(new CommunityPlateRLAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CommunityMianBean.List2Bean data) {
                Intent intent = new Intent(getActivity(), PlateDetailActivity.class);
                intent.putExtra("plateid", data.tid + "");
                intent.putExtra("title", data.platename);
                intent.putStringArrayListExtra("plate", plateNameList);
                intent.putStringArrayListExtra("plateId", plateIdList);
                startActivity(intent);

            }
        });
    }

    private void initHotRecyclerView(final List<CommunityMianBean.List3Bean> list3) {
        mCommunityRlHot.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mHotPlateRLAdapter = new HotPlateRLAdapter(R.layout.item_fans_post, list3);
        mCommunityRlHot.setAdapter(mHotPlateRLAdapter);
        mHotPlateRLAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int tid = list3.get(position).tid;
                Intent intent = new Intent(getActivity(), NoticeDetailFixActivity.class);
                intent.putExtra("bbsid", tid + "");
                startActivity(intent);
            }
        });

    }

    private void initBanner(final List<CommunityMianBean.List1Bean> list1) {
        ArrayList<String> images = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for (CommunityMianBean.List1Bean list1Bean : list1) {
            images.add(AppUrl.BASEURL + list1Bean.images);
            titles.add(list1Bean.title);
        }
        //设置图片加载器
        mCommunityBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mCommunityBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mCommunityBanner.setImages(images);
        //设置banner动画效果
//        mCommunityBanner.setBannerAnimation(Transformer.CubeOut);
        //设置标题集合（当banner样式有显示title时）
        mCommunityBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mCommunityBanner.isAutoPlay(true);
        //设置轮播时间
        mCommunityBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
//        mTrainBanner.setIndicatorGravity(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mCommunityBanner.start();
        mCommunityBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (!list1.get(position).type.equals("8")) {
                    int tid = list1.get(position).tid;
                    Intent intent = new Intent(getActivity(), FineNoteDetailWebFixActivity.class);
                    intent.putExtra("art_id", tid + "");
                    startActivity(intent);
                } else {
                    int tid = list1.get(position).tid;
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("art_id", tid + "");
                    startActivity(intent);
                }
//                ToastUtils.showShort(UIUtils.getContext(), tid + "");
                //startActivity(new Intent(getActivity(), FineNoteDetailActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.id_btn_retry, R.id.community_ll_fine, R.id.community_ll_collect, R.id.community_tv_hot})
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
            case R.id.community_ll_collect:
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), MyCollectEditActivity.class));
                break;
            case R.id.community_tv_hot:
                startActivity(new Intent(getActivity(), HotNoteActivity.class));

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
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            CommunityMianBean communityMianBean = new Gson().fromJson(s, CommunityMianBean.class);
                            mList2.clear();
                            mList3.clear();
                            mList2.addAll(communityMianBean.list2);
                            mList3.addAll(communityMianBean.list3);
                            mCommunityPlateRLAdapter.notifyDataSetChanged();
                            mHotPlateRLAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }
}
