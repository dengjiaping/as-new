package com.jkpg.ruchu.view.activity.test;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.IsVipBean;
import com.jkpg.ruchu.bean.TestResultBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.my.OpenVipActivity;
import com.jkpg.ruchu.view.activity.train.TrainPrepareActivity;
import com.lzy.okgo.OkGo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.jkpg.ruchu.utils.ScreenUtils.getBitmapByView;

/**
 * Created by qindi on 2017/5/20.
 */

public class TestReportActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.report_tv_grade)
    TextView mReportTvGrade;
    @BindView(R.id.report_tv_plan)
    TextView mReportTvPlan;
    @BindView(R.id.report_tv_name)
    TextView mReportTvName;
    @BindView(R.id.report_tv_height)
    TextView mReportTvHeight;
    @BindView(R.id.report_tv_age)
    TextView mReportTvAge;
    @BindView(R.id.report_tv_weight)
    TextView mReportTvWeight;
    @BindView(R.id.report_tv_body)
    TextView mReportTvBody;
    @BindView(R.id.report_tv_time)
    TextView mReportTvTime;
    @BindView(R.id.register_scroll_view)
    ScrollView mRegisterScrollView;
    @BindView(R.id.register_view)
    LinearLayout mRegisterView;
    @BindView(R.id.report_code)
    LinearLayout mReportCode;
    private BottomSheetDialog mPopupWindow;
    private TestResultBean mTestResultBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initData() {
        mTestResultBean = getIntent().getParcelableExtra("testResultBean");
        mReportTvGrade.setText(mTestResultBean.count + " 分");
        mReportTvPlan.setText("产后康复  【" + mTestResultBean.level + "】");
        mReportTvName.setText(mTestResultBean.usernick);
        mReportTvAge.setText(mTestResultBean.age + " 岁");
        mReportTvHeight.setText(mTestResultBean.height + " cm");
        mReportTvWeight.setText(mTestResultBean.weight + " kg");
        mReportTvBody.setText(mTestResultBean.content);
        mReportTvTime.setText(mTestResultBean.testtime);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("完整报告");
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
    }


    @OnClick({R.id.header_iv_left, R.id.header_iv_right,R.id.result_btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    PermissionUtils.requestPermissions(this, 200, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                        @Override
                        public void onPermissionGranted() {

                            Bitmap bitmap = getBitmapByView(mRegisterScrollView);
                             FileUtils.saveBitmap(bitmap);
                            showShare(bitmap);
                        }

                        @Override
                        public void onPermissionDenied(String[] deniedPermissions) {

                        }
                    });
                } else {

                    Bitmap bitmap = getBitmapByView(mRegisterScrollView);
                    FileUtils.saveBitmap(bitmap);
                    showShare(bitmap);
                }

                break;
            case R.id.result_btn_start:
                OkGo
                        .post(AppUrl.SELECTISVIP)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringDialogCallback(TestReportActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                IsVipBean isVipBean = new Gson().fromJson(s, IsVipBean.class);
                                if (isVipBean.isVIP) {
                                    if (mTestResultBean.ischange) {
                                        new AlertDialog.Builder(TestReportActivity.this)
                                                .setMessage("开始训练后，您的当前难度将被调至【" + mTestResultBean.level + "】")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        OkGo
                                                                .post(AppUrl.OPENMYPRACTICE)
                                                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                                                .params("level", ((mTestResultBean.levelid) + ""))
                                                                .execute(new StringDialogCallback(TestReportActivity.this) {
                                                                    @Override
                                                                    public void onSuccess(String s, Call call, Response response) {
                                                                        EventBus.getDefault().post("TrainFragment");
//                                                                    initData();
                                                                        startActivity(new Intent(TestReportActivity.this, TrainPrepareActivity.class));
                                                                        finish();
                                                                    }
                                                                });

                                                    }
                                                })
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(TestReportActivity.this, TrainPrepareActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        startActivity(new Intent(TestReportActivity.this, TrainPrepareActivity.class));
                                        finish();
                                    }

                                } else {
                                    new AlertDialog.Builder(TestReportActivity.this)
                                            .setMessage("只有会员才能训练哦")
                                            .setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(TestReportActivity.this, OpenVipActivity.class));
                                                }
                                            })
                                            .setNegativeButton("放弃训练", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            }
                        });
                break;
        }
    }

    private void showShare(Bitmap pic) {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_share, null);
        view.findViewById(R.id.view_share_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new BottomSheetDialog(TestReportActivity.this);
        mPopupWindow.setContentView(view);
        mPopupWindow.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mPopupWindow.show();
        final UMImage image = new UMImage(TestReportActivity.this, pic);
        view.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(TestReportActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(image)
                        .withText("分享我的报告")
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(TestReportActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(image)
                        .withText("分享我的报告")
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();

            }
        });
        view.findViewById(R.id.share_wxq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(TestReportActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(image)
                        .withText("分享我的报告")
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();

            }
        });
        view.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtils.d("plat", "platform" + platform);

//            Toast.makeText(TestReportActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            mPopupWindow.dismiss();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

//            Toast.makeText(TestReportActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                LogUtils.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(TestReportActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this, 200, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }
}
