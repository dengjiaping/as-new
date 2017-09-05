package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.VipManageBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.VipManageVPAdapter;
import com.jkpg.ruchu.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

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
 * Created by qindi on 2017/5/24.
 */

public class VipManageActivity extends BaseActivity {
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
    LinearLayout mVipManagerTvNotOpen;
    @BindView(R.id.vip_manager_tab_layout)
    TabLayout mVipManagerTabLayout;
    @BindView(R.id.vip_manager_view_pager)
    ViewPager mVipManagerViewPager;
    @BindView(R.id.vip_manager_btn_open_vip)
    Button mVipManagerBtnOpenVip;
    @BindView(R.id.vip_manager_iv_vip)
    ImageView mVipManagerIvVip;
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;
    @BindView(R.id.vip_title)
    TextView mVipTitle;
    @BindView(R.id.vip_image)
    ImageView mVipImage;
    @BindView(R.id.vip_manager_tv_not_open_text)
    TextView mVipManagerTvNotOpenText;


    private List<View> viewList;
    private List<String> viewTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_manage);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHeaderView.setElevation(0);
        }
        initHeader();
        initData();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initData() {
        OkGo
                .post(AppUrl.VIPMANAGE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheKey("VIPMANAGE")
                .execute(new StringDialogCallback(VipManageActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        VipManageBean vipManageBean = new Gson().fromJson(s, VipManageBean.class);
                        initVipHeader(vipManageBean);
//                        List<VipManageBean.ListBean> list = vipManageBean.list;
//                        initViewPager(list);

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        VipManageBean vipManageBean = new Gson().fromJson(s, VipManageBean.class);
                        initVipHeader(vipManageBean);
                        List<VipManageBean.ListBean> list = vipManageBean.list;
                        initViewPager(list);
                    }
                });


    }

    private void initVipHeader(VipManageBean vipManageBean) {
        mVipManagerTvName.setText(vipManageBean.nick);
        if (vipManageBean.isVIP.equals("0")) {
            mVipManagerIvVip.setImageResource(R.drawable.icon_vip2);
            mVipManagerTvNotOpen.setVisibility(View.VISIBLE);
            mVipManagerTvRenew.setVisibility(View.GONE);
            mVipTitle.setText("现在开通如初会员，即可享受以下特权：");
            mVipImage.setImageResource(R.drawable.open_vip);
        } else {
            mVipManagerIvVip.setImageResource(R.drawable.icon_vip1);
            mVipManagerTvNotOpen.setVisibility(View.GONE);
            mVipManagerTvRenew.setVisibility(View.VISIBLE);
            mVipManagerTvRenew.setText(vipManageBean.VIPTime + " 到期");
            mVipManagerBtnOpenVip.setText("续费会员");
            mVipTitle.setText("您已开通如初会员，赶快加入训练吧！");
        }
        if (vipManageBean.isoverdue) {
            mVipManagerBtnOpenVip.setText("续费会员");
            mVipTitle.setText("您的会员已经过期，赶快续费吧!");
            mVipManagerTvNotOpenText.setText("已过期");
            mVipImage.setImageResource(R.drawable.expired_vip);
        }

        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + vipManageBean.headImg)
                .crossFade()
                .into(mVipManagerCivPhoto);
    }

    private void initViewPager(List<VipManageBean.ListBean> list) {
        viewList = new ArrayList<>();
        viewTitle = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            viewList.add(View.inflate(UIUtils.getContext(), R.layout.view_train_vip, null));
            viewTitle.add(list.get(i).title);
        }
//        viewList.add(View.inflate(UIUtils.getContext(), R.layout.view_train_vip, null));
//        viewList.add(View.inflate(UIUtils.getContext(), R.layout.view_train_vip, null));
//        viewTitle.add("训练特权");
//        viewTitle.add("咨询特权");
//        viewTitle.add("社区特权");
        mVipManagerTabLayout.setupWithViewPager(mVipManagerViewPager);
        mVipManagerViewPager.setAdapter(new VipManageVPAdapter(viewList, viewTitle, list));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openVip(MessageEvent mess) {
        if (mess.message.equals("Vip")) {
            initData();
        }
    }
}
