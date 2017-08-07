package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FansCenterBean;

import java.util.List;

/**
 * Created by qindi on 2017/5/27.
 */

public class FanCenterRvAdapter extends BaseQuickAdapter<FansCenterBean.BbslistBean, BaseViewHolder> {
    public FanCenterRvAdapter(@LayoutRes int layoutResId, @Nullable List<FansCenterBean.BbslistBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FansCenterBean.BbslistBean item) {
        helper.setText(R.id.item_tv_post_title, item.title);
        helper.setText(R.id.item_tv_post_body, item.nick);
        helper.setText(R.id.item_tv_post_time, item.createtime);
        helper.setText(R.id.item_tv_post_zan, item.zan + "");
        helper.setText(R.id.item_tv_post_huifu, item.reply + "");
        helper.setVisible(R.id.item_feedback_photo,false);
        /*Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.headImg)
                .centerCrop()
                .crossFade()
                .into((ImageView) helper.getView(R.id.item_feedback_photo));*/

    }
}
