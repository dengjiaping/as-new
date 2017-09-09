package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FansBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by qindi on 2017/5/25.
 */

public class MyFansRVAdapter extends BaseQuickAdapter<FansBean.list, BaseViewHolder> {
    private Map<Integer, String> map;

    public MyFansRVAdapter(@LayoutRes int layoutResId, @Nullable List<FansBean.list> data, Map<Integer, String> map) {
        super(layoutResId, data);
        this.map = map;
    }

    @Override
    protected void convert(BaseViewHolder helper, FansBean.list item) {
        helper.setText(R.id.item_fans_tv_name, item.nike);
        helper.setText(R.id.item_fans_tv_body, item.taici + "    " + item.chanhoutime);
        int position = helper.getLayoutPosition();
        if (map.get(position).equals("0")) {
            helper.setText(R.id.item_fans_cb_follow, "加关注");
            helper.setChecked(R.id.item_fans_cb_follow, true);
        } else {
            helper.setText(R.id.item_fans_cb_follow, "已关注");
            helper.setChecked(R.id.item_fans_cb_follow, false);
        }
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.imgurl)
                .crossFade()
                .into((ImageView) helper.getView(R.id.item_fans_civ_photo));
        helper.addOnClickListener(R.id.item_fans_cb_follow);

    }

}
