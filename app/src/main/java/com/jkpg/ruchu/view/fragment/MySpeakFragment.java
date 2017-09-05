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
import com.jkpg.ruchu.bean.MyNoteBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.NoticeDetailActivity;
import com.jkpg.ruchu.view.adapter.MySpeakNoteRVAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/8/29.
 */

public class MySpeakFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private int speakIndex = 1;
    private MySpeakNoteRVAdapter mMySpeakNoteRVAdapter;
    private List<MyNoteBean.MySpeakBean> mMySpeak = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.view_recyclerview_refresh, container, false);
        mRecyclerView = new RecyclerView(getActivity());
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);

        okgo();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                speakIndex = 1;
                OkGo
                        .post(AppUrl.MYSPEAK)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fenyeid", speakIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MyNoteBean myNoteBean = new Gson().fromJson(s, MyNoteBean.class);
                                if (myNoteBean.mySpeak == null) {
                                    mRefreshLayout.setRefreshing(false);
                                    return;
                                }
                                if (mMySpeak == null) {
                                    mMySpeak = new ArrayList<>();
                                } else {
                                    mMySpeak.clear();
                                }
                                mMySpeak.addAll(myNoteBean.mySpeak);
                                mMySpeakNoteRVAdapter.notifyDataSetChanged();


                            }

                            @Override
                            public void onAfter(@Nullable String s, @Nullable Exception e) {
                                super.onAfter(s, e);
                                mRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);
                            }
                        });
            }
        });
    }

    private void okgo() {
        OkGo
                .post(AppUrl.MYSPEAK)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("fenyeid", speakIndex)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyNoteBean myNoteBean = new Gson().fromJson(s, MyNoteBean.class);
                        mMySpeak = myNoteBean.mySpeak;
                        initSpeakRecyclerView(mMySpeak);

                    }

                    @Override
                    public void onAfter(@Nullable String s, @Nullable Exception e) {
                        super.onAfter(s, e);
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mRefreshLayout.setRefreshing(true);

                    }
                });
    }

    private void initSpeakRecyclerView(final List<MyNoteBean.MySpeakBean> mySpeak) {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMySpeakNoteRVAdapter = new MySpeakNoteRVAdapter(R.layout.item_fans_post, mySpeak);
        mRecyclerView.setAdapter(mMySpeakNoteRVAdapter);
        mMySpeakNoteRVAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                speakIndex++;
                OkGo
                        .post(AppUrl.MYSPEAK)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fenyeid", speakIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MyNoteBean myNoteBean = new Gson().fromJson(s, MyNoteBean.class);
                                List<MyNoteBean.MySpeakBean> mySpeak = myNoteBean.mySpeak;
                                if (mySpeak == null) {
                                    mMySpeakNoteRVAdapter.loadMoreEnd();

                                } else if (mySpeak.size() == 0) {
                                    mMySpeakNoteRVAdapter.loadMoreEnd();
                                } else {
                                    mMySpeakNoteRVAdapter.addData(mySpeak);
                                    mMySpeakNoteRVAdapter.loadMoreComplete();
                                }
                            }
                        });
            }
        }, mRecyclerView);
        mMySpeakNoteRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String bbsid = mySpeak.get(position).tid;
                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                intent.putExtra("bbsid", bbsid + "");
                startActivity(intent);
            }
        });
        View view = View.inflate(UIUtils.getContext(), R.layout.view_no_data, null);
        TextView textView = (TextView) view.findViewById(R.id.no_data_text);
        textView.setText("你还没有发帖哦!");
        mMySpeakNoteRVAdapter.setEmptyView(view);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
