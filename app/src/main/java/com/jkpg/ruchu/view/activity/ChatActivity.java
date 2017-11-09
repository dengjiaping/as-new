package com.jkpg.ruchu.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.ChatListBean;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.ChatListRVAdapter;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.preview.ChatImagePreviewActivity;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

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
    @BindView(R.id.chat_recycler_view)
    RecyclerView mChatRecyclerView;
    @BindView(R.id.view_reply_image)
    ImageView mViewReplyImage;
    @BindView(R.id.view_reply_et)
    EditText mViewReplyEt;
    @BindView(R.id.view_reply_btn)
    TextView mViewReplyBtn;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;
    private List<ChatListBean> mChatLists;
    private ChatListRVAdapter mChatListRVAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private TIMConversation mConversation;


    private String myImgUrl = "http://dwz.cn/6GtbF2";
    private String youImgUrl = "http://dwz.cn/6GtdEd";
    private TIMConversationExt mConExt;
    private String mPeer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mPeer = getIntent().getStringExtra("peer");
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");// 用户 头像 !!!
        mHeaderTvTitle.setText(name);
        mHeaderIvRight.setImageResource(R.drawable.chat_more);


//        获取单聊会话
//        String peer = "c12345678";  //获取与用户 "sample_user_1" 的会话
        //会话类型：单聊
        mConversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                mPeer);
        //获取会话扩展实例
        mConExt = new TIMConversationExt(mConversation);

        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                LogUtils.e("getSelfProfile failed: " + code + " desc");
            }

            @Override
            public void onSuccess(TIMUserProfile result) {
                LogUtils.e("getSelfProfile succ");
                LogUtils.e("identifier: " + result.getIdentifier() + " nickName: " + result.getNickName()
                        + " remark: " + result.getRemark() + " allow: " + result.getAllowType());
            }
        });
        //将此会话的所有消息标记为已读
        mConExt.setReadMessage(null, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.e("setReadMessage failed, code: " + code + "|desc: " + desc);
            }

            @Override
            public void onSuccess() {
                LogUtils.d("setReadMessage succ");
            }
        });

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

        //获取此会话的消息
        mConExt.getMessage(50, //获取此会话最近的10条消息
                null, //不指定从哪条消息开始获取 - 等同于从最新的消息开始往前
                new TIMValueCallBack<List<TIMMessage>>() {//回调接口
                    @Override
                    public void onError(int code, String desc) {//获取消息失败
                        //接口返回了错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code含义请参见错误码表
                        LogUtils.d("get message failed. code: " + code + " errmsg: " + desc);
                    }

                    @Override
                    public void onSuccess(List<TIMMessage> msgs) {//获取消息成功
                        //遍历取得的消息
                        for (TIMMessage msg : msgs) {
//                            lastMsg = msg;
                            //可以通过timestamp()获得消息的时间戳, isSelf()是否为自己发送的消息
                            LogUtils.e("get msg: " + msg.timestamp() + " self: " + msg.isSelf()
//                                    + " msg-name: " + msg.getSenderProfile().getNickName() //null
//                                    + " msg-url: " + msg.getSenderProfile().getFaceUrl()   //null
//                                    + " msg : " + msg
                            );


                            for (int i = 0; i < msg.getElementCount(); ++i) {
                                TIMElem elem = msg.getElement(i);
                                //获取当前元素的类型
                                TIMElemType elemType = elem.getType();
                                LogUtils.d("elem type: " + elemType.name());
                                if (elemType == TIMElemType.Text) {
                                    //处理文本消息
                                    TIMTextElem textElem = (TIMTextElem) elem;

                                    LogUtils.d("msg: " + textElem.getText());

                                    if (!msg.isSelf()) {
                                        mChatLists.add(new ChatListBean(0, msg.timestamp(), youImgUrl, textElem.getText()));
                                    } else {
                                        mChatLists.add(new ChatListBean(1, msg.timestamp(), myImgUrl, textElem.getText()));
                                    }
                                } else if (elemType == TIMElemType.Image) {
                                    //处理图片消息
                                    //图片元素
                                    TIMImageElem e = (TIMImageElem) elem;
//                                    for (TIMImage image : e.getImageList()) {
                                    ArrayList<TIMImage> imageList = e.getImageList();
                                    for (TIMImage timImage : imageList) {
                                        //获取图片类型, 大小, 宽高
                                        LogUtils.d("image type: " + timImage.getType() +
//                                                " image size " + image.getSize() +
                                                " image url " + timImage.getUrl() +
                                                " image height " + timImage.getHeight() +
                                                " image width " + timImage.getWidth() +
                                                "");
                                    }
                                    TIMImage image = imageList.get(1);
                                    TIMImage largeImage = imageList.get(2);

                                    if (!msg.isSelf()) {
                                        ChatListBean chatListBean = new ChatListBean(2, msg.timestamp(), youImgUrl, image.getUrl());
                                        chatListBean.setImageHeight(image.getHeight());
                                        chatListBean.setImageWeidth(image.getWidth());
                                        chatListBean.setLargeImage(largeImage.getUrl());
                                        mChatLists.add(chatListBean);
                                    } else {
                                        ChatListBean chatListBean = new ChatListBean(3, msg.timestamp(), myImgUrl, image.getUrl());
                                        chatListBean.setImageHeight(image.getHeight());
                                        chatListBean.setImageWeidth(image.getWidth());
                                        chatListBean.setLargeImage(largeImage.getUrl());
                                        mChatLists.add(chatListBean);
                                    }
//                                        }
                                }//...处理更多消息
                            }
                        }
                        Collections.reverse(mChatLists);
                        mChatListRVAdapter.notifyDataSetChanged();
                    }
                });
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
                            .hideSoftInputFromWindow(mViewReplyEt.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        mChatListRVAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.chat_image_you:
                    case R.id.chat_image_you_image:
                        ToastUtils.showShort(UIUtils.getContext(), "you");
                        break;
                    case R.id.chat_image_my_image:
                    case R.id.chat_image_my:
                        ToastUtils.showShort(UIUtils.getContext(), "my");
                        break;
                    case R.id.chat_photo_you:
                    case R.id.chat_photo_my:
                        List<ImageInfo> images = new ArrayList<>();
                        images.add(new ImageInfo(mChatLists.get(position).largeImage));
                        Intent intent = new Intent(ChatActivity.this, ChatImagePreviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ChatImagePreviewActivity.IMAGE_INFO, (Serializable) images);
                        bundle.putInt(ChatImagePreviewActivity.CURRENT_ITEM, 0);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
//                        ToastUtils.showShort(UIUtils.getContext(), images.get(0).getBigImageUrl());
                        break;
                }

            }
        });
        mChatListRVAdapter.setUpFetchEnable(true);      // 上拉加载开关
        mChatListRVAdapter.setStartUpFetchPosition(2);  // 开始加载位置
        mChatListRVAdapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {

//                /**
//                 * set fetching on when start network request.
//                 */
//                mChatListRVAdapter.setUpFetching(true);
//                /**
//                 * get data from internet.
//                 */
//                mRecyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAdapter.addData(0, genData());
//                        /**
//                         * set fetching off when network request ends.
//                         */
//                        mAdapter.setUpFetching(false);
//                        /**
//                         * set fetch enable false when you don't need anymore.
//                         */
//                        if (count > 5) {
//                            mAdapter.setUpFetchEnable(false);
//                        }
//                    }
//                }, 300);
            }
        });
    }

    @OnClick({R.id.header_iv_left, R.id.header_iv_right, R.id.view_reply_image, R.id.view_reply_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right:
                View inflate = LayoutInflater.from(this).inflate(R.layout.view_chat_menu, null);
                final PopupWindow popupWindow = new PopupWindow(this);
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(inflate);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(mHeaderView, 0, 0, Gravity.RIGHT);
                inflate.findViewById(R.id.chat_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> identifiers = new ArrayList<>();
                        identifiers.add(mPeer);
                        TIMFriendshipManagerExt.getInstance().addBlackList(identifiers, new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                ToastUtils.showShort(UIUtils.getContext(), "加入黑名单失败,请重试");
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                ToastUtils.showShort(UIUtils.getContext(), "加入黑名单成功");
                            }
                        });
                        popupWindow.dismiss();
                    }
                });


                break;
            case R.id.view_reply_image:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setGridColumnCount(4)
                        .start(ChatActivity.this);
                break;
            case R.id.view_reply_btn:

                //构造一条消息
                final TIMMessage msg = new TIMMessage();

                //添加文本内容
                TIMTextElem elem = new TIMTextElem();
                elem.setText(mViewReplyEt.getText().toString());

                //将elem添加到消息
                if (msg.addElement(elem) != 0) {
                    LogUtils.d("addElement failed");
                    return;
                }
                String etString = mViewReplyEt.getText().toString();
                if (etString.equals("")) {
                    return;
                }
                mChatLists.add(mChatLists.size(), new ChatListBean(1, new Date().getTime() / 1000, myImgUrl, etString));
                mChatLists.get(mChatLists.size() - 1).setSending(true);
                mChatListRVAdapter.notifyDataSetChanged();
                mViewReplyEt.setText("");
                mChatRecyclerView.scrollToPosition(mChatLists.size() - 1);
                //发送消息
                mConversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                    @Override
                    public void onError(int code, String desc) {//发送消息失败
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code含义请参见错误码表
                        LogUtils.d("send message failed. code: " + code + " errmsg: " + desc);
                        mChatLists.get(mChatLists.size() - 1).setError(true);
                        mChatLists.get(mChatLists.size() - 1).setSending(false);
//                        mChatListRVAdapter.notifyItemChanged(mChatLists.size() - 1);
                        mChatListRVAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onSuccess(TIMMessage msg) {//发送消息成功
                        LogUtils.e("SendMsg ok");
                        mChatLists.get(mChatLists.size() - 1).setError(false);
                        mChatLists.get(mChatLists.size() - 1).setSending(false);
//                        mChatListRVAdapter.notifyItemChanged(mChatLists.size() - 1);
                        mChatListRVAdapter.notifyDataSetChanged();
                    }
                });

