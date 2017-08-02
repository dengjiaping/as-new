package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.NoticeDetailBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.jkpg.ruchu.widget.nineview.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qindi on 2017/6/15.
 */

public class NoticeDetailReplyAdapter extends BaseQuickAdapter<NoticeDetailBean.List2Bean, BaseViewHolder> {
    public NoticeDetailReplyAdapter(@LayoutRes int layoutResId, @Nullable List<NoticeDetailBean.List2Bean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeDetailBean.List2Bean item) {
        NineGridView view1 = helper.getView(R.id.item_notice_reply_nine_view);
        List<String> images = item.images;
        List<ImageInfo> imageInfo = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            imageInfo.add(new ImageInfo(AppUrl.BASEURL + images.get(i)));
        }
        view1.setAdapter(new NineGridViewClickAdapter(UIUtils.getContext(), imageInfo));
        helper.addOnClickListener(R.id.item_notice_detail_tv_reply);
        helper.addOnClickListener(R.id.item_notice_reply_civ);
        helper.addOnClickListener(R.id.item_notice_reply_name);
        helper.addOnClickListener(R.id.item_notice_reply_body);

        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.headimg)
                .centerCrop()
                .crossFade()
                .into((ImageView) helper.getView(R.id.item_notice_reply_civ));
        helper.setText(R.id.item_notice_reply_name, item.nick);
        helper.setText(R.id.item_notice_reply_baby, item.taici + " " + item.chanhoutime);
        helper.setText(R.id.item_notice_reply_body, item.content);
        helper.setText(R.id.item_notice_reply_floor, (helper.getPosition() + 1) + "楼");
        helper.setText(R.id.item_notice_reply_time, item.replytime);
        helper.setText(R.id.item_notice_reply_cb_love, item.zan);
        helper.setText(R.id.item_notice_detail_tv_reply, item.reply);
        final CheckBox love = helper.getView(R.id.item_notice_reply_cb_love);
        love.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int i = Integer.parseInt(love.getText().toString());
                if (isChecked) {
                    love.setText((i + 1) + "");
                } else {
                    love.setText((i - 1) + "");
                }
            }
        });
        List<NoticeDetailBean.List2Bean.ItemsBean> items = item.items;
        if (items.size() == 0) {
            helper.setVisible(R.id.item_notice_reply_to, false);
        } else {
            NoticeDetailBean.List2Bean.ItemsBean itemsBean0 = items.get(0);
            helper.setText(R.id.item_notice_reply_to_name, itemsBean0.nick + ":");
            helper.setText(R.id.item_notice_reply_to_body, itemsBean0.content);
            helper.addOnClickListener(R.id.item_notice_reply_to_body);
            helper.addOnClickListener(R.id.item_notice_reply_to_name);

            NoticeDetailBean.List2Bean.ItemsBean itemsBean1 = items.get(1);
            if (itemsBean1.userid.equals(itemsBean1.userid2)) {
                helper.setVisible(R.id.item_notice_reply_to_0, true);
                helper.setText(R.id.item_notice_reply_to_name0, itemsBean1.nick + ":");
                helper.setText(R.id.item_notice_reply_to_body0, itemsBean1.content);
                helper.addOnClickListener(R.id.item_notice_reply_to_name0);
                helper.addOnClickListener(R.id.item_notice_reply_to_body0);

            } else {
                helper.setVisible(R.id.item_notice_reply_to_1, true);
                helper.setText(R.id.item_notice_reply_to_name1, itemsBean1.nick);
                helper.setText(R.id.item_notice_reply_to_name2, itemsBean1.nick2 + ":");
                helper.setText(R.id.item_notice_reply_to_body1, itemsBean1.content);
                helper.addOnClickListener(R.id.item_notice_reply_to_name1);
                helper.addOnClickListener(R.id.item_notice_reply_to_name2);
                helper.addOnClickListener(R.id.item_notice_reply_to_body1);

            }

        }
        if (items.size() <= 2) {
            helper.setVisible(R.id.item_notice_reply_to_more, false);
        } else {
            helper.setText(R.id.item_notice_reply_to_more, "更多" + (items.size() - 2) + "条回复");
            helper.addOnClickListener(R.id.item_notice_reply_to_more);

        }

    }
}
