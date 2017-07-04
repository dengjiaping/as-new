package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.FineNoteActivity;
import com.jkpg.ruchu.view.activity.community.FineNoteDetailActivity;
import com.jkpg.ruchu.view.activity.community.HotNoteActivity;
import com.jkpg.ruchu.view.activity.community.MyCollectEditActivity;
import com.jkpg.ruchu.view.activity.community.PlateDetailActivity;
import com.jkpg.ruchu.view.adapter.CommunityPlateRLAdapter;
import com.jkpg.ruchu.view.adapter.FansRLAdapter;
import com.jkpg.ruchu.widget.GridDividerItemDecoration;
import com.jkpg.ruchu.widget.banner.BannerCommunity;
import com.jkpg.ruchu.widget.banner.BannerConfig;
import com.jkpg.ruchu.widget.banner.listener.OnBannerListener;
import com.jkpg.ruchu.widget.banner.loader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

    private List<PlateBean> mPlates;

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
        initBanner();
        initPlateRecyclerView();
        initHotRecyclerView();
    }

    private void initData() {
        mPlates = new ArrayList<>();
        mPlates.add(new PlateBean("", "尴尬体位", "这是我的难言之隐", "213贴"));
        mPlates.add(new PlateBean("", "小确幸", "请给我正能量  ", "213贴"));
        mPlates.add(new PlateBean("", "爱爱糗事", "说出关灯后的故事", "213贴"));
        mPlates.add(new PlateBean("", "我的进步", "记录点滴改变  ", "213贴"));
        mPlates.add(new PlateBean("", "灰色心情", "抑郁，我要直视你", "213贴"));
        mPlates.add(new PlateBean("", "康复百宝箱", "人人都是康复师", "213贴"));
    }


    private void initPlateRecyclerView() {
        mCommunityRlPlate.setLayoutManager(new GridLayoutManager(UIUtils.getContext(), 2));
        CommunityPlateRLAdapter communityPlateRLAdapter = new CommunityPlateRLAdapter(mPlates);
        mCommunityRlPlate.setAdapter(communityPlateRLAdapter);
        mCommunityRlPlate.addItemDecoration(new GridDividerItemDecoration(1, Color.parseColor("#22000000")));
        communityPlateRLAdapter.setOnItemClickListener(new CommunityPlateRLAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, PlateBean data) {
                Intent intent = new Intent(getActivity(), PlateDetailActivity.class);
                intent.putExtra("title", data.title);
                startActivity(intent);

            }
        });
    }

    private void initHotRecyclerView() {
        mCommunityRlHot.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mCommunityRlHot.setAdapter(new FansRLAdapter());
    }

    private void initBanner() {
        ArrayList<String> images = new ArrayList<>();
        images.add("https://imgsa.baidu.com/baike/s%3D500/sign=2e583e4075d98d1072d40c31113eb807/574e9258d109b3de9c074cd4c5bf6c81810a4cd5.jpg");
        images.add("https://imgsa.baidu.com/baike/c0%3Dbaike150%2C5%2C5%2C150%2C50/sign=fa7b7a6872ec54e755e1124cd851f035/574e9258d109b3de47c6133ec5bf6c81800a4c6f.jpg");
        images.add("https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=85bc175fbf003af359b7d4325443ad39/00e93901213fb80ec88396813fd12f2eb9389412.jpg");
        ArrayList<String> titles = new ArrayList<>();
        titles.add("김지원");
        titles.add("Kim Ji Won");
        titles.add("金智媛");
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
                startActivity(new Intent(getActivity(), FineNoteDetailActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.community_ll_fine, R.id.community_ll_collect, R.id.community_tv_hot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.community_ll_fine:
                startActivity(new Intent(getActivity(), FineNoteActivity.class));
                break;
            case R.id.community_ll_collect:
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
}
