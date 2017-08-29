package com.jkpg.ruchu.view.adapter;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MySmsPrivsteBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;

import java.util.List;

/**
 * Created by qindi on 2017/8/23.
 */

public class MySmsPrivsteRvAdapter extends BaseMultiItemQuickAdapter<MySmsPrivsteBean.ListBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MySmsPrivsteRvAdapter(List<MySmsPrivsteBean.ListBean> data) {
        super(data);
        addItemType(MySmsPrivsteBean.ListBean.FROM_USER_MSG, R.layout.item_chat_you);
        addItemType(MySmsPrivsteBean.ListBean.TO_USER_MSG, R.layout.item_chat_my);

    }

    @Override
    protected void convert(BaseViewHolder helper, MySmsPrivsteBean.ListBean item) {
        switch (helper.getItemViewType()) {
            case MySmsPrivsteBean.ListBean.FROM_USER_MSG:
                Glide
                        .with(UIUtils.getContext())
                        .load(AppUrl.BASEURL + item.managerImg)
                        .crossFade()
                        .centerCrop()
                        .into((CircleImageView) helper.getView(R.id.chat_image_you));
                helper.setText(R.id.chat_sms_you, item.content);
                helper.setText(R.id.chat_time_you, item.createtime);
                break;
            case MySmsPrivsteBean.ListBean.TO_USER_MSG:
                Glide
                        .with(UIUtils.getContext())
                        .load(AppUrl.BASEURL + item.userImg)
                        .crossFade()
                        .centerCrop()
                        .into((CircleImageView) helper.getView(R.id.chat_image_my));
                helper.setText(R.id.chat_sms_my, item.content);
                helper.setText(R.id.chat_time_my, item.createtime);
                break;
        }
    }
}
