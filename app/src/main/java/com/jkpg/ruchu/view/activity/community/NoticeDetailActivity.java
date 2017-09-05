package com.jkpg.ruchu.view.activity.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.NoticeDetailBean;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.ImageTools;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PopupWindowUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.NoticeDetailReplyAdapter;
import com.jkpg.ruchu.view.adapter.PhotoAdapter;
import com.jkpg.ruchu.view.adapter.RecyclerItemClickListener;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.MyLinearLayoutManager;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.jkpg.ruchu.widget.nineview.preview.NineGridViewClickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/6/7.
 */

public class NoticeDetailActivity extends BaseActivity implements View.OnClickListener {


    List<ImageInfo> imageInfo;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right2)
    ImageView mHeaderIvRight2;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.notice_detail_reply_recycler)
    RecyclerView mNoticeDetailReplyRecycler;
    @BindView(R.id.notice_detail_reply)
    TextView mNoticeDetailReply;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RecyclerView mReplyRecyclerView;
    private NoticeDetailReplyAdapter mNoticeDetailReplyAdapter;
    private boolean isCollect = false;
    private int isShowImage = View.VISIBLE;
    private int mbbsid;
    private List<NoticeDetailBean.List1Bean> mList1;
    private List<NoticeDetailBean.List2Bean> mList2;
    private int mTid;
    private View mHeaderView;
    private CircleImageView mNoticeDetailCivPhoto;
    private TextView mNoticeDetailTvReply;
    private TextView mNoticeDetailTvName;
    private ImageView mNoticeDetailIvLz;
    private ImageView mNoticeDetailIvFine;
    private TextView mNoticeDetailTvTc;
    private TextView mNoticeDetailTvAddress;
    private TextView mNoticeDetailTvTitle;
    private TextView mNoticeDetailTvTime;
    private TextView mNoticeDetailTvContent;
    private TextView mNoticeDetailTvNum;
    private CheckBox mNoticeDetailTvDz;
    private NineGridView mNoticeDetailNineView;
    private int mIndex;
    private String mUserid;
    private PopupWindow mPopupWindow;
    private String mBbsid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail_revise);
        ButterKnife.bind(this);
        mBbsid = getIntent().getStringExtra("bbsid");
        LogUtils.d(mBbsid+"bbsid");

        initData(mBbsid);
        initHeader();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkGo
                        .post(AppUrl.BBS_DETAILS)
                        .params("bbsid", mBbsid)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .execute(new StringCallback() {


                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
                                mList1.clear();
                                mList2.clear();
                                mList1.addAll(noticeDetailBean.list1);
                                mList2.addAll(noticeDetailBean.list2);
                                mNoticeDetailReplyAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onAfter(@Nullable String s, @Nullable Exception e) {
                                super.onAfter(s, e);
                                mRefreshLayout.setRefreshing(false);
                            }
                        });
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mBbsid = intent.getStringExtra("bbsid");
        LogUtils.d(mBbsid+"bbsid");
