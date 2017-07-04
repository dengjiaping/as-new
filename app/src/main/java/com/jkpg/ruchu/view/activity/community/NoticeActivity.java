package com.jkpg.ruchu.view.activity.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.NoticeBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.NoticRVAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/6.
 */

public class NoticeActivity extends AppCompatActivity {
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

    private List<NoticeBean> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new NoticeBean("鼓励发言", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢"));
        data.add(new NoticeBean("鼓励发言", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢"));
        data.add(new NoticeBean("圈规在先", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢"));
        data.add(new NoticeBean("圈规在先", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢"));
        mNoticeIvClose.setVisibility(View.GONE);
        mNoticeRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mNoticeRecyclerView.setAdapter(new NoticRVAdapter(R.layout.item_notice, data));
    }

    private void initHeader() {
        mHeaderTvTitle.setText("公告详情");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
