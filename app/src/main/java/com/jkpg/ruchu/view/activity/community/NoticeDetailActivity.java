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
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.NoticReplyBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.NoticeDetailReplyAdapter;
import com.jkpg.ruchu.view.adapter.PhotoAdapter;
import com.jkpg.ruchu.view.adapter.RecyclerItemClickListener;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.jkpg.ruchu.widget.nineview.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

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
    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RecyclerView mReplyRecyclerView;
    List<NoticReplyBean> data;
    private NoticeDetailReplyAdapter mNoticeDetailReplyAdapter;
    private boolean isCollect = false;
    private int isShowImage = View.VISIBLE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        initNineView();
        initHeader();
        initRecyclerView();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("帖子详情");
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
        mHeaderIvRight2.setImageResource(R.drawable.icon_collect);
    }

    private void initRecyclerView() {
        data = new ArrayList<>();
        List<ImageInfo> imageInfos = new ArrayList<>();
        imageInfos.add(new ImageInfo("https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg"));
        imageInfos.add(new ImageInfo("https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg"));
        imageInfos.add(new ImageInfo("https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg"));
        List<ImageInfo> toImageInfos = new ArrayList<>();
        toImageInfos.add(new ImageInfo("https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg"));
        toImageInfos.add(new ImageInfo("https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg"));
        toImageInfos.add(new ImageInfo("https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg"));

        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "1楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", false, "3", "1", true, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));
        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "2楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", false, "3", "1", true, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));
        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "3楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", true, "3", "1", false, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));
        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "4楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", true, "3", "1", true, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));
        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "5楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", false, "3", "1", false, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));
        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "6楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", true, "3", "1", true, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));
        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "7楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", false, "3", "1", false, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));
        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "8楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", true, "3", "1", false, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));
        data.add(new NoticReplyBean("", "https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg",
                "哈哈", "9楼", "三个月", "哈哈哈啊哈哈哈好开心啊", imageInfos, "2017-05-21", false, "3", "1", true, "呵呵", "1楼", "2015-15-78", "就要哈哈哈", toImageInfos));

        mNoticeDetailReplyRecycler.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mNoticeDetailReplyAdapter = new NoticeDetailReplyAdapter(R.layout.item_notic_reply, data);
        mNoticeDetailReplyRecycler.setAdapter(mNoticeDetailReplyAdapter);
        mNoticeDetailReplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        mNoticeDetailReplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (data.get(position).isReply) {
                    isShowImage = View.GONE;
                } else {
                    isShowImage = View.VISIBLE;

                }
                replyLZ();
            }
        });
        // 关掉默认动画
        mNoticeDetailReplyRecycler.getItemAnimator().setAddDuration(0);
        mNoticeDetailReplyRecycler.getItemAnimator().setChangeDuration(0);
        mNoticeDetailReplyRecycler.getItemAnimator().setMoveDuration(0);
        mNoticeDetailReplyRecycler.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mNoticeDetailReplyRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
    }


    private void initNineView() {
        imageInfo = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ImageInfo info = new ImageInfo("https://imgsa.baidu.com/baike/c0%3Dbaike220%2C5%2C5%2C220%2C73/sign=0791f0edbe8f8c54f7decd7d5b404690/a9d3fd1f4134970a988e262c9fcad1c8a7865d37.jpg");
            this.imageInfo.add(info);
        }
        mNoticeDetailNineView.setAdapter(new NineGridViewClickAdapter(UIUtils.getContext(), imageInfo));
    }

    @OnClick({R.id.header_iv_left, R.id.header_iv_right2, R.id.header_iv_right, R.id.notice_detail_civ_photo, R.id.notice_detail_tv_reply})
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
            case R.id.notice_detail_civ_photo:
                break;
            case R.id.notice_detail_tv_reply:
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
