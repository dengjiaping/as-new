package com.jkpg.ruchu.view.activity.community;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.MoreReplyBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.my.FansCenterActivity;
import com.jkpg.ruchu.view.adapter.FineMoreReplyFixAdapter;
import com.jkpg.ruchu.widget.ColorStyleTextView;
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
 * Created by qindi on 2017/9/2.
 */

public class FineMoreReplyFixActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.more_reply_recycler_view)
    RecyclerView mMoreReplyRecyclerView;
    @BindView(R.id.more_reply_input)
    TextView mMoreReplyInput;
    private int conunt = 0;
    private String mParentid;
    private FineMoreReplyFixAdapter mMoreReplyAdapter;
    private String mArt_id;
    private String mCommentid;
    private PopupWindow mEditWindow;
    private List<MoreReplyBean.ItemsBean> mItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_reply);
        ButterKnife.bind(this);
        mArt_id = getIntent().getStringExtra("art_id");
        mCommentid = getIntent().getStringExtra("commentid");
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mArt_id = intent.getStringExtra("art_id");
        mCommentid = intent.getStringExtra("commentid");
        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.ARTICLELOOKMOREREPLIES)
                .params("art_id", mArt_id)
                .params("commentid", mCommentid)
                .execute(new StringDialogCallback(FineMoreReplyFixActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MoreReplyBean moreReplyBean = new Gson().fromJson(s, MoreReplyBean.class);
                        mItems = moreReplyBean.items;
                        initRecyclerView(mItems);
                        mHeaderTvTitle.setText("共有" + mItems.size() + "条回复");

                    }
                });

    }

    private void initRecyclerView(final List<MoreReplyBean.ItemsBean> items) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).ftid = mCommentid;
        }
        mMoreReplyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMoreReplyAdapter = new FineMoreReplyFixAdapter(R.layout.item_reply_more_fix, items);
        mMoreReplyRecyclerView.setAdapter(mMoreReplyAdapter);
        mMoreReplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.item_tv_name:
                    case R.id.item_image:
                        String userid = items.get(position).userid;
                        Intent intent = new Intent(FineMoreReplyFixActivity.this, FansCenterActivity.class);
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
                        Intent intent2 = new Intent(FineMoreReplyFixActivity.this, FansCenterActivity.class);
                        intent2.putExtra("fansId", userid2);
                        startActivity(intent2);
                        break;
                }
                if (view instanceof ColorStyleTextView){
                    ColorStyleTextView textView = (ColorStyleTextView) view;
                    textView.setOnClickCallBack(new ColorStyleTextView.ClickCallBack() {
                        @Override
                        public void onClick(int a) {
                            switch (a){
                                case 0:
                                    String userid2 = items.get(position).userid2;
                                    Intent intent2 = new Intent(FineMoreReplyFixActivity.this, FansCenterActivity.class);
                                    intent2.putExtra("fansId", userid2);
                                    startActivity(intent2);
                                    break;
                                case 1:
                                    mParentid = items.get(position).tid + "";
                                    replyLZ();
                                    break;
                            }
                        }
                    });
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
                mParentid = mCommentid;
                replyLZ();
                break;
        }
    }

    private void replyLZ() {
        View editView;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1 ||Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {

            editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input_22, null);
            editView.findViewById(R.id.view_reply_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditWindow.dismiss();
                }
            });

        } else {
            editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input, null);
        }
//        View editView = View.inflate(UIUtils.getContext(), R.layout.view_reply_input, null);
        mEditWindow = new PopupWindow(editView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mEditWindow.setBackgroundDrawable(null);
        mEditWindow.setOutsideTouchable(false);
        mEditWindow.setFocusable(true);
        final EditText replyEdit = (EditText) editView.findViewById(R.id.view_reply_et);
        editView.findViewById(R.id.view_reply_image).setVisibility(View.GONE);
        editView.findViewById(R.id.view_reply_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = replyEdit.getText().toString().trim();
                if (string.length() == 0) {
                    ToastUtils.showShort(UIUtils.getContext(),"请输入内容哦");
                    return;
                }
                OkGo
                        .post(AppUrl.ArticleReply)
                        .params("art_id",mArt_id)
                        .params("parentid",mParentid)
                        .params("userid",SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("content",string)
                        .execute(new StringDialogCallback(FineMoreReplyFixActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                mEditWindow.dismiss();
                                conunt++;
                                OkGo
                                        .post(AppUrl.ARTICLELOOKMOREREPLIES)
                                        .params("art_id", mArt_id)
                                        .params("commentid", mCommentid)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                MoreReplyBean moreReplyBean = new Gson().fromJson(s, MoreReplyBean.class);
                                                List<MoreReplyBean.ItemsBean> items = moreReplyBean.items;
                                                for (int i = 0; i < items.size(); i++) {
                                                    items.get(i).ftid = mCommentid;
                                                }
                                                mItems.clear();
                                                mItems.addAll(items);
                                                mMoreReplyAdapter.notifyDataSetChanged();
//                                                initRecyclerView(items);
                                                mMoreReplyRecyclerView.scrollToPosition(items.size() - 1);
                                                mHeaderTvTitle.setText("共有" + mItems.size() + "条回复");

                                                //// TODO: 2017/9/2  
                                                EventBus.getDefault().post(new MessageEvent("reply", conunt));
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
        mEditWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mEditWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditWindow.showAtLocation(FineMoreReplyFixActivity.this.findViewById(R.id.more_reply), Gravity.BOTTOM, 0, 0);
        // 显示键盘
        final InputMethodManager imm = (InputMethodManager) UIUtils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        mEditWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (imm.isActive()) imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
            }
        });
    }
}
