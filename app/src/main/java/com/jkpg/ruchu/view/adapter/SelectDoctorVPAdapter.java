package com.jkpg.ruchu.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by qindi on 2017/6/12.
 */

public class SelectDoctorVPAdapter extends PagerAdapter {
    private List<View> mViews;
    private List<String> mTitles;

    public SelectDoctorVPAdapter(List<String> titles, List<View> views) {
        this.mViews = views;
        this.mTitles = titles;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
