package com.jkpg.ruchu.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.config.AppUrl;
import com.youzan.sdk.YouzanHybrid;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/7/19.
 */

public class ShopActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.shop_view)
    YouzanHybrid mShopView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("商城");
        mShopView.loadUrl(AppUrl.SHOP);

    }


    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
    //    private static final int CODE_REQUEST_LOGIN = 0x101;
//    private YouzanHybrid mView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mView = new YouzanHybrid(this);
//        setContentView(mView);
//
//        setupYouzanView(mView);
//
//        //替换成需要展示入口的链接
//        mView.loadUrl(AppUrl.SHOP);
//    }
//
//
//    private void setupYouzanView(YouzanClient client) {
//        //订阅认证事件
//        client.subscribe(new AbsAuthEvent() {
//            /**
//             * 有赞SDK认证回调.
//             * 在加载有赞的页面时, SDK相应会回调该方法.
//             *
//             * 从自己的服务器上请求同步认证后组装成{@link com.youzan.sdk.YouzanToken}, 调用{code view.sync(token);}同步信息.
//             *
//             * @param view 发起回调的视图
//             * @param needLogin 表示当下行为是否需要需要用户角色的认证信息, True需要.
//             */
//            @Override
//            public void call(View view, boolean needLogin) {
//                /**
//                 * <pre>
//                 *     建议代码逻辑:
//                 *
//                 *     判断App内的用户是否登录?
//                 *       => 已登录: 请求带用户角色的认证信息(login接口);
//                 *       => 未登录: needLogin为true, 唤起App内登录界面, 请求带用户角色的认证信息(login接口);
//                 *       => 未登录: needLogin为false, 请求不带用户角色的认证信息(initToken接口).
//                 *  </pre>
//                 */
//                //实现代码略...
//                startActivity(new Intent(ShopActivity.this, LoginActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//
//            }
//        });
//
//        //订阅文件选择事件
//        client.subscribe(new AbsChooserEvent() {
//            @Override
//            public void call(View view, Intent intent, int requestCode) throws ActivityNotFoundException {
//                //调用系统图片选择器
//                startActivity(intent);
//            }
//        });
//
//        //订阅分享事件
//        client.subscribe(new AbsShareEvent() {
//            @Override
//            public void call(View view, GoodsShareModel data) {
//                /**
//                 * 在获取数据后, 可以使用其他分享SDK来提高分享体验.
//                 * 这里调用系统分享来简单演示分享的过程.
//                 */
//                String content = String.format("%s %s", data.getDesc(), data.getLink());
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
//                sendIntent.putExtra(Intent.EXTRA_SUBJECT, data.getTitle());
//                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            /**
//             * 用户登录成功返回, 从自己的服务器上请求同步认证后组装成{@link com.youzan.sdk.YouzanToken},
//             * 调用{code view.sync(token);}同步信息.
//             */
//            if (CODE_REQUEST_LOGIN == requestCode) {
//                //mView.sync(token);
//            } else {
//                //处理文件上传
//                mView.receiveFile(requestCode, data);
//            }
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (!mView.pageGoBack()) {
//            super.onBackPressed();
//        }
//    }
}
