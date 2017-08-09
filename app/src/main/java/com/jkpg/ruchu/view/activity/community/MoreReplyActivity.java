package com.jkpg.ruchu.view.activity.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.MoreReplyBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.MoreReplyAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by qindi on 2017/8/7.
 */

public class MoreReplyActivity extends AppCompatActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.more_reply_recycler_view)
    RecyclerView mMoreReplyRecyclerView;
    @BindView(R.id.more_reply_input)
    TextView mMoreReplyInput;
    private String mReplyid;
    private String mBbsid;
    private int conunt = 0;
    private String mParentid;
    private MoreReplyAdapter mMoreReplyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_reply);
        ButterKnife.bind(this);
        mBbsid = getIntent().getStringExtra("bbsid");
        mReplyid = getIntent().getStringExtra("replyid");
        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.BBS_LOOKMOREREPLIES)
                .params("bbsid", mBbsid)
                .params("replyid", mReplyid)
                .execute(new StringDialogCallback(MoreReplyActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MoreReplyBean moreReplyBean = new Gson().fromJson(s, MoreReplyBean.class);
                        List<MoreReplyBean.ItemsBean> items = moreReplyBean.items;
                        initRecyclerView(items);

                    }
                });

    }

    private void initRecyclerView(final List<MoreReplyBean.ItemsBean> items) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).ftid = mReplyid;
        }
        mMoreReplyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMoreReplyAdapter = new MoreReplyAdapter(R.layout.item_reply_more, items);
        mMoreReplyRecyclerView.setAdapter(mMoreReplyAdapter);
        mMoreReplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_tv_name:
                    case R.id.item_image:
                        String userid = items.get(position).userid;
                        Intent intent = new Intent(MoreReplyActivity.this, FansCenterActivity.class);
                        intent.putExtra("fansId", userid);
                        startActivity(intent);
                        break;
                    case R.id.item_tv_content:
                    case R.id.item_tv_content2:
                        mParentid = items.get(position).tid + "";
                        replyLZ();
                        break;
                    case R.id.item_tv_name2:
                        String userid2 = items.get(position).userid2;
                        Intent intent2 = new Intent(MoreReplyActivity.this, FansCenterActivity.class);
                        intent2.putExtra("fansId", userid2);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.header_iv_left, R.id.more_reply_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.more_reply_input:
                mParentid = mReplyid;
                replyLZ();
                break;
        }
    }

    private void replyLZ() {
        View editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input, null);
        final PopupWindow editWindow = new PopupWindow(editView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editWindow.setBackgroundDrawable(null);
        editWindow.setOutsideTouchable(false);
        editWindow.setFocusable(true);
        final EditText replyEdit = (EditText) editView.findViewById(R.id.view_reply_et);
        editView.findViewById(R.id.view_reply_image).setVisibility(View.GONE);
        editView.findViewById(R.id.view_reply_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = replyEdit.getText().toString().trim();
                if (string.length() == 0) {
                    return;
                }
                OkGo
                        .post(AppUrl.BBS_REPLY
                                + "?bbsid=" + mBbsid
                                + "&parentid=" + mParentid
                                + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                                + "&content=" + string)
                        .execute(new StringDialogCallback(MoreReplyActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                editWindow.dismiss();
                                conunt++;
                                OkGo
                                        .post(AppUrl.BBS_LOOKMOREREPLIES)
                                        .params("bbsid", mBbsid)
                                        .params("replyid", mReplyid)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                MoreReplyBean moreReplyBean = new Gson().fromJson(s, MoreReplyBean.class);
                                                List<MoreReplyBean.ItemsBean> items = moreReplyBean.items;
                                                initRecyclerView(items);
                                                mMoreReplyRecyclerView.scrollToPosition(items.size() - 1);
                                                EventBus.getDefault().post(new MessageEvent("reply",conunt));
//                                                    ToastUtils.showShort(UIUtils.getContext(),items.size()+"");
                                            }
                                        });
                            }
                        });

            }
        });
        replyEdit.setFocusable(true);
        replyEdit.requestFocus();
        // 以下两句不能颠倒
        editWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        editWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        editWindow.showAtLocation(MoreReplyActivity.this.findViewById(R.id.more_reply), Gravity.BOTTOM, 0, 0);
        // 显示键盘
        final InputMethodManager imm = (InputMethodManager) UIUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        editWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
            }
        });
    }
}
