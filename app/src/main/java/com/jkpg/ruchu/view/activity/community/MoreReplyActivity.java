package com.jkpg.ruchu.view.activity.community;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jkpg.ruchu.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/8/7.
 */

public class MoreReplyActivity extends AppCompatActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.other_recycler_view)
    RecyclerView mOtherRecyclerView;
    private String mReplyid;
    private String mBbsid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_train);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {


    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
