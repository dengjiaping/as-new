package com.jkpg.ruchu.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.RewardDetailBean;

import java.util.List;

/**
 * Created by qindi on 2017/12/3.
 */

public class RewardDetailsRVAdapter extends BaseQuickAdapter<RewardDetailBean.ListBean, BaseViewHolder> {
    List<RewardDetailBean.ListBean> data;

    public RewardDetailsRVAdapter(int layoutResId, @Nullable List<RewardDetailBean.ListBean> data) {
        super(layoutResId, data);
        this.data = data;

    }

    @Override
    protected void convert(BaseViewHolder helper, RewardDetailBean.ListBean item) {
        helper.setText(R.id.item_reward_time, item.time);
        helper.setText(R.id.item_reward_content, item.msg);
        helper.setText(R.id.item_reward_date, item.msg2);
        helper.addOnClickListener(R.id.item_reward_image);
        if (helper.getLayoutPosition() == data.size() - 1) {
            helper.setVisible(R.id.item_reward_view, false);
        } else {
            helper.setVisible(R.id.item_reward_view, true);
        }
//        0去领取 2未使用 3已使用 4已失效 1未完成
        switch (item.useflag) {
            case "0":
                helper.setImageResource(R.id.item_reward_image, R.drawable.reward_btn_1);
                break;
            case "1":
                helper.setImageResource(R.id.item_reward_image, R.drawable.reward_btn_5);
                break;
            case "2":
                helper.setImageResource(R.id.item_reward_image, R.drawable.reward_btn_2);
                break;
            case "3":
                helper.setImageResource(R.id.item_reward_image, R.drawable.reward_btn_3);
                break;
            case "4":
                helper.setImageResource(R.id.item_reward_image, R.drawable.reward_btn_4);
                break;
            case "5":
                helper.setImageResource(R.id.item_reward_image, R.drawable.reward_btn_6);
                break;
            default:
                helper.setImageResource(R.id.item_reward_image, R.drawable.reward_btn_4);
                break;
        }
    }
}
