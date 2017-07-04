package com.jkpg.ruchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.ChatListBean;
import com.jkpg.ruchu.view.adapter.ChatListRVAdapter;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.preview.ImagePreviewActivity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by qindi on 2017/6/23.
 */

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.view_chat_image)
    CircleImageView mViewChatImage;
    @BindView(R.id.view_chat_name)
    TextView mViewChatName;
    @BindView(R.id.view_chat_rating)
    MaterialRatingBar mViewChatRating;
    @BindView(R.id.view_chat_grade)
    TextView mViewChatGrade;
    @BindView(R.id.view_chat_time)
    TextView mViewChatTime;
    @BindView(R.id.view_chat_appraise)
    TextView mViewChatAppraise;
    @BindView(R.id.chat_recycler_view)
    RecyclerView mChatRecyclerView;
    @BindView(R.id.chat_refresh_layout)
    SwipeRefreshLayout mChatRefreshLayout;
    @BindView(R.id.view_reply_image)
    ImageView mViewReplyImage;
    @BindView(R.id.view_reply_et)
    EditText mViewReplyEt;
    @BindView(R.id.view_reply_btn)
    TextView mViewReplyBtn;
    private List<ChatListBean> mChatLists;
    private ChatListRVAdapter mChatListRVAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initChatData();
        initChatList();
        initSend();
    }

    private void initSend() {
        mViewReplyEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mChatRecyclerView.scrollToPosition(mChatLists.size() - 1);
            }
        });
    }

    private void initChatData() {
        mChatLists = new ArrayList<>();
        mChatLists.add(new ChatListBean(4, "", "", "哈哈哈哈哈哈"));
        mChatLists.add(new ChatListBean(0, "", "http://e.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=8e61b262b4096b63811959563408e079/c2fdfc039245d6885018c7e9aec27d1ed21b2460.jpg", "你好"));
        mChatLists.add(new ChatListBean(3, "", "", ""));
        mChatLists.add(new ChatListBean(1, "", "http://f.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=dfb4068f988fa0ec7fc7630b1eac3ed3/4034970a304e251f28ae2044ae86c9177f3e5335.jpg", "我不好啊"));
        mChatLists.add(new ChatListBean(0, "", "http://e.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=8e61b262b4096b63811959563408e079/c2fdfc039245d6885018c7e9aec27d1ed21b2460.jpg", "开心吗"));
        mChatLists.add(new ChatListBean(1, "", "http://f.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=dfb4068f988fa0ec7fc7630b1eac3ed3/4034970a304e251f28ae2044ae86c9177f3e5335.jpg", "不开心啊"));
        mChatLists.add(new ChatListBean(2, "", "", ""));
        mChatLists.add(new ChatListBean(0, "", "http://e.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=8e61b262b4096b63811959563408e079/c2fdfc039245d6885018c7e9aec27d1ed21b2460.jpg", "为什么"));
        mChatLists.add(new ChatListBean(1, "", "http://f.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=dfb4068f988fa0ec7fc7630b1eac3ed3/4034970a304e251f28ae2044ae86c9177f3e5335.jpg", "骗你的呀"));
        mChatLists.add(new ChatListBean(0, "", "http://e.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=8e61b262b4096b63811959563408e079/c2fdfc039245d6885018c7e9aec27d1ed21b2460.jpg", "装作没有梦想，反而活得更好。 \n" +
                "如果有了梦想，人心就会变得微不足道。"));
    }

    private void initChatList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(layoutManager);
        mChatListRVAdapter = new ChatListRVAdapter(mChatLists);
        mChatRecyclerView.setAdapter(mChatListRVAdapter);
        mChatRecyclerView.scrollToPosition(mChatLists.size() - 1);
        mChatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滑动时候输入框失去焦点
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    mViewReplyEt.clearFocus();
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        mChatListRVAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<ImageInfo> images = new ArrayList<>();
                images.add(new ImageInfo(mChatLists.get(position).content));
                Intent intent = new Intent(ChatActivity.this, ImagePreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ImagePreviewActivity.IMAGE_INFO, (Serializable) images);
                bundle.putInt(ImagePreviewActivity.CURRENT_ITEM, 0);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    @OnClick({R.id.header_iv_left, R.id.header_tv_right, R.id.view_chat_appraise, R.id.view_reply_image, R.id.view_reply_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_tv_right:
                break;
            case R.id.view_chat_appraise:
                break;
            case R.id.view_reply_image:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setGridColumnCount(4)
                        .start(ChatActivity.this);
                break;
            case R.id.view_reply_btn:
                String etString = mViewReplyEt.getText().toString();
                if (etString.equals("")){
                    return;
                }
                mChatLists.add(mChatLists.size(), new ChatListBean(1, "", "http://f.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=dfb4068f988fa0ec7fc7630b1eac3ed3/4034970a304e251f28ae2044ae86c9177f3e5335.jpg", etString));
                mChatLists.add(new ChatListBean(0, "", "http://e.hiphotos.baidu.com/baike/w%3D268%3Bg%3D0/sign=8e61b262b4096b63811959563408e079/c2fdfc039245d6885018c7e9aec27d1ed21b2460.jpg", etString));
                mChatListRVAdapter.notifyDataSetChanged();
                mViewReplyEt.setText("");
                mChatRecyclerView.scrollToPosition(mChatLists.size() - 1);
                break;
        }
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
            }
            Uri uri = Uri.fromFile(new File(selectedPhotos.get(0)));
            mChatLists.add(new ChatListBean(3, "", "", uri + ""));
            mChatLists.add(new ChatListBean(2, "", "", uri + ""));
            mChatListRVAdapter.notifyDataSetChanged();
            mChatRecyclerView.scrollToPosition(mChatLists.size() - 1);

        }
    }
}
