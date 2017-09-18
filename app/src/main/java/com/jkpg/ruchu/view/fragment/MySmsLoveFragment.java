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
import com.jkpg.ruchu.bean.MySmsLoveBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.NoticeDetailFixActivity;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.MySmsLoveAdapter;
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

public class MySmsLoveFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private int PIndex = 1;
    private MySmsLoveAdapter mMySmsLoveAdapter;
    private List<MySmsLoveBean.BackMessBean> mBackMess;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));

        super.onViewCreated(view, savedInstanceState);
        OkGo
                .post(AppUrl.MYDIANZAN)
                .tag(this)

                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("fenyeid", PIndex)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MySmsLoveBean mySmsLoveBean = new Gson().fromJson(s, MySmsLoveBean.class);
                        mBackMess = mySmsLoveBean.backMess;
                        initRecyclerView(mBackMess);
                    }
                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mRefreshLayout.setRefreshing(true);
                    }
                });
        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PIndex = 1;
                OkGo
                        .post(AppUrl.MYDIANZAN)
                        .tag(this)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fenyeid", PIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MySmsLoveBean mySmsLoveBean = new Gson().fromJson(s, MySmsLoveBean.class);
//                                if (mySmsLoveBean.backMess != null){
                                    mBackMess.clear();
                                    mBackMess.addAll(mySmsLoveBean.backMess);
                                    mMySmsLoveAdapter.notifyDataSetChanged();
//                                }
                            }
                            @Override
                            public void onAfter(String s, Exception e) {
                                super.onAfter(s, e);
                                mRefreshLayout.setRefreshing(false);
                                mMySmsLoveAdapter.setEnableLoadMore(true);

                            }

                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);
                                mRefreshLayout.setRefreshing(true);
                                mMySmsLoveAdapter.setEnableLoadMore(false);

                            }
                        });
            }
        });

    }

    private void initRecyclerView(final List<MySmsLoveBean.BackMessBean> backMess) {
        mMySmsLoveAdapter = new MySmsLoveAdapter(R.layout.item_personal_comment, backMess);
        mRecyclerView.setAdapter(mMySmsLoveAdapter);
        mMySmsLoveAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
        mMySmsLoveAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), NoticeDetailFixActivity.class);
                intent.putExtra("bbsid", backMess.get(position).BBSId);
                startActivity(intent);
            }
        });
        mMySmsLoveAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                PIndex++;
                OkGo
                        .post(AppUrl.MYDIANZAN)
                        .tag(this)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fenyeid", PIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MySmsLoveBean mySmsLoveBean = new Gson().fromJson(s, MySmsLoveBean.class);
                                List<MySmsLoveBean.BackMessBean> backMess = mySmsLoveBean.backMess;
                                if (backMess == null) {
                                    mMySmsLoveAdapter.loadMoreEnd();
                                } else if (backMess.size() == 0) {
                                    mMySmsLoveAdapter.loadMoreEnd();
                                } else {
                                    mMySmsLoveAdapter.addData(backMess);
                                    mMySmsLoveAdapter.loadMoreComplete();
                                }
                            }
                        });
            }
        }, mRecyclerView);
        View view = View.inflate(UIUtils.getContext(), R.layout.view_no_data, null);
        TextView textView = (TextView) view.findViewById(R.id.no_data_text);
        textView.setText("你还没有消息哦!");
        mMySmsLoveAdapter.setEmptyView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OkGo.getInstance().cancelTag(this);
        unbinder.unbind();
    }
}
