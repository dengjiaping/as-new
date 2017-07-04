package com.jkpg.ruchu.view.activity.consult;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.DoctorAppraiseBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.DoctorAppraiseAdapter;
import com.jkpg.ruchu.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/13.
 */

public class DoctorMainActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.doctor_main_civ_image)
    CircleImageView mDoctorMainCivImage;
    @BindView(R.id.doctor_main_tv_name)
    TextView mDoctorMainTvName;
    @BindView(R.id.doctor_main_tv_job)
    TextView mDoctorMainTvJob;
    @BindView(R.id.doctor_main_tv_address)
    TextView mDoctorMainTvAddress;
    @BindView(R.id.doctor_main_tv_number)
    TextView mDoctorMainTvNumber;
    @BindView(R.id.doctor_main_tv_grade)
    TextView mDoctorMainTvGrade;
    @BindView(R.id.doctor_main_tv_body)
    TextView mDoctorMainTvBody;
    @BindView(R.id.doctor_main_tv_appraise_num)
    TextView mDoctorMainTvAppraiseNum;
    @BindView(R.id.doctor_main_flex_box)
    FlexboxLayout mDoctorMainFlexBox;
    @BindView(R.id.doctor_main_recycler_view)
    RecyclerView mDoctorMainRecyclerView;
    @BindView(R.id.doctor_main_share)
    TextView mDoctorMainShare;
    @BindView(R.id.doctor_main_collect)
    CheckBox mDoctorMainCollect;
    @BindView(R.id.doctor_main_consult)
    TextView mDoctorMainConsult;

    private List<String> labels;
    private List<DoctorAppraiseBean> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        ButterKnife.bind(this);
        initHeader();
        initData();
        initFlexboxLayout();
        initCheckBox();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mDoctorMainRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        DoctorAppraiseAdapter doctorAppraiseAdapter = new DoctorAppraiseAdapter(R.layout.item_doctor_appraise, data);
        mDoctorMainRecyclerView.setAdapter(doctorAppraiseAdapter);
    }

    private void initData() {
        labels = new ArrayList<>();
        labels.add("好");
        labels.add("好好");
        labels.add("好好好好");
        labels.add("好");
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new DoctorAppraiseBean());
        }
    }

    private void initCheckBox() {
        mDoctorMainCollect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    private void initFlexboxLayout() {
        for (int i = 0; i < labels.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());
            TextView textView = new TextView(UIUtils.getContext());
            textView.setBackgroundResource(R.drawable.shap_rectangle_gray_void_text);
            textView.setTextColor(Color.parseColor("#767676"));
            textView.setText(labels.get(i));
            linearLayout.setPadding(12, 5, 12, 5);
            linearLayout.addView(textView);
            mDoctorMainFlexBox.addView(linearLayout);
        }
    }

    private void initHeader() {
        mHeaderTvTitle.setText("医生主页");
    }

    @OnClick({R.id.header_iv_left, R.id.doctor_main_share, R.id.doctor_main_consult})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.doctor_main_share:
                break;
            case R.id.doctor_main_consult:
                startActivity(new Intent(DoctorMainActivity.this,DoctorPayActivity.class));
                break;
        }
    }
}
