package com.jkpg.ruchu.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by qindi on 2017/5/27.
 */

public class MySpeakVPAdapter extends PagerAdapter {
    private List<View> views;
    private List<String> titles;

    public MySpeakVPAdapter(List<View> views, List<String> titles) {
        this.views = views;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }
}
