package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.CommunityMianBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/5/27.
 */

public class HotPlateRLAdapter extends BaseQuickAdapter<CommunityMianBean.List3Bean, BaseViewHolder> {
    public HotPlateRLAdapter(@LayoutRes int layoutResId, @Nullable List<CommunityMianBean.List3Bean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityMianBean.List3Bean item) {
        helper.setText(R.id.item_tv_post_title, item.title);
        helper.setText(R.id.item_tv_post_body, item.nick);
        helper.setText(R.id.item_tv_post_time, item.createtime);
        helper.setText(R.id.item_tv_post_zan, item.zan + "");
        helper.setText(R.id.item_tv_post_huifu, item.reply + "");

        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.headimg)
                .centerCrop()
                .crossFade()
                .into((ImageView) helper.getView(R.id.item_feedback_photo));

    }
}
