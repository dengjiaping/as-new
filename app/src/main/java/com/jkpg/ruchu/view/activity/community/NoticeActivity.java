package com.jkpg.ruchu.view.activity.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.NoticeBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.NoticeDetailRVAdapter;
import com.lzy.okgo.OkGo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/6/6.
 */

public class NoticeActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.notice_iv_close)
    ImageView mNoticeIvClose;
    @BindView(R.id.notice_recycler_view)
    RecyclerView mNoticeRecyclerView;
    @BindView(R.id.notice_tv_introduce)
    TextView mNoticeTvIntroduce;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        initHeader();
        String plateid = getIntent().getStringExtra("plateid");
        initData(plateid);
    }

    private void initData(String plateid) {
        OkGo
                .post(AppUrl.BBS_NOTICEDETAIL)
                .params("plateid", plateid)
                .execute(new StringDialogCallback(NoticeActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        NoticeBean noticeBean = new Gson().fromJson(s, NoticeBean.class);
                        List<NoticeBean.ListBean> list = noticeBean.list;
                        initRecyclerView(list);
                        mNoticeTvIntroduce.setText(noticeBean.json.platename);
                    }
                });

    }

    private void initRecyclerView(List<NoticeBean.ListBean> list) {
        mNoticeIvClose.setVisibility(View.GONE);
        mNoticeRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mNoticeRecyclerView.setAdapter(new NoticeDetailRVAdapter(R.layout.item_notice, list));
    }

    private void initHeader() {
        mHeaderTvTitle.setText("公告详情");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
