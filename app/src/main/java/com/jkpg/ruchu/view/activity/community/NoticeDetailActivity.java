package com.jkpg.ruchu.view.activity.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jkpg.ruchu.bean.NoticeDetailBean;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.NoticeDetailReplyAdapter;
import com.jkpg.ruchu.view.adapter.PhotoAdapter;
import com.jkpg.ruchu.view.adapter.RecyclerItemClickListener;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.jkpg.ruchu.widget.nineview.preview.NineGridViewClickAdapter;
import com.lzy.okgo.OkGo;

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

public class NoticeDetailActivity extends AppCompatActivity {
    @BindView(R.id.notice_detail_nine_view)
    NineGridView mNoticeDetailNineView;

    List<ImageInfo> imageInfo;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right2)
    ImageView mHeaderIvRight2;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.notice_detail_civ_photo)
    CircleImageView mNoticeDetailCivPhoto;
    @BindView(R.id.notice_detail_tv_reply)
    TextView mNoticeDetailTvReply;
    @BindView(R.id.notice_detail_reply_recycler)
    RecyclerView mNoticeDetailReplyRecycler;
    @BindView(R.id.notice_detail_reply)
    TextView mNoticeDetailReply;
    @BindView(R.id.notice_detail_tv_name)
    TextView mNoticeDetailTvName;
    @BindView(R.id.notice_detail_iv_lz)
    ImageView mNoticeDetailIvLz;
    @BindView(R.id.notice_detail_iv_fine)
    ImageView mNoticeDetailIvFine;
    @BindView(R.id.notice_detail_tv_tc)
    TextView mNoticeDetailTvTc;
    @BindView(R.id.notice_detail_tv_address)
    TextView mNoticeDetailTvAddress;
    @BindView(R.id.notice_detail_tv_dz)
    CheckBox mNoticeDetailTvDz;
    @BindView(R.id.notice_detail_tv_title)
    TextView mNoticeDetailTvTitle;
    @BindView(R.id.notice_detail_tv_time)
    TextView mNoticeDetailTvTime;
    @BindView(R.id.notice_detail_tv_content)
    TextView mNoticeDetailTvContent;
    @BindView(R.id.notice_detail_tv_num)
    TextView mNoticeDetailTvNum;
    @BindView(R.id.notice_detail)
    LinearLayout mNoticeDetail;
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RecyclerView mReplyRecyclerView;
    private NoticeDetailReplyAdapter mNoticeDetailReplyAdapter;
    private boolean isCollect = false;
    private int isShowImage = View.VISIBLE;
    private int mTid;
    private List<NoticeDetailBean.List1Bean> mList1;
    private List<NoticeDetailBean.List2Bean> mList2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        String bbsid = getIntent().getStringExtra("bbsid");
        initData(bbsid);
        initHeader();
        initZan();

    }

    private void initZan() {
        mNoticeDetailTvDz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OkGo
                            .post(AppUrl.UPVOTE)
                            .params("bbsid", mTid)
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
                            .params("bbsid", mTid)
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
    }

    private void initData(String bbsid) {
        OkGo
                .post(AppUrl.BBS_DETAILS)
                .params("bbsid", bbsid)
                .execute(new StringDialogCallback(NoticeDetailActivity.this) {


                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        NoticeDetailBean noticeDetailBean = new Gson().fromJson(s, NoticeDetailBean.class);
                        mList1 = noticeDetailBean.list1;
                        mList2 = noticeDetailBean.list2;
                        initNotice(mList1);
                        if (mList2.size() > 0) {
                            mNoticeDetailTvNum.setText("共有" + mList2.size() + "条回帖");
                        } else {
                            mNoticeDetailTvNum.setText("暂无回帖");
                        }
                        initRecyclerView(mList2);
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
        mNoticeDetailTvAddress.setText(list1Bean.site);
        mNoticeDetailTvDz.setText(list1Bean.zan);
        mNoticeDetailTvDz.setChecked(list1Bean.iszan);

        mNoticeDetailTvReply.setText(list1Bean.reply);
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

        mTid = list1Bean.tid;

    }

    private void initHeader() {
        mHeaderTvTitle.setText("帖子详情");
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
        mHeaderIvRight2.setImageResource(R.drawable.icon_collect);
    }

    private void initRecyclerView(List<NoticeDetailBean.List2Bean> list2) {
        mNoticeDetailReplyRecycler.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mNoticeDetailReplyAdapter = new NoticeDetailReplyAdapter(R.layout.item_notic_reply, list2);
        mNoticeDetailReplyRecycler.setAdapter(mNoticeDetailReplyAdapter);
        mNoticeDetailReplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        mNoticeDetailReplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_notice_detail_tv_reply:
                    case R.id.item_notice_reply_body:
                        isShowImage = View.GONE;
                        replyLZ();
                        break;
                    case R.id.item_notice_reply_civ:
                    case R.id.item_notice_reply_name:
                        ToastUtils.showShort(UIUtils.getContext(), mList1.get(0).userid);
                        break;
                    case R.id.item_notice_reply_to_body:
                        int tid = mList2.get(position).items.get(0).tid;
                        isShowImage = View.GONE;
                        replyLZ();
                        break;
                    case R.id.item_notice_reply_to_body0:
                        isShowImage = View.GONE;
                        replyLZ();
                        break;
                    case R.id.item_notice_reply_to_body1:
                        isShowImage = View.GONE;
                        replyLZ();
                        break;
                    case R.id.item_notice_reply_to_name:
                        String userid = mList2.get(position).items.get(0).userid;
                        ToastUtils.showShort(UIUtils.getContext(), userid);
                        break;
                    case R.id.item_notice_reply_to_name0:
                        String userid0 = mList2.get(position).items.get(1).userid;
                        ToastUtils.showShort(UIUtils.getContext(), userid0);
                        break;
                    case R.id.item_notice_reply_to_name1:
                        String userid1 = mList2.get(position).items.get(1).userid;
                        ToastUtils.showShort(UIUtils.getContext(), userid1);
                        break;
                    case R.id.item_notice_reply_to_name2:
                        String userid2 = mList2.get(position).items.get(1).userid;
                        ToastUtils.showShort(UIUtils.getContext(), userid2);
                        break;
                    case R.id.item_notice_reply_to_more:
                        ToastUtils.showShort(UIUtils.getContext(), "more");
                        break;

                }

            }
        });
    }


    private void initNineView(List<String> images) {
        imageInfo = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImageInfo info = new ImageInfo(AppUrl.BASEURL + images.get(i));
            this.imageInfo.add(info);
        }
        mNoticeDetailNineView.setAdapter(new NineGridViewClickAdapter(UIUtils.getContext(), imageInfo));
    }

    @OnClick({R.id.header_iv_left, R.id.header_iv_right2, R.id.header_iv_right, R.id.notice_detail_civ_photo, R.id.notice_detail_tv_reply, R.id.notice_detail_reply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right2:

                if (!isCollect) {
                    OkGo
                            .post(AppUrl.BBS_COLLECT)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("tid", mTid)
                            .params("flag", 1)
                            .execute(new StringDialogCallback(NoticeDetailActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                    if (successBean.success) {
                                        isCollect = true;
                                        mHeaderIvRight2.setImageResource(R.drawable.icon_collect_ok);
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
                    OkGo
                            .post(AppUrl.BBS_COLLECT)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("tid", mTid)
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
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                share_intent.setType("text/plain");//设置分享内容的类型
                share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享帖子");//添加分享内容标题
                share_intent.putExtra(Intent.EXTRA_TEXT, "www.ruchuapp.com");//添加分享内容
                //创建分享的Dialog
                share_intent = Intent.createChooser(share_intent, "分享帖子");
                startActivity(share_intent);
                break;
            case R.id.notice_detail_civ_photo:
                break;
            case R.id.notice_detail_tv_reply:
            case R.id.notice_detail_reply:
                isShowImage = View.VISIBLE;
                replyLZ();
                break;
        }
    }

    private void replyLZ() {
        View editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input, null);
        PopupWindow editWindow = new PopupWindow(editView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editWindow.setOutsideTouchable(true);
        editWindow.setFocusable(true);
        editWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        EditText replyEdit = (EditText) editView.findViewById(R.id.view_reply_et);
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
    }
}
