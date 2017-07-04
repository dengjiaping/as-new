package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/27.
 */

public class FansRLAdapter extends RecyclerView.Adapter<FansRLAdapter.FansRLViewHolder> {

    @Override
    public FansRLViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_fans_post,parent,false);

        return new FansRLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FansRLViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class FansRLViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_feedback_photo)
        CircleImageView mItemFeedbackPhoto;
        @BindView(R.id.item_tv_post_title)
        TextView mItemTvPostTitle;
        @BindView(R.id.item_tv_post_body)
        TextView mItemTvPostBody;
        @BindView(R.id.item_tv_post_time)
        TextView mItemTvPostTime;
        @BindView(R.id.item_tv_post_zan)
        TextView mItemTvPostZan;
        @BindView(R.id.item_tv_post_huifu)
        TextView mItemTvPostHuifu;

        FansRLViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
