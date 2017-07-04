package com.jkpg.ruchu.widget.banner.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by qindi on 2017/5/17.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        //Glide 加载图片简单用法
        Glide.with(context).load(path).crossFade().into(imageView);
    }
}
