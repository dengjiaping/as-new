package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/30.
 */

public class TrainHistoryAdapter extends RecyclerView.Adapter<TrainHistoryAdapter.TrainHistoryViewHolder> {

    @Override
    public TrainHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_train_history, null);

        return new TrainHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrainHistoryViewHolder holder, int position) {
        holder.mItemHistoryContainer.addView(View.inflate(UIUtils.getContext(), R.layout.item_history_time, null));
        holder.mItemHistoryContainer.addView(View.inflate(UIUtils.getContext(), R.layout.item_history_time, null));
        holder.mItemHistoryContainer.addView(View.inflate(UIUtils.getContext(), R.layout.item_history_time, null));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class TrainHistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_history_tv_time)
        TextView mItemHistoryTvTime;
        @BindView(R.id.item_history_line)
        View mItemHistoryLine;
        @BindView(R.id.item_history_tv_title)
        TextView mItemHistoryTvTitle;
        @BindView(R.id.item_history_tv_count)
        TextView mItemHistoryTvCount;
        @BindView(R.id.item_history_container)
        LinearLayout mItemHistoryContainer;

        TrainHistoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
