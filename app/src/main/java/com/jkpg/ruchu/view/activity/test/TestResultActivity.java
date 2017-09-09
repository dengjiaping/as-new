package com.jkpg.ruchu.view.activity.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.IsVipBean;
import com.jkpg.ruchu.bean.TestResultBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.my.OpenVipActivity;
import com.jkpg.ruchu.view.activity.train.TrainPrepareActivity;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
        mResultTvPlan.setText("产后康复  【" + mTestResultBean.level + "】");
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
                OkGo
                        .post(AppUrl.SELECTISVIP)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringDialogCallback(TestResultActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                IsVipBean isVipBean = new Gson().fromJson(s, IsVipBean.class);
                                if (isVipBean.isVIP) {
                                    if (mTestResultBean.ischange) {
                                        new AlertDialog.Builder(TestResultActivity.this)
                                                .setMessage("开始训练后，您的当前难度将被调至【" + mTestResultBean.level + "】")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        OkGo
                                                                .post(AppUrl.OPENMYPRACTICE)
                                                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                                                .params("level", ((mTestResultBean.levelid) + ""))
                                                                .execute(new StringDialogCallback(TestResultActivity.this) {
                                                                    @Override
                                                                    public void onSuccess(String s, Call call, Response response) {
                                                                        EventBus.getDefault().post("TrainFragment");
//                                                                    initData();
                                                                        startActivity(new Intent(TestResultActivity.this, TrainPrepareActivity.class));
                                                                        finish();
                                                                    }
                                                                });

                                                    }
                                                })
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(TestResultActivity.this, TrainPrepareActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        startActivity(new Intent(TestResultActivity.this, TrainPrepareActivity.class));
                                        finish();
                                    }

                                } else {
                                    new AlertDialog.Builder(TestResultActivity.this)
                                            .setMessage("只有会员才能训练哦")
                                            .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(TestResultActivity.this, OpenVipActivity.class));
                                                }
                                            })
                                            .setNegativeButton("放弃训练", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            }
                        });
                break;
        }
    }
}
