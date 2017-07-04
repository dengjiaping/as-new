package com.jkpg.ruchu.view.activity.consult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.HotQuestionBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.HotQuestionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/6/13.
 */

public class HotQuestionDetailActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.question_detail_question)
    TextView mQuestionDetailQuestion;
    @BindView(R.id.question_detail_answer)
    TextView mQuestionDetailAnswer;
    @BindView(R.id.question_detail_time)
    TextView mQuestionDetailTime;
    @BindView(R.id.question_detail_cb_zan)
    CheckBox mQuestionDetailCbZan;
    @BindView(R.id.question_detail_recycler_view)
    RecyclerView mQuestionDetailRecyclerView;


    private List<HotQuestionBean> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_question_detail);
        ButterKnife.bind(this);
        initData();
        initHeader();
        initCheckBox();
        initRecyclerView();
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            data.add(new HotQuestionBean());
        }
    }

    private void initRecyclerView() {
        mQuestionDetailRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        HotQuestionAdapter hotQuestionAdapter = new HotQuestionAdapter(R.layout.item_hot_question, data);
        mQuestionDetailRecyclerView.setAdapter(hotQuestionAdapter);
        hotQuestionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(HotQuestionDetailActivity.this, HotQuestionDetailActivity.class));
                finish();
            }
        });

    }

    private void initCheckBox() {
        mQuestionDetailCbZan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mQuestionDetailCbZan.setText((Integer.parseInt(mQuestionDetailCbZan.getText() + "") + 1) + "");
                } else {
                    mQuestionDetailCbZan.setText((Integer.parseInt(mQuestionDetailCbZan.getText() + "") - 1) + "");
                }
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("详情");
    }
}
