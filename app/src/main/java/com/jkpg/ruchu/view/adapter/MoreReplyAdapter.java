package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MoreReplyBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/8/8.
 */

public class MoreReplyAdapter extends BaseQuickAdapter<MoreReplyBean.ItemsBean, BaseViewHolder> {
    public MoreReplyAdapter(@LayoutRes int layoutResId, @Nullable List<MoreReplyBean.ItemsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MoreReplyBean.ItemsBean item) {
        helper.setText(R.id.item_tv_name, item.nick);
        helper.addOnClickListener(R.id.item_tv_name);
        helper.setText(R.id.item_tv_time, item.replytime);
        if (item.parentid.equals(item.ftid + "")) {
            helper.setVisible(R.id.item_ll_other, false);
            helper.setVisible(R.id.item_tv_content, true);

            helper.setText(R.id.item_tv_content, item.content);
            helper.addOnClickListener(R.id.item_tv_content);
        } else {
            helper.setVisible(R.id.item_tv_content, false);
            helper.setVisible(R.id.item_ll_other, true);

            helper.setText(R.id.item_tv_name2, item.nick2 + ":");
            helper.setText(R.id.item_tv_content2, item.content);
            helper.addOnClickListener(R.id.item_tv_content2);
            helper.addOnClickListener(R.id.item_tv_name2);
        }
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.headImg)
                .centerCrop()
                .crossFade()
                .into((ImageView) helper.getView(R.id.item_image));
        helper.addOnClickListener(R.id.item_image);

    }

}
