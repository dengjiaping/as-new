package com.jkpg.ruchu.view.activity.train;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.utils.PopupWindowUtils;

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
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    PermissionUtils.requestPermissions(TrainPrepareActivity.this, 222, new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionUtils.OnPermissionListener() {
//                        @Override
//                        public void onPermissionGranted() {
//                            startActivity(new Intent(TrainPrepareActivity.this, StartTrainActivity2.class));
//                            finish();
//                        }
//
//                        @Override
//                        public void onPermissionDenied(String[] deniedPermissions) {
//                            ToastUtils.showShort(UIUtils.getContext(), "您拒绝了,就不能来电暂停了哦,如需要,请到设置应用信息中打开.");
//                            startActivity(new Intent(TrainPrepareActivity.this, StartTrainActivity2.class));
//                            finish();
//                        }
//                    });
//                } else {
                    startActivity(new Intent(TrainPrepareActivity.this, StartTrainActivity2.class));
                    finish();
//                }

                break;
            case R.id.train_prepare_tip:
                final PopupWindow popupWindow = new PopupWindow(TrainPrepareActivity.this);
                popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                View inflate = View.inflate(TrainPrepareActivity.this, R.layout.view_show_tip, null);
                inflate.findViewById(R.id.view_1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                inflate.findViewById(R.id.view_0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setContentView(inflate);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_train_prepare, null), Gravity.CENTER, 0, 0);
                PopupWindowUtils.darkenBackground(TrainPrepareActivity.this, .4f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtils.darkenBackground(TrainPrepareActivity.this, 1f);
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        PermissionUtils.onRequestPermissionsResult(TrainPrepareActivity.this, 222, new String[]{Manifest.permission.READ_PHONE_STATE});
    }
}
