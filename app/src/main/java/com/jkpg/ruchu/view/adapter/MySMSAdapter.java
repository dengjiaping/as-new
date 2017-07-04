package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/27.
 */

public class MySMSAdapter extends RecyclerView.Adapter<MySMSAdapter.MySMSViewHolder> {

    @Override
    public MySMSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(),R.layout.item_personal_sms,null);
        return new MySMSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MySMSViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class MySMSViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_photo)
        CircleImageView mItemPhoto;
        @BindView(R.id.item_tv_title)
        TextView mItemTvTitle;
        @BindView(R.id.item_tv_time)
        TextView mItemTvTime;
        @BindView(R.id.item_tv_body)
        TextView mItemTvBody;

        MySMSViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
