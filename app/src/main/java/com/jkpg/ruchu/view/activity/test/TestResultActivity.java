package com.jkpg.ruchu.view.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.view.activity.train.StartTrainActivity2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/20.
 */

public class TestResultActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        ButterKnife.bind(this);
        initHeader();
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
                startActivity(new Intent(TestResultActivity.this, TestReportActivity.class));
                break;
            case R.id.result_btn_start:
               /* OkGo
                        .post(AppUrl.BEGINPRACTICE)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                LogUtils.i(s);
                                TrainPointBean trainPointBean = new Gson().fromJson(s, TrainPointBean.class);
                                Intent intent = new Intent(TestResultActivity.this, StartTrainActivity2.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("trainPointBean", trainPointBean);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();

                            }
                        });*/
               startActivity(new Intent(TestResultActivity.this, StartTrainActivity2.class));
                break;
        }
    }
}
