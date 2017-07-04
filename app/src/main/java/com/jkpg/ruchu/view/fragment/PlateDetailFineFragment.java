package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateDetailBean;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.community.PlateDetailActivity;
import com.jkpg.ruchu.view.activity.community.SendNoteActivity;
import com.jkpg.ruchu.view.adapter.PlateDetailRVAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qindi on 2017/6/5.
 */

public class PlateDetailFineFragment extends Fragment {
    @BindView(R.id.plate_detail_recycler_view)
    RecyclerView mPlateDetailRecyclerView;
    @BindView(R.id.plate_detail_fab)
    FloatingActionButton mPlateDetailFab;
    Unbinder unbinder;
    @BindView(R.id.plate_detail_refresh)
    SwipeRefreshLayout mPlateDetailRefresh;
    private List<PlateDetailBean> plates;
    private PlateDetailRVAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plate_detail_all, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initRefreshLayout();
        initPlateDetailRecyclerView();
    }

    private void initRefreshLayout() {
        mPlateDetailRefresh.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mPlateDetailRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.setEnableLoadMore(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setNewData(plates);
                        /*isErr = false;
                        mCurrentCounter = PAGE_SIZE;*/
                        mPlateDetailRefresh.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                }, 2000);
            }
        });

    }

    private void initData() {
        plates = new ArrayList<>();
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", false, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", false, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", false, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
        plates.add(new PlateDetailBean("http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617", "哈哈", true, "2017-06-01  09:00", "1", "2", "哈哈哈哈哈哈哈哈啊哈", "哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢", "http://whatscap.ristury.com/java/WhatsCAP/apis/web/viaphoto2/124316/5775617"));
    }

    private void initPlateDetailRecyclerView() {
        mPlateDetailRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mAdapter = new PlateDetailRVAdapter(R.layout.item_plate_detail, plates);
        mPlateDetailRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String url = plates.get(position).url;
                ToastUtils.showShort(UIUtils.getContext(), url);
            }
        });
        mPlateDetailRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1)
                    mPlateDetailFab.hide();
                else if (newState == 0)
                    mPlateDetailFab.show();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

            }
        }, mPlateDetailRecyclerView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.plate_detail_fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.plate_detail_fab:
                Intent intent = new Intent(getActivity(), SendNoteActivity.class);
                intent.putExtra("title", ((PlateDetailActivity) getActivity()).getHeaderTitle());
                startActivity(intent);
                break;
        }
    }
}
