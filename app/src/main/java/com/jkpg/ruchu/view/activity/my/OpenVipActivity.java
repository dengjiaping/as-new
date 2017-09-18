package com.jkpg.ruchu.view.activity.my;

import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.AliPayBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.OpenVipBean;
import com.jkpg.ruchu.bean.PayResult;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.bean.WxPayBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.IpUtils;
import com.jkpg.ruchu.utils.PopupWindowUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class OpenVipActivity extends BaseActivity {

    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.open_vip_ll_wx)
    LinearLayout mOpenVipLlWx;
    @BindView(R.id.open_vip_tv_30)
    TextView mOpenVipTv30;
    @BindView(R.id.open_vip_tv_15)
    TextView mOpenVipTv15;
    @BindView(R.id.open_vip_rl_30)
    RelativeLayout mOpenVipRl30;
    @BindView(R.id.open_vip_tv_90)
    TextView mOpenVipTv90;
    @BindView(R.id.open_vip_tv_45)
    TextView mOpenVipTv45;
    @BindView(R.id.open_vip_rl_90)
    RelativeLayout mOpenVipRl90;
    @BindView(R.id.open_vip_tv_180)
    TextView mOpenVipTv180;
    @BindView(R.id.open_vip_tv_75)
    TextView mOpenVipTv75;
    @BindView(R.id.open_vip_rl_180)
    RelativeLayout mOpenVipRl180;
    @BindView(R.id.open_vip_tv_365)
    TextView mOpenVipTv365;
    @BindView(R.id.open_vip_tv_135)
    TextView mOpenVipTv135;
    @BindView(R.id.open_vip_rl_365)
    RelativeLayout mOpenVipRl365;
    @BindView(R.id.open_vip_wx)
    TextView mOpenVipWx;
    @BindView(R.id.open_vip_tip)
    TextView mOpenVipTip;
    @BindView(R.id.open_vip_btn_pay)
    Button mOpenVipBtnPay;
    @BindView(R.id.open_vip_tv_old_0)
    TextView mOpenVipTvOld0;
    @BindView(R.id.open_vip_tv_old_1)
    TextView mOpenVipTvOld1;
    @BindView(R.id.open_vip_tv_old_2)
    TextView mOpenVipTvOld2;
    @BindView(R.id.open_vip_tv_old_3)
    TextView mOpenVipTvOld3;
    @BindView(R.id.vip_image)
    ImageView mVipImage;
    @BindView(R.id.vip_tip)
    TextView mVipTip;
    private PopupWindow mPopupWindowPay;
    private PopupWindow mPopupWindowPaySuccess;
    String day;
    private OpenVipBean mOpenVipBean;


    private static final int SDK_PAY_FLAG = 1;
    private String cardid;
    private List<TextView> mPriceViews;
    private List<TextView> mTimeViews;
    private List<RelativeLayout> mRLViews;
    private ArrayList<TextView> mOldPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_vip);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("开通会员");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        UMShareAPI.get(OpenVipActivity.this).fetchAuthResultWithBundle(OpenVipActivity.this, savedInstanceState, umAuthListener);

        mPriceViews = new ArrayList<>();
        mPriceViews.add(mOpenVipTv15);
        mPriceViews.add(mOpenVipTv45);
        mPriceViews.add(mOpenVipTv75);
        mPriceViews.add(mOpenVipTv135);
        mTimeViews = new ArrayList<>();
        mTimeViews.add(mOpenVipTv30);
        mTimeViews.add(mOpenVipTv90);
        mTimeViews.add(mOpenVipTv180);
        mTimeViews.add(mOpenVipTv365);
        mRLViews = new ArrayList<>();
        mRLViews.add(mOpenVipRl30);
        mRLViews.add(mOpenVipRl90);
        mRLViews.add(mOpenVipRl180);
        mRLViews.add(mOpenVipRl365);
        mOldPrice = new ArrayList<>();
        mOldPrice.add(mOpenVipTvOld0);
        mOldPrice.add(mOpenVipTvOld1);
        mOldPrice.add(mOpenVipTvOld2);
        mOldPrice.add(mOpenVipTvOld3);
        initData();

    }

    private void initData() {
        OkGo
                .post(AppUrl.VIPCARDSLIST)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(OpenVipActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mOpenVipBean = new Gson().fromJson(s, OpenVipBean.class);
                        if (StringUtils.isEmpty(mOpenVipBean.VIPTime)) {
                            mVipImage.setImageResource(R.drawable.open_vip1);
                        }
                        mOpenVipTip.setText(mOpenVipBean.information);
                        if (mOpenVipBean.isgivenVIP.equals("1")) {
                            mOpenVipLlWx.setVisibility(View.GONE);
                        } else {
                            mOpenVipLlWx.setVisibility(View.VISIBLE);

                            mOpenVipLlWx.setVisibility(mOpenVipBean.WXStatus ? View.GONE : View.VISIBLE);
                        }
                        for (int i = 0; i < mOpenVipBean.list.size(); i++) {
                            mPriceViews.get(i).setText("￥ " + mOpenVipBean.list.get(i).cardprice);
                            mTimeViews.get(i).setText(mOpenVipBean.list.get(i).cardname);
                            mOldPrice.get(i).setText(mOpenVipBean.list.get(i).oldprice);
                            mOldPrice.get(i).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                            mRLViews.get(i).setVisibility(View.VISIBLE);
                        }
                        day = mOpenVipBean.list.get(0).cardtime;
                        if (mOpenVipBean.isVIP.equals("1")) {
                            mHeaderTvTitle.setText("会员续期");
                        }
                        cardid = mOpenVipBean.list.get(0).cardid;
                    }
                });
    }


    private void showPay() {
        mPopupWindowPay = new PopupWindow(this);
        mPopupWindowPay.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindowPay.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        View viewPay = LayoutInflater.from(this).inflate(R.layout.view_pay, null);
        viewPay.findViewById(R.id.view_pay_tv_canael).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindowPay.dismiss();
            }
        });
        viewPay.findViewById(R.id.view_pay_ll_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toWXPay();
                mPopupWindowPay.dismiss();

            }
        });
        viewPay.findViewById(R.id.view_pay_ll_ali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toALiPay();
                mPopupWindowPay.dismiss();
            }
        });
        mPopupWindowPay.setContentView(viewPay);
        mPopupWindowPay.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindowPay.setOutsideTouchable(false);
        mPopupWindowPay.setFocusable(true);
        mPopupWindowPay.showAtLocation(getLayoutInflater().inflate(R.layout.activity_open_vip, null), Gravity.BOTTOM, 0, 0);
        PopupWindowUtils.darkenBackground(OpenVipActivity.this, .5f);
        mPopupWindowPay.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopupWindowUtils.darkenBackground(OpenVipActivity.this, 1f);
            }
        });
    }


    private void showPaySuccess() {
        EventBus.getDefault().post(new MessageEvent("Vip"));
        mPopupWindowPaySuccess = new PopupWindow(this);
        mPopupWindowPaySuccess.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindowPaySuccess.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        View viewPay = LayoutInflater.from(this).inflate(R.layout.view_pay_success, null);
        viewPay.findViewById(R.id.view_pay_success_btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindowPaySuccess.dismiss();
            }
        });
        ((TextView) viewPay.findViewById(R.id.view_pay_success)).setText("    您已成功开通如初会员,有效期为" + day + "天，可在“我的”界面里查看");
        mPopupWindowPaySuccess.setContentView(viewPay);
        mPopupWindowPaySuccess.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindowPaySuccess.setOutsideTouchable(true);
        mPopupWindowPaySuccess.setFocusable(true);
        mPopupWindowPaySuccess.showAtLocation(getLayoutInflater().inflate(R.layout.activity_open_vip, null), Gravity.CENTER, 0, 0);
        PopupWindowUtils.darkenBackground(OpenVipActivity.this, .5f);
        mPopupWindowPaySuccess.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopupWindowUtils.darkenBackground(OpenVipActivity.this, 1f);
                finish();
            }
        });
        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }

    @OnClick({R.id.open_vip_wx, R.id.open_vip_btn_pay, R.id.header_iv_left, R.id.open_vip_rl_30, R.id.open_vip_rl_90, R.id.open_vip_rl_180, R.id.open_vip_rl_365})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.open_vip_btn_pay:
                showPay();
                break;
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.open_vip_rl_30:
                day = mOpenVipBean.list.get(0).cardtime;
                cardid = mOpenVipBean.list.get(0).cardid;
                mOpenVipRl30.setBackgroundResource(R.drawable.rectangle_open_vip1);
                mOpenVipRl90.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl180.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl365.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipTv30.setTextColor(getResources().getColor(R.color.colorPink));
                mOpenVipTv90.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv180.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv365.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv15.setTextColor(getResources().getColor(R.color.colorPink));
                mOpenVipTv45.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv75.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv135.setTextColor(getResources().getColor(R.color.colorGray3));
                break;
            case R.id.open_vip_rl_90:
                day = mOpenVipBean.list.get(1).cardtime;
                cardid = mOpenVipBean.list.get(1).cardid;

                mOpenVipRl30.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl90.setBackgroundResource(R.drawable.rectangle_open_vip1);
                mOpenVipRl180.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl365.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipTv30.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv90.setTextColor(getResources().getColor(R.color.colorPink));
                mOpenVipTv180.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv365.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv15.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv45.setTextColor(getResources().getColor(R.color.colorPink));
                mOpenVipTv75.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv135.setTextColor(getResources().getColor(R.color.colorGray3));
                break;
            case R.id.open_vip_rl_180:
                day = mOpenVipBean.list.get(2).cardtime;
                cardid = mOpenVipBean.list.get(2).cardid;


                mOpenVipRl30.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl90.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl180.setBackgroundResource(R.drawable.rectangle_open_vip1);
                mOpenVipRl365.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipTv30.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv90.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv180.setTextColor(getResources().getColor(R.color.colorPink));
                mOpenVipTv365.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv15.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv45.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv75.setTextColor(getResources().getColor(R.color.colorPink));
                mOpenVipTv135.setTextColor(getResources().getColor(R.color.colorGray3));
                break;
            case R.id.open_vip_rl_365:
                day = mOpenVipBean.list.get(3).cardtime;
                cardid = mOpenVipBean.list.get(3).cardid;

                mOpenVipRl30.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl90.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl180.setBackgroundResource(R.drawable.rectangle_open_vip2);
                mOpenVipRl365.setBackgroundResource(R.drawable.rectangle_open_vip1);
                mOpenVipTv30.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv90.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv180.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv365.setTextColor(getResources().getColor(R.color.colorPink));
                mOpenVipTv15.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv45.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv75.setTextColor(getResources().getColor(R.color.colorGray3));
                mOpenVipTv135.setTextColor(getResources().getColor(R.color.colorPink));

                break;
            case R.id.open_vip_wx:
                UMShareAPI mShareAPI = UMShareAPI.get(OpenVipActivity.this);
                if (mShareAPI.isInstall(OpenVipActivity.this, SHARE_MEDIA.WEIXIN)) {
                    mShareAPI.doOauthVerify(OpenVipActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
//                    mShareAPI.getPlatformInfo(OpenVipActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                } else {
                    ToastUtils.showShort(UIUtils.getContext(), "未安装微信");
                }
                break;
        }
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (share_media == SHARE_MEDIA.WEIXIN) {

                OkGo
                        .post(AppUrl.BIND_WECHAT)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("unionid", map.get("unionid"))
                        .execute(new StringDialogCallback(OpenVipActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                if (successBean.success) {
                                    mOpenVipLlWx.setVisibility(View.GONE);
//                                    EventBus.getDefault().post(new MessageEvent("MyFragment"));
                                    EventBus.getDefault().post(new MessageEvent("Vip"));
                                    if (successBean.giveVIP) {
//                                        Notification.Builder builder = new Notification.Builder(OpenVipActivity.this);
//                                        Intent intent = new Intent(OpenVipActivity.this, VipManageActivity.class);  //需要跳转指定的页面
//                                        PendingIntent pendingIntent = PendingIntent.getActivity(OpenVipActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                        builder.setContentIntent(pendingIntent);
//                                        builder.setSmallIcon(R.mipmap.ic_launcher);// 设置图标
//                                        builder.setContentTitle(getString(R.string.vipTipHeader));// 设置通知的标题
//                                        builder.setContentText(getString(R.string.vipTip));// 设置通知的内容
//                                        builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
//                                        builder.setAutoCancel(true); //自己维护通知的消失
//                                        builder.setTicker(getString(R.string.vipTip));// 第一次提示消失的时候显示在通知栏上的
//                                        builder.setOngoing(true);
//                                        Notification notification = builder.build();
//                                        notification.flags = Notification.FLAG_AUTO_CANCEL;  //只有全部清除时，Notification才会清除
//                                        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
                                    }
                                } else {
                                    ToastUtils.showShort(UIUtils.getContext(), getString(R.string.AccounntWX));
                                }
                            }
                        });
            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(getApplicationContext(), "绑定失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(getApplicationContext(), "绑定取消", Toast.LENGTH_SHORT).show();

        }
    };

    private void toALiPay() {
        OkGo
                .post(AppUrl.ALIPAY)
                .params("cardid", cardid)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(OpenVipActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        AliPayBean aliPayBean = new Gson().fromJson(s, AliPayBean.class);
                        String info = aliPayBean.info;
                        final String orderInfo = info;   // 订单信息

                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(OpenVipActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }
                });


    }

    private IWXAPI iwxapi; //微信支付api

    /**
     * 调起微信支付的方法
     **/
    private void toWXPay() {
        OkGo
                .post(AppUrl.WXPAY)
                .params("ip", IpUtils.getIPAddress(OpenVipActivity.this))
//                .params("ip", "127.0.0.1")
                .params("cardid", cardid)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(OpenVipActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        final WxPayBean wxPayBean = new Gson().fromJson(s, WxPayBean.class);
//                        if (wxPayBean.info.return_code.equals("SUCCESS")) {
                        iwxapi = WXAPIFactory.createWXAPI(OpenVipActivity.this, null); //初始化微信api
                        iwxapi.registerApp(Constants.WX_APP_ID); //注册appid

                        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
                            @Override
                            public void run() {
                                PayReq request = new PayReq(); //调起微信APP的对象
                                request.appId = wxPayBean.info.appid;
                                request.partnerId = wxPayBean.info.partnerid;
                                request.prepayId = wxPayBean.info.prepayid;
                                request.packageValue = "Sign=WXPay";
                                request.nonceStr = wxPayBean.info.noncestr;
                                request.timeStamp = wxPayBean.info.timestamp;
                                request.sign = wxPayBean.info.sign;
                                iwxapi.sendReq(request);//发送调起微信的请求
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
//                        } else {
//                            ToastUtils.showShort(UIUtils.getContext(), wxPayBean.return_msg);
//                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void paySuccess(String mess) {
        if (mess.equals("wxPaySuccess")) {
            showPaySuccess();
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        ToastUtils.showShort(UIUtils.getContext(), "支付成功");
                        showPaySuccess();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.showShort(UIUtils.getContext(), "支付失败");
                    }
                    break;
                }
            }
        }

        ;
    };
}
