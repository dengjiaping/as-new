package com.jkpg.ruchu.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MyCollectBean;
import com.jkpg.ruchu.config.AppUrl;
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

public class MyNoteEditRVAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private List<MyCollectBean.ListBean> data;
    private Map<Integer, Boolean> map = new HashMap<>();
    private RecyclerViewOnItemClickListener onItemClickListener;
    private boolean isshowBox = false;
    //普通布局的type
    static final int TYPE_ITEM = 0;
    //脚布局
    static final int TYPE_FOOTER = 1;

    //正在加载更多
    static final int LOADING_MORE = 1;
    //没有更多
    static final int NO_MORE = 2;
    int footer_state = 1;

    public MyNoteEditRVAdapter(List<MyCollectBean.ListBean> data) {
        this.data = data;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < data.size(); i++) {
            map.put(i, false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = View.inflate(UIUtils.getContext(), R.layout.item_fans_post, null);
            view.setOnClickListener(this);
            return new MyNoteEditViewHolder(view);
        } else {
            //脚布局
            View view = View.inflate(UIUtils.getContext(), R.layout.recycler_load_more_layout, null);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyNoteEditViewHolder) {
            MyNoteEditViewHolder myNoteEditViewHolder = (MyNoteEditViewHolder) holder;
            MyCollectBean.ListBean listBean = data.get(position);
            Glide.with(UIUtils.getContext()).load(AppUrl.BASEURL + listBean.headImg).into(myNoteEditViewHolder.mItemFeedbackPhoto);
            myNoteEditViewHolder.mItemTvPostZan.setText(listBean.zan);
            myNoteEditViewHolder.mItemTvPostBody.setText(listBean.nick);
            myNoteEditViewHolder.mItemTvPostHuifu.setText(listBean.reply);
            myNoteEditViewHolder.mItemTvPostTime.setText(listBean.createtime);
            myNoteEditViewHolder.mItemTvPostTitle.setText(listBean.title);
            if (isshowBox) {
                myNoteEditViewHolder.mItemCbPostDelete.setVisibility(View.VISIBLE);
            } else {
                myNoteEditViewHolder.mItemCbPostDelete.setVisibility(View.GONE);
            }
            myNoteEditViewHolder.itemView.setTag(position);
            //设置checkBox改变监听
            myNoteEditViewHolder.mItemCbPostDelete.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    map.put(position, isChecked);
                }
            });
            // 设置CheckBox的状态
            if (map.get(position) == null) {
                map.put(position, false);
            }
            myNoteEditViewHolder.mItemCbPostDelete.setChecked(map.get(position));
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            if (position == 0) {//如果第一个就是脚布局,,那就让他隐藏
                footViewHolder.mProgressBar.setVisibility(View.GONE);
                footViewHolder.tv_state.setText("");
            }
            switch (footer_state) {//根据状态来让脚布局发生改变
//        case PULL_LOAD_MORE://上拉加载
//          footViewHolder.mProgressBar.setVisibility(View.GONE);
//          footViewHolder.tv_state.setText("上拉加载更多");
//          break;
                case LOADING_MORE:
                    footViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    footViewHolder.tv_state.setText("正在加载...");
                    break;
                case NO_MORE:
                    footViewHolder.mProgressBar.setVisibility(View.GONE);
                    footViewHolder.tv_state.setText("没有更多数据");
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() + 1 : 0;
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

    static class FootViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private TextView tv_state;


        public FootViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
            tv_state = (TextView) itemView.findViewById(R.id.foot_view_item_tv);

        }
    }

    public void changeState(int state) {
        this.footer_state = state;
        notifyDataSetChanged();
    }
}
