package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.ChatActivity;
import com.jkpg.ruchu.view.adapter.FansRLAdapter;
import com.jkpg.ruchu.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/27.
 */

public class FansCenterActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.fans_civ_photo)
    CircleImageView mFansCivPhoto;
    @BindView(R.id.fans_tv_name)
    TextView mFansTvName;
    @BindView(R.id.fans_iv_vip)
    ImageView mFansIvVip;
    @BindView(R.id.fans_tv_follow)
    TextView mFansTvFollow;
    @BindView(R.id.fans_tv_fans)
    TextView mFansTvFans;
    @BindView(R.id.fans_tv_address)
    TextView mFansTvAddress;
    @BindView(R.id.fans_tv_grade)
    TextView mFansTvGrade;
    @BindView(R.id.fans_tv_post_count)
    TextView mFansTvPostCount;
    @BindView(R.id.fans_recycler_view)
    RecyclerView mFansRecyclerView;
    @BindView(R.id.fans_tv_add_follow)
    TextView mFansTvAddFollow;
    @BindView(R.id.fans_tv_ok_follow)
    TextView mFansTvOkFollow;
    @BindView(R.id.fans_tv_chat)
    TextView mFansTvChat;
    @BindView(R.id.fans_tv_more)
    TextView mFansTvMore;

    @BindView(R.id.fans_show_follow)
    LinearLayout mFansShowFollow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_center);
        ButterKnife.bind(this);
        initHeader();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mFansRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFansRecyclerView.setAdapter(new FansRLAdapter());
    }

    private void initHeader() {
        mHeaderTvTitle.setVisibility(View.GONE);
    }

    @OnClick({R.id.fans_tv_add_follow, R.id.fans_tv_ok_follow, R.id.fans_tv_chat, R.id.fans_tv_more, R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fans_tv_add_follow:
                mFansShowFollow.setVisibility(View.VISIBLE);
                mFansTvAddFollow.setVisibility(View.GONE);
                break;
            case R.id.fans_tv_ok_follow:
                showPopupWindow();
                break;
            case R.id.fans_tv_chat:
                startActivity(new Intent(FansCenterActivity.this, ChatActivity.class));
                break;
            case R.id.fans_tv_more:
                break;
            case R.id.header_iv_left:
                finish();
                break;
        }
    }

    private void showPopupWindow() {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_cancel_follow, null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        view.findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFansShowFollow.setVisibility(View.GONE);
                mFansTvAddFollow.setVisibility(View.VISIBLE);
                popupWindow.dismiss();
            }
        });
        int popupWidth = view.getMeasuredWidth();
        int popupHeight = view.getMeasuredHeight();
        int[] location = new int[2];
        mFansTvOkFollow.getLocationOnScreen(location);
        popupWindow.showAtLocation(mFansTvOkFollow, Gravity.NO_GRAVITY, (location[0] + mFansTvOkFollow.getWidth() / 2) - popupWidth / 2,
                location[1] - popupHeight);
    }

}
