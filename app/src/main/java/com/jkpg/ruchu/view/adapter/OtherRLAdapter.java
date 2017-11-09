package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
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
    private List<OtherVideoBean.ItemBean> videos;

    public OtherRLAdapter(List<OtherVideoBean.ItemBean> videos) {
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
        OtherVideoBean.ItemBean vedioMS2Bean = videos.get(position);
        holder.itemView.setTag(position);
        holder.mItemTrainTvTitle.setText(vedioMS2Bean.title);
        holder.mItemTrainTv1.setText(/*vedioMS2Bean.level + "    " +*/ vedioMS2Bean.times + "  人参与训练");
        holder.mItemTrainTvTime.setText("时长: " + vedioMS2Bean.video_time);
        holder.mItemTrainTv2.setText(vedioMS2Bean.content);
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + vedioMS2Bean.imageUrl)
//                .placeholder(R.drawable.photo_error)
//                .error(R.drawable.photo_error)
                .crossFade()
                .into(holder.mItemTrainRlBg);
        boolean cached = MyApplication.getProxy(UIUtils.getContext())
                .isCached(AppUrl.BASEURLHTTP + vedioMS2Bean.video_url);
        holder.mdownload.setVisibility(cached ? View.VISIBLE : View.GONE);
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
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    static class OtherTrainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_train_rl_bg)
        ImageView mItemTrainRlBg;
        @BindView(R.id.item_train_tv_title)
        TextView mItemTrainTvTitle;
        @BindView(R.id.item_train_tv_time)
        TextView mItemTrainTvTime;
        @BindView(R.id.item_train_tv_1)
        TextView mItemTrainTv1;
        @BindView(R.id.item_train_tv_2)
        TextView mItemTrainTv2;
        @BindView(R.id.download_icon)
        ImageView mdownload;

        OtherTrainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
