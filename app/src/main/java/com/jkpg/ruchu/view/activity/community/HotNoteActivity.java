package com.jkpg.ruchu.view.activity.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.HotNoteBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.HotPlateDetailRVAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/6/8.
 */

public class HotNoteActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.hot_note_recycler_view)
    RecyclerView mHotNoteRecyclerView;
    @BindView(R.id.hot_note_refresh_layout)
    SwipeRefreshLayout mHotNoteRefreshLayout;

    private int flag = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_note);
        ButterKnife.bind(this);
        initHeader();
        initRefreshLayout();
        initData();
    }

    private void initRefreshLayout() {
        mHotNoteRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mHotNoteRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flag = 1;
                initData();
            }
        });

    }

    private void initData() {
        OkGo
                .post(AppUrl.BBS_LOOKHOT)
                .params("flag", flag)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mHotNoteRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        HotNoteBean hotNoteBean = new Gson().fromJson(s, HotNoteBean.class);
                        List<HotNoteBean.ListBean> list = hotNoteBean.list;
                        initRecyclerView(list);
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        mHotNoteRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void initRecyclerView(final List<HotNoteBean.ListBean> list) {
        mHotNoteRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        final HotPlateDetailRVAdapter hotPlateDetailRVAdapter = new HotPlateDetailRVAdapter(R.layout.item_fans_post, list);
        mHotNoteRecyclerView.setAdapter(hotPlateDetailRVAdapter);
        hotPlateDetailRVAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mHotNoteRefreshLayout.setRefreshing(false);
                flag++;
                OkGo
                        .post(AppUrl.BBS_LOOKHOT)
                        .params("flag", flag)
                        .execute(new StringCallback() {

                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                HotNoteBean hotNoteBean = new Gson().fromJson(s, HotNoteBean.class);
                                List<HotNoteBean.ListBean> list = hotNoteBean.list;
                                if (list == null) {
                                    hotPlateDetailRVAdapter.loadMoreEnd();
                                } else if (list.size() == 0) {
                                    hotPlateDetailRVAdapter.loadMoreEnd();
                                } else {
                                    hotPlateDetailRVAdapter.addData(list);
                                    hotPlateDetailRVAdapter.loadMoreComplete();
                                }
                            }
                        });
            }
        }, mHotNoteRecyclerView);
        hotPlateDetailRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(HotNoteActivity.this, NoticeDetailActivity.class);
                intent.putExtra("bbsid", list.get(position).tid + "");
                startActivity(intent);
            }
        });

    }

    private void initHeader() {
        mHeaderTvTitle.setText("热门帖子");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
