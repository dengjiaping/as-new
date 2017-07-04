package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.VipManageVPAdapter;
import com.jkpg.ruchu.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/24.
 */

public class VipManageActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.vip_manager_civ_photo)
    CircleImageView mVipManagerCivPhoto;
    @BindView(R.id.vip_manager_tv_name)
    TextView mVipManagerTvName;
    @BindView(R.id.vip_manager_tv_renew)
    TextView mVipManagerTvRenew;
    @BindView(R.id.vip_manager_tv_not_open)
    TextView mVipManagerTvNotOpen;
    @BindView(R.id.vip_manager_tab_layout)
    TabLayout mVipManagerTabLayout;
    @BindView(R.id.vip_manager_view_pager)
    ViewPager mVipManagerViewPager;
    @BindView(R.id.vip_manager_btn_open_vip)
    Button mVipManagerBtnOpenVip;

    private List<View> viewList;
    private List<String> viewTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_manage);
        ButterKnife.bind(this);
        initData();
        initHeader();
        initTabLayout();
        initViewPager();
    }

    private void initData() {
        viewList = new ArrayList<>();
        viewList.add(View.inflate(UIUtils.getContext(), R.layout.view_train_vip, null));
        viewList.add(View.inflate(UIUtils.getContext(), R.layout.view_train_vip, null));
        viewList.add(View.inflate(UIUtils.getContext(), R.layout.view_train_vip, null));
        viewTitle = new ArrayList<>();
        viewTitle.add("训练特权");
        viewTitle.add("咨询特权");
        viewTitle.add("社区特权");
    }

    private void initViewPager() {
        mVipManagerViewPager.setAdapter(new VipManageVPAdapter(viewList, viewTitle));
    }

    private void initTabLayout() {
        mVipManagerTabLayout.setupWithViewPager(mVipManagerViewPager);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("会员管理");
    }

    @OnClick({R.id.header_iv_left, R.id.vip_manager_tv_renew, R.id.vip_manager_tv_not_open, R.id.vip_manager_btn_open_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.vip_manager_tv_renew:
                break;
            case R.id.vip_manager_tv_not_open:
                break;
            case R.id.vip_manager_btn_open_vip:
                startActivity(new Intent(VipManageActivity.this, OpenVipActivity.class));
                break;
        }
    }
}
