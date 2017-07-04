package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FineNoteBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.SmoothCheckBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/6/5.
 */

public class MyNoteEditRVAdapter extends RecyclerView.Adapter<MyNoteEditRVAdapter.MyNoteEditViewHolder> implements View.OnClickListener {
    private List<FineNoteBean> data;
    private Map<Integer, Boolean> map = new HashMap<>();
    private RecyclerViewOnItemClickListener onItemClickListener;
    private boolean isshowBox = false;

    public MyNoteEditRVAdapter(List<FineNoteBean> data) {
        this.data = data;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < data.size(); i++) {
            map.put(i, false);
        }
    }

    @Override
    public MyNoteEditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_fans_post, null);
        view.setOnClickListener(this);
        return new MyNoteEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyNoteEditViewHolder holder, final int position) {
        holder.mItemTvPostZan.setText(data.get(position).eulogy);
        Glide.with(UIUtils.getContext()).load(data.get(position).image).into(holder.mItemFeedbackPhoto);
        if (isshowBox) {
            holder.mItemCbPostDelete.setVisibility(View.VISIBLE);
        } else {
            holder.mItemCbPostDelete.setVisibility(View.GONE);
        }
        holder.itemView.setTag(position);
        //设置checkBox改变监听
        holder.mItemCbPostDelete.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                map.put(position, isChecked);
            }
        });
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.mItemCbPostDelete.setChecked(map.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
        }
    }

    //设置是否显示CheckBox
    public void setShowBox() {
        //取反
        isshowBox = !isshowBox;
    }

    //设置点击事件
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (map.get(position)) {
            map.put(position, false);
        } else {
            map.put(position, true);
        }
        notifyItemChanged(position);
    }

    //返回集合给MainActivity
    public Map<Integer, Boolean> getMap() {
        return map;
    }

    //接口回调设置点击事件
    public interface RecyclerViewOnItemClickListener {
        //点击事件
        void onItemClickListener(View view, int position);
    }

    static class MyNoteEditViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_cb_post_delete)
        SmoothCheckBox mItemCbPostDelete;
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

        MyNoteEditViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
