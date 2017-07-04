package com.jkpg.ruchu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/11.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    public ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_left)
    public TextView mHeaderTvLeft;
    @BindView(R.id.header_tv_title)
    public TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right2)
    public ImageView mHeaderIvRight2;
    @BindView(R.id.header_iv_right)
    public ImageView mHeaderIvRight;
    @BindView(R.id.header_tv_right)
    public TextView mHeaderTvRight;
    @BindView(R.id.base_frame_layout)
    public FrameLayout mBaseFrameLayout;
    @BindView(R.id.base_refresh_layout)
    public SwipeRefreshLayout mBaseRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        init();
    }

    public abstract void init();

    public void setView(int layoutResID) {
        mBaseFrameLayout.addView(View.inflate(UIUtils.getContext(), layoutResID, null));
    }

}
