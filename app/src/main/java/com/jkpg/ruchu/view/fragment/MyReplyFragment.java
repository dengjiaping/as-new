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
import com.jkpg.ruchu.bean.MyReplyNoteBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.NoticeDetailFixActivity;
import com.jkpg.ruchu.view.adapter.SenderRLAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class MyReplyFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView2;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private int replyIndex = 1;
    private SenderRLAdapter mSenderRLAdapter;
    List<MyReplyNoteBean.MyReplyBean> myReplyList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.view_recyclerview_refresh, container, false);

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
                replyIndex = 1;
                OkGo
                        .post(AppUrl.MYREPLY)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fenyeid", replyIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                LogUtils.d(s + "myReply");
                                myReplyList.clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray myReply = jsonObject.getJSONArray("myReply");
                                    for (int i = 0; i < myReply.length(); i++) {
                                        MyReplyNoteBean.MyReplyBean myReplyBean = new MyReplyNoteBean.MyReplyBean();
                                        JSONObject jsonObject1 = myReply.getJSONObject(i);
                                        myReplyBean.content = jsonObject1.getString("content");

                                        myReplyBean.title = jsonObject1.getString("title");
                                        myReplyBean.headImg = jsonObject1.getString("headImg");
                                        myReplyBean.BBSId = jsonObject1.getInt("BBSId");
                                        myReplyBean.replytime = jsonObject1.getString("replytime");
                                        myReplyBean.ct = jsonObject1.getString("ct");
                                        myReplyList.add(myReplyBean);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mSenderRLAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAfter(@Nullable String s, @Nullable Exception e) {
                                super.onAfter(s, e);
                                mRefreshLayout.setRefreshing(false);
                            }
                        });
            }
        });
    }

    private void okgo() {
        OkGo
                .post(AppUrl.MYREPLY)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("fenyeid", replyIndex)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.d(s + "myReply");
                        myReplyList = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray myReply = jsonObject.getJSONArray("myReply");
                            for (int i = 0; i < myReply.length(); i++) {
                                MyReplyNoteBean.MyReplyBean myReplyBean = new MyReplyNoteBean.MyReplyBean();
                                JSONObject jsonObject1 = myReply.getJSONObject(i);
                                myReplyBean.content = jsonObject1.getString("content");

                                myReplyBean.title = jsonObject1.getString("title");
                                myReplyBean.headImg = jsonObject1.getString("headImg");
                                myReplyBean.BBSId = jsonObject1.getInt("BBSId");
                                myReplyBean.replytime = jsonObject1.getString("replytime");
                                myReplyBean.ct = jsonObject1.getString("ct");
                                myReplyList.add(myReplyBean);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initReplyRecyclerView(myReplyList);
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

    private void initReplyRecyclerView(final List<MyReplyNoteBean.MyReplyBean> mySpeak) {
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSenderRLAdapter = new SenderRLAdapter(R.layout.item_sender, myReplyList);
        mRecyclerView2.setAdapter(mSenderRLAdapter);
        mSenderRLAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                replyIndex++;
                OkGo
                        .post(AppUrl.MYREPLY)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fenyeid", replyIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                MyReplyNoteBean myReplyNoteBean = new Gson().fromJson(s, MyReplyNoteBean.class);
                                List<MyReplyNoteBean.MyReplyBean> mySpeak = myReplyNoteBean.myReply;
                                if (mySpeak == null) {
                                    mSenderRLAdapter.loadMoreEnd();

                                } else if (mySpeak.size() == 0) {
                                    mSenderRLAdapter.loadMoreEnd();
                                } else {
                                    mSenderRLAdapter.addData(mySpeak);
                                    mSenderRLAdapter.loadMoreComplete();
                                }
                            }
                        });
            }
        }, mRecyclerView2);
        View view = View.inflate(UIUtils.getContext(), R.layout.view_no_data, null);
        TextView textView = (TextView) view.findViewById(R.id.no_data_text);
        textView.setText("你还没有回复哦!");
        mSenderRLAdapter.setEmptyView(view);
        mSenderRLAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int bbsid = mySpeak.get(position).BBSId;
                Intent intent = new Intent(getActivity(), NoticeDetailFixActivity.class);
                intent.putExtra("bbsid", bbsid + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
