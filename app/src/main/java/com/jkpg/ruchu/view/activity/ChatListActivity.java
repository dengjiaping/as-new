package com.jkpg.ruchu.view.activity;

import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.CustomElemBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.FineNoteDetailWebFixActivity;
import com.jkpg.ruchu.view.activity.community.NoticeDetailFixActivity;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.ChatListAdapter;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.preview.ChatImagePreviewActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/10/24.
 */

public class ChatListActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.chat_recycler_view)
    RecyclerView mChatRecyclerView;
    @BindView(R.id.view_reply_et)
    EditText mViewReplyEt;
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;
    private String mPeer;
    private TIMConversationExt mConExt;

    List<TIMMessage> timMsgs;
    private ChatListAdapter mChatListAdapter;

    private String myImgUrl;
    private String youImgUrl;
    private TIMConversation mConversation;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private boolean mV;
    private AlertDialog mDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        myImgUrl = SPUtils.getString(UIUtils.getContext(), Constants.IMIMAGE, "");
        mPeer = getIntent().getStringExtra("peer");
        String name = getIntent().getStringExtra("name");
        youImgUrl = getIntent().getStringExtra("image");// 用户 头像 !!!
        mV = getIntent().getBooleanExtra("v", false);

        mHeaderTvTitle.setText(name);
        mHeaderIvRight.setImageResource(R.drawable.chat_more);
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatListActivity.this, R.style.dialog);
        builder.setView(View.inflate(UIUtils.getContext(), R.layout.view_animation, null));
        mDialog = builder.show();
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                    Toast.makeText(UIUtils.getContext(), getRunningActivityName(activity), Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();


//        获取单聊会话
//        String peer = "c12345678";  //获取与用户 "sample_user_1" 的会话
        //会话类型：单聊
        //会话类型：单聊
        mConversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                mPeer);
        //获取会话扩展实例
        mConExt = new TIMConversationExt(mConversation);
        //获取自己的资料
        getSelfProfile();
        //设置已读消息
        setReadMessage();
        //获取数据
        initChatData();
        //列表
