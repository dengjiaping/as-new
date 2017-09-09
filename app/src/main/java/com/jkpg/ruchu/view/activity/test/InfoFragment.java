package com.jkpg.ruchu.view.activity.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.TrainQuestionNextBean;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by qindi on 2017/7/3.
 */

public class InfoFragment extends NormalFragment {
    @BindView(R.id.view_test_height_text)
    TextView mViewTestHeightText;
    @BindView(R.id.view_test_height)
    LinearLayout mViewTestHeight;
    @BindView(R.id.view_test_weight_text)
    TextView mViewTestWeightText;
    @BindView(R.id.view_test_weight)
    LinearLayout mViewTestWeight;
    Unbinder unbinder;
    private String mWeight = "";
    private String mHeight = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_test_info, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.view_test_height, R.id.view_test_weight})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_test_height:
                NumberPicker heightPicker = new NumberPicker(getActivity());
                heightPicker.setWidth(heightPicker.getScreenWidthPixels());
                heightPicker.setDividerVisible(false);
                heightPicker.setCanceledOnTouchOutside(true);
                heightPicker.setCycleDisable(false);//不禁用循环
                //picker.setOffset(2);//偏移量
                heightPicker.setRange(140, 200, 1);//数字范围
                heightPicker.setSelectedItem(160);
                heightPicker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));

                heightPicker.setLabel("cm");
                heightPicker.setTextColor(getResources().getColor(R.color.colorPink));
                heightPicker.setDividerColor(Color.parseColor("#ffffff"));
                heightPicker.setSubmitTextColor(Color.parseColor("#000000"));
                heightPicker.setCancelTextColor(Color.parseColor("#000000"));
                heightPicker.setTopLineColor(Color.parseColor("#ffffff"));
                heightPicker.setPressedTextColor(getResources().getColor(R.color.colorPink));
                heightPicker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                    @Override
                    public void onNumberPicked(int index, Number item) {
                        mHeight = item.intValue() + "";
                        mViewTestHeightText.setText(item.intValue() + " cm");
                       /* mStringMap.put("height", height.getText().toString());
                        if (mStringMap.containsKey("weight")) {
                            mTestDetailBtn.setEnabled(true);
                        }*/
                    }
                });
                heightPicker.show();
                break;
            case R.id.view_test_weight:
                NumberPicker weightPicker = new NumberPicker(getActivity());
//        picker.setWidth(picker.getScreenWidthPixels());
                weightPicker.setCanceledOnTouchOutside(true);
                weightPicker.setDividerVisible(false);
                weightPicker.setCycleDisable(false);//不禁用循环
                weightPicker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
//                picker.setItemWidth(picker.getScreenWidthPixels());
                //picker.setOffset(2);//偏移量
                weightPicker.setRange(30, 120, 1);//数字范围
                weightPicker.setSelectedItem(60);
                weightPicker.setLabel("kg");
                weightPicker.setTextColor(getResources().getColor(R.color.colorPink));
                weightPicker.setDividerColor(Color.parseColor("#ffffff"));
                weightPicker.setSubmitTextColor(Color.parseColor("#000000"));
                weightPicker.setCancelTextColor(Color.parseColor("#000000"));
                weightPicker.setTopLineColor(Color.parseColor("#ffffff"));
                weightPicker.setPressedTextColor(getResources().getColor(R.color.colorPink));
                weightPicker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                    @Override
                    public void onNumberPicked(int index, Number item) {
                        mWeight = item.intValue() + "";
                        mViewTestWeightText.setText(item.intValue() + " kg");
                       /* mStringMap.put("weight", weight.getText().toString());
                        if (mStringMap.containsKey("height")) {
                            mTestDetailBtn.setEnabled(true);
                        }*/
                    }
                });
                weightPicker.show();
                break;
        }
    }

    @Override
    public String getFlag() {

        if (mHeight.equals("") || mWeight.equals(""))
            return "";
        else
            return mHeight + "," + mWeight;
    }

    @Override
    public String getTid() {
        return "2";
    }

    @Override
    public TrainQuestionNextBean.ListBean getListBean() {
        return null;
    }

    @Override
    public void setId(int id) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setData(String mess) {
        if (mess.equals("ViewPager2")) {
            if (StringUtils.isEmpty(mWeight) || StringUtils.isEmpty(mHeight)) {

            } else {
                mViewTestWeightText.setText(mWeight + " kg");
                mViewTestHeightText.setText(mHeight + " cm");
            }

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
