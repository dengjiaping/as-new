package com.jkpg.ruchu.view.activity.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.NumberPicker;

/**
 * Created by qindi on 2017/7/3.
 */

public class InfoFragment extends Fragment {
    @BindView(R.id.view_test_height_text)
    TextView mViewTestHeightText;
    @BindView(R.id.view_test_height)
    LinearLayout mViewTestHeight;
    @BindView(R.id.view_test_weight_text)
    TextView mViewTestWeightText;
    @BindView(R.id.view_test_weight)
    LinearLayout mViewTestWeight;
    Unbinder unbinder;

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
                heightPicker.setItemWidth(heightPicker.getScreenWidthPixels());
                heightPicker.setCycleDisable(false);//不禁用循环
                //picker.setOffset(2);//偏移量
                heightPicker.setRange(145, 200, 1);//数字范围
                heightPicker.setSelectedItem(168);
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
                NumberPicker WeightPicker = new NumberPicker(getActivity());
//        picker.setWidth(picker.getScreenWidthPixels());
                WeightPicker.setCanceledOnTouchOutside(true);
                WeightPicker.setDividerVisible(false);
                WeightPicker.setCycleDisable(false);//不禁用循环
//                picker.setItemWidth(picker.getScreenWidthPixels());
                //picker.setOffset(2);//偏移量
                WeightPicker.setRange(30, 150, 1);//数字范围
                WeightPicker.setSelectedItem(45);
                WeightPicker.setLabel("kg");
                WeightPicker.setTextColor(getResources().getColor(R.color.colorPink));
                WeightPicker.setDividerColor(Color.parseColor("#ffffff"));
                WeightPicker.setSubmitTextColor(Color.parseColor("#000000"));
                WeightPicker.setCancelTextColor(Color.parseColor("#000000"));
                WeightPicker.setTopLineColor(Color.parseColor("#ffffff"));
                WeightPicker.setPressedTextColor(getResources().getColor(R.color.colorPink));
                WeightPicker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                    @Override
                    public void onNumberPicked(int index, Number item) {
                        mViewTestWeightText.setText(item.intValue() + " kg");
                       /* mStringMap.put("weight", weight.getText().toString());
                        if (mStringMap.containsKey("height")) {
                            mTestDetailBtn.setEnabled(true);
                        }*/
                    }
                });
                WeightPicker.show();
                break;
        }
    }
}
