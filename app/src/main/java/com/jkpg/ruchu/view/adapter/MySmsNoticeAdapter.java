package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @SuppressWarnings("deprecation")
    protected void convert(BaseViewHolder helper, MySmsNoticeBean.ListBean item) {
        if (item.type.equals("1")) {
            TextView textView = helper.getView(R.id.sms_notice_title);
            textView.setText("");
            textView.append(Html.fromHtml("<font color='#F87C86'>" + item.nick + "</font>"));
            textView.append(item.title);
            helper.setText(R.id.sms_notice_time, item.createtime);
            helper.setText(R.id.sms_notice_content, item.content);
            helper.addOnClickListener(R.id.sms_notice_ll);
            LinearLayout linearLayout = helper.getView(R.id.sms_notice_ll);
            linearLayout.setEnabled(true);
        } else {
            LinearLayout linearLayout = helper.getView(R.id.sms_notice_ll);
            linearLayout.setEnabled(false);
            helper.setText(R.id.sms_notice_time, item.createtime);
            helper.setText(R.id.sms_notice_title, item.title + "");
            helper.setText(R.id.sms_notice_content, item.content);
        }
    }
}
