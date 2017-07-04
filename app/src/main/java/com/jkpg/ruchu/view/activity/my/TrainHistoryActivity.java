package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.view.adapter.TrainHistoryAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/27.
 */

public class TrainHistoryActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.history_recycler_view)
    RecyclerView mHistoryRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_history);
        ButterKnife.bind(this);
        initHeader();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mHistoryRecyclerView.setAdapter(new TrainHistoryAdapter());
    }

    private void initHeader() {
        mHeaderTvTitle.setText("训练历史");
        mHeaderIvRight.setImageResource(R.drawable.icon_data);
    }

    @OnClick({R.id.header_iv_left, R.id.header_iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right:
                startActivity(new Intent(TrainHistoryActivity.this,TrainCountActivity.class));
                break;
        }
    }
}
