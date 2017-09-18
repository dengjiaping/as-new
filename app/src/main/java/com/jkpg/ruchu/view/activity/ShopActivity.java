package com.jkpg.ruchu.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.YouZanBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PopupWindowUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.lzy.okgo.OkGo;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.youzan.sdk.YouzanHybrid;
import com.youzan.sdk.YouzanSDK;
import com.youzan.sdk.YouzanToken;
import com.youzan.sdk.event.AbsAuthEvent;
import com.youzan.sdk.event.AbsChooserEvent;
import com.youzan.sdk.event.AbsShareEvent;
import com.youzan.sdk.event.AbsStateEvent;
import com.youzan.sdk.model.goods.GoodsShareModel;
import com.youzan.sdk.web.plugin.YouzanClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/7/19.
 */

public class ShopActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.view)
    YouzanHybrid mView;
    private static final int CODE_REQUEST_LOGIN = 0x101;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.shop_view)
    LinearLayout mShopView;
    private PopupWindow mPopupWindow;
    private YouzanToken token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        mHeaderIvRight.setVisibility(View.GONE);
        mHeaderIvRight.setClickable(false);

        mHeaderTvTitle.setText("正在拼命加载中···");
        final String url;
        if (StringUtils.isEmpty(getIntent().getStringExtra("url"))) {
            url = AppUrl.SHOP;
        } else {
            url = getIntent().getStringExtra("url");
        }

        LogUtils.i("url=" + url);

        //if (mView.isFromInternal()) {
        //  mView.getInternalUrl();
        //} else {
        mView.loadUrl(url);
        //}
        setupYouzanView(mView);

        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mView.reload();
            }
        });
        mView.subscribe(new AbsStateEvent() {
            @Override
            public void call(View view) {
                mRefreshLayout.setRefreshing(false);
                if (mView.getPageType() == YouzanClient.PAGE_TYPE_NATIVE_GOODS) {
                    mHeaderTvTitle.setText("商品");
                } else {
                    mHeaderTvTitle.setText(mView.getTitle());
                }
                if (mView.getPageType() != YouzanClient.PAGE_TYPE_NATIVE_GOODS
                        ) {
                    mHeaderIvRight.setVisibility(View.GONE);
                    mHeaderIvRight.setClickable(false);
                } else {
                    mHeaderIvRight.setVisibility(View.VISIBLE);
                    mHeaderIvRight.setClickable(true);
                }
            }
        });
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
        mHeaderIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.sharePage();
            }
        });
    }


    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        if (!mView.pageGoBack()) {
            super.onBackPressed();
//        } else {
//            finish();
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//        }
        }
    }

    private void setupYouzanView(YouzanClient client) {
        //订阅认证事件
        client.subscribe(new AbsAuthEvent() {
            /**
             * 有赞SDK认证回调.
             * 在加载有赞的页面时, SDK相应会回调该方法.
             *
             * 从自己的服务器上请求同步认证后组装成{@link YouzanToken}, 调用{code view.sync(token);}同步信息.
             *
             * @param view 发起回调的视图
             * @param needLogin 表示当下行为是否需要需要用户角色的认证信息, True需要.
             */
            @Override
            public void call(final View view, boolean needLogin) {
                /**
                 * <pre>
                 *     建议代码逻辑:
                 *
                 *     判断App内的用户是否登录?
                 *       => 已登录: 请求带用户角色的认证信息(login接口);
                 *       => 未登录: needLogin为true, 唤起App内登录界面, 请求带用户角色的认证信息(login接口);
                 *       => 未登录: needLogin为false, 请求不带用户角色的认证信息(initToken接口).
                 *  </pre>
                 */
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    if (needLogin) {
                        startActivity(new Intent(ShopActivity.this, LoginActivity.class));
                    } else {
                        OkGo
                                .post(AppUrl.YOUZANINITTOKEN)
                                .tag(UIUtils.getContext())
//                                .params("client_id", Constants.YZ_CLIENT_ID)
//                                .params("client_secret", Constants.YZ_CLIENT_SECRET)
                                .execute(new StringDialogCallback(ShopActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        YouZanBean youZanBean = new Gson().fromJson(s, YouZanBean.class);
                                        token = new YouzanToken();
                                        token.setAccessToken(youZanBean.data.access_token);
                                        token.setCookieKey(youZanBean.data.cookie_key);
                                        token.setCookieValue(youZanBean.data.cookie_value);
                                    }

                                    @Override
                                    public void onAfter(@Nullable String s, @Nullable Exception e) {
                                        super.onAfter(s, e);
                                        mView.sync(token);
                                    }
                                });
                    }
                } else {
                    OkGo
                            .post(AppUrl.YOUZAN)
                            .tag(UIUtils.getContext())

//                            .params("client_id", Constants.YZ_CLIENT_ID)
//                            .params("client_secret", Constants.YZ_CLIENT_SECRET)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .execute(new StringDialogCallback(ShopActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    YouZanBean youZanBean = new Gson().fromJson(s, YouZanBean.class);
                                    token = new YouzanToken();
                                    token.setAccessToken(youZanBean.data.access_token);
                                    token.setCookieKey(youZanBean.data.cookie_key);
                                    token.setCookieValue(youZanBean.data.cookie_value);
                                }

                                @Override
                                public void onAfter(@Nullable String s, @Nullable Exception e) {
                                    super.onAfter(s, e);
                                    mView.sync(token);
                                }
                            });
                }
            }
        });

        //订阅文件选择事件
        client.subscribe(new AbsChooserEvent() {
            @Override
            public void call(View view, Intent intent, int requestCode) throws ActivityNotFoundException {
                //调用系统图片选择器
                startActivity(intent);
            }
        });

        //订阅分享事件
        client.subscribe(new AbsShareEvent() {
            @Override
            public void call(View view, GoodsShareModel data) {
                /**
                 * 在获取数据后, 可以使用其他分享SDK来提高分享体验.
                 * 这里调用系统分享来简单演示分享的过程.
                 */
                View window = View.inflate(UIUtils.getContext(), R.layout.view_share, null);
                window.findViewById(R.id.view_share_white).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
                mPopupWindow = new PopupWindow(ShopActivity.this);
                mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setContentView(window);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                mPopupWindow.setOutsideTouchable(false);
                mPopupWindow.setFocusable(true);
                mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
                mPopupWindow.showAsDropDown(getLayoutInflater().inflate(R.layout.activity_shop, null), Gravity.BOTTOM, 0, 0);
                PopupWindowUtils.darkenBackground(ShopActivity.this, .5f);
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtils.darkenBackground(ShopActivity.this, 1f);
                    }
                });
                final UMWeb mWeb = new UMWeb(data.getLink());
                mWeb.setTitle(data.getTitle());//标题
                mWeb.setThumb(new UMImage(ShopActivity.this, data.getImgUrl()));  //缩略图
                mWeb.setDescription(data.getDesc());//描述
                window.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ShareAction(ShopActivity.this).setPlatform(SHARE_MEDIA.QQ)
                                .withMedia(mWeb)
                                .setCallback(umShareListener)
                                .share();
                        mPopupWindow.dismiss();
                    }
                });
                window.findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ShareAction(ShopActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                                .withMedia(mWeb)
                                .setCallback(umShareListener)
                                .share();
                        mPopupWindow.dismiss();

                    }
                });
                window.findViewById(R.id.share_wxq).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ShareAction(ShopActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                .withMedia(mWeb)
                                .setCallback(umShareListener)
                                .share();
                        mPopupWindow.dismiss();

                    }
                });
                window.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            /**
             * 用户登录成功返回, 从自己的服务器上请求同步认证后组装成{@link YouzanToken},
             * 调用{code view.sync(token);}同步信息.
             */
            if (CODE_REQUEST_LOGIN == requestCode) {
                mView.sync(token);
            } else {
                //处理文件上传
                mView.receiveFile(requestCode, data);
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (!mView.pageGoBack()) {
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginShop(MessageEvent event) {
        if (event.message.equals("Login")) {
            OkGo
                    .post(AppUrl.YOUZAN)
                    .tag(UIUtils.getContext())
//                    .params("client_id", Constants.YZ_CLIENT_ID)
//                    .params("client_secret", Constants.YZ_CLIENT_SECRET)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .execute(new StringDialogCallback(ShopActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
//                            mView.sync(token);
                            YouZanBean youZanBean = new Gson().fromJson(s, YouZanBean.class);
                            token = new YouzanToken();
                            token.setAccessToken(youZanBean.data.access_token);
                            token.setCookieKey(youZanBean.data.cookie_key);
                            token.setCookieValue(youZanBean.data.cookie_value);
                        }

                        @Override
                        public void onAfter(@Nullable String s, @Nullable Exception e) {
                            super.onAfter(s, e);
                            mView.sync(token);
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        YouzanSDK.userLogout(this);
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

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

//            Toast.makeText(MySetUpActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                LogUtils.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            // Toast.makeText(MySetUpActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


}
