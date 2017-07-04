package com.jkpg.ruchu.view.activity.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.FansRLAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/8.
 */

public class HotNoteActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.hot_note_recycler_view)
    RecyclerView mHotNoteRecyclerView;
    @BindView(R.id.hot_note_refresh_layout)
    SwipeRefreshLayout mHotNoteRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_note);
        ButterKnife.bind(this);
        initHeader();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mHotNoteRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mHotNoteRecyclerView.setAdapter(new FansRLAdapter());
    }

    private void initHeader() {
        mHeaderTvTitle.setText("热门帖子");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
