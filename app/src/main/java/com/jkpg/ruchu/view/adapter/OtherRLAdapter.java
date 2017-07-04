package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class OtherRLAdapter extends RecyclerView.Adapter<OtherRLAdapter.OtherTrainViewHolder> implements View.OnClickListener {
    private List<VideoBean> videos;

    public OtherRLAdapter(List<VideoBean> videos) {
        this.videos = videos;
    }

    @Override
    public OtherTrainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_other_train, null);
        view.setOnClickListener(this);
        return new OtherTrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OtherTrainViewHolder holder, int position) {
        VideoBean videoBean = videos.get(position);
        holder.itemView.setTag(videos.get(position));
        holder.mItemTrainTvTitle.setText(videoBean.title);
        holder.mItemTrainTvTime.setText(videoBean.time);
    }

    @Override
    public int getItemCount() {
        if (videos == null)
            return 0;
        else
            return videos.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (VideoBean) v.getTag());
        }
    }

    static class OtherTrainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_train_iv_start)
        ImageView mItemTrainIvStart;
        @BindView(R.id.item_train_rl_bg)
        RelativeLayout mItemTrainRlBg;
        @BindView(R.id.item_train_tv_title)
        TextView mItemTrainTvTitle;
        @BindView(R.id.item_train_tv_time)
        TextView mItemTrainTvTime;

        OtherTrainViewHolder(View view) {
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
