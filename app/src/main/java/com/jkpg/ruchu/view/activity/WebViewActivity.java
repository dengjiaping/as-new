package com.jkpg.ruchu.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.config.AppUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by qindi on 2017/8/1.
 */

public class WebViewActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    private String mUrl = "";

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        WebSettings webSettings = mWebView.getSettings();
//支持缩放，默认为true。
        webSettings.setSupportZoom(false);
//调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
// 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
//设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
////设置自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
//多窗口
        webSettings.supportMultipleWindows();
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        ;
//获取触摸焦点
        mWebView.requestFocusFromTouch();
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

//允许访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
//开启javascript
        webSettings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//提高渲染的优先级
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mHeaderTvTitle.setText(title);
            }
        });
        mWebView.loadUrl(AppUrl.BASEURLHTTP + getIntent().getStringExtra("URL"));
        mHeaderTvTitle.setText(mWebView.getTitle());

        mWebView.setWebViewClient(new WebViewClient() {
            // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                mUrl = url;
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                mUrl = request.getUrl().toString();

                return true;
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK)) {


            if (!mUrl.equals("")) {

                mWebView.loadUrl(AppUrl.BASEURLHTTP + getIntent().getStringExtra("URL"));
                mUrl = "";
                return true;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        if (!mUrl.equals("")) {
            mWebView.loadUrl(AppUrl.BASEURLHTTP + getIntent().getStringExtra("URL"));
            mUrl = "";
        } else {
            finish();
        }
    }

}
