package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.TrainHistoryBean;
import com.jkpg.ruchu.utils.DateUtil;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/30.
 */

public class TrainHistoryAdapter extends RecyclerView.Adapter<TrainHistoryAdapter.TrainHistoryViewHolder> {
    private List<TrainHistoryBean.ItemsBean> items;

    public TrainHistoryAdapter(List<TrainHistoryBean.ItemsBean> items) {
        this.items = items;
    }

    @Override
    public TrainHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_train_history, null);

        return new TrainHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrainHistoryViewHolder holder, int position) {
        TrainHistoryBean.ItemsBean itemsBean = items.get(position);
        holder.mItemHistoryTvTime.setText(itemsBean.currenttime);
        int time = Integer.parseInt(itemsBean.sumtime) * 1000;
        String dateFormat = "";
        if (time >= 3600000) {
            dateFormat = DateUtil.dateFormat(time + "", "HH小时mm分钟ss秒");
        } else {
            dateFormat = DateUtil.dateFormat(time + "", "mm分钟ss秒");
        }
        holder.mItemHistoryTvCount.setText("今日训练" + itemsBean.record.size() + "次,训练总时长共计" + dateFormat);
        holder.mItemHistoryTvTitle.setText("产后康复 " + itemsBean.level);
        if (items.size() - 1 == position) {
            holder.mItemHistoryLine.setVisibility(View.GONE);
        } else {
            holder.mItemHistoryLine.setVisibility(View.VISIBLE);
        }
        holder.mItemHistoryContainer.removeAllViewsInLayout();
        for (int i = 0; i < itemsBean.record.size(); i++) {
            TrainHistoryBean.ItemsBean.RecordBean recordBean = itemsBean.record.get(i);
            View view = View.inflate(UIUtils.getContext(), R.layout.item_history_time, null);
            ((TextView) view.findViewById(R.id.history_time_num)).setText((i + 1) + "");
            ((TextView) view.findViewById(R.id.history_time_time)).setText("训练时间" + recordBean.bigintime);
            int practice = Integer.parseInt(recordBean.practice_time) * 1000;
            ((TextView) view.findViewById(R.id.history_time_length)).setText("训练时长" + DateUtil.dateFormat(practice + "", "mm:ss"));
            if (i == itemsBean.record.size() - 1) {
                view.findViewById(R.id.history_time_line).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.history_time_line).setVisibility(View.VISIBLE);
            }
            holder.mItemHistoryContainer.addView(view);
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
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
