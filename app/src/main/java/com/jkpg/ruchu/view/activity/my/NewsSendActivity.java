package com.jkpg.ruchu.view.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

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
        mNewsSendSwitchComment.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchComment", false));
        mNewsSendSwitchZan.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchZan", false));
        mNewsSendSwitchSms.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchSms", false));
        mNewsSendSwitchTime1.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchTime1", false));
        mNewsSendSwitchTime2.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchTime2", false));
        mNewsSendSwitchTime3.setChecked(SPUtils.getBoolean(UIUtils.getContext(), "SwitchTime3", false));
        mNewsSendTvTime1.setText(SPUtils.getString(UIUtils.getContext(),"mNewsSendTvTime1","09:00"));
        mNewsSendTvTime2.setText(SPUtils.getString(UIUtils.getContext(),"mNewsSendTvTime2","14:00"));
        mNewsSendTvTime3.setText(SPUtils.getString(UIUtils.getContext(),"mNewsSendTvTime3","19:00"));
        mNewsSendSwitchComment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchComment", isChecked);
            }
        });
        mNewsSendSwitchZan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchZan", isChecked);

            }
        });
        mNewsSendSwitchSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchSms", isChecked);
            }
        });
        mNewsSendSwitchTime1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchTime1", isChecked);

            }
        });
        mNewsSendSwitchTime2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchTime2", isChecked);

            }
        });
        mNewsSendSwitchTime3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.saveBoolean(UIUtils.getContext(), "SwitchTime3", isChecked);

            }
        });
    }

    @OnClick({R.id.news_send_iv_time1, R.id.news_send_iv_time2, R.id.news_send_iv_time3, R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_send_iv_time1:
                showTimePicker(mNewsSendTvTime1);
                break;
            case R.id.news_send_iv_time2:
                showTimePicker(mNewsSendTvTime2);
                break;
            case R.id.news_send_iv_time3:
                showTimePicker(mNewsSendTvTime3);
                break;
            case R.id.header_iv_left:
                finish();
                break;
        }
    }

    private void showTimePicker(final TextView view) {
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
                SPUtils.saveString(UIUtils.getContext(), view + "", hour + ":" + minute);
            }
        });
        picker.show();
    }
}
