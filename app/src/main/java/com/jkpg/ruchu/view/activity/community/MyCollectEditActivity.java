package com.jkpg.ruchu.view.activity.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MyCollectBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.MyNoteEditRVAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/6/5.
 */

public class MyCollectEditActivity extends BaseActivity {
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

    private MyNoteEditRVAdapter mAdapter;

    private boolean isEdit = false;
    private boolean isSelectAll = false;
    private List<MyCollectBean.ListBean> mList;
    private int pageIndex = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initRecyclerView(final List<MyCollectBean.ListBean> list) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UIUtils.getContext());
        mMyCollectRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MyNoteEditRVAdapter(list);
        mMyCollectRecyclerView.setAdapter(mAdapter);
        mAdapter.setRecyclerViewOnItemClickListener(new MyNoteEditRVAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (isEdit) {
                    mAdapter.setSelectItem(position);
                } else {
                    int bbsid = list.get(position).bbsid;
                    Intent intent = new Intent(MyCollectEditActivity.this, NoticeDetailActivity.class);
                    intent.putExtra("bbsid", bbsid + "");
                    startActivity(intent);
                }
            }
        });
        mMyCollectRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int mLastVisibleItem;
            boolean isLoading = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mAdapter.getItemCount() && !isLoading) {
                    pageIndex++;
                    isLoading = true;
                    OkGo.
                            post(AppUrl.BBS_LOOKCOLLECTION)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("flag", pageIndex + "")
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    MyCollectBean myCollectBean = new Gson().fromJson(s, MyCollectBean.class);
                                    if (myCollectBean.list == null) {
                                        mAdapter.changeState(2);

                                    } else {
                                        List<MyCollectBean.ListBean> listMore = myCollectBean.list;
                                        if (listMore.size() == 10) {
                                            mAdapter.changeState(2);

                                        } else if (listMore.size() <= 10) {
                                            mAdapter.changeState(2);
                                            mList.addAll(listMore);

                                        } else {
                                            mAdapter.changeState(1);
                                            mList.addAll(listMore);
                                            isLoading = false;
                                        }
                                    }
                                }
                            });
                }

            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //拿到最后一个出现的item的位置
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        if (mList.size() <= 10)
            mAdapter.changeState(2);
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
        OkGo
                .post(AppUrl.BBS_LOOKCOLLECTION)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("flag", pageIndex + "")
                .execute(new StringDialogCallback(MyCollectEditActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyCollectBean myCollectBean = new Gson().fromJson(s, MyCollectBean.class);
                        mList = myCollectBean.list;
                        initRecyclerView(mList);

                    }
                });
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
                String s = "";
                for (int i = map.size() - 1; i >= 0; i--) {
                    if (map.get(i)) {
                        LogUtils.i(i + "map");
                        LogUtils.i(mList.get(i).bbsid + "=bbsid");
                        map.put(i, false);
                        map.remove(map.size() - 1);
                        LogUtils.i(map.size() + "size");
                        s = s + mList.get(i).bbsid + ",";
                        mList.remove(i);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                LogUtils.i(s);

                OkGo
                        .post(AppUrl.BBS_DELCOLLECTION)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("bbsid", s)
                        .execute(new StringDialogCallback(MyCollectEditActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {

                                mHeaderIvLeft.setVisibility(View.VISIBLE);
                                mHeaderTvLeft.setVisibility(View.GONE);
                                mHeaderIvRight.setVisibility(View.VISIBLE);
                                mHeaderTvRight.setVisibility(View.GONE);
                                mHeaderTvLeft.setText("全选");
                                isSelectAll = false;
                                mAdapter.setShowBox();
                                mAdapter.notifyDataSetChanged();
                                isEdit = false;

                                OkGo.
                                        post(AppUrl.BBS_LOOKCOLLECTION)
                                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                        .params("flag", pageIndex + "")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                MyCollectBean myCollectBean = new Gson().fromJson(s, MyCollectBean.class);
                                                if (myCollectBean.list == null) {
                                                    mAdapter.changeState(2);

                                                } else {
                                                    List<MyCollectBean.ListBean> listMore = myCollectBean.list;
                                                    if (listMore.size() == 10) {
                                                        mAdapter.changeState(2);

                                                    } else if (listMore.size() <= 10) {
                                                        mAdapter.changeState(2);
                                                        mList.addAll(listMore);

                                                    } else {
                                                        mAdapter.changeState(1);
                                                        mList.addAll(listMore);
                                                    }
                                                }
                                            }
                                        });
                            }
                        });
                break;
            case R.id.header_iv_right:
                if (mList == null || mList.size() == 0) {
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
