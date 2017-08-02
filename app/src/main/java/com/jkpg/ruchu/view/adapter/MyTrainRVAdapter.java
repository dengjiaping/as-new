package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.bean.MyTrainBean;
import com.jkpg.ruchu.R;

import java.util.List;

/**
 * Created by qindi on 2017/7/13.
 */

public class MyTrainRVAdapter extends BaseQuickAdapter<MyTrainBean.ListBean, BaseViewHolder> {

    public MyTrainRVAdapter(@LayoutRes int layoutResId, @Nullable List<MyTrainBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyTrainBean.ListBean item) {
        helper.setBackgroundRes(R.id.item_my_train, item.backgroundRes);
        helper.setText(R.id.item_my_train_difficulty, "训练强度"+item.level);
        helper.setText(R.id.item_my_train_difficulty_num, "训练难度 "+item.strength);
        helper.setText(R.id.item_my_train_time, item.totaltime);
        helper.setVisible(R.id.item_my_train_now, item.isSelect);
    }
}
