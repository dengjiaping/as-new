package com.jkpg.ruchu.view.activity.community;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FineNoteBean;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.FineNoteRVAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/5.
 */

public class FineNoteActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.fine_note_recycler_view)
    RecyclerView mFineNoteRecyclerView;
    @BindView(R.id.fine_note_refresh_layout)
    SwipeRefreshLayout mFineNoteRefreshLayout;

    private List<FineNoteBean> data = new ArrayList<>();
    private FineNoteRVAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_note);
        ButterKnife.bind(this);
        initHeader();
        initRefreshLayout();
        initRecyclerView();
        initData();

    }

    private void initRefreshLayout() {
        mFineNoteRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);

        mFineNoteRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; i++) {
                            data.add(0, new FineNoteBean("如初", "2017-07-01 09:00", i+"", "5", "https://www.dujin.org/sys/bing/1366.php", "呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵呵啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
                            mAdapter.notifyDataSetChanged();
                        }

                        //recyclerView回到最上面
                        mFineNoteRecyclerView.scrollToPosition(0);
                        //判断是否在刷新
//                      refreshLayout.isRefreshing()
                        //刷新完毕，关闭下拉刷新的组件
                        mFineNoteRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

    }

    private void initData() {
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://www.dujin.org/sys/bing/1366.php", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        mFineNoteRefreshLayout.setRefreshing(false);

    }

    private void initRecyclerView() {
        mFineNoteRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mAdapter = new FineNoteRVAdapter(R.layout.item_fine_note, data);
        mFineNoteRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String url = data.get(position).url;
                ToastUtils.showShort(UIUtils.getContext(), url);
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mFineNoteRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*if (mCurrentCounter >= TOTAL_COUNTER) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            if (isErr) {
                                //成功获取更多数据
                                mAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                                mCurrentCounter = mQuickAdapter.getData().size();
                                mAdapter.loadMoreComplete();
                            } else {
                                //获取更多数据失败
                                isErr = true;
                               // Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                                mAdapter.loadMoreFail();

                            }
                        }*/
                    }

                }, 3000);
            }
        }, mFineNoteRecyclerView);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("精选帖子");
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
