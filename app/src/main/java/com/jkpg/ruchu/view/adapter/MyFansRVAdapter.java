package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FansListBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/25.
 */

public class MyFansRVAdapter extends RecyclerView.Adapter<MyFansRVAdapter.MyFansViewHolder> implements View.OnClickListener {

    private List<FansListBean> fansList;

    public MyFansRVAdapter(List<FansListBean> fansList) {
        this.fansList = fansList;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (FansListBean) v.getTag());
        }
    }


    @Override
    public MyFansViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_fans, null);
        view.setOnClickListener(this);
        return new MyFansViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyFansViewHolder holder, int position) {
        FansListBean fansListBean = fansList.get(position);
        holder.itemView.setTag(fansListBean);
        holder.mItemFansTvBody.setText(fansListBean.body);
        holder.mItemFansCbFollow.setChecked(fansListBean.isFollow);

    }

    @Override
    public int getItemCount() {
        return fansList.size();
    }

    static class MyFansViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_fans_civ_photo)
        CircleImageView mItemFansCivPhoto;
        @BindView(R.id.item_fans_tv_name)
        TextView mItemFansTvName;
        @BindView(R.id.item_fans_tv_body)
        TextView mItemFansTvBody;
        @BindView(R.id.item_fans_cb_follow)
        CheckBox mItemFansCbFollow;

        MyFansViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, FansListBean data);

    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
