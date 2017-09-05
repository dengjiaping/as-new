package com.jkpg.ruchu.view.activity.train;

import android.content.DialogInterface;
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
import com.jkpg.ruchu.bean.MyTrainBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.MyTrainRVAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/18.
 */

public class MyTrainActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_train_recycler_view)
    RecyclerView mMyTrainRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("全部难度" +
                "");
        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.OPENMYPRACTICE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("level", "")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .cacheKey("OPENMYPRACTICE")
                .execute(new StringDialogCallback(MyTrainActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyTrainBean myTrainBean = new Gson().fromJson(s, MyTrainBean.class);
                        List<MyTrainBean.ListBean> list = myTrainBean.list;
                        list.get(0).backgroundRes = R.drawable.grade_1;
                        list.get(1).backgroundRes = R.drawable.grade_2;
                        list.get(2).backgroundRes = R.drawable.grade_3;
                        list.get(3).backgroundRes = R.drawable.grade_4;
                        list.get(4).backgroundRes = R.drawable.grade_5;

                        int i = Integer.parseInt(myTrainBean.ulevel) - 1;
                        list.get(i).isSelect = true;
                        initRecyclerView(list);

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        MyTrainBean myTrainBean = new Gson().fromJson(s, MyTrainBean.class);
                        List<MyTrainBean.ListBean> list = myTrainBean.list;
                        list.get(0).backgroundRes = R.drawable.grade_1;
                        list.get(1).backgroundRes = R.drawable.grade_2;
                        list.get(2).backgroundRes = R.drawable.grade_3;
                        list.get(3).backgroundRes = R.drawable.grade_4;
                        list.get(4).backgroundRes = R.drawable.grade_5;

                        int i = Integer.parseInt(myTrainBean.ulevel) - 1;
                        list.get(i).isSelect = true;
                        initRecyclerView(list);
                    }
                });
    }

    private void initRecyclerView(final List<MyTrainBean.ListBean> list) {
        mMyTrainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MyTrainRVAdapter myTrainRVAdapter = new MyTrainRVAdapter(R.layout.item_my_train, list);
        mMyTrainRecyclerView.setAdapter(myTrainRVAdapter);
        myTrainRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, final int position) {
                new AlertDialog.Builder(MyTrainActivity.this)
                        .setMessage(list.get(position).introduction + "\n\n建议强度: " + list.get(position).advise)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("启用", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OkGo
                                        .post(AppUrl.OPENMYPRACTICE)
                                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                        .params("level", (position + 1 + ""))
                                        .execute(new StringDialogCallback(MyTrainActivity.this) {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                for (int i = 0; i < 5; i++) {
                                                    list.get(i).isSelect = false;
                                                }
                                                list.get(position).isSelect = true;
                                                myTrainRVAdapter.notifyDataSetChanged();
                                                EventBus.getDefault().post("TrainFragment");
                                            }
                                        });
                            }
                        })
                        .setTitle("要启用难度 " + (position + 1) + " 训练吗？")
                        .show();
            }
        });
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