//        OkGo
//                .post(AppUrl.BBS_DETAILS)
//                .params("bbsid", mBbsid)
//                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
//                .execute(new StringCallback() {
//
//
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
//                        mList1.clear();
//                        mList2.clear();
//                        mList1.addAll(noticeDetailBean.list1);
//                        mList2.addAll(noticeDetailBean.list2);
//                        mNoticeDetailReplyAdapter.notifyDataSetChanged();
//
//                    }
//
//                    @Override
//                    public void onAfter(@Nullable String s, @Nullable Exception e) {
//                        super.onAfter(s, e);
//                        mRefreshLayout.setRefreshing(false);
//                    }
//                });
        initData(mBbsid);


    }

    private void initZan() {
        mNoticeDetailTvDz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    startActivity(new Intent(NoticeDetailActivity.this, LoginActivity.class));
                    mNoticeDetailTvDz.setChecked(false);
                    return;
                }
                if (mNoticeDetailTvDz.isChecked()) {

                    OkGo
                            .post(AppUrl.UPVOTE)
                            .params("bbsid", mbbsid)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("flag", 1)
                            .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                    if (successBean.success) {
                                        int i = Integer.parseInt(mNoticeDetailTvDz.getText().toString()) + 1;
                                        mNoticeDetailTvDz.setText(i + "");
                                    } else {
                                        ToastUtils.showShort(UIUtils.getContext(), "点赞失败");
                                    }
                                    EventBus.getDefault().post("Community");
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    ToastUtils.showShort(UIUtils.getContext(), "点赞失败");
                                }
                            });

                } else {
                    OkGo
                            .post(AppUrl.UPVOTE)
                            .params("bbsid", mbbsid)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("flag", 0)
                            .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                    if (successBean.success) {
                                        int i = Integer.parseInt(mNoticeDetailTvDz.getText().toString()) - 1;
                                        mNoticeDetailTvDz.setText(i + "");
                                    } else {
                                        ToastUtils.showShort(UIUtils.getContext(), "取消点赞失败");
                                    }
                                    EventBus.getDefault().post("Community");
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    ToastUtils.showShort(UIUtils.getContext(), "取消点赞失败");
                                }
                            });
                }
            }
        });

        mNoticeDetailTvDz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    private void initData(String bbsid) {
        OkGo
                .post(AppUrl.BBS_DETAILS)
                .params("bbsid", bbsid)
                .tag(this)
                .cacheKey("BBS_DETAILS" + bbsid)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringCallback() {


                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
                        mList1 = noticeDetailBean.list1;
                        mList2 = noticeDetailBean.list2;
                        initRecyclerView(mList2);
                    }

                    @Override
                    public void onAfter(@Nullable String s, @Nullable Exception e) {
                        super.onAfter(s, e);
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mRefreshLayout.setRefreshing(true);
                    }

                });
    }

    private void initNotice(List<NoticeDetailBean.List1Bean> list1) {
        NoticeDetailBean.List1Bean list1Bean = list1.get(0);
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + list1Bean.headimg)
                .centerCrop()
                .into(mNoticeDetailCivPhoto);
        mNoticeDetailTvName.setText(list1Bean.nick);
        if (!list1Bean.isGood.equals("1")) {
            mNoticeDetailIvFine.setVisibility(View.GONE);
        } else {
            mNoticeDetailIvFine.setVisibility(View.VISIBLE);
        }
        mNoticeDetailTvTc.setText(list1Bean.taici + " " + list1Bean.chanhoutime);
        if (StringUtils.isEmpty(list1Bean.site)) {
            mNoticeDetailTvAddress.setVisibility(View.GONE);
        } else {
            mNoticeDetailTvAddress.setText(list1Bean.site);
        }
        mNoticeDetailTvDz.setText(list1Bean.zan);
        mNoticeDetailTvDz.setChecked(list1Bean.iszan);

        mNoticeDetailTvReply.setText(mList2.size() + "");
        mNoticeDetailTvTitle.setText(list1Bean.title);
        mNoticeDetailTvTime.setText(list1Bean.createtime);
        mNoticeDetailTvContent.setText(list1Bean.content);

        initNineView(list1Bean.images);

        isCollect = list1Bean.iscollect;
        if (isCollect) {
            mHeaderIvRight2.setImageResource(R.drawable.icon_collect_ok);
        } else {
            mHeaderIvRight2.setImageResource(R.drawable.icon_collect);
        }

        mbbsid = list1Bean.tid;

    }

    private void initHeader() {
        mHeaderTvTitle.setText("帖子详情");
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
        mHeaderIvRight2.setImageResource(R.drawable.icon_collect);
    }

    private void initRecyclerView(final List<NoticeDetailBean.List2Bean> list2) {
        mNoticeDetailReplyRecycler.setLayoutManager(new MyLinearLayoutManager(UIUtils.getContext()));
        mNoticeDetailReplyAdapter = new NoticeDetailReplyAdapter(R.layout.item_notic_reply, list2);
        mNoticeDetailReplyRecycler.setAdapter(mNoticeDetailReplyAdapter);
        mNoticeDetailReplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        mNoticeDetailReplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.item_notice_reply_tv_reply:
                    case R.id.item_notice_reply_body:
                        isShowImage = View.GONE;
                        mIndex = position;
                        mTid = mList2.get(position).tid;
                        replyLZ();
                        break;
                    case R.id.item_notice_reply_civ:
                    case R.id.item_notice_reply_name:
                        toUserMain(mList2.get(position).userid);
                        break;
                    case R.id.item_notice_reply_to_body:
                        mTid = mList2.get(position).items.get(0).tid;
                        mIndex = position;
                        isShowImage = View.GONE;
                        replyLZ();
                        break;
                    case R.id.item_notice_reply_to_body0:
                        mTid = mList2.get(position).items.get(1).tid;
                        isShowImage = View.GONE;
                        mIndex = position;

                        replyLZ();
                        break;
                    case R.id.item_notice_reply_to_body1:
                        mTid = mList2.get(position).items.get(1).tid;
                        isShowImage = View.GONE;
                        mIndex = position;

                        replyLZ();
                        break;
                    case R.id.item_notice_reply_to_name:
                        mUserid = mList2.get(position).items.get(0).userid;
                        toUserMain(mUserid);
                        break;
                    case R.id.item_notice_reply_to_name0:
                        mUserid = mList2.get(position).items.get(1).userid;
                        toUserMain(mUserid);

                        break;
                    case R.id.item_notice_reply_to_name1:
                        mUserid = mList2.get(position).items.get(1).userid;
                        toUserMain(mUserid);

                        break;
                    case R.id.item_notice_reply_to_name2:
                        mUserid = mList2.get(position).items.get(1).userid;
                        toUserMain(mUserid);

                        break;
                    case R.id.item_notice_reply_to_more:
                        int tid = mList2.get(position).tid;
                        mIndex = position;
                        Intent intent = new Intent(NoticeDetailActivity.this, MoreReplyActivity.class);
                        intent.putExtra("replyid", tid + "");
                        intent.putExtra("bbsid", mbbsid + "");
                        startActivity(intent);

                        break;
                    case R.id.item_notice_reply_cb_love:
                        if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                            startActivity(new Intent(NoticeDetailActivity.this, LoginActivity.class));
                            return;
                        }
                        loveNotice((CheckBox) view, position);
                        break;

                }

            }
        });
        mHeaderView = View.inflate(this, R.layout.view_notice_detail_header, null);
        mNoticeDetailReplyAdapter.addHeaderView(mHeaderView);
        mNoticeDetailCivPhoto = (CircleImageView) mHeaderView.findViewById(R.id.notice_detail_civ_photo);
        mNoticeDetailCivPhoto.setOnClickListener(this);
        mNoticeDetailTvReply = (TextView) mHeaderView.findViewById(R.id.notice_detail_tv_reply);
        mNoticeDetailTvReply.setOnClickListener(this);
        mNoticeDetailTvName = (TextView) mHeaderView.findViewById(R.id.notice_detail_tv_name);
        mNoticeDetailTvName.setOnClickListener(this);
        mNoticeDetailIvLz = (ImageView) mHeaderView.findViewById(R.id.notice_detail_iv_lz);
        mNoticeDetailIvFine = (ImageView) mHeaderView.findViewById(R.id.notice_detail_iv_fine);
        mNoticeDetailTvTc = (TextView) mHeaderView.findViewById(R.id.notice_detail_tv_tc);
        mNoticeDetailTvAddress = (TextView) mHeaderView.findViewById(R.id.notice_detail_tv_address);
        mNoticeDetailTvTitle = (TextView) mHeaderView.findViewById(R.id.notice_detail_tv_title);
        mNoticeDetailTvTime = (TextView) mHeaderView.findViewById(R.id.notice_detail_tv_time);
        mNoticeDetailTvContent = (TextView) mHeaderView.findViewById(R.id.notice_detail_tv_content);
        mNoticeDetailTvNum = (TextView) mHeaderView.findViewById(R.id.notice_detail_tv_num);
        mNoticeDetailTvDz = (CheckBox) mHeaderView.findViewById(R.id.notice_detail_tv_dz);
        mNoticeDetailNineView = (NineGridView) mHeaderView.findViewById(R.id.notice_detail_nine_view);
        initZan();
        initNotice(mList1);
        if (mList2.size() > 0) {
            mNoticeDetailTvNum.setText("全部回复");
        } else {
            mNoticeDetailTvNum.setText("暂无回复");
        }


    }

    private void toUserMain(String userid) {
        // TODO: 2017/8/11
        if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
            startActivity(new Intent(NoticeDetailActivity.this, LoginActivity.class));
            return;
        }
        Intent intent = new Intent(NoticeDetailActivity.this, FansCenterActivity.class);
        intent.putExtra("fansId", userid);
        startActivity(intent);
    }

    private void loveNotice(CheckBox view, int position) {
        final CheckBox love = view;
        int i = Integer.parseInt(love.getText().toString());
        if (love.isChecked()) {
            love.setText((i + 1) + "");
            OkGo
                    .post(AppUrl.UPVOTEREPLY)
                    .params("bbsid", mList1.get(0).tid)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .params("replyid", mList2.get(position).tid)
                    .params("flag", 1)
                    .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                        }
                    });
        } else {
            love.setText((i - 1) + "");
            OkGo
                    .post(AppUrl.UPVOTEREPLY)
                    .params("bbsid", mList1.get(0).tid)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .params("replyid", mList2.get(position).tid)
                    .params("flag", 0)
                    .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                        }
                    });
        }
    }


    private void initNineView(List<String> images) {
        imageInfo = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageInfo info = new ImageInfo(AppUrl.BASEURL + images.get(i));
            this.imageInfo.add(info);
        }
        mNoticeDetailNineView.setAdapter(new NineGridViewClickAdapter(UIUtils.getContext(), imageInfo));
    }

    @OnClick({R.id.header_iv_left, R.id.header_iv_right2, R.id.header_iv_right, R.id.notice_detail_reply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right2:

                if (!isCollect) {
                    if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                        startActivity(new Intent(NoticeDetailActivity.this, LoginActivity.class));

                        return;
                    }

                    OkGo
                            .post(AppUrl.BBS_COLLECT)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("tid", mbbsid)
                            .params("flag", 1)
                            .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                    if (successBean.success) {
                                        isCollect = true;
                                        mHeaderIvRight2.setImageResource(R.drawable.icon_collect_ok);
                                        ToastUtils.showShort(UIUtils.getContext(), "收藏成功");
                                    } else {
                                        ToastUtils.showShort(UIUtils.getContext(), "收藏失败");
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    ToastUtils.showShort(UIUtils.getContext(), "收藏失败");

                                }
                            });
                } else {
                    if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                        startActivity(new Intent(NoticeDetailActivity.this, LoginActivity.class));

                        return;
                    }
                    OkGo
                            .post(AppUrl.BBS_COLLECT)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("tid", mbbsid)
                            .params("flag", 0)
                            .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                    if (successBean.success) {
                                        isCollect = false;
                                        mHeaderIvRight2.setImageResource(R.drawable.icon_collect);
                                    } else {
                                        ToastUtils.showShort(UIUtils.getContext(), "取消收藏失败");
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    ToastUtils.showShort(UIUtils.getContext(), "取消收藏失败");

                                }
                            });

                }
                break;
            case R.id.header_iv_right:
                showShare();
                break;

            case R.id.notice_detail_reply:
                isShowImage = View.VISIBLE;
                replyLZ();
                break;
        }
    }


    private void initPhotoPicker() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos);


        mReplyRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(6, OrientationHelper.VERTICAL));
        mReplyRecyclerView.setAdapter(photoAdapter);
        mReplyRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == 1) {
                            PhotoPicker.builder()
                                    .setPhotoCount(3)
                                    .setGridColumnCount(4)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(NoticeDetailActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(NoticeDetailActivity.this);
                        }
                    }
                }));
    }

    private void replyLZ() {
        selectedPhotos.clear();
        //// TODO: 2017/8/11
        if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
            startActivity(new Intent(NoticeDetailActivity.this, LoginActivity.class));
            return;
        }
        View editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input, null);
        final PopupWindow editWindow = new PopupWindow(editView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editWindow.setBackgroundDrawable(null);
        editWindow.setOutsideTouchable(false);
        editWindow.setFocusable(true);

        final EditText replyEdit = (EditText) editView.findViewById(R.id.view_reply_et);
        mReplyRecyclerView = (RecyclerView) editView.findViewById(R.id.view_reply_recycler);
        editView.findViewById(R.id.view_reply_image).setVisibility(isShowImage);
        editView.findViewById(R.id.view_reply_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(3)
                        .setGridColumnCount(4)
                        .start(NoticeDetailActivity.this);
            }
        });

        editView.findViewById(R.id.view_reply_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String string = replyEdit.getText().toString().trim();
                if (isShowImage == View.VISIBLE) {

                    if (selectedPhotos.size() == 0) {
                        if (string.length() == 0) {
                            ToastUtils.showShort(UIUtils.getContext(), "请输入内容哦!");
                            return;
                        }
                        OkGo
                                .post(AppUrl.BBS_REPLY
                                        + "?bbsid=" + mbbsid
                                        + "&parentid=" + "-1"
                                        + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                                        + "&content=" + string)
                                .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        editWindow.dismiss();
                                        EventBus.getDefault().post("Community");
                                        OkGo
                                                .post(AppUrl.BBS_DETAILS)
                                                .params("bbsid", mbbsid)
                                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
                                                        mList1.clear();
                                                        mList2.clear();
                                                        mList1 = noticeDetailBean.list1;
                                                        mList2 = noticeDetailBean.list2;
                                                        initRecyclerView(mList2);
                                                        mNoticeDetailReplyRecycler.getItemAnimator().setChangeDuration(0);
                                                        mNoticeDetailReplyRecycler.scrollToPosition(mList2.size());
                                                    }
                                                });
                                    }
                                });
                    } else {
                        List<File> files = new ArrayList<>();
                        for (String selectedPhoto : selectedPhotos) {
                            Uri uri = Uri.fromFile(new File(selectedPhoto));
                            Bitmap bm = ImageTools.decodeUriAsBitmap(uri);
                            String s = FileUtils.saveBitmapByQuality(bm, 10);
                            files.add(new File(s));
                        }
                        OkGo
                                .post(AppUrl.BBS_REPLY
                                        + "?bbsid=" + mbbsid
                                        + "&parentid=" + "-1"
                                        + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                                        + "&content=" + string)
                                .isMultipart(true)
                                .addFileParams("upload", files)
                                .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        editWindow.dismiss();
                                        OkGo
                                                .post(AppUrl.BBS_DETAILS)
                                                .params("bbsid", mbbsid)
                                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
                                                        mList1.clear();
                                                        mList2.clear();
                                                        mList1 = noticeDetailBean.list1;
                                                        mList2 = noticeDetailBean.list2;
                                                        initRecyclerView(mList2);
                                                        mNoticeDetailReplyRecycler.getItemAnimator().setChangeDuration(0);

                                                        mNoticeDetailReplyRecycler.scrollToPosition(mList2.size());

                                                    }
                                                });
                                    }
                                });

                    }
                } else {
                    if (string.length() == 0) {
                        ToastUtils.showShort(UIUtils.getContext(), "请输入内容哦!");
                        return;
                    }
                    OkGo
                            .post(AppUrl.BBS_REPLY
                                    + "?bbsid=" + mbbsid
                                    + "&parentid=" + mTid
                                    + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                                    + "&content=" + string)
                            .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    editWindow.dismiss();
                                    OkGo
                                            .post(AppUrl.BBS_DETAILS)
                                            .params("bbsid", mbbsid)
                                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
                                                    mList1.clear();
                                                    mList2.clear();
                                                    mList1 = noticeDetailBean.list1;
                                                    mList2 = noticeDetailBean.list2;
                                                    initRecyclerView(mList2);
//                                                    int size = mList2.size();
//                                                    mList2.clear();
//                                                    mNoticeDetailReplyAdapter.notifyItemRangeRemoved(0,size);
//                                                    mList2.addAll(noticeDetailBean.list2);
//                                                    mNoticeDetailReplyAdapter.notifyItemRangeInserted(0,mList2.size());
//                                                    ((SimpleItemAnimator) mNoticeDetailReplyRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
                                                    mNoticeDetailReplyRecycler.scrollToPosition(mIndex + 1);
                                                }

                                            });


                                }
                            });
                }
            }
        });
        initPhotoPicker();
        replyEdit.setFocusable(true);
        replyEdit.requestFocus();
        // 以下两句不能颠倒
        editWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        editWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        editWindow.showAtLocation(NoticeDetailActivity.this.findViewById(R.id.notice_detail), Gravity.BOTTOM, 0, 0);
        // 显示键盘
        final InputMethodManager imm = (InputMethodManager) UIUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        editWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {
                selectedPhotos.addAll(photos);
                mReplyRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mReplyRecyclerView.setVisibility(View.GONE);
            }
            photoAdapter.notifyDataSetChanged();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notice_detail_tv_name:
            case R.id.notice_detail_civ_photo:
                toUserMain(mList1.get(0).userid);
                break;
            case R.id.notice_detail_tv_reply:
                isShowImage = View.VISIBLE;
                replyLZ();
                break;
        }
    }

    private void showShare() {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_share, null);
        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(view);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        mPopupWindow.showAsDropDown(getLayoutInflater().inflate(R.layout.activity_my_set_up, null), Gravity.BOTTOM, 0, 0);
        PopupWindowUtils.darkenBackground(NoticeDetailActivity.this, .5f);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopupWindowUtils.darkenBackground(NoticeDetailActivity.this, 1f);
            }
        });
        final UMWeb mWeb = new UMWeb(AppUrl.BASEURL + mList1.get(0).shareurl + "?bbsid=" + mbbsid);
        mWeb.setTitle(mList1.get(0).title);//标题
        if (mList1.get(0).images.size() == 0) {
            mWeb.setThumb(new UMImage(UIUtils.getContext(), R.drawable.logo));
        } else {
            mWeb.setThumb(new UMImage(UIUtils.getContext(), AppUrl.BASEURL + mList1.get(0).images.get(0)));
        }
        mWeb.setDescription(mList1.get(0).content);//描述
        view.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(NoticeDetailActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(mWeb)
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(NoticeDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(mWeb)
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();

            }
        });
        view.findViewById(R.id.share_wxq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(NoticeDetailActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(mWeb)
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();

            }
        });
        view.findViewById(R.id.share_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogUtils.d("plat", "platform" + platform);

//               Toast.makeText(MySetUpActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
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
//             Toast.makeText(MySetUpActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReply(MessageEvent mess) {
        if (mess.message.equals("reply")) {
            OkGo
                    .post(AppUrl.BBS_DETAILS)
                    .params("bbsid", mbbsid)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
                            mList1.clear();
                            mList2.clear();
                            mList1.addAll(noticeDetailBean.list1);
                            mList2.addAll(noticeDetailBean.list2);
                            mNoticeDetailReplyAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                            mNoticeDetailReplyRecycler.scrollToPosition(mIndex + 1);
                        }
                    });
        } else if (mess.message.equals("Login")) {
            OkGo
                    .post(AppUrl.BBS_DETAILS)
                    .params("bbsid", mBbsid)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .execute(new StringCallback() {


                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
                            mList1.clear();
                            mList2.clear();
                            mList1.addAll(noticeDetailBean.list1);
                            mList2.addAll(noticeDetailBean.list2);
                            mNoticeDetailReplyAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onAfter(@Nullable String s, @Nullable Exception e) {
                            super.onAfter(s, e);
                            mRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            mRefreshLayout.setRefreshing(true);
                        }
                    });
        }
    }
}
