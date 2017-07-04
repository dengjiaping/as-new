package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.center.PersonalInfoActivity;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.activity.my.MyFansActivity;
import com.jkpg.ruchu.view.activity.my.MyFilesActivity;
import com.jkpg.ruchu.view.activity.my.MySMSActivity;
import com.jkpg.ruchu.view.activity.my.MySetUpActivity;
import com.jkpg.ruchu.view.activity.my.MySpeakActivity;
import com.jkpg.ruchu.view.activity.my.MyTestActivity;
import com.jkpg.ruchu.view.activity.my.TrainHistoryActivity;
import com.jkpg.ruchu.view.activity.my.VipManageActivity;
import com.jkpg.ruchu.widget.CircleImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
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
        initPhoto();
        initHeader();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("");
        mHeaderIvLeft.setVisibility(View.GONE);
        mHeaderIvRight.setImageResource(R.drawable.icon_sms_write);
    }


    private void initPhoto() {
        String imgPath = SPUtils.getString(UIUtils.getContext(), Constants.PERSONAIMAGE, "");
        if (!StringUtils.isEmpty(imgPath)) {
            Glide
                    .with(UIUtils.getContext())
                    .load(new File(imgPath))
                    .error(R.drawable.icon_photo)
                    .into(mCenterCivPhoto);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.header_iv_right, R.id.center_ll_speak, R.id.center_ll_follow, R.id.center_ll_fans, R.id.center_civ_photo, R.id.center_ll_vip, R.id.center_ll_files, R.id.center_ll_test, R.id.center_ll_history, R.id.center_ll_setup})
    public void onViewClicked(View view) {
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
                startActivity(followIntent);

                break;
            case R.id.center_ll_fans:
                Intent fansIntent = new Intent(getActivity(), MyFansActivity.class);
                fansIntent.putExtra("title", "我的粉丝");
                startActivity(fansIntent);

                break;
            case R.id.header_iv_right:
                startActivity(new Intent(getActivity(), MySMSActivity.class));

                break;
        }
    }


    @OnClick(R.id.center_no_login)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
