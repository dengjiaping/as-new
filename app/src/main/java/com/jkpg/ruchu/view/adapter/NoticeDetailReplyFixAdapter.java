package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.NoticeDetailBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.ColorStyleTextView;
import com.jkpg.ruchu.widget.nineview.ImageInfo;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.jkpg.ruchu.widget.nineview.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qindi on 2017/6/15.
 */

public class NoticeDetailReplyFixAdapter extends BaseQuickAdapter<NoticeDetailBean.List2Bean, BaseViewHolder> {
    public NoticeDetailReplyFixAdapter(@LayoutRes int layoutResId, @Nullable List<NoticeDetailBean.List2Bean> data) {
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
        helper.addOnClickListener(R.id.item_notice_reply_tv_reply);
        helper.addOnClickListener(R.id.item_notice_reply_civ);
        helper.addOnClickListener(R.id.item_notice_reply_name);
        helper.addOnClickListener(R.id.item_notice_reply_body);
        helper.addOnClickListener(R.id.item_notice_reply_cb_love);

        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + item.headimg)
                .centerCrop()
                .crossFade()
                .into((ImageView) helper.getView(R.id.item_notice_reply_civ));
        helper.setText(R.id.item_notice_reply_name, item.nick);
        if (item.taici.equals("无")) {
            helper.setText(R.id.item_notice_reply_baby, item.taici);

        } else {
            helper.setText(R.id.item_notice_reply_baby, item.taici + " " + item.chanhoutime);
        }
        helper.setText(R.id.item_notice_reply_body, item.content);
        helper.setText(R.id.item_notice_reply_floor, (helper.getPosition()) + "楼");
        helper.setText(R.id.item_notice_reply_time, item.replytime);
        helper.setText(R.id.item_notice_reply_cb_love, item.zan + "");
        helper.setText(R.id.item_notice_reply_tv_reply, (item.items.size()) + "");
        helper.setChecked(R.id.item_notice_reply_cb_love, item.iszan);
        List<NoticeDetailBean.List2Bean.ItemsBean> items = item.items;
        if (items.size() == 0) {
            helper.setVisible(R.id.item_notice_reply_to, false);
        } else if (items.size() >= 2) {
            helper.setVisible(R.id.item_notice_reply_to, true);
            NoticeDetailBean.List2Bean.ItemsBean itemsBean0 = items.get(0);
//            helper.setText(R.id.item_notice_reply_to_name, itemsBean0.nick + ":");
//            helper.setText(R.id.item_notice_reply_to_body, itemsBean0.content);
//            helper.addOnClickListener(R.id.item_notice_reply_to_body);
//            helper.addOnClickListener(R.id.item_notice_reply_to_name);
            helper.setText(R.id.view_0, itemsBean0.nick + " : " + itemsBean0.content);
            ColorStyleTextView view_0 = helper.getView(R.id.view_0);
            view_0.setDefaultTextValue("");
            view_0.setDefaultTextValue(itemsBean0.nick + "," + itemsBean0.content);
            helper.addOnClickListener(R.id.view_0);


            NoticeDetailBean.List2Bean.ItemsBean itemsBean1 = items.get(1);
            if (itemsBean1.parentid.equals(item.tid + "")) {
//                helper.setVisible(R.id.item_notice_reply_to_0, true);
//                helper.setVisible(R.id.item_notice_reply_to_1, false);
//                helper.setText(R.id.item_notice_reply_to_name0, itemsBean1.nick + ":");
//                helper.setText(R.id.item_notice_reply_to_body0, itemsBean1.content);
//                helper.addOnClickListener(R.id.item_notice_reply_to_name0);
//                helper.addOnClickListener(R.id.item_notice_reply_to_body0);
                helper.setVisible(R.id.view_1, true);
                helper.setVisible(R.id.view_2, false);
//                helper.setText(R.id.item_notice_reply_to_name0, itemsBean1.nick + ":");
//                helper.setText(R.id.item_notice_reply_to_body0, itemsBean1.content);
//                helper.addOnClickListener(R.id.item_notice_reply_to_name0);
//                helper.addOnClickListener(R.id.item_notice_reply_to_body0);
                helper.setText(R.id.view_1, itemsBean1.nick + " : " + itemsBean1.content);
                ColorStyleTextView view_1 = helper.getView(R.id.view_1);
                view_1.setDefaultTextValue("");
                view_1.setDefaultTextValue(itemsBean1.nick + "," + itemsBean1.content);
                helper.addOnClickListener(R.id.view_1);


            } else {
                helper.setVisible(R.id.view_1, false);
                helper.setVisible(R.id.view_2, true);
//                helper.setText(R.id.item_notice_reply_to_name1, itemsBean1.nick);
//                helper.setText(R.id.item_notice_reply_to_name2, itemsBean1.nick2 + ":");
//                helper.setText(R.id.item_notice_reply_to_body1, itemsBean1.content);
//                helper.addOnClickListener(R.id.item_notice_reply_to_name1);
//                helper.addOnClickListener(R.id.item_notice_reply_to_name2);
//                helper.addOnClickListener(R.id.item_notice_reply_to_body1);

                helper.setText(R.id.view_2, itemsBean1.nick + " 回复 " + itemsBean1.nick2 + " : " + itemsBean1.content);
                ColorStyleTextView view_2 = helper.getView(R.id.view_2);
                view_2.setDefaultTextValue("");
                view_2.setDefaultTextValue(itemsBean1.nick + "," + itemsBean1.nick2 + "," + itemsBean1.content);
                helper.addOnClickListener(R.id.view_2);


            }

        } else if (items.size() == 1) {
            helper.setVisible(R.id.item_notice_reply_to, true);
            helper.setVisible(R.id.view_0, true);
            helper.setVisible(R.id.view_1, false);
            helper.setVisible(R.id.view_2, false);
            NoticeDetailBean.List2Bean.ItemsBean itemsBean0 = items.get(0);
//            helper.setText(R.id.item_notice_reply_to_name, itemsBean0.nick + ":");
//            helper.setText(R.id.item_notice_reply_to_body, itemsBean0.content);
//            helper.addOnClickListener(R.id.item_notice_reply_to_body);
//            helper.addOnClickListener(R.id.item_notice_reply_to_name);
            helper.setText(R.id.view_0, itemsBean0.nick + " : " + itemsBean0.content);
            ColorStyleTextView view_0 = helper.getView(R.id.view_0);
            view_0.setDefaultTextValue("");
            view_0.setDefaultTextValue(itemsBean0.nick + "," + itemsBean0.content);
            helper.addOnClickListener(R.id.view_0);

        }
//        if (items.size() == 0) {
//            helper.setVisible(R.id.item_notice_reply_to, false);
//        } else if (items.size() >= 2) {
//            helper.setVisible(R.id.item_notice_reply_to, true);
//
//            NoticeDetailBean.List2Bean.ItemsBean itemsBean0 = items.get(0);
//            helper.setText(R.id.item_notice_reply_to_name, itemsBean0.nick + ":");
//            helper.setText(R.id.item_notice_reply_to_body, itemsBean0.content);
//            helper.addOnClickListener(R.id.item_notice_reply_to_body);
//            helper.addOnClickListener(R.id.item_notice_reply_to_name);
//
//            NoticeDetailBean.List2Bean.ItemsBean itemsBean1 = items.get(1);
//            if (itemsBean1.parentid.equals(item.tid + "")) {
//                helper.setVisible(R.id.item_notice_reply_to_0, true);
//                helper.setVisible(R.id.item_notice_reply_to_1, false);
//
//                helper.setText(R.id.item_notice_reply_to_name0, itemsBean1.nick + ":");
//                helper.setText(R.id.item_notice_reply_to_body0, itemsBean1.content);
//                helper.addOnClickListener(R.id.item_notice_reply_to_name0);
//                helper.addOnClickListener(R.id.item_notice_reply_to_body0);
//
//            } else {
//                helper.setVisible(R.id.item_notice_reply_to_1, true);
//                helper.setVisible(R.id.item_notice_reply_to_0, false);
//
//                helper.setText(R.id.item_notice_reply_to_name1, itemsBean1.nick);
//                helper.setText(R.id.item_notice_reply_to_name2, itemsBean1.nick2 + ":");
//                helper.setText(R.id.item_notice_reply_to_body1, itemsBean1.content);
//                helper.addOnClickListener(R.id.item_notice_reply_to_name1);
//                helper.addOnClickListener(R.id.item_notice_reply_to_name2);
//                helper.addOnClickListener(R.id.item_notice_reply_to_body1);
//
//            }
//
//        } else if (items.size() == 1) {
//            helper.setVisible(R.id.item_notice_reply_to, true);
//            helper.setVisible(R.id.item_notice_reply_to_1, false);
//            helper.setVisible(R.id.item_notice_reply_to_0, false);
//            NoticeDetailBean.List2Bean.ItemsBean itemsBean0 = items.get(0);
//            helper.setText(R.id.item_notice_reply_to_name, itemsBean0.nick + ":");
//            helper.setText(R.id.item_notice_reply_to_body, itemsBean0.content);
//            helper.addOnClickListener(R.id.item_notice_reply_to_body);
//            helper.addOnClickListener(R.id.item_notice_reply_to_name);
//        }
        if (items.size() <= 2) {
            helper.setVisible(R.id.item_notice_reply_to_more, false);
        } else {
            helper.setVisible(R.id.item_notice_reply_to_more, true);

            helper.setText(R.id.item_notice_reply_to_more, "更多" + (items.size() - 2) + "条回复");
            helper.addOnClickListener(R.id.item_notice_reply_to_more);

        }

    }
}
