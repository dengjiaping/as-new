package com.jkpg.ruchu.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.VipManageBean;

import java.util.List;

/**
 * Created by qindi on 2017/5/24.
 */

public class VipManageVPAdapter extends PagerAdapter {
    private List<View> viewList;//数据源
    private List<String> viewTitle;
    private List<VipManageBean.ListBean> list;

    public VipManageVPAdapter(List<View> viewList, List<String> viewTitle, List<VipManageBean.ListBean> list) {

        this.viewList = viewList;
        this.viewTitle = viewTitle;
        this.list = list;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        View view = viewList.get(position);
        TextView title = (TextView) view.findViewById(R.id.view_train_vip_title);
        title.setText(list.get(position).title);
        TextView body = (TextView) view.findViewById(R.id.view_train_vip_body);
        body.setText(list.get(position).content);
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return viewTitle.get(position);
    }
}
