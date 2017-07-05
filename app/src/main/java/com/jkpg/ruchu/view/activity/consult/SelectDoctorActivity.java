package com.jkpg.ruchu.view.activity.consult;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.SearchHistoryBean;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.SearchHistoryAdapter;
import com.jkpg.ruchu.view.adapter.SelectDoctorVPAdapter;
import com.jkpg.ruchu.widget.PopupWindow7;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jkpg.ruchu.utils.UIUtils.getContext;

/**
 * Created by qindi on 2017/6/12.
 */

public class SelectDoctorActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.select_doctor_tab_layout)
    TabLayout mSelectDoctorTabLayout;
    @BindView(R.id.select_doctor_view_pager)
    ViewPager mSelectDoctorViewPager;
    @BindView(R.id.select_doctor_now_doctor)
    TextView mSelectDoctorNowDoctor;
    @BindView(R.id.header_search_view)
    MaterialSearchView mHeaderSearchView;
    @BindView(R.id.header_tool_bar)
    Toolbar mHeaderToolBar;
    @BindView(R.id.view_search_history)
    TextView mViewSearchHistory;
    @BindView(R.id.view_search_recycler_view)
    RecyclerView mViewSearchRecyclerView;
    @BindView(R.id.search_history)
    LinearLayout mSearchHistory;
    @BindView(R.id.search_history_view)
    LinearLayout mSearchHistoryView;

    private List<View> mViews;
    private List<String> mTitles;
    private PopupWindow mPopupWindow;

    private List<SearchHistoryBean> mSearchHistories = new ArrayList<>();
    private SearchHistoryAdapter mSearchHistoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initTabLayout();
        initHistory();

        String string = SPUtils.getString(UIUtils.getContext(), Constants.SEARCHHISTORY, "");
        if (!string.equals("")) {
            String[] split = string.split(",");
            for (int i = 0; i < split.length; i++) {
                mSearchHistories.add(new SearchHistoryBean(split[i]));
            }
        }
    }


    private void initHistory() {
        mViewSearchRecyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        mSearchHistoryAdapter = new SearchHistoryAdapter(R.layout.item_search_history, mSearchHistories);
        mViewSearchRecyclerView.setAdapter(mSearchHistoryAdapter);
        mSearchHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mSearchHistories.remove(position);
                mSearchHistoryAdapter.notifyDataSetChanged();
                if (position == 0) {
                    mSearchHistory.setVisibility(View.GONE);
                    mSearchHistoryView.setVisibility(View.VISIBLE);
                }
            }
        });
        mSearchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mHeaderSearchView.setQuery(mSearchHistories.get(position).history, false);
            }
        });
    }

    private void initData() {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTitles = new ArrayList<>();
        mTitles.add("全部");
        mTitles.add("康复师");
        mTitles.add("医生");
        mViews = new ArrayList<>();
        mViews.add(new TextView(getContext()));
        mViews.add(new TextView(getContext()));
        mViews.add(new TextView(getContext()));

    }

    private void initTabLayout() {
        mSelectDoctorTabLayout.setupWithViewPager(mSelectDoctorViewPager);
        mSelectDoctorViewPager.setAdapter(new SelectDoctorVPAdapter(mTitles, mViews));
    }

    private void initTitle() {
        setSupportActionBar(mHeaderToolBar);
        mHeaderTvTitle.setText("选择医师");
        mHeaderSearchView.setEllipsize(true);
        mHeaderSearchView.setCursorDrawable(R.drawable.custom_cursor);
        mHeaderSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                for (int i = 0; i < mSearchHistories.size(); i++) {
                    if (mSearchHistories.get(i).history.equals(query)) {
                        mSearchHistories.remove(i);
                    }
                }
                mSearchHistories.add(0, new SearchHistoryBean(query));
                mSearchHistoryAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        mHeaderSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
               /* View view = View.inflate(getContext(), R.layout.view_search_history, null);
                mPopupWindow = new PopupWindow(getContext());
                mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                mPopupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                mPopupWindow.setContentView(view);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                mPopupWindow.setOutsideTouchable(false);
                mPopupWindow.setFocusable(false);
                mPopupWindow.showAsDropDown(mHeaderSearchView);
*/
                if (mSearchHistories.size() > 0) {
                    mSearchHistory.setVisibility(View.VISIBLE);
                } else {
                    mSearchHistoryView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSearchViewClosed() {
                mSearchHistory.setVisibility(View.GONE);
                mSearchHistoryView.setVisibility(View.GONE);
                String histories = "";
                for (int i = 0; i < mSearchHistories.size(); i++) {
                    histories = histories + mSearchHistories.get(i).history + ",";
                }
                SPUtils.saveString(UIUtils.getContext(), Constants.SEARCHHISTORY, histories);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mHeaderSearchView.setMenuItem(item);
        return true;
    }

    @OnClick({R.id.header_iv_left, R.id.select_doctor_now_doctor, R.id.header_iv_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.select_doctor_now_doctor:
                break;
            case R.id.header_iv_address:
                ToastUtils.showShort(UIUtils.getContext(), "jinan");
                View viewAddress = View.inflate(this, R.layout.view_select_address, null);
                PopupWindow7 popupWindow = new PopupWindow7(this);
                popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                popupWindow.setContentView(viewAddress);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(mHeaderToolBar);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mHeaderSearchView.isSearchOpen()) {
            mHeaderSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.view_search_history)
    public void onViewClicked() {
        mSearchHistories.clear();
        mSearchHistoryAdapter.notifyDataSetChanged();
        mSearchHistory.setVisibility(View.GONE);
    }
}
