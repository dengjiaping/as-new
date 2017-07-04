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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.FineNoteBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.MyNoteRVAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/5.
 */

public class MyCollectActivity extends AppCompatActivity {
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

    private List<FineNoteBean> data;
    private boolean isEdit = false;

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
        MyNoteRVAdapter adapter = new MyNoteRVAdapter(R.layout.item_fans_post, data);
        mMyCollectRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(MyCollectActivity.this, NoticeDetailActivity.class));
            }
        });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的收藏");
        mHeaderTvRight.setText("编辑");
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

    @OnClick({R.id.header_tv_left, R.id.header_tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_tv_left:
                break;
            case R.id.header_tv_right:
               /* if (!isEdit) {
                    isEdit = true;
                    mHeaderIvLeft.setVisibility(View.GONE);
                    mHeaderTvLeft.setText("全选");
                    mHeaderTvRight.setText("完成");
                } else {
                    isEdit = false;
                    mHeaderIvLeft.setVisibility(View.VISIBLE);
                    mHeaderTvRight.setText("编辑");
                    mHeaderTvLeft.setText("");
                }*/
                startActivity(new Intent(MyCollectActivity.this, MyCollectEditActivity.class));
                break;
        }
    }
}
