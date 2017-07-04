package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.view.activity.train.MyTrainActivity;
import com.jkpg.ruchu.view.activity.train.NewTrainActivity;
import com.jkpg.ruchu.view.activity.train.OtherTrainActivity;
import com.jkpg.ruchu.view.activity.train.TestTrainActivity;
import com.jkpg.ruchu.widget.banner.Banner;
import com.jkpg.ruchu.widget.banner.BannerConfig;
import com.jkpg.ruchu.widget.banner.Transformer;
import com.jkpg.ruchu.widget.banner.loader.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qindi on 2017/5/16.
 */

public class TrainFragment extends Fragment {
    @BindView(R.id.train_banner)
    Banner mTrainBanner;
    Unbinder unbinder;
    List<Integer> images;
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
        initHeader();
        initBanner();
        initTestBtn();
    }

    //初始化训练按钮状态 + 新手提示
    private void initTestBtn() {
        // TODO: 2017/5/18
        mTrainBtTest.setBackgroundResource(R.drawable.menu_2);
        mTrainBtTest.setText("开始训练");
        mTrainTvTest.setText("点击按钮开始我的训练");
    }

    private void initHeader() {
        mHeaderIvLeft.setVisibility(View.GONE);
        mHeaderTvTitle.setText("盆底肌训练");
        mHeaderIvRight.setImageResource(R.drawable.icon_sms_red);
    }

    private void initBanner() {
        images = new ArrayList<>();
        images.add(R.drawable.banner);
        images.add(R.drawable.banner);
        images.add(R.drawable.banner);
        titles = new ArrayList<>();
        titles.add("入门训练---认识盆底肌");
        titles.add("入门训练---认识盆底肌");
        titles.add("入门训练---认识盆底肌");
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
        //设置指示器位置（当banner模式中有指示器时）
//        mTrainBanner.setIndicatorGravity(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        mTrainBanner.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.train_ll_new, R.id.train_ll_my, R.id.train_ll_other, R.id.train_tv_tip, R.id.train_bt_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.train_ll_new:
                startActivity(new Intent(getActivity(), NewTrainActivity.class));
                break;
            case R.id.train_ll_my:

                startActivity(new Intent(getActivity(), MyTrainActivity.class));
                break;
            case R.id.train_ll_other:
                startActivity(new Intent(getActivity(), OtherTrainActivity.class));
                break;
            case R.id.train_tv_tip:
                mTrainTvTip.setVisibility(View.GONE);
                break;
            case R.id.train_bt_test:
                //判断 与 训练入口
                startActivity(new Intent(getActivity(), TestTrainActivity.class));
//                startActivity(new Intent(getActivity(), StartTrainActivity.class));
//
                break;
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
}
