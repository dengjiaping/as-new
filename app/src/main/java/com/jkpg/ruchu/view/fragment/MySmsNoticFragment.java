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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MySmsNoticeBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.MySmsNoticeAdapter;
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
 * Created by qindi on 2017/8/24.
 */

public class MySmsNoticFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private List<MySmsNoticeBean.ListBean> mList;
    private MySmsNoticeAdapter mMyCommentAdapter;


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
        OkGo
                .post(AppUrl.MYNOTICE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MySmsNoticeBean mySmsNoticeBean = new Gson().fromJson(s, MySmsNoticeBean.class);
                        mList = mySmsNoticeBean.list;
                        initRecyclerView(mySmsNoticeBean.list);
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (mRefreshLayout != null) {
                            mRefreshLayout.setRefreshing(false);
                        }
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
                OkGo
                        .post(AppUrl.MYNOTICE)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MySmsNoticeBean mySmsNoticeBean = new Gson().fromJson(s, MySmsNoticeBean.class);
                                if (mySmsNoticeBean.list != null) {
                                    mList.clear();
                                    mList.addAll(mySmsNoticeBean.list);
                                    mMyCommentAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onAfter(String s, Exception e) {
                                super.onAfter(s, e);
                                mRefreshLayout.setRefreshing(false);
                                mMyCommentAdapter.setEnableLoadMore(true);

                            }

                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);
                                mRefreshLayout.setRefreshing(true);
                                mMyCommentAdapter.setEnableLoadMore(false);

                            }
                        });
            }
        });


    }

    private void initRecyclerView(List<MySmsNoticeBean.ListBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setStackFromEnd(false);

        try {
            mRecyclerView.setLayoutManager(layoutManager);
            mMyCommentAdapter = new MySmsNoticeAdapter(R.layout.item_sms_notic, list);
            mRecyclerView.setAdapter(mMyCommentAdapter);
            mMyCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mList.get(position).type.equals("1")) {
                        Intent i = new Intent(getActivity(), FansCenterActivity.class);
                        i.putExtra("fansId", mList.get(position).userid);
                        startActivity(i);
                    }
                }
            });
//            mMyCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    if (mList.get(position).type.equals("1")) {
//                        Intent i = new Intent(getActivity(), FansCenterActivity.class);
//                        i.putExtra("fansId", mList.get(position).userid);
//                        startActivity(i);
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mRecyclerView.smoothScrollToPosition(list.size() - 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
