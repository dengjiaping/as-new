package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.tencent.imsdk.TIMUserProfile;

import java.util.List;

/**
 * Created by qindi on 2017/10/24.
 */

public class BlackListRVAdapter extends BaseQuickAdapter<TIMUserProfile, BaseViewHolder> {
    public BlackListRVAdapter(@LayoutRes int layoutResId, @Nullable List<TIMUserProfile> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TIMUserProfile item) {
        helper.setText(R.id.black_name, item.getNickName());
        Glide
                .with(UIUtils.getContext())
                .load(item.getFaceUrl())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.gray_bg)
                .error(R.drawable.gray_bg)
                .into((ImageView) helper.getView(R.id.black_image));
        helper.addOnClickListener(R.id.black_delete);
    }
}
