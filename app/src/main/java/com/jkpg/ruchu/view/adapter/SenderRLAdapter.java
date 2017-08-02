package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;

import java.util.List;

/**
 * Created by qindi on 2017/5/27.
 */

public class SenderRLAdapter extends BaseQuickAdapter<MyReplyNoteBean.MySpeakBean, BaseViewHolder> {

    public SenderRLAdapter(@LayoutRes int layoutResId, @Nullable List<MyReplyNoteBean.MySpeakBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyReplyNoteBean.MySpeakBean item) {
        helper.setText(R.id.item_tv_post_title, item.title);
        helper.setText(R.id.item_tv_post_body, item.content);
        helper.setText(R.id.item_tv_post_time, item.replytime);
        if (item.ct != null) {
            if (item.ct.equals("0")) {
                helper.setVisible(R.id.item_tv_post_unread, false);
            } else
                helper.setText(R.id.item_tv_post_unread, item.ct);
        }
    }
}
