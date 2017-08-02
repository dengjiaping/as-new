package com.jkpg.ruchu.view.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/25.
 */

public class QuestionFeedbackActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.question_feedback_et)
    EditText mQuestionFeedbackEt;
    @BindView(R.id.question_feedback_btn)
    Button mQuestionFeedbackBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_feedback);
        ButterKnife.bind(this);
        initHeader();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("问题反馈");
    }

    @OnClick({R.id.header_iv_left, R.id.question_feedback_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.question_feedback_btn:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        if (StringUtils.isEmpty(mQuestionFeedbackEt.getText().toString())){
            ToastUtils.showShort(UIUtils.getContext(),"请输入你的反馈内容再提交~");
            return;
        }
        OkGo
                .post(AppUrl.FEEDBACK)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("content", mQuestionFeedbackEt.getText().toString())
                .execute(new StringDialogCallback(QuestionFeedbackActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                        if (successBean.success) {
                            new AlertDialog.Builder(QuestionFeedbackActivity.this)
                                    .setView(R.layout.view_question_feedback_success)
                                    .show();
                        } else {
                            ToastUtils.showShort(UIUtils.getContext(), "提交失败");
                        }
                    }
                });


    }
}
