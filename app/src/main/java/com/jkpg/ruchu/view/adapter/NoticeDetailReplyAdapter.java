package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.NoticReplyBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.jkpg.ruchu.widget.nineview.preview.NineGridViewClickAdapter;

import java.util.List;

/**
 * Created by qindi on 2017/6/15.
 */

public class NoticeDetailReplyAdapter extends BaseQuickAdapter<NoticReplyBean, BaseViewHolder> {
    public NoticeDetailReplyAdapter(@LayoutRes int layoutResId, @Nullable List<NoticReplyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticReplyBean item) {
        NineGridView view1 = helper.getView(R.id.item_notice_reply_nine_view);
        view1.setAdapter(new NineGridViewClickAdapter(UIUtils.getContext(), item.imageInfos));
        NineGridView view2 = helper.getView(R.id.item_notice_reply_to_nine_view);
        view2.setAdapter(new NineGridViewClickAdapter(UIUtils.getContext(), item.toImageInfos));
        helper.addOnClickListener(R.id.item_notice_detail_tv_reply);
    }
}
