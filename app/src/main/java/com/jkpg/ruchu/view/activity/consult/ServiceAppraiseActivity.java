package com.jkpg.ruchu.view.activity.consult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.flexbox.StringTagAdapter;
import com.jkpg.ruchu.widget.flexbox.interfaces.OnFlexboxSubscribeListener;
import com.jkpg.ruchu.widget.flexbox.widget.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by qindi on 2017/6/14.
 */

public class ServiceAppraiseActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.service_appraise_civ)
    CircleImageView mServiceAppraiseCiv;
    @BindView(R.id.service_appraise_tv_name)
    TextView mServiceAppraiseTvName;
    @BindView(R.id.service_appraise_tv_job)
    TextView mServiceAppraiseTvJob;
    @BindView(R.id.service_appraise_tv_address)
    TextView mServiceAppraiseTvAddress;
    @BindView(R.id.service_appraise_tv_number)
    TextView mServiceAppraiseTvNumber;
    @BindView(R.id.service_appraise_tv_grade)
    TextView mServiceAppraiseTvGrade;
    @BindView(R.id.service_appraise_rating)
    MaterialRatingBar mServiceAppraiseRating;
    @BindView(R.id.service_appraise_flow_layout)
    TagFlowLayout mServiceAppraiseFlowLayout;
    @BindView(R.id.service_appraise_et)
    EditText mServiceAppraiseEt;
    @BindView(R.id.service_appraise_cb)
    CheckBox mServiceAppraiseCb;
    @BindView(R.id.service_appraise_tv_send)
    TextView mServiceAppraiseTvSend;

    private List<String> sourceData;
    private List<String> selectItems;
    private StringTagAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_appraise);
        ButterKnife.bind(this);
        initHeader();
        initFlowLayout();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("医生主页");
    }

    private void initFlowLayout() {
        sourceData = new ArrayList<>();
        sourceData.add("好啊");
        sourceData.add("好不好");
        sourceData.add("好");
        sourceData.add("真是太好了");
        sourceData.add("大家都说好啊啊");
        sourceData.add("大家好才是真的好");
        sourceData.add("你好吗");

        selectItems = new ArrayList<>();
        adapter = new StringTagAdapter(this, sourceData, selectItems);
        adapter.setOnSubscribeListener(new OnFlexboxSubscribeListener<String>() {
            @Override
            public void onSubscribe(List<String> selectedItem) {
                LogUtils.i(selectedItem.size() + "getSelectItems");

            }
        });
        mServiceAppraiseFlowLayout.setAdapter(adapter);

    }

    @OnClick({R.id.header_iv_left, R.id.service_appraise_tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.service_appraise_tv_send:

                break;
        }
    }
}
