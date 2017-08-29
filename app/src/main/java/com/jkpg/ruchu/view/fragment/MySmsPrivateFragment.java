package com.jkpg.ruchu.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MySmsPrivsteBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.my.MySMSActivity;
import com.jkpg.ruchu.view.adapter.MySmsPrivsteRvAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/8/12.
 */

public class MySmsPrivateFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.view_reply_et)
    EditText mViewReplyEt;
    @BindView(R.id.view_reply_btn)
    TextView mViewReplyBtn;
    @BindView(R.id.reply_view)
    LinearLayout mReplyView;
    private MySmsPrivsteBean mMySmsPrivsteBean;
    private MySmsPrivsteRvAdapter mMySmsPrivsteRvAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mReplyView.setVisibility(View.VISIBLE);
        OkGo
                .post(AppUrl.MYNOTICE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("flag", "0")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mMySmsPrivsteBean = new Gson().fromJson(s, MySmsPrivsteBean.class);
                        initRecyclerView(mMySmsPrivsteBean.list);
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        mRefreshLayout.setRefreshing(true);
                    }
                });
        mRefreshLayout.setColorSchemeResources(R.color.colorPink, R.color.colorPink2);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkGo
                        .post(AppUrl.MYNOTICE)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("flag", "0")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                mMySmsPrivsteBean = new Gson().fromJson(s, MySmsPrivsteBean.class);
                                initRecyclerView(mMySmsPrivsteBean.list);
                            }

                            @Override
                            public void onAfter(String s, Exception e) {
                                super.onAfter(s, e);
                                mRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onBefore(BaseRequest request) {
                                super.onBefore(request);
                                mRefreshLayout.setRefreshing(true);
                            }
                        });
            }
        });

        ((MySMSActivity) getActivity()).getHeaderIvLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewReplyEt.clearFocus();
                getActivity().finish();
            }
        });

    }

    private void initRecyclerView(final List<MySmsPrivsteBean.ListBean> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext());
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mMySmsPrivsteRvAdapter = new MySmsPrivsteRvAdapter(list);
        try {
            mRecyclerView.setAdapter(mMySmsPrivsteRvAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecyclerView.scrollToPosition(list.size() - 1);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //滑动时候输入框失去焦点
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    mViewReplyEt.clearFocus();
                    if (getActivity().getCurrentFocus() != null) {
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        });
        mViewReplyEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mRecyclerView != null)
                    mRecyclerView.scrollToPosition(list.size() - 1);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.view_reply_btn)
    public void onViewClicked() {
        if (mViewReplyEt.getText().toString().trim().length() != 0) {
            OkGo
                    .post(AppUrl.MYNOTICE)
                    .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .params("content", mViewReplyEt.getText().toString().trim())
                    .params("flag", "1")
                    .execute(new StringDialogCallback(getActivity()) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            MySmsPrivsteBean mySmsPrivsteBean = new Gson().fromJson(s, MySmsPrivsteBean.class);
                            mMySmsPrivsteBean.list.add(mySmsPrivsteBean.list.get(0));
                            mMySmsPrivsteRvAdapter.notifyDataSetChanged();

                            mViewReplyEt.setText("");
                            mRecyclerView.scrollToPosition(mMySmsPrivsteBean.list.size() - 1);

                        }

                    });
        }
    }


}
