package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

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
    @SuppressWarnings("deprecation")
    protected void convert(BaseViewHolder helper, NoticeBean.ListBean item) {
//        helper
//                .setText(R.id.notice_iv_rule_title, "" + item.title + " ")
//                .setText(R.id.notice_iv_rule_body, item.notice);
        TextView view = helper.getView(R.id.notice_iv_rule_title);
//        view.setText(Html.fromHtml("<font color='#ff5070'>" + item.title + "</font>  " + item.notice));
        view.setText(Html.fromHtml("<font color='#F87C86'>" + item.title + "</font> <font color='#767676'> " + item.notice + "</font>"));

    }

}
