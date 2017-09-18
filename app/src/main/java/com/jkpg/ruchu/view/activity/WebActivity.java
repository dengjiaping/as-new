package com.jkpg.ruchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.FineNoteWebBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/9/13.
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    private String mArt_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        mArt_id = getIntent().getStringExtra("art_id");
        OkGo
                .post(AppUrl.ARTICLEDETAIL)
                .tag(this)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("art_id", mArt_id)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        FineNoteWebBean fineNoteWebBean = new Gson().fromJson(s, FineNoteWebBean.class);
                        FineNoteWebBean.List1Bean list1 = fineNoteWebBean.list1;
                        mWebView.loadData(list1.Content, "text/html; charset=UTF-8", null);
                        mHeaderTvTitle.setText(list1.header);
                    }
                });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent(WebActivity.this, ShopActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                return true;
            }
        });
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
