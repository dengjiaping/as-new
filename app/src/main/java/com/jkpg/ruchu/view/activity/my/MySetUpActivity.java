package com.jkpg.ruchu.view.activity.my;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.PopupWindowUtils;
import com.jkpg.ruchu.utils.UIUtils;
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

/**
 * Created by qindi on 2017/5/24.
 */

public class MySetUpActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.center_iv_vip)
    ImageView mCenterIvVip;
    @BindView(R.id.my_set_up_tv_wx)
    TextView mMySetUpTvWx;
    @BindView(R.id.my_set_up_tv_wx_bd)
    TextView mMySetUpTvWxBd;
    @BindView(R.id.my_set_up_tv_account_manage)
    TextView mMySetUpTvAccountManage;
    @BindView(R.id.my_set_up_tv_news)
    TextView mMySetUpTvNews;
    @BindView(R.id.my_set_up_tv_about)
    TextView mMySetUpTvAbout;
    @BindView(R.id.my_set_up_tv_friend)
    TextView mMySetUpTvFriend;
    @BindView(R.id.my_set_up_tv_question)
    TextView mMySetUpTvQuestion;
    @BindView(R.id.set_up_root)
    LinearLayout mSetUpRoot;
    private PopupWindow mPopupWindow;
    private UMWeb mWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_set_up);
        ButterKnife.bind(this);
        initHeader();


    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的设置");
    }

    @OnClick({R.id.header_iv_left, R.id.my_set_up_tv_wx_bd, R.id.my_set_up_tv_account_manage, R.id.my_set_up_tv_news, R.id.my_set_up_tv_about, R.id.my_set_up_tv_friend, R.id.my_set_up_tv_question})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.my_set_up_tv_wx_bd:
                break;
            case R.id.my_set_up_tv_account_manage:
                startActivity(new Intent(MySetUpActivity.this, AccountManagementActivity.class));

                break;
            case R.id.my_set_up_tv_news:
                startActivity(new Intent(MySetUpActivity.this, NewsSendActivity.class));

                break;
            case R.id.my_set_up_tv_about:
                startActivity(new Intent(MySetUpActivity.this, SetUpAboutActivity.class));
                break;
            case R.id.my_set_up_tv_friend:
                showInviteFriend();
                break;
            case R.id.my_set_up_tv_question:
                startActivity(new Intent(MySetUpActivity.this, QuestionFeedbackActivity.class));

                break;
        }
    }

    private void showInviteFriend() {

//        Intent share_intent = new Intent();
//        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
//        share_intent.setType("text/plain");//设置分享内容的类型
//        share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享如初");//添加分享内容标题
//        share_intent.putExtra(Intent.EXTRA_TEXT, "www.ruchuapp.com");//添加分享内容
//        //创建分享的Dialog
//        share_intent = Intent.createChooser(share_intent, "分享如初");
//        startActivity(share_intent);
        View view = View.inflate(UIUtils.getContext(), R.layout.view_share, null);
        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(view);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        mPopupWindow.showAsDropDown(getLayoutInflater().inflate(R.layout.activity_my_set_up, null), Gravity.BOTTOM, 0, 0);
        PopupWindowUtils.darkenBackground(MySetUpActivity.this, .5f);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopupWindowUtils.darkenBackground(MySetUpActivity.this, 1f);
            }
        });
        mWeb = new UMWeb(getString(R.string.share_url));
        mWeb.setTitle(getString(R.string.share_title));//标题
        mWeb.setThumb(new UMImage(UIUtils.getContext(), R.drawable.logo));  //缩略图
        mWeb.setDescription(getString(R.string.share_description));//描述
        view.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(MySetUpActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(mWeb)
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(MySetUpActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(mWeb)
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();

            }
        });
        view.findViewById(R.id.share_wxq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(MySetUpActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
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

            Snackbar.make(mSetUpRoot, "分享成功啦", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Snackbar.make(mSetUpRoot, "分享失败啦", Snackbar.LENGTH_SHORT).show();

//            Toast.makeText(MySetUpActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                LogUtils.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            // Toast.makeText(MySetUpActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            Snackbar.make(mSetUpRoot, "分享取消了", Snackbar.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.message.equals("Quit"))
            finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}