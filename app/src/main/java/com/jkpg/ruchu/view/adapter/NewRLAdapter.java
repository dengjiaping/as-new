package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.VideoBean;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/18.
 */

public class NewRLAdapter extends RecyclerView.Adapter<NewRLAdapter.NewTrainViewHolder> implements View.OnClickListener {
    private List<VideoBean> videos;

    public NewRLAdapter(List<VideoBean> videos) {
        this.videos = videos;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (VideoBean) v.getTag());
        }
    }

    @Override
    public NewTrainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_new_guide, null);
        view.setOnClickListener(this);
        return new NewTrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewTrainViewHolder holder, int position) {
        VideoBean videoBean = videos.get(position);
        holder.itemView.setTag(videos.get(position));
        holder.mItemNewTitle.setText(videoBean.title);
        holder.mItemNewTime.setText(videoBean.time);
    }

    @Override
    public int getItemCount() {
        if (videos == null)
            return 0;
        else
            return videos.size();
    }

    static class NewTrainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_new_title)
        TextView mItemNewTitle;
        @BindView(R.id.item_new_time)
        TextView mItemNewTime;
        @BindView(R.id.item_card_view)
        CardView mItemCardView;

        NewTrainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, VideoBean data);

    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