//                TIMConversationExt conExt = new TIMConversationExt(mConversation);
                mConExt.setDraft(null);
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
            final Uri uri = Uri.fromFile(new File(selectedPhotos.get(0)));
            LogUtils.d("Url = " + uri);
            LogUtils.d("path = " + selectedPhotos.get(0));


            //构造一条消息
            TIMMessage msg = new TIMMessage();

            //添加图片
            TIMImageElem elem = new TIMImageElem();
            elem.setPath(selectedPhotos.get(0));
            Bitmap thumb = getThumb(selectedPhotos.get(0));
            int height = thumb.getHeight();
            int width = thumb.getWidth();

            //将elem添加到消息
            if (msg.addElement(elem) != 0) {
                LogUtils.d("addElement failed");
                return;
            }
            mChatLists.add(new ChatListBean(3, new Date().getTime() / 1000, myImgUrl, uri + ""));
            ChatListBean chatListBean = mChatLists.get(mChatLists.size() - 1);
            chatListBean.setSending(true);
            chatListBean.setImageWeidth(width);
            chatListBean.setImageHeight(height);
            chatListBean.setNewSend(false);
            chatListBean.setLargeImage(uri + "");
            mChatListRVAdapter.notifyDataSetChanged();
            mChatRecyclerView.scrollToPosition(mChatLists.size() - 1);
