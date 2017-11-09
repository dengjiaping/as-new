package com.jkpg.ruchu.view.fragment;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.IMUserBean;
import com.jkpg.ruchu.bean.MySmsNoticeBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.ChatListActivity;
import com.jkpg.ruchu.view.activity.my.NoticeActivity;
import com.jkpg.ruchu.view.adapter.MySmsHistoryRvAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/10/18.
 */

public class MySmsHistoryFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    private List<TIMConversation> mList;
    private List<IMUserBean> mUserList;
    private View mNoticeView;
    private MySmsHistoryRvAdapter mMySmsHistoryRvAdapter;
    private List<String> mUsers;
    private boolean isNews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initRefreshLayout();
        initRecyclerView();
        NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(MyApplication.getContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    private void init() {
        mUsers = new ArrayList<>();
        mUserList = new ArrayList<>();
        mList = TIMManagerExt.getInstance().getConversationList();
        for (TIMConversation timConversation : mList) {
            if (timConversation.getType() == TIMConversationType.C2C) {
                mUsers.add(timConversation.getPeer());
            }
        }
        if (mUsers.size() != 0) {
            //获取用户资料
            TIMFriendshipManager.getInstance().getUsersProfile(mUsers, new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int code, String desc) {
                    //错误码code和错误描述desc，可用于定位请求失败原因
                    //错误码code列表请参见错误码表
                    LogUtils.e("getUsersProfile failed: " + code + " desc");
                    if (mRefreshLayout != null) {
                        mRefreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onSuccess(List<TIMUserProfile> result) {
                    LogUtils.e("getUsersProfile succ");
                    for (TIMUserProfile res : result) {
                        LogUtils.e("identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
                                + " FaceUrl: " + res.getFaceUrl());

                        //获取会话扩展实例
                        TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.C2C, res.getIdentifier());
                        TIMConversationExt conExt = new TIMConversationExt(con);
                        /*
                         * 从cache中获取最后几条消息
                         * @param count 需要获取的消息数，最多为20
                         * @return 消息列表，第一条为最新消息。会话非法时，返回null。
                         */
                        List<TIMMessage> lastMsgs = conExt.getLastMsgs(1);
                        TIMMessage timMessage = lastMsgs.get(0);
                        long timestamp = timMessage.timestamp();
                        TIMElemType type = timMessage.getElement(0).getType();

                        IMUserBean imUserBean = new IMUserBean(res.getIdentifier(), res.getNickName(), res.getFaceUrl());
                        Map<String, byte[]> customInfo = res.getCustomInfo();
                        byte[] tag = customInfo.get("Tag_Profile_Custom_ruchuxzs");
                        if (tag != null) {
                            imUserBean.setV(true);
                            LogUtils.e(new String(tag) + "Tag_Profile_Custom_ruchuxzs");
                        }
                        imUserBean.setTime(timestamp);
                        if (type == TIMElemType.Image) {
                            imUserBean.setContent("[图片]");
                        } else if (type == TIMElemType.Text) {
                            imUserBean.setContent(((TIMTextElem) timMessage.getElement(0)).getText());
                        } else if (type == TIMElemType.Custom) {
                            String desc = ((TIMCustomElem) timMessage.getElement(0)).getDesc();
                            if (desc == null) {
                                imUserBean.setContent("[新消息]");
                            } else {
                                imUserBean.setContent(desc);
                            }
                        }
                        imUserBean.setNum(conExt.getUnreadMessageNum());


                        mUserList.add(imUserBean);

                        Collections.sort(mUserList);
                        mMySmsHistoryRvAdapter.notifyDataSetChanged();
                        if (mRefreshLayout != null) {
                            mRefreshLayout.setRefreshing(false);
                        }
                    }
                }
            });
        } else {
            if (mRefreshLayout != null) {
                mRefreshLayout.setRefreshing(false);
            }
        }

        OkGo
                .post(AppUrl.MYNOTICE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MySmsNoticeBean mySmsNoticeBean = new Gson().fromJson(s, MySmsNoticeBean.class);
                        if (mySmsNoticeBean.list != null && mySmsNoticeBean.list.size() >= 1) {
                            String content;
                            if (mySmsNoticeBean.list.get(0).type.equals("1")) {
                                content = mySmsNoticeBean.list.get(0).nick + mySmsNoticeBean.list.get(0).title;
                            } else {
                                content = mySmsNoticeBean.list.get(0).title;
                            }
                            String createtime = mySmsNoticeBean.list.get(0).createtime;
                            ((TextView) mNoticeView.findViewById(R.id.item_tv_body)).setText(content);
                            ((TextView) mNoticeView.findViewById(R.id.item_tv_time)).setText(createtime);
                        }

                    }

//                    @Override
//                    public void onAfter(String s, Exception e) {
//                        super.onAfter(s, e);
//                        if (mRefreshLayout != null) {
//                            mRefreshLayout.setRefreshing(false);
//                        }
//                    }
//
//                    @Override
//                    public void onBefore(BaseRequest request) {
//                        super.onBefore(request);
//                        mRefreshLayout.setRefreshing(true);
//                    }
                });

    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
    }

    private void refreshList() {
        if (!isNews) {
            isNews = true;
            mUsers.clear();
            mUserList.clear();
            mList = TIMManagerExt.getInstance().getConversationList();
            for (TIMConversation timConversation : mList) {
                if (timConversation.getType() == TIMConversationType.C2C) {
                    mUsers.add(timConversation.getPeer());
                }
            }
            if (mUsers.size() != 0) {

                //获取用户资料
                TIMFriendshipManager.getInstance().getUsersProfile(mUsers, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int code, String desc) {
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code列表请参见错误码表
                        LogUtils.e("getUsersProfile failed: " + code + " desc");
                        if (mRefreshLayout != null) {
                            mRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> result) {
                        LogUtils.e("getUsersProfile succ");
                        for (TIMUserProfile res : result) {
//                            Map<String, byte[]> customInfo = res.getCustomInfo();
//                            byte[] tag = customInfo.get("Tag_Profile_Custom_ruchuxzs");
//                            if (tag != null) {
//                                LogUtils.e(new String(tag) + "Tag_Profile_Custom_ruchuxzs");
//                            }
                            LogUtils.e("identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
                                    + " FaceUrl: " + res.getFaceUrl());
                            //获取会话扩展实例
                            TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.C2C, res.getIdentifier());
                            TIMConversationExt conExt = new TIMConversationExt(con);
                        /*
                         * 从cache中获取最后几条消息
                         * @param count 需要获取的消息数，最多为20
                         * @return 消息列表，第一条为最新消息。会话非法时，返回null。
                         */
                            List<TIMMessage> lastMsgs = conExt.getLastMsgs(1);
                            if (lastMsgs.size() != 0) {
                                TIMMessage timMessage = lastMsgs.get(0);
                                long timestamp = timMessage.timestamp();
                                TIMElemType type = timMessage.getElement(0).getType();

                                IMUserBean imUserBean = new IMUserBean(res.getIdentifier(), res.getNickName(), res.getFaceUrl());
                                Map<String, byte[]> customInfo = res.getCustomInfo();
                                byte[] tag = customInfo.get("Tag_Profile_Custom_ruchuxzs");
                                if (tag != null) {
                                    imUserBean.setV(true);
                                    LogUtils.e(new String(tag) + "Tag_Profile_Custom_ruchuxzs");
                                }
                                imUserBean.setTime(timestamp);
                                if (type == TIMElemType.Image) {
                                    imUserBean.setContent("[图片]");
                                } else if (type == TIMElemType.Text) {
                                    imUserBean.setContent(((TIMTextElem) timMessage.getElement(0)).getText());
                                } else if (type == TIMElemType.Custom) {
                                    String desc = ((TIMCustomElem) timMessage.getElement(0)).getDesc();
                                    if (desc == null) {
                                        imUserBean.setContent("[新消息]");
                                    } else {
                                        imUserBean.setContent(desc);
                                    }
                                }
                                imUserBean.setNum(conExt.getUnreadMessageNum());


                                mUserList.add(imUserBean);
                                Collections.sort(mUserList);
//                        mUserList.add(new IMUserBean(res.getIdentifier(), res.getNickName(), res.getFaceUrl()));
                                mMySmsHistoryRvAdapter.notifyDataSetChanged();
                                if (mRefreshLayout != null) {
                                    mRefreshLayout.setRefreshing(false);
                                    isNews = false;

                                }
                            }
                        }
                    }
                });
            } else {
                mRefreshLayout.setRefreshing(false);
                isNews = false;
            }
        }

    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMySmsHistoryRvAdapter = new MySmsHistoryRvAdapter(R.layout.item_personal_sms, mUserList);
        mRecyclerView.setAdapter(mMySmsHistoryRvAdapter);

        mNoticeView = View.inflate(UIUtils.getContext(), R.layout.item_personal_sms, null);
        mNoticeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NoticeActivity.class));
            }
        });
        mMySmsHistoryRvAdapter.addHeaderView(mNoticeView);
        mMySmsHistoryRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), ChatListActivity.class);
                if (mUserList.size() == 0) {
                    return;
                }
                intent.putExtra("peer", mUserList.get(position).identifier);
                intent.putExtra("name", mUserList.get(position).name);
                intent.putExtra("image", mUserList.get(position).url);
                intent.putExtra("v", mUserList.get(position).v);
                startActivity(intent);
            }
        });

        mMySmsHistoryRvAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.add(0, position, 0, "删除私信");
                    }
                });
                return false;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.C2C, mUserList.get(item.getItemId()).identifier);
        mUserList.remove(item.getItemId());
        mMySmsHistoryRvAdapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            LogUtils.d("onCreate");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            LogUtils.d("onDestroy");

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshIMList(String ss) {
        if (ss.equals("RefreshIMList")) {
            refreshList();
            LogUtils.d("RefreshIMList");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newSend(List<TIMMessage> list) {
        refreshList();
    }
}
