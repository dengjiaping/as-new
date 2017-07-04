package com.jkpg.ruchu.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by qindi on 2017/5/24.
 */

public class PlateDetailVPAdapter extends FragmentPagerAdapter {
    private List<Fragment> viewList;//数据源
    private List<String> viewTitle;

    public PlateDetailVPAdapter(FragmentManager fragmentManager, List<Fragment> viewList, List<String> viewTitle) {
        super(fragmentManager);
        this.viewList = viewList;
        this.viewTitle = viewTitle;
    }


    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return viewTitle.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return viewList.get(position);
    }
}
