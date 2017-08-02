package com.jkpg.ruchu.view.activity.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.VideoBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.NewRLAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/18.
 */

public class NewTrainActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.other_recycler_view)
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_train);
        ButterKnife.bind(this);
        initData();
        initHeader();
    }

    private void initData() {

        OkGo
                .post(AppUrl.NEWHANDVEDIO)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("type", 1)
                .execute(new StringDialogCallback(NewTrainActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.i("NEWHANDVEDIO" + s);
                        VideoBean videoBean = new Gson().fromJson(s, VideoBean.class);
                        List<VideoBean.VideoMSBean> vedioMS = videoBean.videoMS;
                        initRecycleView(vedioMS);
                    }
                });

    }

    private void initRecycleView(List<VideoBean.VideoMSBean> vedioMS) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mRecyclerView.setHasFixedSize(true);
        NewRLAdapter newRLAdapter = new NewRLAdapter(NewTrainActivity.this, vedioMS);
        mRecyclerView.setAdapter(newRLAdapter);
        newRLAdapter.setOnItemClickListener(new NewRLAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, VideoBean.VideoMSBean.VideomessBean data) {
                Intent intent = new Intent(NewTrainActivity.this, NewVideoDetailActivity.class);
                intent.putExtra("VediomessBean", data);
                startActivity(intent);
                OkGo
                        .post(AppUrl.ISFIRST)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                            }
                        });
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("入门指导");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
