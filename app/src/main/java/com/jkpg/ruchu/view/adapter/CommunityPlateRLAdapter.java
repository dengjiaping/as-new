package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.CommunityMianBean;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/18.
 */

public class CommunityPlateRLAdapter extends RecyclerView.Adapter<CommunityPlateRLAdapter.CommunityPlateRLViewHolder> implements View.OnClickListener {
    private List<CommunityMianBean.List2Bean> plates;

    public CommunityPlateRLAdapter(List<CommunityMianBean.List2Bean> plates) {
        this.plates = plates;
    }


    @Override
    public CommunityPlateRLViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_plate, null);
        view.setOnClickListener(this);
        return new CommunityPlateRLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommunityPlateRLViewHolder holder, int position) {
        CommunityMianBean.List2Bean plateBean = plates.get(position);
        holder.itemView.setTag(plateBean);
        holder.mPlateTvTitle.setText(plateBean.platename);
        holder.mPlateTvBody.setText(plateBean.remark);
        holder.mPlateTvNum.setText(plateBean.zongshu+"贴");
        Glide.with(UIUtils.getContext()).load(AppUrl.BASEURL+plateBean.plateimg).error(R.mipmap.ic_launcher).into(holder.mPlateCivPhoto);

    }

    @Override
    public int getItemCount() {
        if (plates == null)
            return 0;
        else
            return plates.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (CommunityMianBean.List2Bean) v.getTag());
        }
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, CommunityMianBean.List2Bean data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    static class CommunityPlateRLViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.plate_civ_photo)
        ImageView mPlateCivPhoto;
        @BindView(R.id.plate_tv_title)
        TextView mPlateTvTitle;
        @BindView(R.id.plate_tv_body)
        TextView mPlateTvBody;
        @BindView(R.id.plate_tv_num)
        TextView mPlateTvNum;

        CommunityPlateRLViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
