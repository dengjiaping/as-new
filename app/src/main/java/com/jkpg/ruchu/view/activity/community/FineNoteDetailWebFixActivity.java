package com.jkpg.ruchu.view.activity.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.FineNoteWebBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.ImageTools;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.FineNoteDetailWebFixRVAdapter;
import com.jkpg.ruchu.view.adapter.PhotoAdapter;
import com.jkpg.ruchu.view.adapter.RecyclerItemClickListener;
import com.jkpg.ruchu.widget.ColorStyleTextView;
import com.lzy.okgo.OkGo;
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
import java.net.URLEncoder;
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
 * Created by qindi on 2017/9/1.
 */

public class FineNoteDetailWebFixActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_iv_right2)
    ImageView mHeaderIvRight2;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.notice_detail_reply_recycler)
    RecyclerView mNoticeDetailReplyRecycler;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.notice_detail_reply)
    TextView mNoticeDetailReply;
    @BindView(R.id.notice_detail)
    LinearLayout mNoticeDetail;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    private List<FineNoteWebBean.List2Bean> mList2;
    private FineNoteWebBean.List1Bean mList1;
    private FineNoteDetailWebFixRVAdapter mFineNoteDetailWebRVAdapter;
    private String mArt_id;
    private BottomSheetDialog mPopupWindow;
    private CheckBox mNoticeDetailTvDz;
    private boolean isCollect = false;
    private int isShowImage = View.VISIBLE;
    private int mIndex;
    private int mTid;
    private String mUserid;
    private RecyclerView mReplyRecyclerView;
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private PopupWindow mEditWindow;
    private TextView mNoticeDetailTvReply;
    private TextView mNoticeDetailTvNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail_revise);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mArt_id = getIntent().getStringExtra("art_id");
        mHeaderTvTitle.setText("文章详情");
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
        mHeaderIvRight2.setVisibility(View.GONE);
        initRefreshLayout();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mArt_id = intent.getStringExtra("art_id");
        initData();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                initData();
                mRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void initData() {

        mHeaderIvRight.setEnabled(false);

        OkGo
                .post(AppUrl.ARTICLEDETAIL)
                .tag(this)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("art_id", mArt_id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        FineNoteWebBean fineNoteWebBean = new Gson().fromJson(s, FineNoteWebBean.class);
                        mList1 = fineNoteWebBean.list1;
                        mList2 = new ArrayList<>();
//                        mList2 = fineNoteWebBean.list2;
                        initRecyclerView(fineNoteWebBean);
                        mHeaderIvRight.setEnabled(true);
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        if (mRefreshLayout != null)
                            mRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        if (mRefreshLayout != null)
                            mRefreshLayout.setRefreshing(true);

                    }
                });
    }

    private void initRecyclerView(final FineNoteWebBean fineNoteWebBean) {
        mNoticeDetailReplyRecycler.setLayoutManager(new LinearLayoutManager(this));
        mFineNoteDetailWebRVAdapter = new FineNoteDetailWebFixRVAdapter(R.layout.item_notic_reply_fix, mList2);
        mNoticeDetailReplyRecycler.setAdapter(mFineNoteDetailWebRVAdapter);
        final View inflate = View.inflate(this, R.layout.view_web_view, null);
        mFineNoteDetailWebRVAdapter.addHeaderView(inflate);
        WebView webView = (WebView) inflate.findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mList2.addAll(fineNoteWebBean.list2);

                mFineNoteDetailWebRVAdapter.notifyDataSetChanged();
                inflate.findViewById(R.id.web_view_ll).setVisibility(View.VISIBLE);
            }
        });

        if (mList1 == null || mList1.Content.isEmpty()) {
            ToastUtils.showShort(UIUtils.getContext(), "帖子不存在,可能已被删除!");
            finish();
            return;
        }
        webView.loadData(mList1.Content, "text/html; charset=UTF-8", null);
        mNoticeDetailTvDz = (CheckBox) inflate.findViewById(R.id.notice_detail_tv_dz);
        initZan();
        mNoticeDetailTvNum = (TextView) inflate.findViewById(R.id.notice_detail_tv_num);

        mNoticeDetailTvReply = (TextView) inflate.findViewById(R.id.notice_detail_tv_reply);
        mNoticeDetailTvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    startActivity(new Intent(FineNoteDetailWebFixActivity.this, LoginActivity.class));
                    return;
                }
                isShowImage = View.VISIBLE;
                replyLZ();
            }
        });
        isCollect = mList1.iscollect;
        if (isCollect) {
            mHeaderIvRight2.setImageResource(R.drawable.icon_collect_ok);
        } else {
            mHeaderIvRight2.setImageResource(R.drawable.icon_collect);
        }
        mNoticeDetailTvDz.setText(mList1.zan);
        mNoticeDetailTvDz.setChecked(mList1.iszan.equals("1"));
        mNoticeDetailTvReply.setText(mList1.reply);

        mNoticeDetailTvNum.setText("全部回复");


        mFineNoteDetailWebRVAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    startActivity(new Intent(FineNoteDetailWebFixActivity.this, LoginActivity.class));
                    return;
                }
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
//                    case R.id.item_notice_reply_to_body:
//                        mTid = mList2.get(position).items.get(0).tid;
//                        mIndex = position;
//                        isShowImage = View.GONE;
//                        replyLZ();
//                        break;
//                    case R.id.item_notice_reply_to_body0:
//                        mTid = mList2.get(position).items.get(1).tid;
//                        isShowImage = View.GONE;
//                        mIndex = position;
//
//                        replyLZ();
//                        break;
//                    case R.id.item_notice_reply_to_body1:
//                        mTid = mList2.get(position).items.get(1).tid;
//                        isShowImage = View.GONE;
//                        mIndex = position;
//
//                        replyLZ();
//                        break;
//                    case R.id.item_notice_reply_to_name:
//                        mUserid = mList2.get(position).items.get(0).userid;
//                        toUserMain(mUserid);
//                        break;
//                    case R.id.item_notice_reply_to_name0:
//                        mUserid = mList2.get(position).items.get(1).userid;
//                        toUserMain(mUserid);
//
//                        break;
//                    case R.id.item_notice_reply_to_name1:
//                        mUserid = mList2.get(position).items.get(1).userid;
//                        toUserMain(mUserid);
//
//                        break;
//                    case R.id.item_notice_reply_to_name2:
//                        mUserid = mList2.get(position).items.get(1).userid;
//                        toUserMain(mUserid);
//                        break;
                    case R.id.item_notice_reply_to_more:
                        int tid = mList2.get(position).tid;
                        mIndex = position;
                        Intent intent = new Intent(FineNoteDetailWebFixActivity.this, FineMoreReplyFixActivity.class);
                        intent.putExtra("commentid", tid + "");
                        intent.putExtra("art_id", mArt_id + "");
                        startActivity(intent);

                        break;
                    case R.id.item_notice_reply_cb_love:
                        if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                            startActivity(new Intent(FineNoteDetailWebFixActivity.this, LoginActivity.class));
                            return;
                        }
                        loveNotice((CheckBox) view, position);
                        break;
                    case R.id.view_0:
                        LogUtils.i("----view_0----0");

                        ColorStyleTextView view_0 = (ColorStyleTextView) view;
                        view_0.setOnClickCallBack(new ColorStyleTextView.ClickCallBack() {
                            @Override
                            public void onClick(int a) {
                                switch (a) {
                                    case 0:
                                        LogUtils.i("----------0");
                                        mUserid = mList2.get(position).items.get(0).userid;
                                        LogUtils.i("mUserid" + mUserid);
                                        toUserMain(mUserid);
                                        break;
                                    case 1:
                                        LogUtils.i("----------1");
                                        mTid = mList2.get(position).items.get(0).tid;
                                        mIndex = position;
                                        isShowImage = View.GONE;
                                        replyLZ();
                                        break;
                                }
                            }
                        });
                        break;
                    case R.id.view_1:
                        ColorStyleTextView view_1 = (ColorStyleTextView) view;
                        view_1.setOnClickCallBack(new ColorStyleTextView.ClickCallBack() {
                            @Override
                            public void onClick(int a) {
                                switch (a) {
                                    case 0:
                                        mUserid = mList2.get(position).items.get(1).userid;
                                        toUserMain(mUserid);
                                        LogUtils.i("mUserid" + mUserid);

                                        break;
                                    case 1:
                                        mTid = mList2.get(position).items.get(1).tid;
                                        isShowImage = View.GONE;
                                        mIndex = position;
                                        replyLZ();
                                        break;
                                }
                            }
                        });
                        break;
                    case R.id.view_2:
                        ColorStyleTextView view_2 = (ColorStyleTextView) view;
                        view_2.setOnClickCallBack(new ColorStyleTextView.ClickCallBack() {
                            @Override
                            public void onClick(int a) {
                                switch (a) {
                                    case 0:
                                        mUserid = mList2.get(position).items.get(1).userid;
                                        toUserMain(mUserid);
                                        LogUtils.i("mUserid" + mUserid);

                                        break;
                                    case 1:
                                        mUserid = mList2.get(position).items.get(1).userid2;
                                        toUserMain(mUserid);
                                        LogUtils.i("mUserid" + mUserid);

                                        break;
                                    case 2:
                                        mTid = mList2.get(position).items.get(1).tid;
                                        isShowImage = View.GONE;
                                        mIndex = position;
                                        replyLZ();
                                        break;
                                }
                            }
                        });
                        break;
                }

            }
        });
    }

    private void loveNotice(CheckBox view, int position) {
        final CheckBox love = view;
        int i = Integer.parseInt(love.getText().toString());
        if (love.isChecked()) {
            love.setText((i + 1) + "");
            OkGo
                    .post(AppUrl.ARTICLEREPLYPRAISE)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .params("commentid", mList2.get(position).tid)
                    .params("flag", 1)
                    .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                        }
                    });
        } else {
            love.setText((i - 1) + "");
            OkGo
                    .post(AppUrl.ARTICLEREPLYPRAISE)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .params("commentid", mList2.get(position).tid)
                    .params("flag", 0)
                    .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                        }
                    });
        }
    }
    @SuppressWarnings("deprecation")
    private void replyLZ() {
        selectedPhotos.clear();
        if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
            startActivity(new Intent(FineNoteDetailWebFixActivity.this, LoginActivity.class));
            return;
        }
        View editView;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {

            editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input_22, null);
            editView.findViewById(R.id.view_reply_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditWindow.dismiss();
                }
            });

        } else {
            editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input, null);
        }
        mEditWindow = new PopupWindow(editView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mEditWindow.setBackgroundDrawable(null);
        mEditWindow.setOutsideTouchable(true);
        mEditWindow.setFocusable(true);

        final EditText replyEdit = (EditText) editView.findViewById(R.id.view_reply_et);
        mReplyRecyclerView = (RecyclerView) editView.findViewById(R.id.view_reply_recycler);
        editView.findViewById(R.id.view_reply_image).setVisibility(isShowImage);
        editView.findViewById(R.id.view_reply_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(3)
                        .setGridColumnCount(4)
                        .start(FineNoteDetailWebFixActivity.this);
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
                                .post(AppUrl.ArticleReply)
                                .params("art_id",mArt_id)
                                .params("parentid","-1")
                                .params("userid",SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                .params("content",string)
                                .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        mEditWindow.dismiss();
                                        EventBus.getDefault().post("Community");
                                        OkGo
                                                .post(AppUrl.ARTICLEDETAIL)
                                                .params("art_id", mArt_id)
                                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        FineNoteWebBean fineNoteWebBean = new Gson().fromJson(s, FineNoteWebBean.class);
                                                        mList2.clear();
                                                        mList2.addAll(fineNoteWebBean.list2);
                                                        mFineNoteDetailWebRVAdapter.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onAfter(String s, Exception e) {
                                                        super.onAfter(s, e);
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
                            String s = FileUtils.saveBitmapByQuality(bm, 40);
                            files.add(new File(s));
                        }
                        OkGo
                                .post(AppUrl.ArticleReply
                                        + "?art_id=" + mArt_id
                                        + "&parentid=" + "-1"
                                        + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                                        + "&content=" + URLEncoder.encode(string))
                                .isMultipart(true)
                                .addFileParams("upload", files)
                                .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        mEditWindow.dismiss();
                                        OkGo
                                                .post(AppUrl.ARTICLEDETAIL)
                                                .params("art_id", mArt_id)
                                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                                .execute(new StringCallback() {
                                                    @Override
                                                    public void onSuccess(String s, Call call, Response response) {
                                                        FineNoteWebBean fineNoteWebBean = new Gson().fromJson(s, FineNoteWebBean.class);

                                                        mList2.clear();
                                                        mList2.addAll(fineNoteWebBean.list2);
                                                        mFineNoteDetailWebRVAdapter.notifyDataSetChanged();
//                                                        mNoticeDetailTvReply.setText(fineNoteWebBean.list2.size());
                                                    }

                                                    @Override
                                                    public void onAfter(String s, Exception e) {
                                                        super.onAfter(s, e);
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
                            .post(AppUrl.ArticleReply
                                    + "?art_id=" + mArt_id
                                    + "&parentid=" + mTid
                                    + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                                    + "&content=" + URLEncoder.encode(string))
                            .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    mEditWindow.dismiss();
                                    OkGo
                                            .post(AppUrl.ARTICLEDETAIL)
                                            .params("art_id", mArt_id)
                                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    FineNoteWebBean fineNoteWebBean = new Gson().fromJson(s, FineNoteWebBean.class);
                                                    mList2.clear();
//                                                    mList2 = fineNoteWebBean.list2;
                                                    mList2.addAll(fineNoteWebBean.list2);
                                                    mFineNoteDetailWebRVAdapter.notifyDataSetChanged();
//                                                    initRecyclerView(fineNoteWebBean);
                                                    mNoticeDetailReplyRecycler.getItemAnimator().setChangeDuration(0);
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
        mEditWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mEditWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditWindow.showAtLocation(FineNoteDetailWebFixActivity.this.findViewById(R.id.notice_detail), Gravity.BOTTOM, 0, 0);
        // 显示键盘
        final InputMethodManager imm = (InputMethodManager) UIUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        mEditWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
            }
        });
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
                                    .start(FineNoteDetailWebFixActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(FineNoteDetailWebFixActivity.this);
                        }
                    }
                }));
    }

    private void toUserMain(String userid) {
        // TODO: 2017/8/11
        if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
            startActivity(new Intent(FineNoteDetailWebFixActivity.this, LoginActivity.class));
            return;
        }
        Intent intent = new Intent(FineNoteDetailWebFixActivity.this, FansCenterActivity.class);
        intent.putExtra("fansId", userid);
        startActivity(intent);
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
                        startActivity(new Intent(FineNoteDetailWebFixActivity.this, LoginActivity.class));

                        return;
                    }

                    OkGo
                            .post(AppUrl.ARTICLECOLLECTION)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("art_id", mArt_id)
                            .params("flag", 1)
                            .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
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
                        startActivity(new Intent(FineNoteDetailWebFixActivity.this, LoginActivity.class));

                        return;
                    }
                    OkGo
                            .post(AppUrl.ARTICLECOLLECTION)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("art_id", mArt_id)
                            .params("flag", 0)
                            .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
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

    private void showShare() {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_share, null);
