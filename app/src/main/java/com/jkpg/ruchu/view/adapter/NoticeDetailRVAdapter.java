package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.NoticeBean;

import java.util.List;

/**
 * Created by qindi on 2017/6/6.
 */

public class NoticeDetailRVAdapter extends BaseQuickAdapter<NoticeBean.ListBean, BaseViewHolder> {
    public NoticeDetailRVAdapter(@LayoutRes int layoutResId, @Nullable List<NoticeBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean.ListBean item) {
        helper
                .setText(R.id.notice_iv_rule_title, "" + item.title + " ")
                .setText(R.id.notice_iv_rule_body, item.notice);
    }
}
