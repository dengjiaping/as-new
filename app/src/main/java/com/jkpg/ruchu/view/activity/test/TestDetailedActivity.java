package com.jkpg.ruchu.view.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.TestResultBean;
import com.jkpg.ruchu.bean.TrainQuestionNextBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/6/27.
 */

public class TestDetailedActivity extends BaseActivity {
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

    private List<NormalFragment> mViewList;


    private FragmentStatePagerAdapter mFragmentPagerAdapter;
    private SingleFragment mFragment;
    private int viewPagerPosition;
    private String mFlag;
    private List<TrainQuestionNextBean.ListBean> mList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detailed);
        ButterKnife.bind(this);
        initHeader();
        initData();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void initData() {
        OkGo
                .post(AppUrl.TEST)
                .params("flag", "0")
                .execute(new StringDialogCallback(TestDetailedActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        TrainQuestionNextBean trainQuestionNextBean = new Gson().fromJson(s, TrainQuestionNextBean.class);
                        TrainQuestionNextBean.ListBean listBean = trainQuestionNextBean.list.get(2);
                        initViewPager(listBean);

                    }
                });

    }

    private void initViewPager(TrainQuestionNextBean.ListBean listBean) {

        mFragment = new SingleFragment(listBean);
        mViewList = new ArrayList<>();
        mViewList.add(new BirthFragment());
        mViewList.add(new InfoFragment());
        mViewList.add(mFragment);


        mTestDetailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerPosition = position;
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
                if (position == mViewList.size() - 1 && position != 2) {
                    mTestDetailBtn.setText("完成");
                } else {
                    mTestDetailBtn.setText("继续");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mFragmentPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mViewList.get(position);
            }

            @Override
            public int getCount() {
                return mViewList.size();
            }

            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

        };

        mTestDetailViewPager.setAdapter(mFragmentPagerAdapter);

        mTestDetailViewPager.setOffscreenPageLimit(mViewList.size() - 1);
    }

    private void getNext(SingleFragment fragment) {
        mFlag = fragment.getFlag();
        OkGo
                .post(AppUrl.TEST)
                .params("flag", mFlag)
                .execute(new StringDialogCallback(TestDetailedActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        TrainQuestionNextBean trainQuestionNextBean = new Gson().fromJson(s, TrainQuestionNextBean.class);
                        mList = trainQuestionNextBean.list;
                        int nowSize = mViewList.size();
                        LogUtils.i(nowSize + "");
                        if (nowSize > 3) {
                            for (int size = nowSize - 1; size > 0; size--) {
                                if (size > 2) {
                                    mViewList.remove(size);
                                }
                            }
                            mFragmentPagerAdapter.notifyDataSetChanged();
                            LogUtils.i(mViewList.size() + "last");
                        }

                        for (int j = 0; j < mList.size(); j++) {
                            if (mList.get(j).kuohao.equals("（可多选）")) {
                                mViewList.add(new MoreFragment(mList.get(j)));
                                LogUtils.i(mList.get(j).title + "--" + mViewList.size());
                            } else {
                                mViewList.add(new SingleFragment(mList.get(j)));
                                LogUtils.i(mList.get(j).title + "--" + mViewList.size());
                            }
                        }
                        mFragmentPagerAdapter.notifyDataSetChanged();

                    }
                });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("盆地测试");
    }

    JsonArray jsonElements = new JsonArray();

    @OnClick({R.id.header_iv_left, R.id.test_detail_btn, R.id.test_detail_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.test_detail_btn:
                if (mTestDetailViewPager.getCurrentItem() != 2 && mTestDetailViewPager.getCurrentItem() == mViewList.size() - 1) {
                    String answer = new Gson().toJson(jsonElements);

                    OkGo
                            .post(AppUrl.BACKDOCTOR)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("questionid", mFlag)
                            .params("answer", answer)
                            .execute(new StringDialogCallback(TestDetailedActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    TestResultBean testResultBean = new Gson().fromJson(s, TestResultBean.class);
                                    Intent intent = new Intent(TestDetailedActivity.this, TestResultActivity.class);
                                    intent.putExtra("testResultBean", testResultBean);
                                    startActivity(intent);
                                    finish();
                                    /*startActivity(new Intent(TestDetailedActivity.this, TestResultActivity.class));
                                    finish();*/
                                }
                            });

                }
                NormalFragment normalFragment = mViewList.get(mTestDetailViewPager.getCurrentItem());
                String flag = normalFragment.getFlag();
                String tid = normalFragment.getTid();


                TrainQuestionNextBean.ListBean listBean = normalFragment.getListBean();
                if (flag.equals("")) {
                    ToastUtils.showShort(UIUtils.getContext(), "请选择后再继续下一题");
                } else {

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("tid", tid);
                    jsonObject.addProperty("answer", flag);
                    jsonElements.add(jsonObject);


                    if (listBean != null && listBean.tiaozhuanselect != null && listBean.tiaozhuanselect.equals(flag)) {
                        int i = Integer.parseInt(listBean.tz_titlesum) - Integer.parseInt(listBean.questionid);
                        int id = mTestDetailViewPager.getCurrentItem() + i;
                        mTestDetailViewPager.setCurrentItem(id);
                        mViewList.get(id).getListBean().fromquestionid = i + "";

                    } else {
                        int id = mTestDetailViewPager.getCurrentItem() + 1;
                        mTestDetailViewPager.setCurrentItem(id);
//                        mViewList.get(id).getListBean().fromquestionid = id + "";
                    }
                }
                break;
            case R.id.test_detail_back:
                NormalFragment normal = mViewList.get(mTestDetailViewPager.getCurrentItem());
                if (normal.getListBean() != null) {
                    String s = normal.getListBean().fromquestionid;
                    if (s != null && !s.equals("")) {
                        int i = Integer.parseInt(s);
                        mTestDetailViewPager.setCurrentItem(mTestDetailViewPager.getCurrentItem() - i);
                    } else {
                        mTestDetailViewPager.setCurrentItem(mTestDetailViewPager.getCurrentItem() - 1);

                    }
                } else {
                    mTestDetailViewPager.setCurrentItem(mTestDetailViewPager.getCurrentItem() - 1);

                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFlagEvent(MessageEvent event) {
        if (event.message.equals("flag") && viewPagerPosition == 2) {
            if (!mFragment.getFlag().equals(mFlag))
                getNext(mFragment);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