//
//            mChatListRVAdapter.notifyDataSetChanged();

            //发送消息

            mConversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                @Override
                public void onError(int code, String desc) {//发送消息失败
                    //错误码code和错误描述desc，可用于定位请求失败原因
                    //错误码code列表请参见错误码表
                    LogUtils.d("send message failed. code: " + code + " errmsg: " + desc);
                    mChatLists.get(mChatLists.size() - 1).setSending(false);
                    mChatLists.get(mChatLists.size() - 1).setError(true);
//                    mChatListRVAdapter.notifyItemChanged(mChatLists.size()-1);
                    mChatListRVAdapter.notifyDataSetChanged();

                }


                @Override
                public void onSuccess(TIMMessage msg) {//发送消息成功
                    LogUtils.e("SendMsg ok");
                    mChatLists.get(mChatLists.size() - 1).setSending(false);
                    mChatLists.get(mChatLists.size() - 1).setError(false);
//                    mChatListRVAdapter.notifyItemChanged(mChatLists.size()-1);
                    mChatListRVAdapter.notifyDataSetChanged();
                }
            });


        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().post("RefreshIMList");


        // 设置草稿
        String s = mViewReplyEt.getText().toString();
        if (StringUtils.isEmpty(s)) {
            return;
        }
        TIMTextElem timTextElem = new TIMTextElem();
        timTextElem.setText(s);
        TIMMessageDraft timMessageDraft = new TIMMessageDraft();
        timMessageDraft.addElem(timTextElem);

        mConExt.setDraft(timMessageDraft);


