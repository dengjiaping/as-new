package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.ShareBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.SpannableBuilder;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/9/29.
 */

public class InvitationActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.invitation_tv0)
    TextView mInvitationTv0;
    @BindView(R.id.invitation_tv1)
    TextView mInvitationTv1;
    @BindView(R.id.invitation_btn)
    Button mInvitationBtn;
    @BindView(R.id.invitation_root)
    LinearLayout mInvitationRoot;
    private BottomSheetDialog mPopupWindow;
    private String mUrl;
    private String mContent;
    private String mContent2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mHeaderTvTitle.setText("邀请有礼");
        initTV();

        OkGo
                .post(AppUrl.DOWNLOAD)
                .execute(new StringDialogCallback(InvitationActivity.this) {


                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ShareBean shareBean = new Gson().fromJson(s, ShareBean.class);
//                        mUrl = shareBean.url;
                        mContent = shareBean.content;
                        mContent2 = shareBean.content2;
                    }
                });
        OkGo
                .post(AppUrl.GETFENXIANGDAY)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID,""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ShareBean shareBean = new Gson().fromJson(s, ShareBean.class);
                        mUrl = shareBean.url;
                        String day = shareBean.day;
                        mInvitationTv0.setText(SpannableBuilder.create(InvitationActivity.this)
                                .append("累计获得会员 ", R.dimen.sp16, R.color.colorGray3)
                                .append(day, R.dimen.sp20, R.color.colorPink)
                                .append(" 天    邀请详情 >", R.dimen.sp16, R.color.colorGray3)
                                .build());
                    }
                });



    }

    private void initTV() {
        mInvitationTv0.setText(SpannableBuilder.create(this)
                .append("累计获得会员 ", R.dimen.sp16, R.color.colorGray3)
                .append("0", R.dimen.sp20, R.color.colorPink)
                .append(" 天    邀请详情 >", R.dimen.sp16, R.color.colorGray3)
                .build());

    }

    @OnClick({R.id.header_iv_left, R.id.invitation_btn, R.id.invitation_tv0})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.invitation_btn:
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(),Constants.USERID,""))){
                    ToastUtils.showShort(UIUtils.getContext(),"你需要先登录才能继续本操作");
                    startActivity(new Intent(InvitationActivity.this,LoginActivity.class));
                } else {
                    showInviteFriend();
                }
                break;
            case R.id.invitation_tv0:
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(),Constants.USERID,""))){
                    startActivity(new Intent(InvitationActivity.this,LoginActivity.class));
                } else {
                    startActivity(new Intent(InvitationActivity.this,InvitationDetailActivity.class));
                }
                break;
        }
    }
    private void showInviteFriend() {

        View view = View.inflate(UIUtils.getContext(), R.layout.view_share, null);
//        mPopupWindow = new PopupWindow(this);
        mPopupWindow = new BottomSheetDialog(InvitationActivity.this);
        mPopupWindow.setContentView(view);
        mPopupWindow.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mPopupWindow.show();
        view.findViewById(R.id.view_share_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        final UMWeb mWeb = new UMWeb(mUrl);
        mWeb.setTitle(mContent);//标题
        mWeb.setThumb(new UMImage(UIUtils.getContext(), R.drawable.ic_logo));  //缩略图
        mWeb.setDescription(mContent2);//描述
        view.findViewById(R.id.share_qq).setVisibility(View.GONE);
        view.findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(InvitationActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(mWeb)
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();

            }
        });
        view.findViewById(R.id.share_wxq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(InvitationActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(mWeb)
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

            //   Toast.makeText(MySetUpActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            mPopupWindow.dismiss();

            Snackbar.make(mInvitationRoot, "分享成功啦", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Snackbar.make(mInvitationRoot, "分享失败啦", Snackbar.LENGTH_SHORT).show();

//            Toast.makeText(MySetUpActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                LogUtils.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            // Toast.makeText(MySetUpActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            Snackbar.make(mInvitationRoot, "分享取消了", Snackbar.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.message.equals("Login") || event.message.equals("Quit")) {
            MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OkGo
                            .post(AppUrl.GETFENXIANGDAY)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID,""))
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    ShareBean shareBean = new Gson().fromJson(s, ShareBean.class);
                                    mUrl = shareBean.url;
                                    String day = shareBean.day;
                                    mInvitationTv0.setText(SpannableBuilder.create(InvitationActivity.this)
                                            .append("累计获得会员 ", R.dimen.sp16, R.color.colorGray3)
                                            .append(day, R.dimen.sp20, R.color.colorPink)
                                            .append(" 天    邀请详情 >", R.dimen.sp16, R.color.colorGray3)
                                            .build());
                                }
                            });

                }
            }, 500);
        }
    }
}
