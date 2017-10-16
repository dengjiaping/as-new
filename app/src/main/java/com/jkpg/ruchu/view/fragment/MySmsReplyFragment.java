package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MySmsReplyBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.NoticeDetailFixActivity;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.MyCommentAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/8/12.
 */

public class MySmsReplyFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private int PIndex = 1;
    private List<MySmsReplyBean.BackMessBean> mBackMess;
    private MyCommentAdapter mMyCommentAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));

        OkGo
                .post(AppUrl.MYPINGLUN)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("fenyeid", PIndex)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MySmsReplyBean mySmsReplyBean = new Gson().fromJson(s, MySmsReplyBean.class);
                        mBackMess = mySmsReplyBean.backMess;
                        initRecyclerView(mBackMess);

                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (mRefreshLayout != null)
                            mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        if (mRefreshLayout != null)
                            mRefreshLayout.setRefreshing(true);
                    }
                });
        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PIndex = 1;
                OkGo
                        .post(AppUrl.MYPINGLUN)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fenyeid", PIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MySmsReplyBean mySmsReplyBean = new Gson().fromJson(s, MySmsReplyBean.class);
                                List<MySmsReplyBean.BackMessBean> backMess = mySmsReplyBean.backMess;
                                if (backMess != null) {
                                    mBackMess.clear();
                                    mBackMess.addAll(backMess);
                                    mMyCommentAdapter.notifyDataSetChanged();
                                }
//                                initRecyclerView(backMess);

                            }

                            @Override
                            public void onAfter(String s, Exception e) {
                                super.onAfter(s, e);
                                if (mRefreshLayout != null) {
                                    mRefreshLayout.setRefreshing(false);
                                    mMyCommentAdapter.setEnableLoadMore(true);
                                }

                            }

                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);
                                if (mRefreshLayout != null) {
                                    mRefreshLayout.setRefreshing(true);
                                    mMyCommentAdapter.setEnableLoadMore(false);
                                }
                            }
                        });
            }
        });

    }

    private void initRecyclerView(final List<MySmsReplyBean.BackMessBean> backMess) {
        mMyCommentAdapter = new MyCommentAdapter(R.layout.item_personal_comment, backMess);

        try {
            mRecyclerView.setAdapter(mMyCommentAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMyCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (backMess.get(position).flag.equals("1")) {
                    Intent intent = new Intent(getActivity(), FansCenterActivity.class);
                    intent.putExtra("fansId", backMess.get(position).ruserid);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), FansCenterActivity.class);
                    intent.putExtra("fansId", backMess.get(position).userid3);
                    startActivity(intent);
                }
            }
        });
        mMyCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), NoticeDetailFixActivity.class);
                intent.putExtra("bbsid", backMess.get(position).BBSId);
                startActivity(intent);
            }
        });
        mMyCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                PIndex++;
                OkGo
                        .post(AppUrl.MYPINGLUN)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fenyeid", PIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MySmsReplyBean mySmsReplyBean = new Gson().fromJson(s, MySmsReplyBean.class);
                                List<MySmsReplyBean.BackMessBean> backMess = mySmsReplyBean.backMess;
                                if (backMess == null) {
                                    mMyCommentAdapter.loadMoreEnd();
                                } else if (backMess.size() == 0) {
                                    mMyCommentAdapter.loadMoreEnd();
                                } else {
                                    mMyCommentAdapter.addData(backMess);
                                    mMyCommentAdapter.loadMoreComplete();
                                }
                            }
                        });
            }
        }, mRecyclerView);
        View view = View.inflate(UIUtils.getContext(), R.layout.view_no_data, null);
        TextView textView = (TextView) view.findViewById(R.id.no_data_text);
        textView.setText("你还没有消息哦!");
        mMyCommentAdapter.setEmptyView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
