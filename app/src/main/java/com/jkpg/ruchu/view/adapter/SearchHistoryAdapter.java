package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.SearchHistoryBean;

import java.util.List;

/**
 * Created by qindi on 2017/6/16.
 */

public class SearchHistoryAdapter extends BaseQuickAdapter<SearchHistoryBean, BaseViewHolder> {
    public SearchHistoryAdapter(@LayoutRes int layoutResId, @Nullable List<SearchHistoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchHistoryBean item) {
        helper.setText(R.id.search_history_tv, item.history);
        helper.addOnClickListener(R.id.search_history_iv);
    }
}
