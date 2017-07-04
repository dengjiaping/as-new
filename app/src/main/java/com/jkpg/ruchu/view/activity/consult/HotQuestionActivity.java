package com.jkpg.ruchu.view.activity.consult;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.HotQuestionBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.HotQuestionVPAdapter;
import com.jkpg.ruchu.view.fragment.HotQuestionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/13.
 */

public class HotQuestionActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.hot_question_tab)
    TabLayout mHotQuestionTab;
    @BindView(R.id.hot_question_iv_add)
    ImageView mHotQuestionIvAdd;
    @BindView(R.id.hot_question_view_pager)
    ViewPager mHotQuestionViewPager;

    private List<String> mTitles;
    private List<Fragment> mViews;
    private List<HotQuestionBean> data;
    private List<HotQuestionBean> data1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_question);
        ButterKnife.bind(this);
        initHeader();
        initData();
        initTabLayout();
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new HotQuestionBean());
        }
        data1 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            data1.add(new HotQuestionBean());
        }
        mTitles = new ArrayList<>();
        mTitles.add("常见问题1");
        mTitles.add("常见2");
        mTitles.add("题3");
        mTitles.add("题3");
        mTitles.add("常见问题哈哈4");
        mTitles.add("常见问题5");
        mTitles.add("常见问题6");
        mTitles.add("常见问题7");
        mTitles.add("常见问题8");
        mViews = new ArrayList<>();
        for (int i = 0; i < mTitles.size(); i++) {
            if (i % 2 == 0){

            mViews.add(new HotQuestionFragment(data1));
            } else {

            mViews.add(new HotQuestionFragment(data));
            }
        }

    }

    private void initTabLayout() {
        mHotQuestionTab.setupWithViewPager(mHotQuestionViewPager);
        mHotQuestionViewPager.setAdapter(new HotQuestionVPAdapter(getSupportFragmentManager(),mTitles, mViews));
    }

    private void initHeader() {
        mHeaderTvTitle.setText("常见问题");
    }

    @OnClick({R.id.header_iv_left, R.id.hot_question_iv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.hot_question_iv_add:
                showPopupWindow();
                break;
        }
    }

    private void showPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_select_question, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        FlexboxLayout flexboxLayout = (FlexboxLayout) view.findViewById(R.id.view_question_flex);
        for (int i = 0; i < mTitles.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());
            TextView textView = new TextView(UIUtils.getContext());
            textView.setBackgroundResource(R.drawable.shap_rectangle_gray_void_text);
            textView.setTextColor(Color.BLACK);
            textView.setText(mTitles.get(i));
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHotQuestionViewPager.setCurrentItem(finalI);
                    window.dismiss();
                }
            });
            linearLayout.setPadding(12, 5, 12, 5);
            linearLayout.addView(textView);
            flexboxLayout.addView(linearLayout);
        }
        ObjectAnimator
                .ofFloat(mHotQuestionIvAdd, "rotation", 0.0F, 135.0F)//
                .setDuration(500)//
                .start();
        window.setFocusable(true);
        window.showAsDropDown(mHotQuestionTab);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ObjectAnimator
                        .ofFloat(mHotQuestionIvAdd, "rotation", 135.0F, 0.0F)//
                        .setDuration(500)//
                        .start();
            }
        });

    }
}
