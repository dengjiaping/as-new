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
import com.jkpg.ruchu.bean.FineNoteBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.FineNoteRVAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/6/5.
 */

public class FineNoteActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.fine_note_recycler_view)
    RecyclerView mFineNoteRecyclerView;
    @BindView(R.id.fine_note_refresh_layout)
    SwipeRefreshLayout mFineNoteRefreshLayout;

    private FineNoteRVAdapter mAdapter;
    private int flag = 1;
    private List<FineNoteBean.ListBean> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_note);
        ButterKnife.bind(this);
        initHeader();
        initRefreshLayout();
        initData();

    }

    private void initRefreshLayout() {
        mFineNoteRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mFineNoteRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                initData();
//                mFineNoteRefreshLayout.setRefreshing(false);
                OkGo
                        .post(AppUrl.ARTICLEINFOS)
                        .execute(new StringCallback() {

                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);
                                mFineNoteRefreshLayout.setRefreshing(true);
                            }

                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                FineNoteBean fineNoteBean = new Gson().fromJson(s, FineNoteBean.class);
                                if (mList != null) {
                                    mList.clear();
                                    mList.addAll(fineNoteBean.list);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    mList = fineNoteBean.list;
                                    initRecyclerView();
                                }

                            }

                            @Override
                            public void onAfter(String s, Exception e) {
                                super.onAfter(s, e);
                                mFineNoteRefreshLayout.setRefreshing(false);

                            }
                        });
            }
        });

    }

    private void initData() {
        OkGo
                .post(AppUrl.ARTICLEINFOS)
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mFineNoteRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        FineNoteBean fineNoteBean = new Gson().fromJson(s, FineNoteBean.class);
                        mList = fineNoteBean.list;
                        initRecyclerView();

                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        mFineNoteRefreshLayout.setRefreshing(false);

                    }
                });
    }

    private void initRecyclerView() {
        mFineNoteRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mAdapter = new FineNoteRVAdapter(R.layout.item_fine_note, mList);
        mFineNoteRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(FineNoteActivity.this, FineNoteDetailWebFixActivity.class);
                intent.putExtra("art_id", mList.get(position).articleid + "");
                startActivity(intent);
            }
        });
//        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                mFineNoteRefreshLayout.setRefreshing(false);
//                flag++;
//
//                OkGo
//                        .post(AppUrl.BBS_LOOKGOOD)
//                        .params("flag", flag)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(String s, Call call, Response response) {
//                                FineNoteBean fineNoteBean = new Gson().fromJson(s, FineNoteBean.class);
//                                List<FineNoteBean.ListBean> list = fineNoteBean.list;
//                                if (list == null) {
//                                    mAdapter.setEnableLoadMore(false);
//                                } else if (list.size() == 0) {
//                                    mAdapter.setEnableLoadMore(false);
//                                } else {
//                                    mAdapter.addData(list);
//                                    mAdapter.loadMoreComplete();
//                                }
//                            }
//                        });
//            }
//        }, mFineNoteRecyclerView);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("精选文章");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(String mess) {
        if (mess.equals("Community")) {
            OkGo
                    .post(AppUrl.ARTICLEINFOS)
                    .execute(new StringCallback() {

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            mFineNoteRefreshLayout.setRefreshing(true);
                        }

                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            FineNoteBean fineNoteBean = new Gson().fromJson(s, FineNoteBean.class);
                            mList.clear();
                            mList.addAll(fineNoteBean.list);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                            mFineNoteRefreshLayout.setRefreshing(false);

                        }
                    });
        }
    }
}
