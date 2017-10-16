package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.ExperienceBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.MyIndex;
import com.jkpg.ruchu.bean.MyMessageBean;
import com.jkpg.ruchu.bean.SmsEvent;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.center.PersonalInfoActivity;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.activity.my.GrowthValueActivity;
import com.jkpg.ruchu.view.activity.my.InvitationActivity;
import com.jkpg.ruchu.view.activity.my.MyFansActivity;
import com.jkpg.ruchu.view.activity.my.MyFilesActivity;
import com.jkpg.ruchu.view.activity.my.MySMSActivity;
import com.jkpg.ruchu.view.activity.my.MySetUpActivity;
import com.jkpg.ruchu.view.activity.my.MySpeakActivity;
import com.jkpg.ruchu.view.activity.my.MyTestActivity;
import com.jkpg.ruchu.view.activity.my.TrainHistoryActivity;
import com.jkpg.ruchu.view.activity.my.VipManageActivity;
import com.jkpg.ruchu.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

import static com.jkpg.ruchu.R.id.center_tv_name;

/**
 * Created by qindi on 2017/5/16.
 */

public class MyFragment extends Fragment {
    @BindView(R.id.center_civ_photo)
    CircleImageView mCenterCivPhoto;
    @BindView(R.id.center_iv_vip)
    ImageView mCenterIvVip;
    @BindView(R.id.center_tv_grade)
    TextView mCenterTvGrade;
    @BindView(R.id.center_tv_mark)
    TextView mCenterTvMark;
    @BindView(R.id.center_ll_vip)
    LinearLayout mCenterLlVip;
    @BindView(R.id.center_ll_test)
    LinearLayout mCenterLlTest;
    @BindView(R.id.center_ll_history)
    LinearLayout mCenterLlHistory;
    @BindView(R.id.center_ll_setup)
    LinearLayout mCenterLlSetup;
    Unbinder unbinder;
    //    @BindView(R.id.header_iv_left)
//    ImageView mHeaderIvLeft;
//    @BindView(R.id.header_tv_title)
//    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.center_tv_empiric)
    TextView mCenterTvEmpiric;
    @BindView(R.id.center_tv_speak)
    TextView mCenterTvSpeak;
    @BindView(R.id.center_tv_follow)
    TextView mCenterTvFollow;
    @BindView(R.id.center_tv_fans)
    TextView mCenterTvFans;
    @BindView(R.id.center_ll_files)
    LinearLayout mCenterLlFiles;
    @BindView(R.id.center_ll_speak)
    LinearLayout mCenterLlSpeak;
    @BindView(R.id.center_ll_follow)
    LinearLayout mCenterLlFollow;
    @BindView(R.id.center_ll_fans)
    LinearLayout mCenterLlFans;
    @BindView(R.id.center_no_login)
    LinearLayout mCenterNoLogin;
    @BindView(center_tv_name)
    TextView mCenterTvName;
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;
    @BindView(R.id.center_tv_uid)
    TextView mCenterTvUid;

    private String imgUrl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHeaderView.setElevation(0);
        }
        if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
            mCenterNoLogin.setVisibility(View.VISIBLE);
        } else {
            initData();
        }
        initHeader();

        OkGo
                .post(AppUrl.MYMASSAGE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("flag", 0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyMessageBean myMessageBean = new Gson().fromJson(s, MyMessageBean.class);
                        if (myMessageBean.notice || myMessageBean.reply || myMessageBean.zan) {
                            mHeaderIvRight.setImageResource(R.drawable.icon_sms);
                        } else {
                            mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
                        }

                    }
                });
        mCenterTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
                    return;
                }
                if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
