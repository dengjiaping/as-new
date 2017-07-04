package com.jkpg.ruchu.view.activity.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class MoreFragment extends Fragment {
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

    private List<String> mLists;
    private List<CheckBox> mCheckBoxes;

    public MoreFragment(List<String> lists) {
        mLists = lists;
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
    }

    private void initCheckBox() {
        mCheckBoxes = new ArrayList<>();
        mCheckBoxes.add(mTestMoreCb0);
        mCheckBoxes.add(mTestMoreCb1);
        mCheckBoxes.add(mTestMoreCb2);
        mCheckBoxes.add(mTestMoreCb3);
        for (int i = 0; i < mLists.size(); i++) {
            CheckBox checkBox = mCheckBoxes.get(i);
            checkBox.setText(mLists.get(i));
            checkBox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