//        将此会话的所有消息标记为已读
        mConExt.setReadMessage(null, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                LogUtils.e("setReadMessage failed, code: " + code + "|desc: " + desc);
            }

            @Override
            public void onSuccess() {
                LogUtils.d("setReadMessage succ   onStop");
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mConExt.hasDraft()) {
            List<TIMElem> elems = mConExt.getDraft().getElems();
            for (TIMElem elem : elems) {
                mViewReplyEt.setText(((TIMTextElem) elem).getText());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 生成缩略图
     * 缩略图是将原图等比压缩，压缩后宽、高中较小的一个等于198像素
     * 详细信息参见文档
     */
    private Bitmap getThumb(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width = options.outWidth, height = options.outHeight;
        if (width > height) {
            reqWidth = 198;
            reqHeight = (reqWidth * height) / width;
        } else {
            reqHeight = 198;
            reqWidth = (width * reqHeight) / height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try {
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        } catch (IOException e) {
            return null;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newSend(List<TIMMessage> list) {
        for (TIMMessage timMessage : list) {
            LogUtils.d(" 新消息 --- ChatActivity --- start");
            if (timMessage.getSenderProfile().getIdentifier().equals(mPeer) && !timMessage.isSelf()) {
                LogUtils.d(timMessage.isSelf() + " 对方");
                for (int i = 0; i < timMessage.getElementCount(); ++i) {
                    TIMElem elem = timMessage.getElement(i);
                    //获取当前元素的类型
                    TIMElemType elemType = elem.getType();
                    LogUtils.d("elem type: " + elemType.name());
                    if (elemType == TIMElemType.Text) {
                        //处理文本消息
                        TIMTextElem textElem = (TIMTextElem) elem;

                        LogUtils.d("msg: " + textElem.getText());

                        mChatLists.add(mChatLists.size(), new ChatListBean(0, timMessage.timestamp(), youImgUrl, textElem.getText()));
                        mChatListRVAdapter.notifyDataSetChanged();
                        mChatRecyclerView.scrollToPosition(mChatLists.size() - 1);
                    } else if (elemType == TIMElemType.Image) {
                        //处理图片消息
                        //图片元素
                        TIMImageElem e = (TIMImageElem) elem;
//                        for (TIMImage image : e.getImageList()) {
                        ArrayList<TIMImage> imageList = e.getImageList();

                        TIMImage image = imageList.get(1);
                        TIMImage largeImage = imageList.get(2);
//                            if (image.getType() == TIMImageType.Thumb) {
                        //获取图片类型, 大小, 宽高
                        LogUtils.d("image type: " + image.getType() +
//                                                " image size " + image.getSize() +
                                " image url " + image.getUrl() +
                                " image height " + image.getHeight() +
                                " image width " + image.getWidth() +
                                "");

                        ChatListBean chatListBean = new ChatListBean(2, timMessage.timestamp(), youImgUrl, image.getUrl());
                        chatListBean.setImageHeight(image.getHeight());
                        chatListBean.setImageWeidth(image.getWidth());
                        chatListBean.setLargeImage(largeImage.getUrl());
                        mChatLists.add(mChatLists.size(), chatListBean);
                        mChatListRVAdapter.notifyDataSetChanged();
                        mChatRecyclerView.scrollToPosition(mChatLists.size() - 1);
//                            }
//                        }
                    }//...处理更多消息
                }
            }
        }
    }
}
