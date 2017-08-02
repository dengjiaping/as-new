package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateDetailBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/6/6.
 */

public class PlateDetailRVAdapter extends BaseQuickAdapter<PlateDetailBean.ListBean, BaseViewHolder> {
    public PlateDetailRVAdapter(@LayoutRes int layoutResId, @Nullable List<PlateDetailBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateDetailBean.ListBean item) {

        helper
                .setText(R.id.plate_detail_tv_name, item.nick)
                .setVisible(R.id.plate_detail_iv_fine, (item.isGood).equals("1") ? true : false)
                .setText(R.id.plate_detail_tv_time, item.createtime)
                .setText(R.id.plate_detail_tv_eulogy, item.zan)
                .setText(R.id.plate_detail_tv_reply, item.reply)
                .setText(R.id.plate_detail_tv_title, item.title)
                .setText(R.id.plate_detail_tv_body, item.content);
        Glide.with(UIUtils.getContext()).load(AppUrl.BASEURL + item.headImg).crossFade().into((ImageView) helper.getView(R.id.plate_detail_civ_photo));

    }
}
