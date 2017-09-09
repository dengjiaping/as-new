package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateDetailBean;
import com.jkpg.ruchu.bean.SendNoteMess;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.NoticeDetailActivity;
import com.jkpg.ruchu.view.activity.community.PlateDetailActivity;
import com.jkpg.ruchu.view.activity.community.SendNoteActivity;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.adapter.PlateDetailRVAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/6/5.
 */

public class PlateDetailNewFragment extends Fragment {
    @BindView(R.id.plate_detail_recycler_view)
    RecyclerView mPlateDetailRecyclerView;
    @BindView(R.id.plate_detail_fab)
    FloatingActionButton mPlateDetailFab;
    Unbinder unbinder;
    @BindView(R.id.plate_detail_refresh)
    SwipeRefreshLayout mPlateDetailRefresh;
    private List<PlateDetailBean> plates;
    private PlateDetailRVAdapter mAdapter;

    int flag = 1;
    private String mPlateid;
    private List<PlateDetailBean.NoticeBean> mNotice = new ArrayList<>();
    private List<PlateDetailBean.ListBean> mList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plate_detail_all, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPlateid = ((PlateDetailActivity) getActivity()).getPlateid();
        mPlateDetailRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        initData();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mPlateDetailRefresh.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mPlateDetailRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (mList != null) {
//                    mList.clear();
//                } else {
//                    return;
//                }
//                flag = 1;
//                initData();
                refresh();
            }
        });

    }

    private void refresh() {
        flag = 1;
        OkGo
                .post(AppUrl.BBS_LOOKUP)
                .params("plateid", mPlateid)
                .params("flag", flag)
                .tag(this)
                .params("isgood", 2)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mPlateDetailRefresh.setRefreshing(true);
                        mAdapter.setEnableLoadMore(false);


                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        PlateDetailBean plateDetailBean = new Gson().fromJson(s, PlateDetailBean.class);
                        mList.clear();
                        mList.addAll(plateDetailBean.list);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        try {
                            mPlateDetailRefresh.setRefreshing(false);
                            mAdapter.setEnableLoadMore(true);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                });

    }

    private void initData() {
        OkGo
                .post(AppUrl.BBS_LOOKUP)
                .params("plateid", mPlateid)
                .params("flag", flag)
                .tag(this)
                .params("isgood", 2)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mPlateDetailRefresh.setRefreshing(true);

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        PlateDetailBean plateDetailBean = new Gson().fromJson(s, PlateDetailBean.class);
                        mNotice = plateDetailBean.notice;
                        mList = plateDetailBean.list;
                        initPlateDetailRecyclerView(mNotice, mList);
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        mPlateDetailRefresh.setRefreshing(false);
                    }
                });

    }

    private void initPlateDetailRecyclerView(List<PlateDetailBean.NoticeBean> notice, final List<PlateDetailBean.ListBean> list) {
        mAdapter = new PlateDetailRVAdapter(R.layout.item_plate_detail, list);
        mPlateDetailRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                intent.putExtra("bbsid", list.get(position).tid + "");
                startActivity(intent);
            }
        });
        mPlateDetailRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1)
                    mPlateDetailFab.hide();
                else if (newState == 0)
                    mPlateDetailFab.show();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                flag++;
                OkGo
                        .post(AppUrl.BBS_LOOKUP)
                        .params("plateid", mPlateid)
                        .params("flag", flag)
                        .tag(this)
                        .params("isgood", 3)
                        .execute(new StringCallback() {

                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                PlateDetailBean plateDetailBean = new Gson().fromJson(s, PlateDetailBean.class);
                                List<PlateDetailBean.ListBean> list = plateDetailBean.list;
                                if (list == null) {
                                    mAdapter.loadMoreEnd();
                                } else if (list.size() == 0) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.addData(list);
                                    mAdapter.loadMoreComplete();
                                }
                            }
                        });
            }
        }, mPlateDetailRecyclerView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.plate_detail_fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.plate_detail_fab:
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                Intent intent = new Intent(getActivity(), SendNoteActivity.class);
                intent.putExtra("title", ((PlateDetailActivity) getActivity()).getHeaderTitle());
                intent.putStringArrayListExtra("plate", ((PlateDetailActivity) getActivity()).getPlate());
                intent.putStringArrayListExtra("plateId", ((PlateDetailActivity) getActivity()).getPlateId());
                intent.putExtra("plateid", ((PlateDetailActivity) getActivity()).getPlateid());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMess(String mess) {
        if (mess.equals("send"))
            refresh();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void send(SendNoteMess mess) {
        if (mess.mess.equals("sendSkip")) {
            mPlateid = mess.id;
            refresh();
        }
    }
}
