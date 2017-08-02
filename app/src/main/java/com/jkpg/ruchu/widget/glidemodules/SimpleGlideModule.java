package com.jkpg.ruchu.widget.glidemodules;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * Created by qindi on 2017/7/17.
 */

public class SimpleGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(HttpsUtils.getTrustAllClient()));
    }
}