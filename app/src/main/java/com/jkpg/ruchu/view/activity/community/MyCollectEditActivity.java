package com.jkpg.ruchu.view.activity.community;

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
import com.jkpg.ruchu.bean.FineNoteBean;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.MyNoteEditRVAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/5.
 */

public class MyCollectEditActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_collect_recycler_view)
    RecyclerView mMyCollectRecyclerView;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.header_tv_left)
    TextView mHeaderTvLeft;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;

    private List<FineNoteBean> data;
    private MyNoteEditRVAdapter mAdapter;

    private boolean isEdit = false;
    private boolean isSelectAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        ButterKnife.bind(this);
        initData();
        initHeader();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mMyCollectRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mAdapter = new MyNoteEditRVAdapter(data);
        mMyCollectRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerViewOnItemClickListener(new MyNoteEditRVAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (isEdit) {
                    mAdapter.setSelectItem(position);
                } else {
                    startActivity(new Intent(MyCollectEditActivity.this, NoticeDetailActivity.class));
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
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "2", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "3", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "4", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "5", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "6", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "7", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "8", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "9", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "10", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "11", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "12", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "13", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "14", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "15", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "16", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "17", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "18", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
        data.add(new FineNoteBean("如初", "2017-07-01 09:00", "1", "5", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496831897187&di=e8ac08f5e3f54dc009613ca962332165&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Fwap180%2F75d91745jw1dtprt062sjj.jpg", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈呢哈啊哈哈哈哈哈哈哈哈哈啊哈哈哈啊阿花啊哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈", "https://www.dujin.org/sys/bing/1366.php"));
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
