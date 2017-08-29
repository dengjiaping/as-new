package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MySmsLoveBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/8/12.
 */

public class MySmsLoveAdapter extends BaseQuickAdapter<MySmsLoveBean.BackMessBean, BaseViewHolder> {

    public MySmsLoveAdapter(@LayoutRes int layoutResId, @Nullable List<MySmsLoveBean.BackMessBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySmsLoveBean.BackMessBean item) {
        helper.addOnClickListener(R.id.sms_image);
        if (item.flag.equals("1")) {
            helper.setVisible(R.id.sms_ll_reply, false);
            Glide
                    .with(UIUtils.getContext())
                    .load(AppUrl.BASEURL + item.reimgurl)
                    .centerCrop()
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.sms_image));
            helper.setText(R.id.sms_name, item.renick);
            helper.setText(R.id.sms_time, item.retime);
            helper.setText(R.id.sms_content, item.text);
            helper.setText(R.id.sms_name, item.renick);

        } else {
            helper.setVisible(R.id.sms_ll_reply, true);
            Glide
                    .with(UIUtils.getContext())
                    .load(AppUrl.BASEURL + item.imgurl3)
                    .centerCrop()
                    .crossFade()
                    .into((ImageView) helper.getView(R.id.sms_image));
            helper.setText(R.id.sms_name, item.nick3);
            helper.setText(R.id.sms_time, item.retime);
            helper.setText(R.id.sms_content, item.text);
            helper.setText(R.id.sms_reply, item.replycontent);
        }

        helper.setText(R.id.sms_content2, item.fttitle);
        helper.setText(R.id.sms_time2, item.ftnick + "  " + item.fttime);


    }
}