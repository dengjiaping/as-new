package com.jkpg.ruchu.view.activity.community;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.SendNoteMess;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.view.adapter.PlateDetailVPAdapter;
import com.jkpg.ruchu.view.fragment.PlateDetailAllFragment;
import com.jkpg.ruchu.view.fragment.PlateDetailFineFragment;
import com.jkpg.ruchu.view.fragment.PlateDetailNewFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/5.
 */

public class PlateDetailActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.plate_detail_tab_layout)
    TabLayout mPlateDetailTabLayout;
    @BindView(R.id.plate_detail_view_pager)
    ViewPager mPlateDetailViewPager;
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;

    private List<Fragment> viewList;
    private List<String> viewTitle;
    public static String mTitle;
    private String mPlateid;
    private ArrayList<String> mPlate;
    private ArrayList<String> mPlateId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_detail);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHeaderView.setElevation(0);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mTitle = getIntent().getStringExtra("title");
        mPlateid = getIntent().getStringExtra("plateid");
        mPlate = getIntent().getStringArrayListExtra("plate");
        mPlateId = getIntent().getStringArrayListExtra("plateId");
        for (String s : mPlate) {
            LogUtils.i(s + "1");
        }
        initHeader();
        initData();
        initTabLayout();
    }

    private void initData() {
        viewTitle = new ArrayList<>();
        viewTitle.add("全部");
        viewTitle.add("精华");
        viewTitle.add("最新");
        viewList = new ArrayList<>();
        viewList.add(new PlateDetailAllFragment());
        viewList.add(new PlateDetailFineFragment());
        viewList.add(new PlateDetailNewFragment());
    }

    private void initTabLayout() {
        mPlateDetailTabLayout.setupWithViewPager(mPlateDetailViewPager);
        mPlateDetailViewPager.setAdapter(new PlateDetailVPAdapter(getSupportFragmentManager(), viewList, viewTitle));
        mPlateDetailViewPager.setOffscreenPageLimit(3);
    }

    private void initHeader() {
        mHeaderIvRight.setImageResource(R.drawable.icon_notice2);
        mHeaderTvTitle.setText(mTitle);
    }

    @OnClick({R.id.header_iv_left, R.id.header_iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right:
                Intent intent = new Intent(PlateDetailActivity.this, NoticeActivity.class);
                intent.putExtra("plateid", mPlateid);
                startActivity(intent);
                break;
        }
    }

    public String getHeaderTitle() {
        return mTitle;
    }

    public String getPlateid() {
        return mPlateid;
    }

    public ArrayList<String> getPlate() {
        return mPlate;
    }

    public ArrayList<String> getPlateId() {
        return mPlateId;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void send(SendNoteMess mess) {
        if (mess.mess.equals("sendSkip")) {
            mPlateid = mess.id;
            mHeaderTvTitle.setText(mess.title);
        }
    }
}
