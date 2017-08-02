package com.jkpg.ruchu.view.activity.test;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.TrainQuestionNextBean;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qindi on 2017/7/3.
 */

public class SingleFragment extends NormalFragment {
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
    @BindView(R.id.test_single_title)
    TextView mTestSingleTitle;
    @BindView(R.id.test_single_subhead)
    TextView mTestSingleSubhead;

    private TrainQuestionNextBean.ListBean listBean;
    private List<RadioButton> mRadioButtons;
    private final String[] mFlag = new String[1];

    public SingleFragment(TrainQuestionNextBean.ListBean listBean) {
        this.listBean = listBean;
    }

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
        mTestSingleTitle.setText(listBean.title);
    }

    private void initRadioGroup() {
        mRadioButtons = new ArrayList<>();
        mRadioButtons.add(mTestSingleRb0);
        mRadioButtons.add(mTestSingleRb1);
        mRadioButtons.add(mTestSingleRb2);
        mRadioButtons.add(mTestSingleRb3);
        for (int i = 0; i < listBean.question.size(); i++) {
            RadioButton radioButton = mRadioButtons.get(i);
            radioButton.setVisibility(View.VISIBLE);
            TrainQuestionNextBean.ListBean.QuestionBean questionBean = listBean.question.get(i);
            if (i == 0) {
                radioButton.setText(questionBean.A);
            } else if (i == 1) {
                radioButton.setText(questionBean.B);
            } else if (i == 2) {
                radioButton.setText(questionBean.C);
            } else if (i == 3) {
                radioButton.setText(questionBean.D);
            }
        }

        mTestSingleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                mFlag[0] = "A";
                switch (checkedId) {
                    case R.id.test_single_rb0:
                        mFlag[0] = "A";
                        break;
                    case R.id.test_single_rb1:
                        mFlag[0] = "B";
                        break;
                    case R.id.test_single_rb2:
                        mFlag[0] = "C";
                        break;
                    case R.id.test_single_rb3:
                        mFlag[0] = "D";
                        break;
                }
                EventBus.getDefault().post(new MessageEvent("flag"));

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getFlag() {
        if (StringUtils.isEmpty(mFlag[0]))
            return "";
        return mFlag[0];
    }

    @Override
    public String getTid() {
        return listBean.tid;
    }

    @Override
    public TrainQuestionNextBean.ListBean getListBean() {
        return listBean;
    }


}
