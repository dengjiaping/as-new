package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.OtherVideoBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/18.
 */

public class OtherRLAdapter extends RecyclerView.Adapter<OtherRLAdapter.OtherTrainViewHolder> implements View.OnClickListener {
    private List<OtherVideoBean.VideoMS2Bean> videos;

    public OtherRLAdapter(List<OtherVideoBean.VideoMS2Bean> videos) {
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
        OtherVideoBean.VideoMS2Bean vedioMS2Bean = videos.get(position);
        holder.itemView.setTag(videos.get(position));
        holder.mItemTrainTvTitle.setText(vedioMS2Bean.title);
        holder.mItemTrainTvTime.setText(vedioMS2Bean.videotime);
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + vedioMS2Bean.imageUrl)
                .crossFade()
                .centerCrop()
                .into(holder.mItemTrainRlBg);
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
            mOnItemClickListener.onItemClick(v, (OtherVideoBean.VideoMS2Bean) v.getTag());
        }
    }

    static class OtherTrainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_train_iv_start)
        ImageView mItemTrainIvStart;
        @BindView(R.id.item_train_rl_bg)
        ImageView mItemTrainRlBg;
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
        void onItemClick(View view, OtherVideoBean.VideoMS2Bean data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
