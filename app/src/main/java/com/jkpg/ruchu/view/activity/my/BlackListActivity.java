package com.jkpg.ruchu.view.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.BlackListRVAdapter;
import com.lzy.okgo.OkGo;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/10/24.
 */

public class BlackListActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("黑名单");


        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }
        });

        TIMFriendshipManagerExt.getInstance().getBlackList(new TIMValueCallBack<List<String>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<String> strings) {
                //获取用户资料
                TIMFriendshipManager.getInstance().getUsersProfile(strings, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int code, String desc) {
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code列表请参见错误码表
                        LogUtils.e("getUsersProfile failed: " + code + " desc");
                        mRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> result) {
                        LogUtils.e("getUsersProfile succ " + result.size());
                        for (TIMUserProfile res : result) {
                            LogUtils.e("identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
                                    + " remark: " + res.getFaceUrl());
                        }

                        initRecyclerView(result);


                        mRefreshLayout.setRefreshing(false);
                        mRefreshLayout.setEnabled(false);
                    }
                });

            }
        });
    }

    private void initRecyclerView(final List<TIMUserProfile> result) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BlackListRVAdapter blackListRVAdapter = new BlackListRVAdapter(R.layout.item_black_list, result);
        mRecyclerView.setAdapter(blackListRVAdapter);
        blackListRVAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                final String identifier = result.get(position).getIdentifier();
                ArrayList<String> identifiers = new ArrayList<>();
                identifiers.add(identifier);
                TIMFriendshipManagerExt.getInstance().delBlackList(identifiers, new TIMValueCallBack<List<TIMFriendResult>>() {
                    @Override
                    public void onError(int i, String s) {
                        ToastUtils.showShort(UIUtils.getContext(), "解除黑名单失败");
                    }

                    @Override
                    public void onSuccess(List<TIMFriendResult> timFriendResults) {
                        ToastUtils.showShort(UIUtils.getContext(), "解除黑名单成功");
                        blackListRVAdapter.remove(position);
                        blackListRVAdapter.notifyDataSetChanged();

                        OkGo
                                .post(AppUrl.ADDHEIMINGDAN)
                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID,""))
                                .params("othernameid",identifier)
                                .params("flag",0)
                                .execute(new StringDialogCallback(BlackListActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {

                                    }
                                });
                    }
                });
            }
        });
        View inflate = View.inflate(UIUtils.getContext(), R.layout.view_no_data, null);
        ((TextView) inflate.findViewById(R.id.no_data_text)).setText("黑名单里还没有人哦!");
        blackListRVAdapter.setEmptyView(inflate);
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
