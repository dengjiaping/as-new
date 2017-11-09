package com.jkpg.ruchu.view.adapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.ChatListBean;
import com.jkpg.ruchu.utils.TimeUtil;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.CustomShapeTransformation;

import java.util.List;

import static com.bumptech.glide.Glide.with;

/**
 * Created by qindi on 2017/6/23.
 */

public class ChatListRVAdapter extends BaseMultiItemQuickAdapter<ChatListBean, BaseViewHolder> {
    private List<ChatListBean> data;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ChatListRVAdapter(List<ChatListBean> data) {
        super(data);
        this.data = data;
        addItemType(ChatListBean.FROM_USER_IMG, R.layout.item_chat_image_you);
        addItemType(ChatListBean.FROM_USER_MSG, R.layout.item_chat_you);
        addItemType(ChatListBean.TO_USER_IMG, R.layout.item_chat_image_my);
        addItemType(ChatListBean.TO_USER_MSG, R.layout.item_chat_my);
        addItemType(ChatListBean.NOTICE, R.layout.item_chat_notice);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatListBean item) {
        helper.addOnClickListener(R.id.chat_image_you);
        helper.addOnClickListener(R.id.chat_image_my);
        helper.addOnClickListener(R.id.chat_image_you_image);
        helper.addOnClickListener(R.id.chat_image_my_image);

        int layoutPosition = helper.getLayoutPosition();
        if (layoutPosition == 0) {
            helper.setText(R.id.chat_time, TimeUtil.getChatTimeStr(item.time));
            helper.setVisible(R.id.chat_time, true);
        } else {
            boolean b = (item.time - data.get(layoutPosition - 1).time) > 300;
            if (b) {
                helper.setText(R.id.chat_time, TimeUtil.getChatTimeStr(item.time));
                helper.setVisible(R.id.chat_time, true);
            } else {
                helper.setVisible(R.id.chat_time, false);
            }
        }


        switch (helper.getItemViewType()) {
            case ChatListBean.FROM_USER_MSG:
                with(UIUtils.getContext()).load(item.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .placeholder(R.drawable.gray_bg).error(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.chat_image_you));
                helper.setText(R.id.chat_sms_you, item.content);
                break;
            case ChatListBean.TO_USER_MSG:
                with(UIUtils.getContext()).load(item.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .placeholder(R.drawable.gray_bg).error(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.chat_image_my));
                helper.setText(R.id.chat_sms_my, item.content);
                if (item.sending) {
                    helper.setVisible(R.id.sending, true);
                    helper.setVisible(R.id.sendError, false);
                } else if (item.error) {
                    helper.setVisible(R.id.sending, false);
                    helper.setVisible(R.id.sendError, true);
                } else {
                    helper.setVisible(R.id.sending, false);
                    helper.setVisible(R.id.sendError, false);
                }
                break;
            case ChatListBean.FROM_USER_IMG:
                with(UIUtils.getContext()).load(item.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .placeholder(R.drawable.gray_bg).error(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.chat_image_you_image));
                final ImageView imageViewU = new ImageView(UIUtils.getContext());
//                imageViewU.setAdjustViewBounds(true);
//                RelativeLayout.LayoutParams layoutParamsU = new RelativeLayout.LayoutParams((int) item.imageWeidth, (int) item.imageHeight);
//                imageViewU.setLayoutParams(layoutParamsU);
//                imageViewU.setScaleType(ImageView.ScaleType.CENTER_CROP);


                Glide
                        .with(UIUtils.getContext())
                        .load(item.content)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_you))
                        .crossFade()
                        .placeholder(R.drawable.gray_bg)
                        .thumbnail(0.1f)
                        .into((new SimpleTarget<GlideDrawable>((int) item.imageWeidth, (int) item.imageHeight) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                imageViewU.setImageDrawable(resource);
                            }
                        }));
                RelativeLayout viewU = helper.getView(R.id.chat_photo_you);
                viewU.removeAllViews();
                viewU.addView(imageViewU);
                helper.addOnClickListener(R.id.chat_photo_you);
                break;
            case ChatListBean.TO_USER_IMG:
                Glide.with(UIUtils.getContext()).load(item.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .placeholder(R.drawable.gray_bg).error(R.mipmap.ic_launcher).into((CircleImageView) helper.getView(R.id.chat_image_my_image));

                final ImageView imageView = new ImageView(UIUtils.getContext());

//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) item.imageWeidth, (int) item.imageHeight);
//                imageView.setLayoutParams(layoutParams);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if (item.isNewSend) {
                    Glide
                            .with(UIUtils.getContext())
                            .load(item.content)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_my))
                            .crossFade()
//                            .placeholder(R.drawable.gray_bg)

//                            .override(352,198)
                            .thumbnail(0.1f)
                            .into(imageView);
                } else {

                    Glide
                            .with(UIUtils.getContext())
                            .load(item.content)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_my))
                            .crossFade()
                            .placeholder(R.drawable.gray_bg)

//                            .override(352,198)
                            .thumbnail(0.1f)
                            .into(new SimpleTarget<GlideDrawable>((int) item.imageWeidth, (int) item.imageHeight) {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                    imageView.setImageDrawable(resource);
                                }
                            });
                }
                RelativeLayout view = helper.getView(R.id.chat_photo_my);
                view.removeAllViews();
                view.addView(imageView);
                helper.addOnClickListener(R.id.chat_photo_my);
                if (item.sending) {
                    helper.setVisible(R.id.sending, true);
                    helper.setVisible(R.id.sendError, false);
                } else if (item.error) {
                    helper.setVisible(R.id.sending, false);
                    helper.setVisible(R.id.sendError, true);
                } else {
                    helper.setVisible(R.id.sending, false);
                    helper.setVisible(R.id.sendError, false);
                }
                break;
            case ChatListBean.NOTICE:
                helper.setText(R.id.chat_notice, item.content);
                break;
        }


    }
}
