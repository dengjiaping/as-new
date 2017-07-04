package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateBean;

import java.util.List;

/**
 * Created by qindi on 2017/6/6.
 */

public class PlateNameRVAdapter extends BaseQuickAdapter<PlateBean, BaseViewHolder> {
    public PlateNameRVAdapter(@LayoutRes int layoutResId, @Nullable List<PlateBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateBean item) {
        helper
                .setText(R.id.item_plate_tv_name, item.title);
    }
}
