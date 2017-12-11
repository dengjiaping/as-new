package com.jkpg.ruchu.view.activity.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/7/13.
 */

public class TrainPrepareActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_prepare);
        ButterKnife.bind(this);

        mHeaderTvTitle.setText("训练准备");
    }

    @OnClick({R.id.header_iv_left, R.id.train_prepare_btn, R.id.train_prepare_tip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.train_prepare_btn:
                    startActivity(new Intent(TrainPrepareActivity.this, StartTrainActivity2.class));
                    finish();
                break;
            case R.id.train_prepare_tip:

                View inflate = View.inflate(TrainPrepareActivity.this, R.layout.view_show_tip, null);
                final AlertDialog show = new AlertDialog.Builder(TrainPrepareActivity.this, R.style.dialog_invitation)
                        .setView(inflate)
                        .show();
                inflate.findViewById(R.id.scrollView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        show.dismiss();
                    }
                });
                break;
        }
    }

}
