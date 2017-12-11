package com.jkpg.ruchu.view.activity.my;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.HtmlActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/24.
 */

public class SetUpAboutActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.set_up_about_serve)
    TextView mSetUpAboutServe;
    @BindView(R.id.set_up_about_conceal)
    TextView mSetUpAboutConceal;
    @BindView(R.id.set_up_about_version)
    TextView mSetUpAboutVersion;
    @BindView(R.id.set_up_about_phone)
    RelativeLayout mSetUpAboutPhone;
    @BindView(R.id.set_up_about_wx)
    RelativeLayout mSetUpAboutWx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_about);
        ButterKnife.bind(this);
        initHeader();

        try {
            mSetUpAboutVersion.setText("当前版本号： " + UIUtils.getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initHeader() {
        mHeaderTvTitle.setText("关于如初");
    }

    @OnClick({R.id.set_up_about_serve, R.id.set_up_about_conceal, R.id.header_iv_left,
            R.id.set_up_about_phone, R.id.set_up_about_email, R.id.set_up_about_wx,R.id.set_up_about_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_up_about_serve:
                Intent intent = new Intent(SetUpAboutActivity.this, HtmlActivity.class);
                intent.putExtra("URL", Constants.XIEYI);
                startActivity(intent);
                break;
            case R.id.set_up_about_conceal:
                Intent intent1 = new Intent(SetUpAboutActivity.this, HtmlActivity.class);
                intent1.putExtra("URL", Constants.SECRET);
                startActivity(intent1);
                break;
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.set_up_about_phone:
                Intent intenturl = new Intent();
                intenturl.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://weibo.com/u/6289196115");
                intenturl.setData(content_url);
                startActivity(intenturl);
//                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                cm.setPrimaryClip(ClipData.newPlainText(null, getString(R.string.about_phone)));
//                ToastUtils.showShort(UIUtils.getContext(), "已复制到剪切板,去关注吧!");
                // 将文本内容放到系统剪贴板里。
//                PermissionUtils.requestPermissions(SetUpAboutActivity.this, 100, new String[]{Manifest.permission.CALL_PHONE}, new PermissionUtils.OnPermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        Uri data = Uri.parse("tel:" + getString(R.string.about_phone));
//                        intent.setData(data);
//                        if (ActivityCompat.checkSelfPermission(SetUpAboutActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            return;
//                        }
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(String[] deniedPermissions) {
//                        ToastUtils.showShort(UIUtils.getContext(), "请拨打手机号联系我们");
//                    }
//                });

                break;
            case R.id.set_up_about_wx:

                new AlertDialog.Builder(this)
                        .setTitle("如初康复")
                        .setMessage("欢迎关注如初康复微信公众号: ruchukangfu")
                        .setPositiveButton("复制", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClipboardManager cm1 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                cm1.setPrimaryClip(ClipData.newPlainText(null, getString(R.string.wx)));
                                ToastUtils.showShort(UIUtils.getContext(), "已复制到剪切板,欢迎关注");
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;

            case R.id.set_up_about_email:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822"); // 真机上使用这行
                i.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{getString(R.string.about_email)});
                i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
                i.putExtra(Intent.EXTRA_TEXT, "我们很希望能得到您的建议！！！");
                startActivity(Intent.createChooser(i,
                        "请选择您的邮箱"));
                break;
            case R.id.set_up_about_qq:
                joinQQGroup("m1BOrwUwhxFT5SIb77UB8VrZjYDNjIPL");
                break;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionUtils.onRequestPermissionsResult(SetUpAboutActivity.this, 100, new String[]{Manifest.permission.CALL_PHONE});
//    }
    /****************
     *
     * 发起添加群流程。群号：如初盆底康复(546071681) 的 key 为： m1BOrwUwhxFT5SIb77UB8VrZjYDNjIPL
     * 调用 joinQQGroup(m1BOrwUwhxFT5SIb77UB8VrZjYDNjIPL) 即可发起手Q客户端申请加群 如初盆底康复(546071681)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     *  public boolean joinQQGroup(String key) {
     *    Intent intent = new Intent();
     *    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
     *    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
     *    try {
     *      startActivity(intent);
     *        return true;
     *   } catch (Exception e) {
     *       // 未安装手Q或安装的版本不支持
     *       return false;
     *   }
     *}
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            new AlertDialog.Builder(this)
                    .setTitle("如初康复")
                    .setMessage("欢迎加入如初康复QQ群: 546071681")
                    .setPositiveButton("复制", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager cm1 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            if (cm1 != null) {
                                cm1.setPrimaryClip(ClipData.newPlainText(null, "546071681"));
                            }
                            ToastUtils.showShort(UIUtils.getContext(), "已复制到剪切板,欢迎加入");
                            dialog.dismiss();
                        }
                    })
                    .show();

            return false;
        }
    }

}