//        mPopupWindow = new PopupWindow(this);
        view.findViewById(R.id.view_share_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
//        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
//        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        mPopupWindow.setContentView(view);
//        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
//        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setFocusable(true);
//        mPopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
//        mPopupWindow.showAsDropDown(getLayoutInflater().inflate(R.layout.activity_notice_detail_revise, null), Gravity.BOTTOM, 0, 0);
//        PopupWindowUtils.darkenBackground(FineNoteDetailWebFixActivity.this, .5f);
//        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                PopupWindowUtils.darkenBackground(FineNoteDetailWebFixActivity.this, 1f);
//            }
//        });
        mPopupWindow = new BottomSheetDialog(FineNoteDetailWebFixActivity.this);
        mPopupWindow.setContentView(view);
        mPopupWindow.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mPopupWindow.show();


        final UMWeb mWeb = new UMWeb(AppUrl.BASEURL + mList1.shareurl);
        mWeb.setTitle(mList1.Title);//标题
//        if (mList1.get(0).images.size() == 0) {
        mWeb.setThumb(new UMImage(UIUtils.getContext(), AppUrl.BASEURL + mList1.images));
//        } else {
//        mWeb.setThumb(new UMImage(UIUtils.getContext(), AppUrl.BASEURL + mList1.get(0).images.get(0)));
//        }
        if (StringUtils.isEmpty(mList1.simplecontent)) {
            mWeb.setDescription(" ");//描述
        } else {
            mWeb.setDescription(mList1.simplecontent);//描述
        }
        view.findViewById(R.id.share_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(FineNoteDetailWebFixActivity.this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(mWeb)
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.share_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(FineNoteDetailWebFixActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(mWeb)
                        .setCallback(umShareListener)
                        .share();
                mPopupWindow.dismiss();

            }
        });
        view.findViewById(R.id.share_wxq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(FineNoteDetailWebFixActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
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

    private void initZan() {
        mNoticeDetailTvDz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    startActivity(new Intent(FineNoteDetailWebFixActivity.this, LoginActivity.class));
                    mNoticeDetailTvDz.setChecked(false);
                    return;
                }
                if (mNoticeDetailTvDz.isChecked()) {

                    OkGo
                            .post(AppUrl.ARTICLEPRAISE)
                            .params("art_id", mArt_id)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("flag", 1)
                            .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
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
                            .post(AppUrl.ARTICLEPRAISE)
                            .params("art_id", mArt_id)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("flag", 0)
                            .execute(new StringDialogCallback(FineNoteDetailWebFixActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                    if (successBean.success) {
                                        int i = Integer.parseInt(mNoticeDetailTvDz.getText().toString()) - 1;
                                        mNoticeDetailTvDz.setText(i + "");
                                    } else {
                                        ToastUtils.showShort(UIUtils.getContext(), "取消点赞失败");
                                    }
                                    // TODO: 2017/9/2  刷新!!!!

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
                    .post(AppUrl.ARTICLEDETAIL)
                    .params("art_id", mArt_id)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            FineNoteWebBean fineNoteWebBean = new Gson().fromJson(s, FineNoteWebBean.class);
                            mList2.clear();
                            mList2.addAll(fineNoteWebBean.list2);
                            mFineNoteDetailWebRVAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAfter(String s, Exception e) {
                            super.onAfter(s, e);
                            mNoticeDetailReplyRecycler.scrollToPosition(mIndex + 1);
                        }
                    });
        } else if (mess.message.equals("Login")) {
            OkGo
                    .post(AppUrl.ARTICLEDETAIL)
                    .params("art_id", mArt_id)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .execute(new StringCallback() {


                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            FineNoteWebBean fineNoteWebBean = new Gson().fromJson(s, FineNoteWebBean.class);
                            mList2.clear();
                            mList2.addAll(fineNoteWebBean.list2);
                            mFineNoteDetailWebRVAdapter.notifyDataSetChanged();

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
