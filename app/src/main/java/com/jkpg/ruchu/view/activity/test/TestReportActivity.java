package com.jkpg.ruchu.view.activity.test;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.TestResultBean;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initData() {
        TestResultBean testResultBean = getIntent().getParcelableExtra("testResultBean");
        mReportTvGrade.setText(testResultBean.count + " 分");
        mReportTvPlan.setText("产后康复  【" + testResultBean.level + "】");
        mReportTvName.setText(testResultBean.usernick);
        mReportTvAge.setText(testResultBean.age + " 岁");
        mReportTvHeight.setText(testResultBean.height + " cm");
        mReportTvWeight.setText(testResultBean.weight + " kg");
        mReportTvBody.setText(testResultBean.content);
        mReportTvTime.setText(testResultBean.testtime);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("完整报告");
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
    }


    @OnClick({R.id.header_iv_left, R.id.header_iv_right})
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
//                        mReportCode.setVisibility(View.VISIBLE);

                            Bitmap bitmap = getBitmapByView(mRegisterScrollView);
                            String pic = FileUtils.saveBitmap(bitmap);
               /* Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/jpeg");
                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pic));
                startActivity(Intent.createChooser(imageIntent, "分享我的报告"));*/
                            showShare(bitmap);
                        }

                        @Override
                        public void onPermissionDenied(String[] deniedPermissions) {

                        }
                    });
                } else {

                    Bitmap bitmap = getBitmapByView(mRegisterScrollView);
                    String pic = FileUtils.saveBitmap(bitmap);
               /* Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/jpeg");
                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pic));
                startActivity(Intent.createChooser(imageIntent, "分享我的报告"));*/
                    showShare(bitmap);
                }

                break;
        }
    }

    private void showShare(Bitmap pic) {
//        Intent share_intent = new Intent();
//        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
//        share_intent.setType("text/plain");//设置分享内容的类型
//        share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享如初");//添加分享内容标题
//        share_intent.putExtra(Intent.EXTRA_TEXT, "www.ruchuapp.com");//添加分享内容
//        //创建分享的Dialog
//        share_intent = Intent.createChooser(share_intent, "分享如初");
//        startActivity(share_intent);
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
//        mPopupWindow = new PopupWindow(this);
//        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
//        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        mPopupWindow.setContentView(view);
//        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
//        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
//        mPopupWindow.showAsDropDown(getLayoutInflater().inflate(R.layout.activity_report, null), Gravity.BOTTOM, 0, 0);
//        PopupWindowUtils.darkenBackground(TestReportActivity.this, .5f);
//        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
////                mReportCode.setVisibility(View.GONE);
//                PopupWindowUtils.darkenBackground(TestReportActivity.this, 1f);
//            }
//        });
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
