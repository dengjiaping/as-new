package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MySmsNoticeBean;

import java.util.List;

/**
 * Created by qindi on 2017/8/24.
 */

public class MySmsNoticeAdapter extends BaseQuickAdapter<MySmsNoticeBean.ListBean, BaseViewHolder> {
    public MySmsNoticeAdapter(@LayoutRes int layoutResId, @Nullable List<MySmsNoticeBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySmsNoticeBean.ListBean item) {
        helper.setText(R.id.sms_notice_time, item.createtime);
        helper.setText(R.id.sms_notice_title, item.title + "");
        helper.setText(R.id.sms_notice_content, item.content);
    }
}
