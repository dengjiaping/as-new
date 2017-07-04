package com.jkpg.ruchu.view.activity.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qindi on 2017/7/3.
 */

public class SingleFragment extends Fragment {
    @BindView(R.id.test_single_radio_group)
    RadioGroup mTestSingleRadioGroup;
    Unbinder unbinder;
    @BindView(R.id.test_single_rb0)
    RadioButton mTestSingleRb0;
    @BindView(R.id.test_single_rb1)
    RadioButton mTestSingleRb1;
    @BindView(R.id.test_single_rb2)
    RadioButton mTestSingleRb2;
    @BindView(R.id.test_single_rb3)
    RadioButton mTestSingleRb3;

    private List<String> mList;

    public SingleFragment(List<String> list) {
        mList = list;
    }

    private List<RadioButton> mRadioButtons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_test_single, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRadioGroup();
    }

    private void initRadioGroup() {
        mRadioButtons = new ArrayList<>();
        mRadioButtons.add(mTestSingleRb0);
        mRadioButtons.add(mTestSingleRb1);
        mRadioButtons.add(mTestSingleRb2);
        mRadioButtons.add(mTestSingleRb3);
        for (int i = 0; i < mList.size(); i++) {
            RadioButton radioButton = mRadioButtons.get(i);
            radioButton.setText(mList.get(i));
            radioButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
