package com.jkpg.ruchu.view.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.ChatListBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.CustomShapeTransformation;

import java.util.List;

/**
 * Created by qindi on 2017/6/23.
 */

public class ChatListRVAdapter extends BaseMultiItemQuickAdapter<ChatListBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatListRVAdapter(List<ChatListBean> data) {
        super(data);
        addItemType(ChatListBean.FROM_USER_IMG, R.layout.item_chat_image_you);
        addItemType(ChatListBean.FROM_USER_MSG, R.layout.item_chat_you);
        addItemType(ChatListBean.TO_USER_IMG, R.layout.item_chat_image_my);
        addItemType(ChatListBean.TO_USER_MSG, R.layout.item_chat_my);
        addItemType(ChatListBean.NOTICE, R.layout.item_chat_notice);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatListBean item) {
        switch (helper.getItemViewType()) {
            case ChatListBean.FROM_USER_MSG:
                Glide.with(UIUtils.getContext()).load(item.image).error(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.chat_image_you));
                helper.setText(R.id.chat_sms_you, item.content);
                break;
            case ChatListBean.TO_USER_MSG:
                Glide.with(UIUtils.getContext()).load(item.image).error(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.chat_image_my));
                helper.setText(R.id.chat_sms_my, item.content);
                break;
            case ChatListBean.FROM_USER_IMG:
                Glide.with(UIUtils.getContext()).load(item.image).error(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.chat_image_you_image));
                Glide
                        .with(UIUtils.getContext())
                        .load(item.content)
                        .error(R.drawable.photo_error)
                        .transform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_you))
                        .crossFade()
                        .thumbnail(0.1f)
                        .into((ImageView) helper.getView(R.id.chat_photo_you));
                helper.addOnClickListener(R.id.chat_photo_you);
                break;
            case ChatListBean.TO_USER_IMG:
                Glide.with(UIUtils.getContext()).load(item.image).error(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.chat_image_my_image));
                Glide
                        .with(UIUtils.getContext())
                        .load(item.content)
                        .error(R.drawable.photo_error)
                        .transform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_my))
                        .crossFade()
                        .thumbnail(0.1f)
                        .into((ImageView) helper.getView(R.id.chat_photo_my));
                helper.addOnClickListener(R.id.chat_photo_my);

                break;
            case ChatListBean.NOTICE:
                helper.setText(R.id.chat_notice, item.content);
                break;
        }
    }
}
