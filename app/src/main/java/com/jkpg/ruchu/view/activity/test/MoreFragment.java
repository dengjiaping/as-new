package com.jkpg.ruchu.view.activity.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.TestId;
import com.jkpg.ruchu.bean.TrainQuestionNextBean;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qindi on 2017/7/3.
 */

public class MoreFragment extends NormalFragment {
    @BindView(R.id.test_more_tv_title)
    TextView mTestMoreTvTitle;
    @BindView(R.id.test_more_tv_body)
    TextView mTestMoreTvBody;
    @BindView(R.id.test_more_ll)
    LinearLayout mTestMoreLl;
    Unbinder unbinder;
    @BindView(R.id.test_more_cb0)
    CheckBox mTestMoreCb0;
    @BindView(R.id.test_more_cb1)
    CheckBox mTestMoreCb1;
    @BindView(R.id.test_more_cb2)
    CheckBox mTestMoreCb2;
    @BindView(R.id.test_more_cb3)
    CheckBox mTestMoreCb3;
    @BindView(R.id.test_more_id)
    TextView mTestMoreId;

    private List<CheckBox> mCheckBoxes;
    TrainQuestionNextBean.ListBean listBean;
    private Map<Integer, String> mIntegerStringMap;

    public MoreFragment(TrainQuestionNextBean.ListBean listBean) {
        this.listBean = listBean;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_test_more, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCheckBox();
        mTestMoreTvTitle.setText(listBean.title);
        mTestMoreTvBody.setText(listBean.kuohao);
    }

    private void initCheckBox() {
        mIntegerStringMap = new HashMap<>();
        mIntegerStringMap.put(1, "");
        mIntegerStringMap.put(2, "");
        mIntegerStringMap.put(0, "");
        mIntegerStringMap.put(3, "");
        mCheckBoxes = new ArrayList<>();
        mCheckBoxes.add(mTestMoreCb0);
        mCheckBoxes.add(mTestMoreCb1);
        mCheckBoxes.add(mTestMoreCb2);
        mCheckBoxes.add(mTestMoreCb3);
        for (int i = 0; i < listBean.question.size(); i++) {
            CheckBox checkBox = mCheckBoxes.get(i);
            checkBox.setVisibility(View.VISIBLE);
            TrainQuestionNextBean.ListBean.QuestionBean questionBean = listBean.question.get(i);
            if (i == 0) {
                checkBox.setText(questionBean.A);
            } else if (i == 1) {
                checkBox.setText(questionBean.B);
            } else if (i == 2) {
                checkBox.setText(questionBean.C);
            } else if (i == 3) {
                checkBox.setText(questionBean.D);
            }

            final int finalI = i;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (finalI == 0) {
                            mIntegerStringMap.put(0, "A");

                        } else if (finalI == 1) {
                            mIntegerStringMap.put(1, "B");

                        } else if (finalI == 2) {
                            mIntegerStringMap.put(2, "C");

                        } else if (finalI == 3) {
                            mIntegerStringMap.put(3, "D");
                        }

                    } else {
                        if (finalI == 0) {
                            mIntegerStringMap.put(0, "");

                        } else if (finalI == 1) {
                            mIntegerStringMap.put(1, "");

                        } else if (finalI == 2) {
                            mIntegerStringMap.put(2, "");

                        } else if (finalI == 3) {
                            mIntegerStringMap.put(3, "");
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String getFlag() {
        for (int i = 0; i < mIntegerStringMap.size(); i++) {
            String s = mIntegerStringMap.get(i);
            LogUtils.i(s + "   mIntegerStringMap");
        }
        if (mIntegerStringMap.get(3).equals("D")) {
            return "D";
        } else if (mIntegerStringMap.get(2).equals("C")) {
            return "C";
        } else if (mIntegerStringMap.get(1).equals("B")) {
            return "B";
        } else if (mIntegerStringMap.get(0).equals("A")) {
            return "A";
        }
        return "";
    }

    @Override
    public String getTid() {
        return listBean.tid;
    }

    @Override
    public TrainQuestionNextBean.ListBean getListBean() {
        return listBean;
    }

    @Override
    public void setId(int id) {
        mTestMoreId.setText(id + ". ");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setTitle(TestId mess) {
        mTestMoreId.setText(mess.testID + ". ");
    }
}
