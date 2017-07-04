package com.jkpg.ruchu.view.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/27.
 */

public class TestDetailedActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;

    @BindView(R.id.test_detail_view_pager)
    ViewPager mTestDetailViewPager;
    @BindView(R.id.test_detail_btn)
    Button mTestDetailBtn;
    @BindView(R.id.test_detail_back)
    TextView mTestDetailBack;

    private List<Fragment> mViewList;


    private Map<String, String> mStringMap = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detailed);
        ButterKnife.bind(this);
        initHeader();
        initViewPager();
    }

    private void initViewPager() {

        List<String> title = new ArrayList<>();
        title.add("第一题");
        title.add("第二题");
        title.add("第三题");
        title.add("第四题");
        List<String> title2 = new ArrayList<>();
        title2.add("一题");
        title2.add("二题");
        title2.add("三题");
        mViewList = new ArrayList<>();
        mViewList.add(new BirthFragment());
        mViewList.add(new InfoFragment());
        mViewList.add(new SingleFragment(title));
        mViewList.add(new MoreFragment(title));
        mViewList.add(new SingleFragment(title2));
        mViewList.add(new SingleFragment(title));
        mViewList.add(new MoreFragment(title2));
        mViewList.add(new SingleFragment(title2));
        mViewList.add(new SingleFragment(title));

        mTestDetailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

              /*  if (mStringMap.size() - 1 > position) {
                    mTestDetailBtn.setEnabled(true);
                } else {
                    mTestDetailBtn.setEnabled(false);
                }*/
                if (position == 0) {
                    mTestDetailBack.setVisibility(View.INVISIBLE);
//                mTestDetailBtn.setEnabled(true);
                } else {
                    mTestDetailBack.setVisibility(View.VISIBLE);
                }
                if (position == mViewList.size() - 1) {
                    mTestDetailBtn.setText("完成");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTestDetailViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mViewList.get(position);
            }

            @Override
            public int getCount() {
                return mViewList.size();
            }
        });

        mTestDetailViewPager.setOffscreenPageLimit(mViewList.size() - 1);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("盆地测试");
    }

    int i = 0;

    @OnClick({R.id.header_iv_left, R.id.test_detail_btn, R.id.test_detail_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.test_detail_btn:
                if (mTestDetailViewPager.getCurrentItem() == mViewList.size() - 1) {
                    startActivity(new Intent(TestDetailedActivity.this, TestResultActivity.class));
                    finish();
                }
                mTestDetailViewPager.setCurrentItem(mTestDetailViewPager.getCurrentItem() + 1);
                break;
            case R.id.test_detail_back:
                mTestDetailViewPager.setCurrentItem(mTestDetailViewPager.getCurrentItem() - 1);
                break;
        }
    }
}
