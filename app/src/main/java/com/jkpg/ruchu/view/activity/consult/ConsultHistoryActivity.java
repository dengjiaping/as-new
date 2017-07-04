package com.jkpg.ruchu.view.activity.consult;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.ConsultHistoryBean;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.ConsultHistoryRVAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/12.
 */

public class ConsultHistoryActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_left)
    TextView mHeaderTvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right2)
    ImageView mHeaderIvRight2;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.consult_history_recycler_view)
    RecyclerView mConsultHistoryRecyclerView;

    private boolean isEdit = false;
    private boolean isSelectAll = false;
    private boolean isShow = false;

    private List<ConsultHistoryBean> data;
    private ConsultHistoryRVAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_history);
        ButterKnife.bind(this);
        initHeader();
        initData();
        initRecyclerView();
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new ConsultHistoryBean());
        data.add(new ConsultHistoryBean());
        data.add(new ConsultHistoryBean());
        data.add(new ConsultHistoryBean());
        data.add(new ConsultHistoryBean());
        data.add(new ConsultHistoryBean());
        data.add(new ConsultHistoryBean());
        data.add(new ConsultHistoryBean());
        data.add(new ConsultHistoryBean());
    }

    private void initRecyclerView() {
        mConsultHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mAdapter = new ConsultHistoryRVAdapter(data);
        mConsultHistoryRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerViewOnItemClickListener(new ConsultHistoryRVAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(final View view, int position) {
                if (isEdit) {
                    mAdapter.setSelectItem(position);
                    ToastUtils.showShort(UIUtils.getContext(), "" + position);
                } else {
                    view.findViewById(R.id.consult_history_appraise).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isShow) {
                                view.findViewById(R.id.consult_history_ll_appraise).setVisibility(View.VISIBLE);
                                ObjectAnimator anim = ObjectAnimator.ofFloat(view.findViewById(R.id.consult_history_iv_appraise), "rotation", 0f, 90f);
                                anim.setDuration(500);
                                anim.start();
                                isShow = true;
                            } else {
                                view.findViewById(R.id.consult_history_ll_appraise).setVisibility(View.GONE);
                                ObjectAnimator anim = ObjectAnimator.ofFloat(view.findViewById(R.id.consult_history_iv_appraise), "rotation", 90f, 0f);
                                anim.setDuration(500);
                                anim.start();
                                isShow = false;
                            }
                        }
                    });
                    view.findViewById(R.id.consult_history_tv_custom).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort(UIUtils.getContext(), "客服呢！");
                        }
                    });
                    view.findViewById(R.id.consult_history_main).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort(UIUtils.getContext(), "医生呢！");
                        }
                    });
                }
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("咨询记录");

        mHeaderIvLeft.setVisibility(View.VISIBLE);
        mHeaderIvRight.setImageResource(R.drawable.icon_delete);
        mHeaderTvRight.setText("完成");
        mHeaderTvLeft.setText("全选");
        mHeaderTvRight.setVisibility(View.GONE);
        mHeaderTvLeft.setVisibility(View.GONE);

    }

    @OnClick({R.id.header_iv_left, R.id.header_tv_left, R.id.header_iv_right, R.id.header_tv_right})
    public void onViewClicked(View view) {
        Map<Integer, Boolean> map = mAdapter.getMap();
        switch (view.getId()) {
            case R.id.header_tv_left:
                if (!isSelectAll) {
                    mHeaderTvLeft.setText("取消全选");
                    isSelectAll = true;

                    for (int i = 0; i < map.size(); i++) {
                        map.put(i, true);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mHeaderTvLeft.setText("全选");
                    isSelectAll = false;
                    Map<Integer, Boolean> m = mAdapter.getMap();
                    for (int i = 0; i < m.size(); i++) {
                        m.put(i, false);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.header_tv_right:
                for (int i = map.size() - 1; i >= 0; i--) {
                    if (map.get(i)) {
                        LogUtils.i(i + "map");
                        data.remove(i);
                        map.put(i, false);
                        map.remove(map.size() - 1);
                        LogUtils.i(map.size() + "size");
                    }
                    mAdapter.notifyDataSetChanged();
                }
                mHeaderIvLeft.setVisibility(View.VISIBLE);
                mHeaderTvLeft.setVisibility(View.GONE);
                mHeaderIvRight.setVisibility(View.VISIBLE);
                mHeaderTvRight.setVisibility(View.GONE);
                mAdapter.setShowBox();
                mAdapter.notifyDataSetChanged();
                isEdit = false;
                break;
            case R.id.header_iv_right:
                if (data.size() == 0) {
                    return;
                }
                mAdapter.setShowBox();
                mAdapter.notifyDataSetChanged();
                mHeaderIvLeft.setVisibility(View.GONE);
                mHeaderIvRight.setVisibility(View.GONE);
                mHeaderTvRight.setVisibility(View.VISIBLE);
                mHeaderTvLeft.setVisibility(View.VISIBLE);

                isEdit = true;
                break;
            case R.id.header_iv_left:
                finish();
                break;
        }
    }
}
