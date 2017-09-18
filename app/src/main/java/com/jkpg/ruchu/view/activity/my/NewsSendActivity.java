package com.jkpg.ruchu.view.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.SendBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/25.
 */

public class NewsSendActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.news_send_switch_comment)
    Switch mNewsSendSwitchComment;
    @BindView(R.id.news_send_switch_zan)
    Switch mNewsSendSwitchZan;
    @BindView(R.id.news_send_switch_sms)
    Switch mNewsSendSwitchSms;
    @BindView(R.id.news_send_tv_time1)
    TextView mNewsSendTvTime1;
    @BindView(R.id.news_send_iv_time1)
    ImageView mNewsSendIvTime1;
    @BindView(R.id.news_send_switch_time1)
    Switch mNewsSendSwitchTime1;
    @BindView(R.id.news_send_tv_time2)
    TextView mNewsSendTvTime2;
    @BindView(R.id.news_send_iv_time2)
    ImageView mNewsSendIvTime2;
    @BindView(R.id.news_send_switch_time2)
    Switch mNewsSendSwitchTime2;
    @BindView(R.id.news_send_tv_time3)
    TextView mNewsSendTvTime3;
    @BindView(R.id.news_send_iv_time3)
    ImageView mNewsSendIvTime3;
    @BindView(R.id.news_send_switch_time3)
    Switch mNewsSendSwitchTime3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_send);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("消息推送");
        OkGo
                .post(AppUrl.APPSEND)
                .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .cacheKey("APPSEND")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringDialogCallback(NewsSendActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        SendBean sendBean = new Gson().fromJson(s, SendBean.class);
                        if (sendBean.appsend_pl.equals("1")) {
                            mNewsSendSwitchComment.setChecked(true);
                        } else {
                            mNewsSendSwitchComment.setChecked(false);

                        }


                        if (sendBean.appsend_zan.equals("1")) {
                            mNewsSendSwitchZan.setChecked(true);
                        } else {
                            mNewsSendSwitchZan.setChecked(false);

                        }

                        if (sendBean.appsend_tz.equals("1")) {
                            mNewsSendSwitchSms.setChecked(true);
                        } else {
                            mNewsSendSwitchSms.setChecked(false);

                        }

                        if (sendBean.appsend_zao.equals("1")) {
                            mNewsSendSwitchTime1.setChecked(true);
                        } else {
                            mNewsSendSwitchTime1.setChecked(false);

                        }
                        if (sendBean.appsend_zhong.equals("1")) {
                            mNewsSendSwitchTime2.setChecked(true);
                        } else {
                            mNewsSendSwitchTime2.setChecked(false);

                        }
                        if (sendBean.appsend_wan.equals("1")) {
                            mNewsSendSwitchTime3.setChecked(true);
                        } else {
                            mNewsSendSwitchTime3.setChecked(false);

                        }

                        mNewsSendTvTime1.setText(sendBean.appsend_zaotime);
                        mNewsSendTvTime2.setText(sendBean.appsend_wutime);
                        mNewsSendTvTime3.setText(sendBean.appsend_wantime);
                    }
                });