//        initRecyclerView();
        //显示草稿
        showDraft();

        NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(MyApplication.getContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(1);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        myImgUrl = SPUtils.getString(UIUtils.getContext(), Constants.IMIMAGE, "");
        mPeer = intent.getStringExtra("peer");
        String name = intent.getStringExtra("name");
        youImgUrl = intent.getStringExtra("image");// 用户 头像 !!!
        mV = getIntent().getBooleanExtra("v", false);

        mHeaderTvTitle.setText(name);
        mHeaderIvRight.setImageResource(R.drawable.chat_more);
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatListActivity.this, R.style.dialog);
        builder.setView(View.inflate(UIUtils.getContext(), R.layout.view_animation, null));
        mDialog = builder.show();
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                    Toast.makeText(UIUtils.getContext(), getRunningActivityName(activity), Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

//        获取单聊会话
//        String peer = "c12345678";  //获取与用户 "sample_user_1" 的会话
        //会话类型：单聊
        //会话类型：单聊
        mConversation = TIMManager.getInstance().getConversation(
                TIMConversationType.C2C,    //会话类型：单聊
                mPeer);
        //获取会话扩展实例
        mConExt = new TIMConversationExt(mConversation);
        //获取自己的资料
        getSelfProfile();
        //设置已读消息
        setReadMessage();
        //获取数据
        initChatData();
        //列表
//        initRecyclerView();
        //显示草稿
        showDraft();


        NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(MyApplication.getContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    private void getProfile() {
        //待获取用户资料的用户列表
        List<String> users = new ArrayList<>();
        users.add(mPeer);

//获取用户资料
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int code, String desc) {
//                initRecyclerView();

            }

            @Override
            public void onSuccess(List<TIMUserProfile> result) {
                Map<String, byte[]> customInfo = result.get(0).getCustomInfo();
                byte[] tag = customInfo.get("Tag_Profile_Custom_ruchuxzs");
                if (tag != null) {
                    mV = true;
                }
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                initRecyclerView();
            }
        });
    }

    private void showDraft() {
        if (mConExt.hasDraft()) {
            List<TIMElem> elems = mConExt.getDraft().getElems();
            for (TIMElem elem : elems) {
                mViewReplyEt.setText(((TIMTextElem) elem).getText());
            }
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(layoutManager);
        mChatListAdapter = new ChatListAdapter(R.layout.item_chat, timMsgs, myImgUrl, youImgUrl, mV);
        mChatRecyclerView.setAdapter(mChatListAdapter);
        mChatListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.chat_image_my:
//                        ToastUtils.showShort(UIUtils.getContext(), "my");
                        Intent in = new Intent(ChatListActivity.this, FansCenterActivity.class);
                        in.putExtra("fansId", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""));
                        startActivity(in);
                        break;
                    case R.id.chat_image_you:
                        Intent i = new Intent(ChatListActivity.this, FansCenterActivity.class);
                        i.putExtra("fansId", mPeer);
                        startActivity(i);
//                        ToastUtils.showShort(UIUtils.getContext(), "you");
                        break;
                    case R.id.chat_photo_you:
                    case R.id.chat_photo_my:
                        List<ImageInfo> images = new ArrayList<>();
                        if (((TIMImageElem) timMsgs.get(position).getElement(0)).getImageList().size() == 0) {
                            images.add(new ImageInfo((((TIMImageElem) timMsgs.get(position).getElement(0)).getPath())));
                        } else {
                            TIMImage timImage = ((TIMImageElem) timMsgs.get(position).getElement(0)).getImageList().get(2);
                            images.add(new ImageInfo(timImage.getUrl()));
                        }
                        Intent intent = new Intent(ChatListActivity.this, ChatImagePreviewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ChatImagePreviewActivity.IMAGE_INFO, (Serializable) images);
                        bundle.putInt(ChatImagePreviewActivity.CURRENT_ITEM, 0);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.chat_custom_ll:
                        TIMElem element = timMsgs.get(position).getElement(0);
                        TIMCustomElem e = (TIMCustomElem) element;
                        String s = new String(e.getData());
                        CustomElemBean customElemBean = new Gson().fromJson(s, CustomElemBean.class);
                        switch (customElemBean.type) {
                            case "1":
                                Intent intent1 = new Intent(ChatListActivity.this, ShopActivity.class);
                                intent1.putExtra("url", customElemBean.content);
                                startActivity(intent1);
                                break;
                            case "2":
                                Intent intent2 = new Intent(ChatListActivity.this, NoticeDetailFixActivity.class);
                                intent2.putExtra("bbsid", customElemBean.content);
                                startActivity(intent2);
                                break;
                            case "3":
                                Intent intent3 = new Intent(ChatListActivity.this, FineNoteDetailWebFixActivity.class);
                                intent3.putExtra("art_id", customElemBean.content);
                                startActivity(intent3);
                                break;
                            case "4":
                                break;
                        }
                        break;
                }
            }
        });
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
        mChatListAdapter.setUpFetchEnable(true);
        mChatListAdapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {
                mChatListAdapter.setUpFetching(true);

                mConExt.getMessage(10, //获取此会话最近的10条消息
                        timMsgs.get(0), //不指定从哪条消息开始获取 - 等同于从最新的消息开始往前
                        new TIMValueCallBack<List<TIMMessage>>() {//回调接口
                            @Override
                            public void onError(int code, String desc) {//获取消息失败
                                //接口返回了错误码code和错误描述desc，可用于定位请求失败原因
                                //错误码code含义请参见错误码表
                                LogUtils.d("get message failed. code: " + code + " errmsg: " + desc);
                            }

                            @Override
                            public void onSuccess(List<TIMMessage> msgs) {//获取消息成功
//                                timMsgs = msgs;
                                ArrayList<TIMMessage> timMessages = new ArrayList<>();
                                Collections.reverse(msgs);

                                for (int i = 0; i < msgs.size(); i++) {
                                    TIMElem elem = msgs.get(i).getElement(0);
                                    //获取当前元素的类型
                                    TIMElemType elemType = elem.getType();
                                    if (elemType == TIMElemType.Text
                                            || elemType == TIMElemType.Image
                                            || elemType == TIMElemType.Custom) {
                                        timMessages.add(msgs.get(i));
                                    }
                                }

                                mChatListAdapter.addData(0, timMessages);
                                mChatListAdapter.setUpFetching(false);
                            }
                        });
            }
        });
        mChatListAdapter.setStartUpFetchPosition(2);
        mChatListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {

                view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        if (timMsgs.get(position).getElement(0).getType() == TIMElemType.Text) {
                            menu.add(0, position, 0, "复制");

                        }
//                        menu.add(0, position, 1, "删除");
                        if (timMsgs.get(position).isSelf() && timMsgs.get(position).status() == TIMMessageStatus.SendFail) {
                            menu.add(0, position, 2, "重发发送");
                        }
                    }
                });
                return false;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getOrder()) {
            case 0:
                TIMMessage timMessage = timMsgs.get(item.getItemId());
                String text = ((TIMTextElem) timMessage.getElement(0))
                        .getText();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                assert cm != null;
                cm.setPrimaryClip(ClipData.newPlainText(null, text));

                break;
            case 1:
                break;
            case 2:

                TIMMessage timMessage1 = timMsgs.get(item.getItemId());
                TIMElemType type = timMessage1.getElement(0).getType();
                TIMMessage msg = new TIMMessage();

                if (type == TIMElemType.Text) {

                    String text1 = ((TIMTextElem) timMessage1.getElement(0))
                            .getText();
                    new TIMMessageExt(timMessage1).remove();
                    timMsgs.remove(item.getItemId());

                    //构造一条消息

                    //添加文本内容
                    TIMTextElem elem = new TIMTextElem();
                    elem.setText(text1);

                    //将elem添加到消息
                    if (msg.addElement(elem) != 0) {
                        LogUtils.d("addElement failed");
                        break;
                    }
                    timMsgs.add(msg);
                    mChatListAdapter.notifyDataSetChanged();
                    mViewReplyEt.setText("");
                    mConExt.setDraft(null); //取消草稿
                } else if (type == TIMElemType.Image) {
                    String path = ((TIMImageElem) timMessage1.getElement(0)).getPath();
                    if (new File(path).exists()) {
                        new TIMMessageExt(timMessage1).remove();
                        timMsgs.remove(item.getItemId());
                        TIMImageElem timImageElem = new TIMImageElem();
                        timImageElem.setPath(path);
                        //将elem添加到消息
                        if (msg.addElement(timImageElem) != 0) {
                            LogUtils.d("addElement failed");
                            break;
                        }
                        timMsgs.add(msg);
                        mChatListAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showShort(UIUtils.getContext(), "图片不存在");
                    }
                }
                mChatRecyclerView.scrollToPosition(timMsgs.size() - 1);
                //发送消息
                mConversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                    @Override
                    public void onError(int code, String desc) {//发送消息失败
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code含义请参见错误码表
                        LogUtils.d("send message failed. code: " + code + " errmsg: " + desc);
                        mChatListAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onSuccess(TIMMessage msg) {//发送消息成功
                        LogUtils.e("SendMsg ok");
                        mChatListAdapter.notifyDataSetChanged();
                    }
                });

                break;
        }
        return super.onContextItemSelected(item);
    }

    private void getSelfProfile() {
        //获取自己的资料
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
    }

    private void setReadMessage() {
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
    }

    private void initChatData() {
        timMsgs = new ArrayList<>();
        //获取此会话的消息
        mConExt.getMessage(20, //获取此会话最近的10条消息
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
//                        timMsgs = msgs;
                        for (int i = 0; i < msgs.size(); i++) {
                            TIMElem elem = msgs.get(i).getElement(0);
                            //获取当前元素的类型
                            TIMElemType elemType = elem.getType();
                            if (elemType == TIMElemType.Text
                                    || elemType == TIMElemType.Image
                                    || elemType == TIMElemType.Custom) {
                                timMsgs.add(msgs.get(i));
                            }
                        }
                        Collections.reverse(timMsgs);
                        getProfile();

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
                showBlackPopupWindow();
                break;
            case R.id.view_reply_image:
                showPhotoView();
                break;
            case R.id.view_reply_btn:
                sendTextMess();
                break;
        }
    }

    private void sendTextMess() {
        String etString = mViewReplyEt.getText().toString();
        if (etString.equals("")) {
            return;
        }
        //构造一条消息
        TIMMessage msg = new TIMMessage();

        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(etString);

        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            LogUtils.d("addElement failed");
            return;
        }
        timMsgs.add(msg);

        mChatListAdapter.notifyDataSetChanged();
        mViewReplyEt.setText("");
        mConExt.setDraft(null); //取消草稿
        mChatRecyclerView.scrollToPosition(timMsgs.size() - 1);
        //发送消息
        mConversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
            @Override
            public void onError(int code, String desc) {//发送消息失败
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code含义请参见错误码表
                LogUtils.d("send message failed. code: " + code + " errmsg: " + desc);
                mChatListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(TIMMessage msg) {//发送消息成功
                LogUtils.e("SendMsg ok");
                mChatListAdapter.notifyDataSetChanged();
            }
        });


    }

    private void showPhotoView() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setGridColumnCount(4)
                .start(ChatListActivity.this);
    }

    private void showBlackPopupWindow() {
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
                        ToastUtils.showShort(UIUtils.getContext(), "已加入黑名单，将取消对她的关注并不再收到她的任何消息");
                        OkGo
                                .post(AppUrl.ADDHEIMINGDAN)
                                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                .params("othernameid", mPeer)
                                .params("flag", 1)
                                .execute(new StringDialogCallback(ChatListActivity.this) {
                                    @Override
                                    public void onSuccess(String s, Call call, Response response) {
                                        EventBus.getDefault().post("addBlack");
                                        TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, mPeer);
                                        EventBus.getDefault().post("RefreshIMList");
                                    }
                                });
                    }
                });
                popupWindow.dismiss();
            }
        });
        inflate.findViewById(R.id.chat_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"色情、裸露", "广告、推销", "恶意骚扰、不文明语言", "其他"};
                new AlertDialog.Builder(ChatListActivity.this)
                        .setTitle("请告诉我们举报原因,帮助我们让如初康复变得更好.")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OkGo
                                        .post(AppUrl.JUBAO)
                                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                        .params("nameid", mPeer)
                                        .params("jbmsg", items[which])
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
//                                                ToastUtils.showShort(UIUtils.getContext(), "举报成功!");
                                            }
                                        });
                                dialog.dismiss();
                                ToastUtils.showShort(UIUtils.getContext(), "举报成功!");
                            }
                        })
                        .show();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setReadMessage();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
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

            //将elem添加到消息
            if (msg.addElement(elem) != 0) {
                LogUtils.d("addElement failed");
                return;
            }

            timMsgs.add(msg);
            mChatListAdapter.notifyDataSetChanged();
            mChatRecyclerView.scrollToPosition(timMsgs.size() - 1);

            //发送消息

            mConversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                @Override
                public void onError(int code, String desc) {//发送消息失败
                    //错误码code和错误描述desc，可用于定位请求失败原因
                    //错误码code列表请参见错误码表
                    LogUtils.d("send message failed. code: " + code + " errmsg: " + desc);
                    mChatListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onSuccess(TIMMessage msg) {//发送消息成功
                    LogUtils.e("SendMsg ok");
                    mChatListAdapter.notifyDataSetChanged();
                }
            });

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newSend(List<TIMMessage> list) {
        int i = 0;
        for (TIMMessage timMessage : list) {
            if (timMessage.getSender().equals(mPeer)) {
                timMsgs.add(timMessage);
                i++;
            }
            LogUtils.d("list size() = " + list.size());
        }
        if (i > 0) {
            mChatListAdapter.notifyDataSetChanged();
            mChatRecyclerView.scrollToPosition(timMsgs.size() - 1);
        }
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
}
