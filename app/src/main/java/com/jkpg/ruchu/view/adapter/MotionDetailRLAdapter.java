package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/20.
 */

public class MotionDetailRLAdapter extends RecyclerView.Adapter<MotionDetailRLAdapter.MotionDetailViewHolder> {

    @Override
    public MotionDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_motion_detail, null);
        return new MotionDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MotionDetailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    static class MotionDetailViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_motion_detail_step)
        TextView mItemMotionDetailStep;
        @BindView(R.id.item_motion_detail_title)
        TextView mItemMotionDetailTitle;
        @BindView(R.id.item_motion_detail_body)
        TextView mItemMotionDetailBody;

        MotionDetailViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
