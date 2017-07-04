package com.jkpg.ruchu.view.activity.my;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpenVipActivity extends AppCompatActivity {

    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.open_vip_civ_photo)
    CircleImageView mOpenVipCivPhoto;
    @BindView(R.id.open_vip_tv_name)
    TextView mOpenVipTvName;
    @BindView(R.id.open_vip_tv_body)
    TextView mOpenVipTvBody;
    @BindView(R.id.open_vip_tv_wx)
    TextView mOpenVipTvWx;
    @BindView(R.id.open_vip_rl_30)
    RelativeLayout mOpenVipRl30;
    @BindView(R.id.open_vip_rl_90)
    RelativeLayout mOpenVipRl90;
    @BindView(R.id.open_vip_rl_180)
    RelativeLayout mOpenVipRl180;
    @BindView(R.id.open_vip_rl_365)
    RelativeLayout mOpenVipRl365;
    @BindView(R.id.open_vip_btn_pay)
    Button mOpenVipBtnPay;
    @BindView(R.id.open_vip_tv_30)
    TextView mOpenVipTv30;
    @BindView(R.id.open_vip_tv_15)
    TextView mOpenVipTv15;
    @BindView(R.id.open_vip_tv_90)
    TextView mOpenVipTv90;
    @BindView(R.id.open_vip_tv_45)
    TextView mOpenVipTv45;
    @BindView(R.id.open_vip_tv_180)
    TextView mOpenVipTv180;
    @BindView(R.id.open_vip_tv_75)
    TextView mOpenVipTv75;
    @BindView(R.id.open_vip_tv_365)
    TextView mOpenVipTv365;
    @BindView(R.id.open_vip_tv_135)
    TextView mOpenVipTv135;
    private PopupWindow mPopupWindowPay;
    private PopupWindow mPopupWindowPaySuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_vip);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.open_vip_tv_wx, R.id.open_vip_rl_30, R.id.open_vip_rl_90, R.id.open_vip_rl_180, R.id.open_vip_rl_365, R.id.open_vip_btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.open_vip_tv_wx:
                break;
            case R.id.open_vip_rl_30:
                //不要打我...一时偷懒...后期改成自定义view
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
            case R.id.open_vip_btn_pay:
                showPay();
                break;
        }
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
    }
}