//        mNewsSendSwitchComment.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchComment", true));
//        mNewsSendSwitchZan.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchZan", true));
//        mNewsSendSwitchSms.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchSms", true));
//        mNewsSendSwitchTime1.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchTime1", false));
//        mNewsSendSwitchTime2.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchTime2", false));
//        mNewsSendSwitchTime3.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchTime3", false));
//        mNewsSendTvTime1.setText();
//        mNewsSendTvTime2.setText(SPUtils.getString(UIUtils.getContext(), 2 + "", "14:00"));
//        mNewsSendTvTime3.setText(SPUtils.getString(UIUtils.getContext(), 3 + "", "19:00"));
        mNewsSendSwitchComment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchComment", isChecked);
                OkGo
                        .post(AppUrl.APPSENDSEND)
                        .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("appsend_pl", isChecked ? "1" : "0")
                        .execute(new StringDialogCallback(NewsSendActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                            }
                        });
            }
        });
        mNewsSendSwitchZan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchZan", isChecked);
                OkGo
                        .post(AppUrl.APPSENDSEND)
                        .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("appsend_zan", isChecked ? "1" : "0")
                        .execute(new StringDialogCallback(NewsSendActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                            }
                        });

            }
        });
        mNewsSendSwitchSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchSms", isChecked);
                OkGo
                        .post(AppUrl.APPSENDSEND)
                        .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("appsend_tz", isChecked ? "1" : "0")
                        .execute(new StringDialogCallback(NewsSendActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                            }
                        });
            }
        });
        mNewsSendSwitchTime1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchTime1", isChecked);
                OkGo
                        .post(AppUrl.APPSENDSEND)
                        .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("appsend_zao", isChecked ? "1" : "0")
                        .params("appsend_zaotime", mNewsSendTvTime1.getText().toString())
                        .execute(new StringDialogCallback(NewsSendActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                LogUtils.i(s+"-------");
                            }
                        });
                LogUtils.i(mNewsSendTvTime1.getText().toString() + "mNewsSendTvTime1.getText().toString()");


            }
        });
        mNewsSendSwitchTime2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchTime2", isChecked);
                OkGo
                        .post(AppUrl.APPSENDSEND)
                        .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("appsend_zhong", isChecked ? "1" : "0")
                        .params("appsend_wutime", mNewsSendTvTime2.getText().toString())
                        .execute(new StringDialogCallback(NewsSendActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                            }
                        });

            }
        });
        mNewsSendSwitchTime3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchTime3", isChecked);
                OkGo
                        .post(AppUrl.APPSENDSEND)
                        .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("appsend_wan", isChecked ? "1" : "0")
                        .params("appsend_wantime", mNewsSendTvTime3.getText().toString())
                        .execute(new StringDialogCallback(NewsSendActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                            }
                        });

            }
        });
    }

    @OnClick({R.id.news_send_iv_time1, R.id.news_send_iv_time2, R.id.news_send_iv_time3, R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_send_iv_time1:
                showTimePicker(mNewsSendTvTime1, 1);
                break;
            case R.id.news_send_iv_time2:
                showTimePicker(mNewsSendTvTime2, 2);
                break;
            case R.id.news_send_iv_time3:
                showTimePicker(mNewsSendTvTime3, 3);
                break;
            case R.id.header_iv_left:
                finish();
                break;
        }
    }

    private void showTimePicker(final TextView view, final int a) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
//        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        int currentHour = Integer.parseInt((view.getText().subSequence(0, 2).toString()));
        int currentMinute = Integer.parseInt((view.getText().subSequence(3, 4).toString()));
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTextColor(getResources().getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                view.setText(hour + ":" + minute);
//                SPUtils.saveString(UIUtils.getContext(), a + "", hour + ":" + minute);
                switch (a) {
                    case 1:
                        if (mNewsSendSwitchTime1.isChecked()) {
                            OkGo
                                    .post(AppUrl.APPSENDSEND)
                                    .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                    .params("appsend_zao", "1")
                                    .params("appsend_zaotime", mNewsSendTvTime1.getText().toString())
                                    .execute(new StringDialogCallback(NewsSendActivity.this) {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {

                                        }
                                    });

                        }
                        LogUtils.i(mNewsSendTvTime1.getText().toString() + "mNewsSendTvTime1.getText()");
                        break;
                    case 2:
                        if (mNewsSendSwitchTime2.isChecked()) {
                            OkGo
                                    .post(AppUrl.APPSENDSEND)
                                    .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                    .params("appsend_zhong", "1")
                                    .params("appsend_wutime", mNewsSendTvTime2.getText().toString())
                                    .execute(new StringDialogCallback(NewsSendActivity.this) {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {

                                        }
                                    });
                        }
                        break;
                    case 3:
                        if (mNewsSendSwitchTime3.isChecked()) {
                            OkGo
                                    .post(AppUrl.APPSENDSEND)
                                    .params("user_id", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                    .params("appsend_wan", "1")
                                    .params("appsend_wantime", mNewsSendTvTime3.getText().toString())
                                    .execute(new StringDialogCallback(NewsSendActivity.this) {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {

                                        }
                                    });
                        }
                        break;
                }
            }
        });
        picker.show();
    }
}
