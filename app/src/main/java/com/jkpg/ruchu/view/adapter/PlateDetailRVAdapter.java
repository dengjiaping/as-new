package com.jkpg.ruchu.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateDetailBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/6/6.
 */

public class PlateDetailRVAdapter extends BaseQuickAdapter<PlateDetailBean.ListBean, BaseViewHolder> {
    public PlateDetailRVAdapter(@LayoutRes int layoutResId, @Nullable List<PlateDetailBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void convert(BaseViewHolder helper, PlateDetailBean.ListBean item) {

        helper
                .setText(R.id.plate_detail_tv_name, item.nick)
//                .setVisible(R.id.plate_detail_iv_fine, (item.isGood).equals("1"))
                .setText(R.id.plate_detail_tv_time, item.createtime)
                .setText(R.id.plate_detail_tv_eulogy, item.zan)
                .setText(R.id.plate_detail_tv_reply, item.reply)
//                .setText(R.id.plate_detail_tv_title, item.title)
                .setText(R.id.plate_detail_tv_body, item.content);
        TextView title = helper.getView(R.id.plate_detail_tv_title);
        Glide.with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.headImg)
                .crossFade()
                .dontAnimate()
                .placeholder(R.drawable.gray_bg)
                .into((ImageView) helper.getView(R.id.plate_detail_civ_photo));
        if ((item.isGood).equals("1")) {
            String html = item.title +" "+ "<img src='" + R.drawable.icon_fine + "'>";
            title.setText(Html.fromHtml(html, new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    int id = Integer.parseInt(source);
                    Drawable drawable = UIUtils.getResources().getDrawable(id, null);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight());
                    return drawable;
                }
            }, null));
        } else {
            title.setText(item.title);
        }


    }
}
