package com.jkpg.ruchu.view.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.InvitationBean;
import com.jkpg.ruchu.bean.ShareBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.SpannableBuilder;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.InvitationInfoRvAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/10/11.
 */

public class InvitationDetailActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;
    @BindView(R.id.invitation_detail_tv0)
    TextView mInvitationDetailTv0;
    @BindView(R.id.invitation_detail_tv1)
    TextView mInvitationDetailTv1;
    @BindView(R.id.invitation_detail_tv2)
    TextView mInvitationDetailTv2;
    @BindView(R.id.invitation_detail_rv)
    RecyclerView mInvitationDetailRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail);
        ButterKnife.bind(this);
        mHeaderView.setElevation(0);
        mHeaderTvTitle.setText("邀请详情");

        mInvitationDetailTv1.setText(SpannableBuilder.create(InvitationDetailActivity.this)
                .append("邀请人数 ", R.dimen.sp14, R.color.yzappsdk_white)
                .append("0", R.dimen.sp18, R.color.yzappsdk_white)
                .append(" 人", R.dimen.sp14, R.color.yzappsdk_white)
                .build());
        mInvitationDetailTv2.setText(SpannableBuilder.create(InvitationDetailActivity.this)
                .append("已注册人数 ", R.dimen.sp14, R.color.yzappsdk_white)
                .append("0", R.dimen.sp18, R.color.yzappsdk_white)
                .append(" 人", R.dimen.sp14, R.color.yzappsdk_white)
                .build());
        OkGo
                .post(AppUrl.GETFENXIANGDAY)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        ShareBean shareBean = new Gson().fromJson(s, ShareBean.class);
                        String day = shareBean.day;
                        mInvitationDetailTv0.setText(day);

                    }
                });
        OkGo
                .post(AppUrl.GETFENXIANGMESS)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
//                .params("userid", "f490f9cb-8c92-11e7-89b2-00163e0814ca")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        InvitationBean invitationBean = new Gson().fromJson(s, InvitationBean.class);
                        mInvitationDetailTv1.setText(SpannableBuilder.create(InvitationDetailActivity.this)
                                .append("邀请人数 ", R.dimen.sp14, R.color.yzappsdk_white)
                                .append(invitationBean.zfx, R.dimen.sp18, R.color.yzappsdk_white)
                                .append(" 人", R.dimen.sp14, R.color.yzappsdk_white)
                                .build());
                        mInvitationDetailTv2.setText(SpannableBuilder.create(InvitationDetailActivity.this)
                                .append("已注册人数 ", R.dimen.sp14, R.color.yzappsdk_white)
                                .append(invitationBean.zzc, R.dimen.sp18, R.color.yzappsdk_white)
                                .append(" 人", R.dimen.sp14, R.color.yzappsdk_white)
                                .build());

                        initRecyclerView(invitationBean.array);
                    }
                });


    }

    private void initRecyclerView(List<InvitationBean.ArrayBean> array) {
        mInvitationDetailRv.setLayoutManager(new LinearLayoutManager(this));
        mInvitationDetailRv.setAdapter(new InvitationInfoRvAdapter(R.layout.item_invitation_info, array));
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
