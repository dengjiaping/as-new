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

public class FineNoteRVAdapter extends BaseQuickAdapter<FineNoteBean, BaseViewHolder> {
    public FineNoteRVAdapter(@LayoutRes int layoutResId, @Nullable List<FineNoteBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FineNoteBean item) {
        helper
                .setText(R.id.item_fine_note_tv_title, item.title)
                .setText(R.id.item_fine_note_tv_time, item.time)
                .setText(R.id.item_fine_note_tv_eulogy, item.eulogy)
                .setText(R.id.item_fine_note_tv_reply, item.reply)
                .setText(R.id.item_fine_note_iv_body, item.body);
        Glide.with(UIUtils.getContext()).load(item.image).crossFade().into((ImageView) helper.getView(R.id.item_fine_note_tv_image));
    }
}
