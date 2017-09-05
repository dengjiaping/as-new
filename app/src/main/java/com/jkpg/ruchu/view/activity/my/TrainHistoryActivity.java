package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.TrainHistoryBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.TrainHistoryAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/27.
 */

public class TrainHistoryActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.history_recycler_view)
    RecyclerView mHistoryRecyclerView;
    @BindView(R.id.no_data_text)
    TextView mNoDataText;
    @BindView(R.id.no_data)
    RelativeLayout mNoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_history);
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.EXERICESHISTORY)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheKey("EXERICESHISTORY")
                .execute(new StringDialogCallback(TrainHistoryActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        TrainHistoryBean trainHistoryBean = new Gson().fromJson(s, TrainHistoryBean.class);
                        List<TrainHistoryBean.ItemsBean> items = trainHistoryBean.items;
                        initRecyclerView(items);
                        if (items == null || items.size() == 0) {
                            mNoData.setVisibility(View.VISIBLE);
                            mNoDataText.setText("你还没有训练哦!");
                        } else {
                            mNoData.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        TrainHistoryBean trainHistoryBean = new Gson().fromJson(s, TrainHistoryBean.class);
                        List<TrainHistoryBean.ItemsBean> items = trainHistoryBean.items;
                        initRecyclerView(items);
                    }
                });
    }

    private void initRecyclerView(List<TrainHistoryBean.ItemsBean> items) {
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHistoryRecyclerView.setAdapter(new TrainHistoryAdapter(items));

    }

    private void initHeader() {
        mHeaderTvTitle.setText("训练历史");
        mHeaderIvRight.setImageResource(R.drawable.icon_data);
    }

    @OnClick({R.id.header_iv_left, R.id.header_iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right:
                startActivity(new Intent(TrainHistoryActivity.this, TrainCountActivity.class));
                break;
        }
    }
}
