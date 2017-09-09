package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MyTestBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.train.TestTrainActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/24.
 */

public class MyTestActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_test_tv_grade)
    TextView mMyTestTvGrade;
    @BindView(R.id.my_test_tv_level)
    TextView mMyTestTvLevel;
    @BindView(R.id.my_test_tv_time)
    TextView mMyTestTvTime;
    @BindView(R.id.my_test_tv_body)
    TextView mMyTestTvBody;
    @BindView(R.id.my_test_sl)
    ScrollView mMyTestSl;
    @BindView(R.id.my_test_btn_test)
    Button mMyTestBtnTest;
    @BindView(R.id.my_test_ll)
    LinearLayout mMyTestLl;
    @BindView(R.id.my_test_ll_btn_again_test)
    LinearLayout mMyTestLlBtnAgainTest;
    @BindView(R.id.my_test_header_no)
    LinearLayout mMyTestHeaderNo;
    @BindView(R.id.my_test_header)
    LinearLayout mMyTestHeader;
    @BindView(R.id.tip)
    TextView mTip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        ButterKnife.bind(this);
        initHeader();
        initData();
        mTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTip.setVisibility(View.GONE);
            }
        });
    }

    private void initData() {
        OkGo
                .post(AppUrl.MYTEST)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .cacheKey("MYTEST")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyTestBean myTestBean = new Gson().fromJson(s, MyTestBean.class);
                        if (myTestBean.istest.equals("1")) {
                            mMyTestHeader.setVisibility(View.VISIBLE);
                            mMyTestHeaderNo.setVisibility(View.GONE);
                            mMyTestSl.setVisibility(View.VISIBLE);
                            mMyTestLl.setVisibility(View.GONE);
                            mMyTestLlBtnAgainTest.setVisibility(View.VISIBLE);


                            mMyTestTvBody.setText(myTestBean.report.content);
                            mMyTestTvTime.setText(myTestBean.report.createtime);
                            mMyTestTvGrade.setText(myTestBean.report.count + "分");
                            mMyTestTvLevel.setText("产后康复 【" + myTestBean.report.level + "】");

                        }

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        MyTestBean myTestBean = new Gson().fromJson(s, MyTestBean.class);
                        if (myTestBean.istest.equals("1")) {
                            mMyTestHeader.setVisibility(View.VISIBLE);
                            mMyTestHeaderNo.setVisibility(View.GONE);
                            mMyTestSl.setVisibility(View.VISIBLE);
                            mMyTestLl.setVisibility(View.GONE);
                            mMyTestLlBtnAgainTest.setVisibility(View.VISIBLE);
                            mMyTestTvBody.setText(myTestBean.report.content);
                            mMyTestTvTime.setText(myTestBean.report.createtime);
                            mMyTestTvGrade.setText(myTestBean.report.count + "分");
                            mMyTestTvLevel.setText("产后康复 【" + myTestBean.report.level + "】");

                        }
                    }
                });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的测试");
    }

    @OnClick({R.id.header_iv_left, R.id.my_test_btn_test, R.id.my_test_btn_again_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.my_test_btn_test:
                startActivity(new Intent(MyTestActivity.this, TestTrainActivity.class));
                finish();
                break;
            case R.id.my_test_btn_again_test:
                startActivity(new Intent(MyTestActivity.this, TestTrainActivity.class));
                finish();
                break;
        }
    }
}
