package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.bean.RecommendDoctorBean;

import java.util.List;

/**
 * Created by qindi on 2017/6/20.
 */

public class RecommendDoctorAdapter extends BaseQuickAdapter<RecommendDoctorBean,BaseViewHolder> {
    public RecommendDoctorAdapter(@LayoutRes int layoutResId, @Nullable List<RecommendDoctorBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendDoctorBean item) {

    }
}
