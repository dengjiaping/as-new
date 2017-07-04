package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.bean.HotQuestionBean;

import java.util.List;

/**
 * Created by qindi on 2017/6/20.
 */

public class HotQuestionAdapter extends BaseQuickAdapter<HotQuestionBean, BaseViewHolder> {
    public HotQuestionAdapter(@LayoutRes int layoutResId, @Nullable List<HotQuestionBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotQuestionBean item) {

    }
}
