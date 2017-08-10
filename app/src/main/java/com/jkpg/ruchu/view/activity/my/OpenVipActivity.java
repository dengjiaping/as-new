package com.jkpg.ruchu.view.activity.my;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.OpenVipBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.PopupWindowUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

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
    TextView mOpenVipWx;
    @BindView(R.id.open_vip_wx)
    RelativeLayout mOpenVipRl365;
    @BindView(R.id.open_vip_btn_pay)
    Button mOpenVipBtnPay;
    private PopupWindow mPopupWindowPay;
    private PopupWindow mPopupWindowPaySuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_vip);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("开通会员");
        initData();
        UMShareAPI.get(OpenVipActivity.this).fetchAuthResultWithBundle(OpenVipActivity.this, savedInstanceState, umAuthListener);

    }

    private void initData() {
        OkGo
                .post(AppUrl.VIPCARDSLIST)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(OpenVipActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        OpenVipBean openVipBean = new Gson().fromJson(s, OpenVipBean.class);
                        mOpenVipLlWx.setVisibility(openVipBean.WXStatus ? View.GONE : View.VISIBLE);
                        mOpenVipTv15.setText("￥ " + openVipBean.list.get(0).cardprice);
                        mOpenVipTv45.setText("￥ " + openVipBean.list.get(1).cardprice);
                        mOpenVipTv75.setText("￥ " + openVipBean.list.get(2).cardprice);
                        mOpenVipTv135.setText("￥ " + openVipBean.list.get(3).cardprice);
                        mOpenVipTv30.setText(openVipBean.list.get(0).cardname);
                        mOpenVipTv90.setText(openVipBean.list.get(1).cardname);
                        mOpenVipTv180.setText(openVipBean.list.get(2).cardname);
                        mOpenVipTv365.setText(openVipBean.list.get(3).cardname);
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
                ToastUtils.showShort(UIUtils.getContext(), "wx");
                showPaySuccess();
            }
        });
        viewPay.findViewById(R.id.view_pay_ll_ali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(UIUtils.getContext(), "ali");
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

            }
        });
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
            Toast.makeText(getApplicationContext(), "绑定成功", Toast.LENGTH_SHORT).show();

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
}
