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
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by qindi on 2017/7/3.
 */

public class BirthFragment extends NormalFragment {
    @BindView(R.id.view_test_birth_text)
    TextView mViewTestBirthText;
    @BindView(R.id.view_test_birth)
    LinearLayout mViewTestBirth;
    Unbinder unbinder;

    private String mYear = "";
    private String mMonth = "";
    private String mDay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_test_birth, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            String birth = savedInstanceState.getString("birth");
            mViewTestBirthText.setText(birth);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.view_test_birth)
    public void onViewClicked() {
        final DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH_DAY);
        picker.setCanceledOnTouchOutside(true);
        picker.setCycleDisable(false);//不禁用循环
        picker.setDividerVisible(false);
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
        picker.setRangeStart(1970, 1, 1);
        picker.setUseWeight(false);
        picker.setRangeEnd(2002, 12, 31);
        picker.setSelectedItem(1990, 5, 31);
        picker.setTextColor(UIUtils.getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(UIUtils.getColor(R.color.colorPink));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                mYear = year;
                mMonth = month;
                mDay = day;
                mViewTestBirthText.setText(year + "年" + month + "月" + day + "日");
            }
        });
        picker.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("birth", mViewTestBirthText.getText().toString());

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String birth = savedInstanceState.getString("birth");
            mViewTestBirthText.setText(birth);
        }

    }

    @Override
    public String getFlag() {
        if (mYear.equals("")) {
            return "";
        } else {
            return mYear + "-" + mMonth + "-" + mDay;
        }
    }

    @Override
    public String getTid() {
        return "1";
    }

    @Override
    public TrainQuestionNextBean.ListBean getListBean() {
        return null;
    }

    @Override
    public void setId(int id) {

    }
}
