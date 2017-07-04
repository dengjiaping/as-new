package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/20.
 */

public class FeedBackRLAdapter extends RecyclerView.Adapter<FeedBackRLAdapter.FeedBackViewHolder> {


    @Override
    public FeedBackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_feedback, null);
        return new FeedBackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedBackViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    static class FeedBackViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_feedback_photo)
        CircleImageView mItemFeedbackPhoto;
        @BindView(R.id.item_feedback_name)
        TextView mItemFeedbackName;
        @BindView(R.id.item_feedback_time)
        TextView mItemFeedbackTime;
        @BindView(R.id.item_feedback_body)
        TextView mItemFeedbackBody;

        FeedBackViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
