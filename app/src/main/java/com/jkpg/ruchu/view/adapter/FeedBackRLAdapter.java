package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.VideoDetailBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/5/20.
 */

public class FeedBackRLAdapter extends BaseQuickAdapter<VideoDetailBean.VideoMSBean.DiscussBean,BaseViewHolder> {
    public FeedBackRLAdapter(@LayoutRes int layoutResId, @Nullable List<VideoDetailBean.VideoMSBean.DiscussBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoDetailBean.VideoMSBean.DiscussBean item) {
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL+item.userimageurl)
                .centerCrop()
                .crossFade()
                .into((ImageView) helper.getView(R.id.item_feedback_photo));
        helper.addOnClickListener(R.id.item_feedback_photo);
        helper.setText(R.id.item_feedback_name,item.username);
        helper.setText(R.id.item_feedback_time,item.createtime);
        helper.setText(R.id.item_feedback_body,item.discussContent);
    }
}
