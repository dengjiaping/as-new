package com.jkpg.ruchu.view.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FineNoteBodyBean;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/6/17.
 */

public class FineNoteBodyAdapter extends BaseMultiItemQuickAdapter<FineNoteBodyBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FineNoteBodyAdapter(List<FineNoteBodyBean> data) {
        super(data);
        addItemType(FineNoteBodyBean.TITLE, R.layout.item_detail_title);
        addItemType(FineNoteBodyBean.BODY, R.layout.item_detail_body);
        addItemType(FineNoteBodyBean.IMG, R.layout.item_detail_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, FineNoteBodyBean item) {
        switch (helper.getItemViewType()) {
            case FineNoteBodyBean.TITLE:
                helper.setText(R.id.item_note_detail_title, item.content);
                break;
            case FineNoteBodyBean.IMG:
                Glide.with(UIUtils.getContext()).load(item.content).crossFade().into((ImageView) helper.getView(R.id.item_note_detail_image));
                break;
            case FineNoteBodyBean.BODY:
                helper.setText(R.id.item_note_detail_body, item.content);
                break;
        }
    }
}
