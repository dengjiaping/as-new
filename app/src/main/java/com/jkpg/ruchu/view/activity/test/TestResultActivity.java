package com.jkpg.ruchu.view.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.TestResultBean;
import com.jkpg.ruchu.view.activity.train.TrainPrepareActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/20.
 */

public class TestResultActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.result_tv_grade)
    TextView mResultTvGrade;
    @BindView(R.id.result_tv_plan)
    TextView mResultTvPlan;
    @BindView(R.id.result_btn_look)
    Button mResultBtnLook;
    @BindView(R.id.result_btn_start)
    Button mResultBtnStart;
    private TestResultBean mTestResultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initData() {
        mTestResultBean = getIntent().getParcelableExtra("testResultBean");
        mResultTvGrade.setText(mTestResultBean.count + "分");
        mResultTvPlan.setText(mTestResultBean.level);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("测试结果");
    }

    @OnClick({R.id.header_iv_left, R.id.result_btn_look, R.id.result_btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.result_btn_look:
                Intent intent = new Intent(TestResultActivity.this, TestReportActivity.class);
                intent.putExtra("testResultBean", mTestResultBean);
                startActivity(intent);
                break;
            case R.id.result_btn_start:
                startActivity(new Intent(TestResultActivity.this, TrainPrepareActivity.class));
                finish();
                break;
        }
    }
}