//            ToastUtils.showShort(UIUtils.getContext(), "未登录");
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
            }
        });

    }

    private void initData() {
        mCenterNoLogin.setVisibility(View.VISIBLE);
        OkGo
                .post(AppUrl.MYINDEX)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .cacheKey("MYINDEX")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyIndex myIndex = new Gson().fromJson(s, MyIndex.class);
                        MyIndex.MymessBean mymess = myIndex.mymess;
                        initMess(mymess);
                        if (!SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                            mCenterNoLogin.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        MyIndex myIndex = new Gson().fromJson(s, MyIndex.class);
                        MyIndex.MymessBean mymess = myIndex.mymess;
                        initMess(mymess);
                        if (!SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
                            mCenterNoLogin.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void initHeader() {
//        mHeaderTvTitle.setText("");
//        mHeaderIvLeft.setVisibility(View.GONE);
        mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
    }


    private void initMess(MyIndex.MymessBean mymess) {
        if (StringUtils.isEmpty(imgUrl) || !imgUrl.equals(mymess.uImgurl)) {
            Glide
                    .with(UIUtils.getContext())
                    .load(AppUrl.BASEURL + mymess.uImgurl)
                    .error(R.drawable.icon_photo)
                    .centerCrop()
                    .crossFade()
                    .into(mCenterCivPhoto);
            imgUrl = mymess.uImgurl;
        }
        mCenterTvName.setText(mymess.uNick);
        mCenterTvEmpiric.setText(mymess.experience);
        mCenterTvMark.setText(mymess.amount);
        mCenterTvSpeak.setText(mymess.ftiecount);
        mCenterTvFollow.setText(mymess.mygz);
        mCenterTvFans.setText(mymess.fens);
        mCenterTvGrade.setText(mymess.levelname);
        mCenterTvUid.setText(mymess.uid + "");
        if (mymess.isVIP.equals("0")) {
            mCenterIvVip.setImageResource(R.drawable.icon_vip2);
        } else {
            mCenterIvVip.setImageResource(R.drawable.icon_vip1);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.header_iv_right, R.id.center_ll_speak, R.id.center_ll_follow, R.id.center_ll_fans,
            R.id.center_civ_photo, R.id.center_ll_vip, R.id.center_ll_files, R.id.center_ll_test,
            R.id.center_ll_invitation,
            R.id.center_ll_history, R.id.center_ll_setup, R.id.center_ll_on1, R.id.center_ll_on2})
    public void onViewClicked(View view) {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        if (SPUtils.getString(UIUtils.getContext(), Constants.USERID, "").equals("")) {
//            ToastUtils.showShort(UIUtils.getContext(), "未登录");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            case R.id.center_civ_photo:
                startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
                break;
            case R.id.center_ll_vip:
                startActivity(new Intent(getActivity(), VipManageActivity.class));
                break;
            case R.id.center_ll_files:
                startActivity(new Intent(getActivity(), MyFilesActivity.class));

                break;
            case R.id.center_ll_history:
                startActivity(new Intent(getActivity(), TrainHistoryActivity.class));

                break;
            case R.id.center_ll_setup:
                startActivity(new Intent(getActivity(), MySetUpActivity.class));

                break;
            case R.id.center_ll_test:
                startActivity(new Intent(getActivity(), MyTestActivity.class));

                break;
            case R.id.center_ll_speak:
                startActivity(new Intent(getActivity(), MySpeakActivity.class));

                break;
            case R.id.center_ll_follow:
                Intent followIntent = new Intent(getActivity(), MyFansActivity.class);
                followIntent.putExtra("title", "我的关注");
                followIntent.putExtra("flag", 1);

                startActivity(followIntent);

                break;
            case R.id.center_ll_fans:
                Intent fansIntent = new Intent(getActivity(), MyFansActivity.class);
                fansIntent.putExtra("title", "我的粉丝");
                fansIntent.putExtra("flag", 2);

                startActivity(fansIntent);

                break;
            case R.id.header_iv_right:
                startActivity(new Intent(getActivity(), MySMSActivity.class));
                break;
            case R.id.center_ll_on1:
                break;
            case R.id.center_ll_on2:
                startActivity(new Intent(getActivity(), GrowthValueActivity.class));
                break;
            case R.id.center_ll_invitation:
                startActivity(new Intent(getActivity(), InvitationActivity.class));

                break;
        }
    }


    @OnClick(R.id.center_no_login)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.message.equals("Login") || event.message.equals("MyFragment")
                || event.message.equals("Quit") || event.message.equals("Vip")) {
            MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            }, 500);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newMess(SmsEvent sms) {
        if (sms.sms) {
            mHeaderIvRight.setImageResource(R.drawable.icon_sms);
        } else {
            mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newnewMess(ExperienceBean sms) {
        if (!StringUtils.isEmpty(SPUtils.getString(UIUtils
                .getContext(), Constants.USERID, ""))) {
//
            initData();

        }
    }
}
