package com.jkpg.ruchu.view.adapter;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.TrainMainBean2;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/11/24.
 */

public class TrainVedioAdapter extends BaseQuickAdapter<TrainMainBean2.TuozhanxlBean, BaseViewHolder> {
    public TrainVedioAdapter(int layoutResId, @Nullable List<TrainMainBean2.TuozhanxlBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrainMainBean2.TuozhanxlBean item) {
        helper.setText(R.id.item_train_video_title, item.title);
        helper.setText(R.id.item_train_video_time, "时长:  " + item.video_time);
        helper.setText(R.id.item_train_video_num, item.times + "正在参与训练");
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.imgurl)
                .crossFade()
                .placeholder(R.color.colorXmlBg)
                .error(R.color.colorXmlBg)
                .into((ImageView) helper.getView(R.id.item_train_video_image));
        boolean cached = MyApplication.getProxy(UIUtils.getContext())
                .isCached(AppUrl.BASEURLHTTP + item.video_url);
        helper.setVisible(R.id.item_train_video_buffer, cached);

    }
}
