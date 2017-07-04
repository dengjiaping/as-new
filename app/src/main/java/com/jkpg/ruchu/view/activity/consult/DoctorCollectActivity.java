package com.jkpg.ruchu.view.activity.consult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.DoctorCollectBean;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.NoticeDetailActivity;
import com.jkpg.ruchu.view.adapter.DoctorCollectRVAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/13.
 */

public class DoctorCollectActivity extends AppCompatActivity {
    @BindView(R.id.doctor_collect_recycler_view)
    RecyclerView mDoctorCollectRecyclerView;
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

    private List<DoctorCollectBean> data;
    private DoctorCollectRVAdapter mAdapter;

    private boolean isEdit = false;
    private boolean isSelectAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_collect);
        ButterKnife.bind(this);
        initData();
        initHeader();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mDoctorCollectRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mAdapter = new DoctorCollectRVAdapter(data);
        mDoctorCollectRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerViewOnItemClickListener(new DoctorCollectRVAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (isEdit) {
                    mAdapter.setSelectItem(position);
                } else {
                    startActivity(new Intent(DoctorCollectActivity.this, NoticeDetailActivity.class));
                }
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的收藏");
        mHeaderIvLeft.setVisibility(View.VISIBLE);
        mHeaderIvRight.setImageResource(R.drawable.icon_delete);
        mHeaderTvRight.setText("完成");
        mHeaderTvLeft.setText("全选");
        mHeaderTvRight.setVisibility(View.GONE);
        mHeaderTvLeft.setVisibility(View.GONE);

    }

    private void initData() {
        data = new ArrayList<>();
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
        data.add(new DoctorCollectBean());
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.header_tv_left, R.id.header_tv_right, R.id.header_iv_right})
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
        }
    }
}
