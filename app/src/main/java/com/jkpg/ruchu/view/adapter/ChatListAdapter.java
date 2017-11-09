package com.jkpg.ruchu.view.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.CustomElemBean;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.TimeUtil;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.CustomShapeTransformation;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMImageType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.TIMTextElem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.bumptech.glide.Glide.with;

/**
 * Created by qindi on 2017/10/24.
 */

public class ChatListAdapter extends BaseQuickAdapter<TIMMessage, BaseViewHolder> {
    private List<TIMMessage> data;
    private String myImgUrl;
    private String youImgUrl;
    private boolean v;

    public ChatListAdapter(@LayoutRes int layoutResId, @Nullable List<TIMMessage> data, String myImgUrl, String youImgUrl, boolean v) {
        super(layoutResId, data);
        this.data = data;
        this.myImgUrl = myImgUrl;
        this.youImgUrl = youImgUrl;
        this.v = v;

    }

    @Override
    protected void convert(BaseViewHolder helper, TIMMessage item) {

        helper.setVisible(R.id.item_v, v);
        helper.addOnClickListener(R.id.chat_image_my);
        helper.addOnClickListener(R.id.chat_image_you);
        helper.addOnClickListener(R.id.chat_photo_my);
        helper.addOnClickListener(R.id.chat_photo_you);


        int layoutPosition = helper.getLayoutPosition();
        if (layoutPosition == 0) {
            helper.setText(R.id.chat_time, TimeUtil.getChatTimeStr(item.timestamp()));
            helper.setVisible(R.id.chat_time, true);
        } else {
            boolean b = (item.timestamp() - data.get(layoutPosition - 1).timestamp()) > 300;
            if (b) {
                helper.setText(R.id.chat_time, TimeUtil.getChatTimeStr(item.timestamp()));
                helper.setVisible(R.id.chat_time, true);
            } else {
                helper.setVisible(R.id.chat_time, false);
            }
        }

        if (item.isSelf()) {
            helper.setVisible(R.id.chat_you, false);
            helper.setVisible(R.id.chat_my, true);
            if (item.status() == TIMMessageStatus.Sending) {
                helper.setVisible(R.id.sending, true);
                helper.setVisible(R.id.sendError, false);
            } else if (item.status() == TIMMessageStatus.SendFail) {
                helper.setVisible(R.id.sending, false);
                helper.setVisible(R.id.sendError, true);

            } else if (item.status() == TIMMessageStatus.SendSucc) {
                helper.setVisible(R.id.sending, false);
                helper.setVisible(R.id.sendError, false);
            }


            Glide.with(UIUtils.getContext()).load(myImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .placeholder(R.drawable.gray_bg).error(R.drawable.gray_bg).into((CircleImageView) helper.getView(R.id.chat_image_my));
            for (int i = 0; i < item.getElementCount(); ++i) {
                TIMElem elem = item.getElement(i);

                //获取当前元素的类型
                TIMElemType elemType = elem.getType();

//                if (elemType != TIMElemType.Text || elemType != TIMElemType.Image){
//                }

                LogUtils.d("elem type: " + elemType.name());
                if (elemType == TIMElemType.Text) {
                    TIMTextElem textElem = (TIMTextElem) elem;
                    helper.setVisible(R.id.chat_sms_my, true);
                    helper.setVisible(R.id.chat_photo_my, false);
                    helper.setText(R.id.chat_sms_my, textElem.getText());

                    //处理文本消息
                } else if (elemType == TIMElemType.Image) {
                    //处理图片消息
                    helper.setVisible(R.id.chat_sms_my, false);
                    helper.setVisible(R.id.chat_photo_my, true);
                    TIMImageElem e = (TIMImageElem) elem;
                    LogUtils.d("image path" + e.getPath());
                    final ImageView imageView = new ImageView(UIUtils.getContext());
                    final ImageView imagePlace = new ImageView(UIUtils.getContext());
                    if (item.status() == TIMMessageStatus.Sending || item.status() == TIMMessageStatus.SendFail) {
                        Bitmap thumb = getThumb(e.getPath());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        thumb.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        Glide.with(UIUtils.getContext())
                                .load(bytes)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .transform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_my))
                                .crossFade()
                                .thumbnail(0.001f)
                                .into(new SimpleTarget<GlideDrawable>(thumb.getWidth(), thumb.getHeight()) {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        imageView.setImageDrawable(resource);
                                    }
                                });
                    } else {
                        imageView.setVisibility(View.GONE);
                        for (TIMImage image : e.getImageList()) {
                            LogUtils.d("image sum" + e.getImageList().size());
                            if (image.getType() == TIMImageType.Thumb) {

                                //获取图片类型, 大小, 宽高
                                LogUtils.d("image type: " + image.getType() +
                                        "\n image url " + image.getUrl() +
                                        "\n image height " + image.getHeight() +
                                        " image width " + image.getWidth());
                                int width;
                                int height;
                                if (image.getWidth() == 0 || image.getHeight() == 0) {
                                    width = 198;
                                    height = 198;
                                } else {
                                    width = (int) image.getWidth();
                                    height = (int) image.getHeight();
                                }
                                with(UIUtils.getContext())
                                        .load(R.drawable.gray_bg)
                                        .transform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_my))
                                        .override(width, height)
                                        .thumbnail(0.001f)
                                        .into(imagePlace);
                                Glide.with(UIUtils.getContext())
                                        .load(image.getUrl())
                                        .placeholder(new ColorDrawable(Color.TRANSPARENT))
                                        .error(new ColorDrawable(UIUtils.getColor(R.color.colorGray2)))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .transform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_my))
                                        .crossFade()
                                        .thumbnail(0.001f)
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                imageView.setVisibility(View.VISIBLE);
                                                imagePlace.setVisibility(View.GONE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                imageView.setVisibility(View.VISIBLE);
                                                imagePlace.setVisibility(View.GONE);
                                                return false;
                                            }
                                        })
                                        .into(new SimpleTarget<GlideDrawable>(width, height) {
                                            @Override
                                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                                imageView.setImageDrawable(resource);
                                            }
                                        });
                            }
                        }
                    }
                    RelativeLayout view = helper.getView(R.id.chat_photo_my);
                    view.removeAllViews();
                    view.addView(imagePlace);
                    view.addView(imageView);
                }//...处理更多消息
            }
        } else {


            helper.setVisible(R.id.chat_you, true);
            helper.setVisible(R.id.chat_my, false);
            Glide.with(UIUtils.getContext()).load(youImgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .placeholder(R.drawable.gray_bg).error(R.drawable.gray_bg).into((CircleImageView) helper.getView(R.id.chat_image_you));


            for (int i = 0; i < item.getElementCount(); ++i) {
                TIMElem elem = item.getElement(i);

                //获取当前元素的类型
                TIMElemType elemType = elem.getType();
                LogUtils.d("elem type: " + elemType.name());
                if (elemType == TIMElemType.Text) {
                    TIMTextElem textElem = (TIMTextElem) elem;
                    helper.setVisible(R.id.chat_sms_you, true);
                    helper.setVisible(R.id.chat_photo_you, false);
                    helper.setVisible(R.id.chat_custom_ll, false);
                    helper.setText(R.id.chat_sms_you, textElem.getText());

                    //处理文本消息
                } else if (elemType == TIMElemType.Image) {

                    //处理图片消息
                    helper.setVisible(R.id.chat_sms_you, false);
                    helper.setVisible(R.id.chat_photo_you, true);
                    helper.setVisible(R.id.chat_custom_ll, false);
                    TIMImageElem e = (TIMImageElem) elem;

                    for (TIMImage image : e.getImageList()) {
                        //获取图片类型, 大小, 宽高
                        LogUtils.d("image type: " + image.getType() +
                                " image size " + image.getSize() +
                                " image height " + image.getHeight() +
                                " image width " + image.getWidth());
                        if (image.getType() == TIMImageType.Thumb) {
                            int width;
                            int height;
                            if (image.getWidth() == 0 || image.getHeight() == 0) {
                                width = 198;
                                height = 198;
                            } else {
                                width = (int) image.getWidth();
                                height = (int) image.getHeight();
                            }
                            final ImageView imageView = new ImageView(UIUtils.getContext());
                            final ImageView imagePlace = new ImageView(UIUtils.getContext());
                            imageView.setVisibility(View.GONE);
                            with(UIUtils.getContext())
                                    .load(R.drawable.gray_bg)
                                    .transform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_you))
                                    .override(width, height)
                                    .thumbnail(0.001f)
                                    .into(imagePlace);

                            with(UIUtils.getContext())
                                    .load(image.getUrl())
//                                    .placeholder(R.drawable.gray_bg)
                                    .placeholder(new ColorDrawable(Color.TRANSPARENT))
//                                    .override(width,height)
                                    .error(new ColorDrawable(UIUtils.getColor(R.color.colorGray2)))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .transform(new CustomShapeTransformation(UIUtils.getContext(), R.drawable.reply_you))
                                    .crossFade()
                                    .thumbnail(0.001f)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            imageView.setVisibility(View.VISIBLE);
                                            imagePlace.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            imageView.setVisibility(View.VISIBLE);
                                            imagePlace.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(new SimpleTarget<GlideDrawable>(width, height) {
                                        @Override
                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                            imageView.setImageDrawable(resource);
                                        }
                                    });

                            RelativeLayout view = helper.getView(R.id.chat_photo_you);
                            view.removeAllViews();
                            view.addView(imagePlace);
                            view.addView(imageView);
                        }
                    }
                } else if (elemType == TIMElemType.Custom) {
                    helper.setVisible(R.id.chat_sms_you, false);
                    helper.setVisible(R.id.chat_photo_you, false);
                    helper.setVisible(R.id.chat_custom_ll, true);
                    helper.addOnClickListener(R.id.chat_custom_ll);

                    TIMCustomElem e = (TIMCustomElem) elem;
                    String s = new String(e.getData());
                    LogUtils.d("json : " + s);
                    CustomElemBean customElemBean = new Gson().fromJson(s, CustomElemBean.class);
                    helper.setText(R.id.chat_custom_title, customElemBean.describe);
                    Glide
                            .with(UIUtils.getContext())
                            .load(customElemBean.imgurl)
                            .error(R.drawable.gray_bg)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .crossFade()
                            .thumbnail(0.01f)
                            .into((ImageView) helper.getView(R.id.chat_custom_image));
                    if (customElemBean.type.equals("4")) {
                        helper.setVisible(R.id.chat_custom_look, false);
                    } else {
                        helper.setVisible(R.id.chat_custom_look, true);
                    }
                }
            }
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

}
