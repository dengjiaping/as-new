package com.jkpg.ruchu.view.activity.train;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/9/4.
 */

public class FullActivity extends BaseActivity {
    @BindView(R.id.tip_close)
    ImageView mTipClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.view_show_header);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        getWindow().setExitTransition(explode);
        ButterKnife.bind(this);
        mTipClose.setVisibility(View.GONE);
    }
}
