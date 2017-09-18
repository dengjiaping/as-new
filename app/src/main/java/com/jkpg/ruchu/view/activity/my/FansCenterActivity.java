package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.FansCenterBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.NoticeDetailFixActivity;
import com.jkpg.ruchu.view.adapter.FanCenterRvAdapter;
import com.jkpg.ruchu.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/27.
 */

public class FansCenterActivity extends BaseActivity {
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
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;

    @BindView(R.id.fans_show_follow)
    LinearLayout mFansShowFollow;
    @BindView(R.id.fans_tv_time)
    TextView mFansTvTime;
    @BindView(R.id.fans_scroll_view)
    ScrollView mFansScrollView;

    private int flag = 1;
    private String mFansId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_center);
        ButterKnife.bind(this);
        initHeader();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHeaderView.setElevation(0);
        }
        mFansId = getIntent().getStringExtra("fansId");
        if (StringUtils.isEmpty(mFansId)) {
            ToastUtils.showShort(UIUtils.getContext(), "用户不存在哦");
            MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        } else {
            initData(mFansId);
        }
        LogUtils.i("mFansId"+mFansId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mFansId = intent.getStringExtra("fansId");
        flag = 1;
        initData(mFansId);
    }

    private void initData(String fansId) {
        OkGo
                .post(AppUrl.FANSDETAIL)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("fansid", fansId)
                .params("flag", flag)
                .execute(new StringDialogCallback(FansCenterActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        FansCenterBean fansCenterBean = new Gson().fromJson(s, FansCenterBean.class);
                        init(fansCenterBean);
                        initRecyclerView(fansCenterBean.bbslist);
                    }
                });
    }

    private void init(FansCenterBean fansCenterBean) {
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + fansCenterBean.headImg)
                .crossFade()
                .centerCrop()
                .into(mFansCivPhoto);
        mFansTvTime.setText(fansCenterBean.chanhoutime);
        mFansTvName.setText(fansCenterBean.nick);
        if (fansCenterBean.isVIP.equals("2")) {
            mFansIvVip.setImageResource(R.drawable.icon_vip1);
        } else {
            mFansIvVip.setImageResource(R.drawable.icon_vip2);
        }
        mFansTvFollow.setText(fansCenterBean.attNum);
        mFansTvFans.setText(fansCenterBean.fansNum);
        mFansTvAddress.setText(fansCenterBean.address);
        mFansTvGrade.setText(fansCenterBean.levelname);
        if (fansCenterBean.isAtt.equals("1")) {
            mFansTvAddFollow.setVisibility(View.GONE);
            mFansShowFollow.setVisibility(View.VISIBLE);
        } else {
            mFansTvAddFollow.setVisibility(View.VISIBLE);
            mFansShowFollow.setVisibility(View.GONE);
        }
        if (fansCenterBean.bbsnum == null || fansCenterBean.bbslist.size() == 0) {
            mFansTvPostCount.setText("暂无发帖");
        } else {
            mFansTvPostCount.setText("全部发帖（" + fansCenterBean.bbsnum + "）");
        }
    }

    private void initRecyclerView(final List<FansCenterBean.BbslistBean> bbslist) {
        mFansRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final FanCenterRvAdapter fanCenterRvAdapter = new FanCenterRvAdapter(R.layout.item_fans_post, bbslist);
        mFansRecyclerView.setAdapter(fanCenterRvAdapter);
        fanCenterRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Intent intent = new Intent(FansCenterActivity.this, NoticeDetailFixActivity.class);
                intent.putExtra("bbsid", bbslist.get(position).tid + "");
                startActivity(intent);
            }
        });

        fanCenterRvAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                flag++;
                LogUtils.i(flag + "");
                OkGo
                        .post(AppUrl.FANSDETAIL)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("fansid", mFansId)
                        .params("flag", flag)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                FansCenterBean fansCenterBean = new Gson().fromJson(s, FansCenterBean.class);
                                List<FansCenterBean.BbslistBean> list = fansCenterBean.bbslist;
                                if (list == null) {
                                    fanCenterRvAdapter.loadMoreEnd();
                                } else if (list.size() == 0) {
                                    fanCenterRvAdapter.loadMoreEnd();
                                } else {
                                    fanCenterRvAdapter.addData(list);
                                    fanCenterRvAdapter.loadMoreComplete();
                                }
                            }
                        });

            }
        }, mFansRecyclerView);
    }

    private void initHeader() {
//        mHeaderTvTitle.setVisibility(View.GONE);
        mHeaderTvTitle.setText("个人主页");
    }


    @OnClick({R.id.fans_tv_add_follow, R.id.fans_tv_ok_follow, R.id.fans_tv_chat, R.id.fans_tv_more, R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fans_tv_add_follow:
                if (mFansId.equals(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {

                    ToastUtils.showShort(UIUtils.getContext(), "自己不能关注自己哦");
                    return;
                }

                OkGo
                        .post(AppUrl.ATTENTION)
                        .params("followUserid", mFansId)
                        .params("MyUserid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringDialogCallback(FansCenterActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                if (!successBean.success) {
                                    ToastUtils.showShort(UIUtils.getContext(), successBean.msg);

                                } else {
                                    EventBus.getDefault().post("fans");
                                    EventBus.getDefault().post(new MessageEvent("MyFragment"));
                                    mFansShowFollow.setVisibility(View.VISIBLE);
                                    mFansTvAddFollow.setVisibility(View.GONE);
                                }
                            }
                        });

                break;
            case R.id.fans_tv_ok_follow:
                showPopupWindow();
                break;
            case R.id.fans_tv_chat:
                ToastUtils.showShort(UIUtils.getContext(), "即将登场，敬请期待");
                //startActivity(new Intent(FansCenterActivity.this, ChatActivity.class));
                break;
            case R.id.fans_tv_more:
                ToastUtils.showShort(UIUtils.getContext(), "即将登场，敬请期待");
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
                OkGo
                        .post(AppUrl.CANCLEATT)
                        .params("fansid", mFansId)
                        .params("myuserid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringDialogCallback(FansCenterActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                if (!successBean.success) {
                                    ToastUtils.showShort(UIUtils.getContext(), "取消关注失败");
                                } else {
                                    mFansShowFollow.setVisibility(View.GONE);
                                    mFansTvAddFollow.setVisibility(View.VISIBLE);
                                    EventBus.getDefault().post("fans");

                                    EventBus.getDefault().post(new MessageEvent("MyFragment"));
                                    popupWindow.dismiss();
                                }
                            }
                        });


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
