package com.jkpg.ruchu.view.activity.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.OtherVideoBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.OtherRLAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/18.
 */

public class OtherTrainActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.other_recycler_view)
    RecyclerView mOtherRecyclerView;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_train);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.NEWHANDINTRODUCTION)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("type", 2)
                .cacheKey("NEWHANDINTRODUCTION")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringDialogCallback(OtherTrainActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        OtherVideoBean otherVideoBean = new Gson().fromJson(s, OtherVideoBean.class);
                        List<OtherVideoBean.ItemBean> vedioMS2 = otherVideoBean.item;
                        initRecycleView(vedioMS2);

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        OtherVideoBean otherVideoBean = new Gson().fromJson(s, OtherVideoBean.class);
                        List<OtherVideoBean.ItemBean> vedioMS2 = otherVideoBean.item;
                        initRecycleView(vedioMS2);

                    }
                });

    }

    private void initRecycleView(List<OtherVideoBean.ItemBean> vedioMS2) {
        mOtherRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mOtherRecyclerView.setHasFixedSize(true);
        OtherRLAdapter otherRLAdapter = new OtherRLAdapter(vedioMS2);
        mOtherRecyclerView.setAdapter(otherRLAdapter);
        otherRLAdapter.setOnItemClickListener(new OtherRLAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, OtherVideoBean.ItemBean data) {
                Intent intent = new Intent(OtherTrainActivity.this, VideoDetailActivity.class);
                intent.putExtra("tid", data.tid);
                startActivity(intent);
            }
        });

    }

    private void initHeader() {
        mHeaderTvTitle.setText("拓展训练");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(String mess) {
        if (mess.equals("OtherTrain")) {
            initData();
        }
    }
}
