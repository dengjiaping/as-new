package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FineNoteBean;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/6/5.
 */

public class MyNoteRVAdapter extends BaseQuickAdapter<FineNoteBean, BaseViewHolder> {
    public MyNoteRVAdapter(@LayoutRes int layoutResId, @Nullable List<FineNoteBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FineNoteBean item) {
        helper
                .setText(R.id.item_tv_post_title, item.title)
                .setText(R.id.item_tv_post_time, item.time)
                .setText(R.id.item_tv_post_zan, item.eulogy)
                .setText(R.id.item_tv_post_huifu, item.reply)
                .setText(R.id.item_tv_post_body, item.body);
        Glide.with(UIUtils.getContext()).load(item.image).crossFade().into((ImageView) helper.getView(R.id.item_feedback_photo));
    }
}
