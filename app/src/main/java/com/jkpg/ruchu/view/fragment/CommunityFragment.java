package com.jkpg.ruchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jkpg.ruchu.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import cn.bingoogolapple.badgeview.BGABadgeRadioButton;
import cn.bingoogolapple.badgeview.BGABadgeViewHelper;

/**
 * Created by qindi on 2017/6/5.
 */

public class CommunityFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.community_main_tv_sq)
    RadioButton mCommunityMainTvSq;
    @BindView(R.id.community_main_tv_zx)
    BGABadgeRadioButton mCommunityMainTvZx;
    @BindView(R.id.community_main_view_pager)
    ViewPager mCommunityMainViewPager;
    @BindView(R.id.community_main_radio_group)
    RadioGroup mCommunityMainRadioGroup;
    @BindView(R.id.community_main_ll)
    BGABadgeLinearLayout mCommunityMainLl;

    private List<Fragment> mFragments;
    private CommunityModuleFragment mCommunityModuleFragment;
    private ConsultModuleFragment mConsultModuleFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeader();
        initFragment();
        initViewPager();
        initBadgeView();
    }

    private void initBadgeView() {
        mCommunityMainLl.getBadgeViewHelper().setBadgeBorderWidthDp(1);
        mCommunityMainLl.getBadgeViewHelper().showCirclePointBadge();
        mCommunityMainLl.getBadgeViewHelper().setBadgeVerticalMarginDp(0);
        mCommunityMainLl.getBadgeViewHelper().setBadgeHorizontalMarginDp(0);
        mCommunityMainLl.getBadgeViewHelper().setBadgeGravity(BGABadgeViewHelper.BadgeGravity.RightTop);
    }

    private void initFragment() {
        mCommunityModuleFragment = new CommunityModuleFragment();
        mConsultModuleFragment = new ConsultModuleFragment();
        mFragments = new ArrayList<>();
        mFragments.add(mCommunityModuleFragment);
        mFragments.add(mConsultModuleFragment);

    }

    private void initViewPager() {
        mCommunityMainViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        mCommunityMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mCommunityMainTvSq.setChecked(true);
                } else {
                    mCommunityMainTvZx.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initHeader() {
        /*FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if (mCommunityMainTvSq.isChecked()) {
            ft.replace(R.id.community_main_frame_layout, new CommunityModuleFragment()).commit();
        } else {
            ft.replace(R.id.community_main_frame_layout, new ConsultModuleFragment()).commit();
        }*/
        mCommunityMainRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.community_main_tv_sq:
                        mCommunityMainViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.community_main_tv_zx:
                        mCommunityMainViewPager.setCurrentItem(1, false);
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.community_main_tv_sq, R.id.community_main_tv_zx})
    public void onViewClicked(View view) {
      /*  FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.community_main_tv_sq:
                ft.replace(R.id.community_main_frame_layout, new CommunityModuleFragment()).commit();
                break;
            case R.id.community_main_tv_zx:
                ft.replace(R.id.community_main_frame_layout, new ConsultModuleFragment()).commit();
                break;
        }*/
    }
}
