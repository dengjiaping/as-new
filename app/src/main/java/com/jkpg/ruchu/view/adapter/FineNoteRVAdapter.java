package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FineNoteBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/6/5.
 */

public class FineNoteRVAdapter extends BaseQuickAdapter<FineNoteBean.ListBean, BaseViewHolder> {
    public FineNoteRVAdapter(@LayoutRes int layoutResId, @Nullable List<FineNoteBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FineNoteBean.ListBean item) {
        helper
                .setText(R.id.item_fine_note_tv_title, item.title)
                .setText(R.id.item_fine_note_tv_time, item.createtime)
                .setText(R.id.item_fine_note_tv_eulogy, item.zan)
                .setText(R.id.item_fine_note_tv_reply, item.reply)
                .setText(R.id.item_fine_note_iv_body, item.simplecontent);
        if (StringUtils.isEmpty(item.simplecontent)) {
           helper.setVisible(R.id.item_fine_note_iv_body, false);
        } else {
            helper.setVisible(R.id.item_fine_note_iv_body, true);
        }

        Glide.with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.images)
                .crossFade().centerCrop()
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .into((ImageView) helper.getView(R.id.item_fine_note_tv_image));
    }
}
