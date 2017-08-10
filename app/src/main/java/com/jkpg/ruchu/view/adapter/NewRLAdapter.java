package com.jkpg.ruchu.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.VideoBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/18.
 */

public class NewRLAdapter extends RecyclerView.Adapter<NewRLAdapter.NewTrainViewHolder> implements View.OnClickListener {
    List<VideoBean.VideoMSBean> vedioMS;
    Context context;

    public NewRLAdapter(Context context, List<VideoBean.VideoMSBean> vedioMS) {
        this.context = context;
        this.vedioMS = vedioMS;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (VideoBean.VideoMSBean.VideomessBean) v.getTag());
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
        holder.itemView.setTag(vedioMS.get(position).videomess);
        VideoBean.VideoMSBean videoBean = vedioMS.get(position);
        holder.mItemNewTitle.setText(videoBean.title);
        holder.mItemNewTime.setText(videoBean.videotime);
        Glide.with(UIUtils.getContext())
                .load(AppUrl.BASEURL + videoBean.imageUrl)
//                .placeholder(R.drawable.new_guide_bg)
                .crossFade()
                .centerCrop()
                .error(R.drawable.new_guide_bg)
                .into(holder.mItemRL);
    }

    @Override
    public int getItemCount() {
        if (vedioMS == null)
            return 0;
        else
            return vedioMS.size();
    }

    static class NewTrainViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_new_title)
        TextView mItemNewTitle;
        @BindView(R.id.item_new_time)
        TextView mItemNewTime;
        @BindView(R.id.item_new_rl)
        ImageView mItemRL;

        NewTrainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, VideoBean.VideoMSBean.VideomessBean data);

    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
