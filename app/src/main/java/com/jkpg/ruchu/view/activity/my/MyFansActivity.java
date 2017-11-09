package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.FansBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.MyFansRVAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by qindi on 2017/5/25.
 */

public class MyFansActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_fans_recyclerView)
    RecyclerView mMyFansRecyclerView;

    private int fansIndex = 1;
    private Map<Integer, String> mMap = new HashMap<>();
    private int mFlag;
    private List<FansBean.list> mList;
    private MyFansRVAdapter mFansRVAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        ButterKnife.bind(this);
        initHeader();
        initData();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initData() {
        OkGo
                .post(AppUrl.FANS)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("flag", mFlag)
                .params("fenyeid", fansIndex)
                .execute(new StringDialogCallback(MyFansActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        FansBean fansBean = new Gson().fromJson(s, FansBean.class);
                        mList = fansBean.list;
                        initRecyclerView();
                        for (int i = 0; i < mList.size(); i++) {
                            mMap.put(i, mList.get(i).flag);
                        }

                    }
                });
    }

    private void initRecyclerView() {
        mMyFansRecyclerView.removeAllViews();
        mMyFansRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mFansRVAdapter = new MyFansRVAdapter(R.layout.item_fans, mList, mMap);
        mMyFansRecyclerView.setAdapter(mFansRVAdapter);
        mFansRVAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                final CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    OkGo
                            .post(AppUrl.CANCLEATT)
                            .params("fansid", mList.get(position).userid)
                            .params("myuserid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .execute(new StringDialogCallback(MyFansActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                    if (!successBean.success) {
                                        ToastUtils.showShort(UIUtils.getContext(), "取消关注失败");
                                        checkBox.setText("已关注");
                                        mMap.put(position, "1");
                                    } else {
                                        checkBox.setText("加关注");
                                        mMap.put(position, "0");
                                        EventBus.getDefault().post(new MessageEvent("MyFragment"));
                                    }
                                    mFansRVAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    ToastUtils.showShort(UIUtils.getContext(), "取消关注失败");
                                    checkBox.setText("已关注");
                                    mMap.put(position, "1");
                                    mFansRVAdapter.notifyDataSetChanged();
                                }
                            });
                } else {
                    OkGo
                            .post(AppUrl.ATTENTION)
                            .params("followUserid", mList.get(position).userid)
                            .params("MyUserid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .execute(new StringDialogCallback(MyFansActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                    if (!successBean.success) {
                                        ToastUtils.showShort(UIUtils.getContext(), "关注失败,请重试");
                                        checkBox.setText("加关注");
                                        mMap.put(position, "0");

                                    } else {
                                        if (mFlag == 2){
                                            checkBox.setText("互相关注");
                                            mMap.put(position, "2");

                                        } else {
                                            if (mList.get(position).flag.equals("1")) {
                                                checkBox.setText("已关注");
                                            } else if (mList.get(position).flag.equals("2")) {
                                                checkBox.setText("互相关注");
                                            }
                                            mMap.put(position, mList.get(position).flag);
                                        }
                                        EventBus.getDefault().post(new MessageEvent("MyFragment"));
                                    }
                                    mFansRVAdapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    ToastUtils.showShort(UIUtils.getContext(), "关注失败,请重试");
                                    checkBox.setText("加关注");
                                    mMap.put(position, "0");
                                    mFansRVAdapter.notifyDataSetChanged();
                                }
                            });
                }
                mFansRVAdapter.notifyDataSetChanged();
            }
        });
        mFansRVAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                fansIndex++;
                OkGo
                        .post(AppUrl.FANS)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("flag", mFlag)
                        .params("fenyeid", fansIndex)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                FansBean fansBean = new Gson().fromJson(s, FansBean.class);
                                List<FansBean.list> list = fansBean.list;
                                if (list == null) {
                                    mFansRVAdapter.loadMoreEnd();
                                } else if (list.size() == 0) {
                                    mFansRVAdapter.loadMoreEnd();
                                } else {
                                    for (int i = 0; i < list.size(); i++) {
                                        mMap.put(mMap.size(), list.get(i).flag);
                                    }
                                    mFansRVAdapter.addData(list);
                                    mFansRVAdapter.loadMoreComplete();
                                }
                            }
                        });
                LogUtils.i(fansIndex + "fansIndex");
            }
        }, mMyFansRecyclerView);
        View view = View.inflate(UIUtils.getContext(), R.layout.view_no_data, null);
        TextView textView = (TextView) view.findViewById(R.id.no_data_text);
        if (mFlag == 1) {
            textView.setText("你还没有关注哦!");
        } else {
            textView.setText("你还没有粉丝哦!");
        }
        mFansRVAdapter.setEmptyView(view);

        mFansRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyFansActivity.this, FansCenterActivity.class);
                intent.putExtra("fansId", mList.get(position).userid);
                startActivity(intent);
            }
        });

    }

    private void initHeader() {
        mHeaderTvTitle.setText(getIntent().getStringExtra("title"));
        mFlag = getIntent().getIntExtra("flag", 1);
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(String mess) {
        if (mess.equals("fans")) {
            fansIndex = 1;
            OkGo
                    .post(AppUrl.FANS)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .params("flag", mFlag)
                    .params("fenyeid", fansIndex)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            FansBean fansBean = new Gson().fromJson(s, FansBean.class);
                            mList.clear();
                            mMap.clear();

                            mList.addAll(fansBean.list);
                            mFansRVAdapter.notifyDataSetChanged();
                            for (int i = 0; i < mList.size(); i++) {
                                mMap.put(i, mList.get(i).flag);
                            }
                        }

                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
