package com.jkpg.ruchu.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by qindi on 2017/6/12.
 */

public class HotQuestionVPAdapter extends FragmentPagerAdapter {
    private List<Fragment> mViews;
    private List<String> mTitles;
    private FragmentManager fm;

    public HotQuestionVPAdapter(FragmentManager fm, List<String> titles, List<Fragment> views) {
        super(fm);
        this.mViews = views;
        this.mTitles = titles;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mViews.get(position);
    }
}
