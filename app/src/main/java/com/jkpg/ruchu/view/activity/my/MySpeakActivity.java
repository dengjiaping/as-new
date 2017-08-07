package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MyNoteBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.NoticeDetailActivity;
import com.jkpg.ruchu.view.adapter.MyReplyNoteBean;
import com.jkpg.ruchu.view.adapter.MySpeakNoteRVAdapter;
import com.jkpg.ruchu.view.adapter.MySpeakVPAdapter;
import com.jkpg.ruchu.view.adapter.SenderRLAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/25.
 */

public class MySpeakActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_speak_tab_layout)
    TabLayout mMySpeakTabLayout;
    @BindView(R.id.my_speak_view_pager)
    ViewPager mMySpeakViewPager;

    private List<View> views;
    private List<String> titles;

    private int speakIndex = 1;
    private int replyIndex = 1;
    private boolean speak;
    private boolean reply;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private SenderRLAdapter mSenderRLAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_speak);
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.MYSPEAK)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("fenyeid", speakIndex)
                .execute(new StringDialogCallback(MySpeakActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyNoteBean myNoteBean = new Gson().fromJson(s, MyNoteBean.class);
                        List<MyNoteBean.MySpeakBean> mySpeak = myNoteBean.mySpeak;
                        initSpeakRecyclerView(mySpeak);
                        OkGo
                                .post(AppUrl.MYREPLY)
                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                .params("fenyeid", replyIndex)
                                .execute(new StringDialogCallback(MySpeakActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        MyReplyNoteBean myReplyNoteBean = new Gson().fromJson(s, MyReplyNoteBean.class);
                                        List<MyReplyNoteBean.MySpeakBean> mySpeak = myReplyNoteBean.mySpeak;
                                        initReplyRecyclerView(mySpeak);
                                    }
                                });
                    }
                });

    }

    private void initReplyRecyclerView(final List<MyReplyNoteBean.MySpeakBean> mySpeak) {
        mRecyclerView2 = new RecyclerView(this);
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mSenderRLAdapter = new SenderRLAdapter(R.layout.item_sender, mySpeak);
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
                                List<MyReplyNoteBean.MySpeakBean> mySpeak = myReplyNoteBean.mySpeak;
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
        mSenderRLAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int bbsid = mySpeak.get(position).BBSId;
                Intent intent = new Intent(MySpeakActivity.this, NoticeDetailActivity.class);
                intent.putExtra("bbsid", bbsid + "");
                startActivity(intent);
            }
        });


        initTabLayout();
        initViewPager();

    }

    private void initSpeakRecyclerView(final List<MyNoteBean.MySpeakBean> mySpeak) {
        mRecyclerView = new RecyclerView(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MySpeakNoteRVAdapter mySpeakNoteRVAdapter = new MySpeakNoteRVAdapter(R.layout.item_fans_post, mySpeak);
        mRecyclerView.setAdapter(mySpeakNoteRVAdapter);
        mySpeakNoteRVAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
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
                                    mySpeakNoteRVAdapter.loadMoreEnd();

                                } else if (mySpeak.size() == 0) {
                                    mySpeakNoteRVAdapter.loadMoreEnd();
                                } else {
                                    mySpeakNoteRVAdapter.addData(mySpeak);
                                    mySpeakNoteRVAdapter.loadMoreComplete();
                                }
                            }
                        });
            }
        }, mRecyclerView);
        mySpeakNoteRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String bbsid = mySpeak.get(position).tid;
                Intent intent = new Intent(MySpeakActivity.this, NoticeDetailActivity.class);
                intent.putExtra("bbsid", bbsid + "");
                startActivity(intent);
            }
        });

    }

    private void initTabLayout() {
        mMySpeakTabLayout.setupWithViewPager(mMySpeakViewPager);
    }

    private void initViewPager() {
        views = new ArrayList<>();
        views.add(mRecyclerView);
        views.add(mRecyclerView2);
        titles = new ArrayList<>();
        titles.add("我的帖子");
        titles.add("我的回帖");
        mMySpeakViewPager.setAdapter(new MySpeakVPAdapter(views, titles));

    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的发言");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
