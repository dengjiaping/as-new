package com.jkpg.ruchu.widget;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jkpg.ruchu.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by qindi on 2017/11/18.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.photo_error)
                .centerCrop()
                .into(imageView);
    }

}