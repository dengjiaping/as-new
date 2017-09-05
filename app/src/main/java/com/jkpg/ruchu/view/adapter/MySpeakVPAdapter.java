package com.jkpg.ruchu.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by qindi on 2017/5/27.
 */

public class MySpeakVPAdapter extends FragmentPagerAdapter {
    private List<Fragment> views;
    private List<String> titles;

    public MySpeakVPAdapter(FragmentManager fm, List<Fragment> views, List<String> titles) {
        super(fm);
        this.views = views;
        this.titles = titles;
    }


    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return views.get(position);
    }
}
