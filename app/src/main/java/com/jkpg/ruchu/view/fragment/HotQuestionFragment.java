package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.HotQuestionBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.consult.HotQuestionDetailActivity;
import com.jkpg.ruchu.view.adapter.HotQuestionAdapter;

import java.util.List;

/**
 * Created by qindi on 2017/6/13.
 */

public class HotQuestionFragment extends Fragment {

    private RecyclerView mRecyclerView;
    List<HotQuestionBean> data;


    public HotQuestionFragment(List<HotQuestionBean> data) {
        this.data = data;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(UIUtils.getContext());

        return mRecyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        HotQuestionAdapter hotQuestionAdapter = new HotQuestionAdapter(R.layout.item_hot_question, data);
        mRecyclerView.setAdapter(hotQuestionAdapter);
        hotQuestionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), HotQuestionDetailActivity.class));
            }
        });
    }

}
