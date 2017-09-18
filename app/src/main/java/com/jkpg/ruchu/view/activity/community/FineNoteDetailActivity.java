package com.jkpg.ruchu.view.activity.community;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.FineNoteBodyBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.FineNoteBodyAdapter;
import com.jkpg.ruchu.view.adapter.PhotoAdapter;
import com.jkpg.ruchu.view.adapter.RecyclerItemClickListener;
import com.jkpg.ruchu.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by qindi on 2017/6/17.
 */

public class FineNoteDetailActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right2)
    ImageView mHeaderIvRight2;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.note_detail_title)
    TextView mNoteDetailTitle;
    @BindView(R.id.note_detail_civ_title)
    CircleImageView mNoteDetailCivTitle;
    @BindView(R.id.note_detail_name)
    TextView mNoteDetailName;
    @BindView(R.id.note_detail_time)
    TextView mNoteDetailTime;
    @BindView(R.id.note_detail_civ_rv_body)
    RecyclerView mNoteDetailCivRvBody;
    @BindView(R.id.note_detail_cb_love)
    CheckBox mNoteDetailCbLove;
    @BindView(R.id.note_detail_tv_reply)
    TextView mNoteDetailTvReply;
    @BindView(R.id.note_detail_tv_comment)
    TextView mNoteDetailTvComment;
    @BindView(R.id.note_detail_rv_reply)
    RecyclerView mNoteDetailRvReply;

    private boolean isCollect = false;
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RecyclerView mReplyRecyclerView;
    private int isShowImage = View.VISIBLE;

    private List<FineNoteBodyBean> data;
    private PopupWindow mEditWindow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_note_detail);
        ButterKnife.bind(this);
        initHeader();
        initData();
        initRecyclerViewBody();
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new FineNoteBodyBean(2, "https://imgsa.baidu.com/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=41958fa300d162d991e36a4e70b6c289/9922720e0cf3d7ca3856fe11f81fbe096b63a931.jpg"));
        data.add(new FineNoteBodyBean(0, "不知何时开始，我们都成为了“只是那样”的大人。"));
        data.add(new FineNoteBodyBean(1, "崔爱拉梦想成为新闻主播，却屡屡落榜，迫于现实只能在百货公司服务台工作。"));
        data.add(new FineNoteBodyBean(2, "https://imgsa.baidu.com/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=a3c15eaba3014c080d3620f76b12696d/42166d224f4a20a40cc6efc59a529822730ed0dc.jpg"));
        data.add(new FineNoteBodyBean(1, "与爱罗青梅竹马的高东万，虽然有着成为格斗选手的梦想，现实中却只是一名平凡的约聘员工"));
        data.add(new FineNoteBodyBean(2, "https://imgsa.baidu.com/baike/c0%3Dbaike116%2C5%2C5%2C116%2C38/sign=8297dea2eafe9925df01610255c135ba/a9d3fd1f4134970a88f0d6489fcad1c8a6865dd2.jpg"));
        data.add(new FineNoteBodyBean(1, "没钱没背景、如配角般走在三流之路上的他们，携手战胜挫折与束缚，踏上人生的康庄大道"));

    }

    private void initRecyclerViewBody() {
        mNoteDetailCivRvBody.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        FineNoteBodyAdapter fineNoteBodyAdapter = new FineNoteBodyAdapter(data);
        mNoteDetailCivRvBody.setAdapter(fineNoteBodyAdapter);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("帖子详情");
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
        mHeaderIvRight2.setImageResource(R.drawable.icon_collect);
    }

    @OnClick({R.id.header_iv_right2, R.id.header_iv_right, R.id.note_detail_tv_reply, R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right2:
                if (!isCollect) {
                    isCollect = true;
                    mHeaderIvRight2.setImageResource(R.drawable.icon_collect_ok);
                } else {
                    isCollect = false;
                    mHeaderIvRight2.setImageResource(R.drawable.icon_collect);
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
            case R.id.note_detail_tv_reply:
                replyLZ();
                break;
        }
    }

    private void replyLZ() {
        View editView;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
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
//        View editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input, null);
        mEditWindow = new PopupWindow(editView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mEditWindow.setOutsideTouchable(true);
        mEditWindow.setFocusable(true);
        mEditWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        EditText replyEdit = (EditText) editView.findViewById(R.id.view_reply_et);
        mReplyRecyclerView = (RecyclerView) editView.findViewById(R.id.view_reply_recycler);
        editView.findViewById(R.id.view_reply_image).setVisibility(isShowImage);
        editView.findViewById(R.id.view_reply_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(3)
                        .setGridColumnCount(4)
                        .start(FineNoteDetailActivity.this);
            }
        });
        initPhotoPicker();
        replyEdit.setFocusable(true);
        replyEdit.requestFocus();
        // 以下两句不能颠倒
        mEditWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mEditWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditWindow.showAtLocation(FineNoteDetailActivity.this.findViewById(R.id.fine_note), Gravity.BOTTOM, 0, 0);
        // 显示键盘
        final InputMethodManager imm = (InputMethodManager) UIUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                                    .start(FineNoteDetailActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(FineNoteDetailActivity.this);
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
