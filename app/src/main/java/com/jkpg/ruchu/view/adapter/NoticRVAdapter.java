package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateDetailBean;

import java.util.List;

/**
 * Created by qindi on 2017/6/6.
 */

public class NoticRVAdapter extends BaseQuickAdapter<PlateDetailBean.NoticeBean, BaseViewHolder> {
    public NoticRVAdapter(@LayoutRes int layoutResId, @Nullable List<PlateDetailBean.NoticeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateDetailBean.NoticeBean item) {
//        helper
//                .setText(R.id.notice_iv_rule_title, item.title + " , " + item.notice);
//                .setText(R.id.notice_iv_rule_body, item.notice);
        TextView view = helper.getView(R.id.notice_iv_rule_title);
        view.setText(Html.fromHtml("<font color='#ff5070'>" + item.title + "</font> <font color='#767676'> " + item.notice + "</font>"));
    }
}
