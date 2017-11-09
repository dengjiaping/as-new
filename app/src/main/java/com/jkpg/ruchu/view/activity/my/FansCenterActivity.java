package com.jkpg.ruchu.view.activity.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.jkpg.ruchu.utils.Loadingutil;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.ChatListActivity;
import com.jkpg.ruchu.view.activity.community.NoticeDetailFixActivity;
import com.jkpg.ruchu.view.adapter.FanCenterRvAdapter;
import com.jkpg.ruchu.widget.CircleImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
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
    private String mNameid;
    private String mNick;
    private String mHeadImg;

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
        LogUtils.i("mFansId" + mFansId);


        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


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
                        if (fansCenterBean.isVIP == null) {
                            ToastUtils.showShort(UIUtils.getContext(), "用户不存在哦!");
                            finish();
                            return;
                        }
                        init(fansCenterBean);
                        mFansId = fansCenterBean.userid;
                        mNameid = fansCenterBean.nameid;
                        mNick = fansCenterBean.nick;
                        mHeadImg = fansCenterBean.headImg;
                        initRecyclerView(fansCenterBean.bbslist);
                    }
                });
    }

    private void init(FansCenterBean fansCenterBean) {
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + fansCenterBean.headImg)
                .crossFade()
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .dontAnimate()
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
                String loginUser = TIMManager.getInstance().getLoginUser();
                if (StringUtils.isEmpty(loginUser)){

                    final AlertDialog loading = Loadingutil.getLoading(FansCenterActivity.this);
                    loading.show();

                    TIMManager.getInstance().login(
                            SPUtils.getString(UIUtils.getContext(), Constants.IMID, "")
                            , SPUtils.getString(UIUtils.getContext(), Constants.IMSIGN, "")
                            , new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {
                                    //错误码code和错误描述desc，可用于定位请求失败原因
                                    //错误码code列表请参见错误码表
                                    LogUtils.d("login failed. code: " + code + " errmsg: " + desc);
                                    ToastUtils.showShort(UIUtils.getContext(),"服务器开小差了，请稍后再试吧~");
                                    if (loading != null && loading.isShowing()){
                                        loading.dismiss();
                                    }

                                }

                                @Override
                                public void onSuccess() {
                                    LogUtils.d("login success");
                                    EventBus.getDefault().post("TIMLogin");
                                    //获取自己的资料
                                    TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                                        @Override
                                        public void onError(int code, String desc) {
                                            ToastUtils.showShort(UIUtils.getContext(),"服务器开小差了，请稍后再试吧~");
                                            if (loading != null && loading.isShowing()){
                                                loading.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onSuccess(TIMUserProfile result) {
                                            SPUtils.saveString(UIUtils.getContext(), Constants.IMIMAGE, result.getFaceUrl());
                                            Intent intent = new Intent(FansCenterActivity.this, ChatListActivity.class);
                                            intent.putExtra("peer", mNameid);
                                            intent.putExtra("name", mNick);
                                            intent.putExtra("image", AppUrl.BASEURL + mHeadImg);
//                ToastUtils.showShort(UIUtils.getContext(), "即将登场，敬请期待");
                                            startActivity(intent);
                                            if (loading != null && loading.isShowing()){
                                                loading.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                } else {
                    Intent intent = new Intent(FansCenterActivity.this, ChatListActivity.class);
                    intent.putExtra("peer", mNameid);
                    intent.putExtra("name", mNick);
                    intent.putExtra("image", AppUrl.BASEURL + mHeadImg);
//                ToastUtils.showShort(UIUtils.getContext(), "即将登场，敬请期待");
                    startActivity(intent);
                }
                break;
            case R.id.fans_tv_more:
//                ToastUtils.showShort(UIUtils.getContext(), "即将登场，敬请期待");
                showMorePopupWindow();
                break;
            case R.id.header_iv_left:
                finish();
                break;
        }
    }

    private void showMorePopupWindow() {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_fans_more, null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        view.findViewById(R.id.text_view_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> identifiers = new ArrayList<>();
                identifiers.add(mNameid);
                TIMFriendshipManagerExt.getInstance().addBlackList(identifiers, new TIMValueCallBack<List<TIMFriendResult>>() {
                    @Override
                    public void onError(int i, String s) {
                        ToastUtils.showShort(UIUtils.getContext(), "加入黑名单失败,请重试");
                    }

                    @Override
                    public void onSuccess(List<TIMFriendResult> timFriendResults) {
                        ToastUtils.showShort(UIUtils.getContext(), "加入黑名单成功");
                        OkGo
                                .post(AppUrl.ADDHEIMINGDAN)
                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                .params("othernameid", mNameid)
                                .params("flag", 1)
                                .execute(new StringDialogCallback(FansCenterActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        mFansShowFollow.setVisibility(View.GONE);
                                        mFansTvAddFollow.setVisibility(View.VISIBLE);
                                        EventBus.getDefault().post("fans");

                                        EventBus.getDefault().post(new MessageEvent("MyFragment"));
                                        TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, mNameid);
                                        EventBus.getDefault().post("RefreshIMList");

                                    }
                                });
                    }
                });
                popupWindow.dismiss();

            }
        });
        view.findViewById(R.id.text_view_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"色情、裸露", "广告、推销", "恶意骚扰、不文明语言", "其他"};
                new AlertDialog.Builder(FansCenterActivity.this)
                        .setTitle("请告诉我们举报原因,帮助我们让如初康复变得更好.")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OkGo
                                        .post(AppUrl.JUBAO)
                                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                        .params("nameid", mNameid)
                                        .params("jbmsg", items[which])
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
//                                                ToastUtils.showShort(UIUtils.getContext(), "举报成功!");
                                            }
                                        });
                                ToastUtils.showShort(UIUtils.getContext(), "举报成功!");
                                dialog.dismiss();
                            }
                        })
                        .show();
                popupWindow.dismiss();
            }
        });
        int popupWidth = view.getMeasuredWidth();
        int popupHeight = view.getMeasuredHeight();
        int[] location = new int[2];
        mFansTvMore.getLocationOnScreen(location);
        popupWindow.showAtLocation(mFansTvMore, Gravity.NO_GRAVITY, (location[0] + mFansTvMore.getWidth() / 2) - popupWidth / 2,
                location[1] - popupHeight);
    }

    private void showPopupWindow() {
        final View view = View.inflate(UIUtils.getContext(), R.layout.view_cancel_follow, null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addBlack(String ss) {
        if (ss.equals("addBlack")) {
            mFansShowFollow.setVisibility(View.GONE);
            mFansTvAddFollow.setVisibility(View.VISIBLE);
            EventBus.getDefault().post("fans");
            EventBus.getDefault().post(new MessageEvent("MyFragment"));
        }
    }
}
