package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.IMUserBean;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.TimeUtil;
import com.jkpg.ruchu.utils.UIUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.ext.message.TIMConversationExt;

import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import cn.bingoogolapple.badgeview.BGABadgeable;
import cn.bingoogolapple.badgeview.BGADragDismissDelegate;

/**
 * Created by qindi on 2017/10/18.
 */

public class MySmsHistoryRvAdapter extends BaseQuickAdapter<IMUserBean, BaseViewHolder> {
    public MySmsHistoryRvAdapter(@LayoutRes int layoutResId, @Nullable List<IMUserBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final IMUserBean item) {
        helper.setVisible(R.id.item_v, item.v);
        Glide
                .with(UIUtils.getContext())
                .load(item.url)
                .centerCrop()
                .crossFade()
                .error(R.drawable.gray_bg)
                .into((ImageView) helper.getView(R.id.item_photo));
        helper.setText(R.id.item_tv_title, item.name);

//        //获取会话扩展实例
//        TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.C2C, item.identifier);
//        TIMConversationExt conExt = new TIMConversationExt(con);
//        /*
//         * 从cache中获取最后几条消息
//         * @param count 需要获取的消息数，最多为20
//         * @return 消息列表，第一条为最新消息。会话非法时，返回null。
//         */
//        List<TIMMessage> lastMsgs = conExt.getLastMsgs(1);
//        TIMMessage timMessage = lastMsgs.get(0);
//        long timestamp = timMessage.timestamp();
        String timeStr = TimeUtil.getTimeStr(item.time);
        helper.setText(R.id.item_tv_time, timeStr + "");


//        TIMElemType type = timMessage.getElement(0).getType();
//        if (type == TIMElemType.Image) {
        helper.setText(R.id.item_tv_body, item.content);
//        } else if (type == TIMElemType.Text) {
//            helper.setText(R.id.item_tv_body, ((TIMTextElem) timMessage.getElement(0)).getText());
//        }


        //获取会话未读数
        long num = item.num;
        LogUtils.d("unread msg num: " + num);

        BGABadgeLinearLayout view = helper.getView(R.id.root_view);


        if (num == 0) {
            view.hiddenBadge();
            helper.setVisible(R.id.item_tv_sum, false);
        } else {
            helper.setVisible(R.id.item_tv_sum, false);
            if (num > 99) {
                view.showTextBadge("99+");
                helper.setText(R.id.item_tv_sum, "99+");
            } else {
                view.showTextBadge(num + "");
                helper.setText(R.id.item_tv_sum, num + "");
            }
        }

        view.setDragDismissDelegage(new BGADragDismissDelegate() {
            @Override
            public void onDismiss(BGABadgeable badgeable) {
                //对单聊会话已读上报
                String peer = item.identifier;  //获取与用户 "sample_user_1" 的会话
                TIMConversation conversation = TIMManager.getInstance().getConversation(
                        TIMConversationType.C2C,    //会话类型：单聊
                        peer);                      //会话对方用户帐号
//获取会话扩展实例
                TIMConversationExt conExt = new TIMConversationExt(conversation);
//将此会话的所有消息标记为已读
                conExt.setReadMessage(null, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        LogUtils.d("setReadMessage failed, code: " + code + "|desc: " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        LogUtils.d("setReadMessage succ");
                    }
                });
            }
        });


    }
}
