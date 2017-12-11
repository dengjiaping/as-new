package com.jkpg.ruchu.view.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.CommunityMianBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/5/18.
 */

public class CommunityPlateRLAdapter extends BaseQuickAdapter<CommunityMianBean.List2Bean, BaseViewHolder> {
    public CommunityPlateRLAdapter(int layoutResId, @Nullable List<CommunityMianBean.List2Bean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityMianBean.List2Bean item) {
        helper.setText(R.id.plate_tv_title, item.platename);
        helper.setText(R.id.plate_tv_body, item.simpleremark);
        helper.setText(R.id.plate_tv_num, item.zongshu + "贴");
        Glide.with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.plateimg)
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .into((ImageView) helper.getView(R.id.plate_civ_photo));
    }

}
