package com.jkpg.ruchu.view.activity.consult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/14.
 */

public class DoctorPayActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.doctor_pay_tv_name)
    TextView mDoctorPayTvName;
    @BindView(R.id.doctor_pay_tv_price)
    TextView mDoctorPayTvPrice;
    @BindView(R.id.doctor_pay_cb_ali)
    CheckBox mDoctorPayCbAli;
    @BindView(R.id.doctor_pay_cb_wx)
    CheckBox mDoctorPayCbWx;
    @BindView(R.id.doctor_pay_tv_count)
    TextView mDoctorPayTvCount;
    @BindView(R.id.doctor_pay_tv_pay)
    TextView mDoctorPayTvPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_pay);
        ButterKnife.bind(this);
        initHeader();
        initCheckBox();
    }

    private void initCheckBox() {
        mDoctorPayCbAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mDoctorPayCbWx.setChecked(false);
                }
            }
        });
        mDoctorPayCbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mDoctorPayCbAli.setChecked(false);
                }
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("支付信息");
    }

    @OnClick({R.id.header_iv_left, R.id.doctor_pay_tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.doctor_pay_tv_pay:

                break;
        }
    }
}
