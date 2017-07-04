package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateDetailBean;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/6/6.
 */

public class PlateDetailRVAdapter extends BaseQuickAdapter<PlateDetailBean, BaseViewHolder> {
    public PlateDetailRVAdapter(@LayoutRes int layoutResId, @Nullable List<PlateDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateDetailBean item) {
        helper
                .setText(R.id.plate_detail_tv_name, item.name)
                .setVisible(R.id.plate_detail_iv_fine, item.fine)
                .setText(R.id.plate_detail_tv_time, item.time)
                .setText(R.id.plate_detail_tv_eulogy, item.eulogy)
                .setText(R.id.plate_detail_tv_reply, item.reply)
                .setText(R.id.plate_detail_tv_title, item.title)
                .setText(R.id.plate_detail_tv_body, item.body);
        Glide.with(UIUtils.getContext()).load(item.image).crossFade().into((ImageView) helper.getView(R.id.plate_detail_civ_photo));

    }
}
