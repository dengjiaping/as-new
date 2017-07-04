package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.ConsultHistoryBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.SmoothCheckBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by qindi on 2017/6/5.
 */

public class ConsultHistoryRVAdapter extends RecyclerView.Adapter<ConsultHistoryRVAdapter.ConsultHistoryViewHolder> implements View.OnClickListener {
    private List<ConsultHistoryBean> data;
    private Map<Integer, Boolean> map = new HashMap<>();
    private RecyclerViewOnItemClickListener onItemClickListener;
    private boolean isshowBox = false;

    public ConsultHistoryRVAdapter(List<ConsultHistoryBean> data) {
        this.data = data;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < data.size(); i++) {
            map.put(i, false);
        }
    }

    @Override
    public ConsultHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_consult_history, null);
        view.setOnClickListener(this);
        return new ConsultHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConsultHistoryViewHolder holder, final int position) {
//        Glide.with(UIUtils.getContext()).load(data.get(position).image).into(holder.mConsultHistoryImage);
        if (isshowBox) {
            holder.mConsultHistoryDelete.setVisibility(View.VISIBLE);
        } else {
            holder.mConsultHistoryDelete.setVisibility(View.GONE);
        }
        holder.itemView.setTag(position);
        //设置checkBox改变监听
        holder.mConsultHistoryDelete.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                map.put(position, isChecked);
            }
        });
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.mConsultHistoryDelete.setChecked(map.get(position));
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

    static class ConsultHistoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.consult_history_delete)
        SmoothCheckBox mConsultHistoryDelete;
        @BindView(R.id.consult_history_image)
        CircleImageView mConsultHistoryImage;
        @BindView(R.id.consult_history_tv_name)
        TextView mConsultHistoryTvName;
        @BindView(R.id.consult_history_tv_job)
        TextView mConsultHistoryTvJob;
        @BindView(R.id.consult_history_tv_state)
        TextView mConsultHistoryTvState;
        @BindView(R.id.consult_history_tv_time)
        TextView mConsultHistoryTvTime;
        @BindView(R.id.consult_history_tv_body)
        TextView mConsultHistoryTvBody;
        @BindView(R.id.consult_history_tv_appraise)
        TextView mConsultHistoryTvAppraise;
        @BindView(R.id.consult_history_iv_appraise)
        ImageView mConsultHistoryIvAppraise;
        @BindView(R.id.consult_history_tv_custom)
        TextView mConsultHistoryTvCustom;
        @BindView(R.id.consult_history_tv_reply_time)
        TextView mConsultHistoryReplyTvTime;
        @BindView(R.id.consult_history_tv_rating)
        MaterialRatingBar mConsultHistoryTvRating;
        @BindView(R.id.consult_history_tv_appraise_body)
        TextView mConsultHistoryTvAppraiseBody;
        @BindView(R.id.consult_history_ll_appraise)
        LinearLayout mConsultHistoryLlAppraise;

        ConsultHistoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
