package com.jkpg.ruchu.view.activity.train;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.RewardDetailBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.HtmlActivity;
import com.jkpg.ruchu.view.activity.ShopActivity;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.RewardDetailsRVAdapter;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/12/2.
 */

public class LookRewardDetailsActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.reward_detail_recycler_view)
    RecyclerView mRewardDetailRecyclerView;
    private List<RewardDetailBean.ListBean> mList;
    private RewardDetailsRVAdapter mRewardDetailsRVAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("查看详情");
        mList = new ArrayList<>();
        mRewardDetailRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mRewardDetailsRVAdapter = new RewardDetailsRVAdapter(R.layout.item_reward_detail, mList);
        mRewardDetailRecyclerView.setAdapter(mRewardDetailsRVAdapter);
        mRewardDetailsRVAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                //   0去领取 2未使用 3已使用 4已失效 1未完成
                switch (mList.get(position).useflag) {
                    case "0":
                        Intent intent1 = new Intent(LookRewardDetailsActivity.this, HtmlActivity.class);
                        intent1.putExtra("URL", mList.get(position).gifturl);
                        startActivity(intent1);
                        break;
                    case "1":
                        break;
                    case "2":
                        Intent intent = new Intent(LookRewardDetailsActivity.this, ShopActivity.class);
                        intent.putExtra("url", mList.get(position).gifturl);
                        startActivity(intent);
                        break;
                    case "3":
                        break;
                    case "4":
                        break;
                    case "5":
                        new AlertDialog.Builder(LookRewardDetailsActivity.this)
                                .setMessage("关注并私信如初福利社领取奖励!")
                                .setNegativeButton("稍后领取", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("立即领取", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(LookRewardDetailsActivity.this, FansCenterActivity.class);
                                        i.putExtra("fansId", mList.get(position).userid);
                                        startActivity(i);
                                    }
                                })
                                .show();

                        break;
                }
            }
        });
    }

    private void initRecyclerView() {
        OkGo
                .post(AppUrl.GETGIFTLIST)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(LookRewardDetailsActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        RewardDetailBean rewardDetailBean = new Gson().fromJson(s, RewardDetailBean.class);
                        mList.clear();
                        mList.addAll(rewardDetailBean.list);
                        mRewardDetailsRVAdapter.notifyDataSetChanged();
                    }
                });
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
    }
}
