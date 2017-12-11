package com.jkpg.ruchu.view.activity.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.ImageTools;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CircleImageView;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.LinkagePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/11.
 */

public class PerfectInfoActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.perfect_civ_photo)
    CircleImageView mPerfectCivPhoto;
    @BindView(R.id.perfect_et_name)
    EditText mPerfectEtName;

    private static final String TAG = "PerfectInfoActivity";

    private static final String FILE_PROVIDER_AUTHORITY = UIUtils.getPackageName() + ".provider";

    private static final int REQ_TAKE_PHOTO = 100;
    private static final int REQ_ALBUM = 101;
    private static final int REQ_ZOOM = 102;
    @BindView(R.id.perfect_tv_birth)
    TextView mPerfectTvBirth;
    @BindView(R.id.perfect_tv_height)
    TextView mPerfectTvHeight;
    @BindView(R.id.perfect_tv_weight)
    TextView mPerfectTvWeight;
    @BindView(R.id.perfect_btn_save)
    Button mPerfectBtnSave;

    private PermissionListener permissionListener;
    private Uri outputUri;
    private String imgPath;//拍照完图片保存的路径
    private String mScaleImgPath;
    private boolean hasImage = false;
    private String mNameString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_info);
        setSwipeBackEnable(false);

        ButterKnife.bind(this);
        initHeader();
        initPhoto();
    }


    private void initPhoto() {
        String imgPath = SPUtils.getString(UIUtils.getContext(), Constants.USERIMAGE, "");
        LogUtils.d(imgPath + "imgPath");
        String name = SPUtils.getString(UIUtils.getContext(), Constants.USERNANE, "");
        if (!StringUtils.isEmpty(imgPath)) {
            Glide
                    .with(UIUtils.getContext())
                    .load(imgPath)
                    .crossFade()
                    .centerCrop()
                    .error(R.drawable.icon_photo)
                    .into(mPerfectCivPhoto);
            if (name != null && name.length() == 0) {
                mPerfectEtName.setSelection(name.length());
            }
        }
        mPerfectEtName.setText(name);
        LogUtils.d(AppUrl.BASEURL + imgPath);
    }


    private void initHeader() {
        mHeaderTvRight.setText("跳过");
        mHeaderIvLeft.setVisibility(View.GONE);
        mHeaderTvTitle.setText("完善信息");
    }


    @OnClick({R.id.header_iv_left, R.id.header_tv_right, R.id.perfect_civ_photo, R.id.perfect_tv_birth, R.id.perfect_tv_height, R.id.perfect_tv_weight, R.id.perfect_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                break;
            case R.id.header_tv_right:
                String s = mPerfectEtName.getText().toString().trim();
                if (RegexUtils.isMatch(RegexUtils.REGEX_NAME, s) && RegexUtils.getStrlength(s) <= 20) {

                } else {
                    ToastUtils.showShort(UIUtils.getContext(), "昵称不能超过20个字符和特殊字符哦");
                    return;
                }
                OkGo
                        .post(AppUrl.LOGINNEXTADDMESS)
                        .params("userid",SPUtils.getString(UIUtils.getContext(),Constants.USERID,""))
                        .params("birthday", "")
                        .params("taici", "")
                        .params("chanHouShiJian", "")
                        .params("nick", s)
                        .execute(new StringDialogCallback(this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                if (successBean.error == 233) {
                                    ToastUtils.showShort(UIUtils.getContext(), "昵称重复了哦,请修改后再提交呢");
                                } else {
                                    EventBus.getDefault().post(new MessageEvent("Login"));
                                    finish();
                                }
                            }
                        });

                // startActivity(new Intent(PerfectInfoActivity.this, MainActivity.class));
//                EventBus.getDefault().post(new MessageEvent("Login"));
//                finish();
                break;
            case R.id.perfect_civ_photo:
                showDialogSelect();
                break;
            case R.id.perfect_tv_birth:
                onYearMonthDayPicker();
                break;
            case R.id.perfect_tv_height:
                countPicker();
                break;
            case R.id.perfect_tv_weight:
                timePicker();
                break;
            case R.id.perfect_btn_save:
                saveInfo();
                break;
        }
    }

    private void saveInfo() {
       /* if (mPerfectCivPhoto.getDrawable().getCurrent().getConstantState()
                == getResources().getDrawable(R.drawable.icon_photo).getConstantState()) {
            ToastUtils.showShort(UIUtils.getContext(), "请上传你的头像");
            return;
        }
        String name = mPerfectEtName.getText().toString();
        if (StringUtils.isEmpty(name)) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入你的专属昵称");
            return;
        }
        if (name.length() > 12) {
            ToastUtils.showShort(UIUtils.getContext(), "专属昵称不能超过十二个字符");
            return;
        }*/
       /* String birth = mPerfectTvBirth.getText().toString();
        if (StringUtils.isEmpty(birth)) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入你的生日");
            return;
        }
        String height = mPerfectTvHeight.getText().toString();
        if (StringUtils.isEmpty(height)) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入你的胎次");
            return;
        }*/
       /* String weight = mPerfectTvWeight.getText().toString();
        if (StringUtils.isEmpty(weight)) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入你的产后时间");
            return;
        }*/

        String s = mPerfectEtName.getText().toString().trim();
        if (RegexUtils.isMatch(RegexUtils.REGEX_NAME, s) && RegexUtils.getStrlength(s) <= 20) {

        } else {
            ToastUtils.showShort(UIUtils.getContext(), "昵称不能超过20个字符和特殊字符哦");
            return;
        }
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        if (!StringUtils.isEmpty(mScaleImgPath)) {

            OkGo
                    .post(AppUrl.UPDATEHEADIMG + "?userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                    .isMultipart(true)
                    .params("image", new File(mScaleImgPath))
                    .execute(new StringDialogCallback(this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            ToastUtils.showShort(UIUtils.getContext(), "头像保存失败");
                        }
                    });
        }

        OkGo
                .post(AppUrl.LOGINNEXTADDMESS)
                .params("userid",SPUtils.getString(UIUtils.getContext(),Constants.USERID,""))
                .params("birthday", mPerfectTvBirth.getText().toString())
                .params("taici", mPerfectTvHeight.getText().toString())
                .params("chanHouShiJian", mPerfectTvWeight.getText().toString())
                .params("nick", s)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                        if (successBean.error == 233) {
                            ToastUtils.showShort(UIUtils.getContext(), "昵称重复,请修改昵称后提交");
                        } else {
                            EventBus.getDefault().post(new MessageEvent("Login"));
                            finish();
                        }
                    }
                });
    }

    // 产后时间
    private void timePicker() {
//        final ArrayList<String> firstData = new ArrayList<>();
//        for (int i = 0; i < 37; i++) {
//            firstData.add(i + "个月");
//        }
//        final ArrayList<String> secondData = new ArrayList<>();
//        for (int i = 0; i < 31; i++) {
//            secondData.add(i + "天");
//        }
//        final DoublePicker picker = new DoublePicker(PerfectInfoActivity.this, firstData, secondData);
//        picker.setDividerVisible(false);
//        picker.setShadowColor(Color.WHITE, 80);
//        picker.setSelectedIndex(2, 1);
//        picker.setCanceledOnTouchOutside(true);
//        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
//        picker.setTextColor(getResources().getColor(R.color.colorPink));
//        picker.setDividerColor(Color.parseColor("#ffffff"));
//        picker.setSubmitTextColor(Color.parseColor("#000000"));
//        picker.setCancelTextColor(Color.parseColor("#000000"));
//        picker.setTopLineColor(Color.parseColor("#ffffff"));
//        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
//        picker.setOnPickListener(new DoublePicker.OnPickListener() {
//            @Override
//            public void onPicked(int selectedFirstIndex, int selectedSecondIndex) {
//                mPerfectTvWeight.setText(firstData.get(selectedFirstIndex) + " " + secondData.get(selectedSecondIndex));
//            }
//        });
//        picker.show();
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {

            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @NonNull
            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                firstList.add("");
                for (int i = 1; i < 12; i++) {
                    firstList.add(i + "个月");
                }
                firstList.add("1年");
                firstList.add("2年");
                firstList.add("3年");
                firstList.add("4年");
                firstList.add("5年");

                return firstList;
            }

            @NonNull
            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
                if (firstIndex < 12) {
                    for (int i = 1; i <= 30; i++) {
                        String str = DateUtils.fillZero(i);
                        secondList.add(str + "天");
                    }
                } else if (firstIndex < 16) {
                    secondList.add("");
                    for (int i = 1; i <= 11; i++) {
                        String str = DateUtils.fillZero(i);
                        secondList.add(str + "月");
                    }
                } else {
                    secondList.add("及以上");

                }
                return secondList;
            }

            @Nullable
            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }

        };
        LinkagePicker picker = new LinkagePicker(PerfectInfoActivity.this, provider);


//                final ArrayList<String> firstData = new ArrayList<>();
//                for (int i = 0; i < 37; i++) {
//                    firstData.add(i + "个月");
//                }
//                final ArrayList<String> secondData = new ArrayList<>();
//                for (int i = 0; i < 31; i++) {
//                    secondData.add(i + "天");
//                }
//                final DoublePicker picker = new DoublePicker(MyFilesActivity.this, firstData, secondData);
        picker.setLabel("", "");
        picker.setDividerVisible(false);
        picker.setShadowColor(Color.WHITE, 80);
        picker.setSelectedIndex(2, 1);
        picker.setCanceledOnTouchOutside(true);
        picker.setTextColor(UIUtils.getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(UIUtils.getColor(R.color.colorPink));
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {

                    mPerfectTvWeight.setText(first + " " + second);

            }
        });
        picker.show();
    }

    //出生年月
    private void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(PerfectInfoActivity.this, DatePicker.YEAR_MONTH_DAY);
        picker.setCanceledOnTouchOutside(true);
        picker.setCycleDisable(false);//不禁用循环
        picker.setDividerVisible(false);
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
        picker.setRangeStart(1970, 1, 1);
        picker.setUseWeight(false);
        picker.setRangeEnd(2002, 11, 31);
        picker.setSelectedItem(1990, 5, 31);
        picker.setTextColor(UIUtils.getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(UIUtils.getColor(R.color.colorPink));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                mPerfectTvBirth.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

    //胎次
    private void countPicker() {
        OptionPicker picker = new OptionPicker(PerfectInfoActivity.this, new String[]{ "头胎", "二胎", "三胎及以上"});
        picker.setCanceledOnTouchOutside(true);
        picker.setDividerRatio(WheelView.DividerConfig.WRAP);
        picker.setShadowColor(Color.WHITE, 40);
        picker.setSelectedIndex(0);
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
        picker.setCycleDisable(true);
        picker.setTextSize(14);
        picker.setTextColor(UIUtils.getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(UIUtils.getColor(R.color.colorPink));
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mPerfectTvHeight.setText(item);
            }
        });
        picker.show();
    }

    private void showDialogSelect() {
        new AlertDialog.Builder(this).setTitle("选择图片").setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        pickPictureFromCamera();
                        break;
                    case 1:
                        pickPictureFromSystem();
                        break;
                }

            }
        }).show();
    }

    private void pickPictureFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0或6.0以上，则要申请运行时权限，这里需要申请拍照和写入SD卡的权限
            requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                @Override
                public void onGranted() {
                    openCamera();
                }

                @Override
                public void onDenied(List<String> deniedPermissions) {
                }
            });
            return;
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        // 指定调用相机拍照后照片的储存路径
        imgPath = FileUtils.generateImgePath();
        File imgFile = new File(imgPath);
        Uri imgUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0或以上
            imgUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imgFile);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, REQ_TAKE_PHOTO);
    }

   /* */

    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        permissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }


    public interface PermissionListener {

        void onGranted();

        void onDenied(List<String> deniedPermissions);

    }


    private void pickPictureFromSystem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0或6.0以上，则要申请运行时权限，这里需要申请拍照和写入SD卡的权限
            requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                @Override
                public void onGranted() {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(intent, REQ_ALBUM);
                }

                @Override
                public void onDenied(List<String> deniedPermissions) {
                }
            });
            return;
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            startActivityForResult(intent, REQ_ALBUM);
        }

    }

    @SuppressWarnings("deprecation")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK://调用图片选择处理成功
                Bitmap bm = null;
                File temFile = null;
                File srcFile = null;
                File outPutFile = null;
                switch (requestCode) {
                    case REQ_TAKE_PHOTO:// 拍照后在这里回调
                        srcFile = new File(imgPath);
                        outPutFile = new File(FileUtils.generateImgePath());
                        outputUri = Uri.fromFile(outPutFile);
                        FileUtils.startPhotoZoom(this, srcFile, outPutFile, REQ_ZOOM);// 发起裁剪请求
                        break;

                    case REQ_ALBUM:// 选择相册中的图片
                        if (data != null) {
                            Uri sourceUri = data.getData();
                            String[] proj = {MediaStore.Images.Media.DATA};

                            // 好像是android多媒体数据库的封装接口，具体的看Android文档
                            Cursor cursor = managedQuery(sourceUri, proj, null, null, null);

                            // 按我个人理解 这个是获得用户选择的图片的索引值
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                            cursor.moveToFirst();
                            // 最后根据索引值获取图片路径
                            String imgPath = cursor.getString(column_index);

                            srcFile = new File(imgPath);
                            outPutFile = new File(FileUtils.generateImgePath());
                            outputUri = Uri.fromFile(outPutFile);
                            FileUtils.startPhotoZoom(this, srcFile, outPutFile, REQ_ZOOM);// 发起裁剪请求
                        }
                        break;

                    case REQ_ZOOM://裁剪后回调
                        //  Bundle extras = data.getExtras();
                        if (data != null) {
                            //  bm = extras.getParcelable("data");
                            if (outputUri != null) {
                                bm = ImageTools.decodeUriAsBitmap(outputUri);
                                //如果是拍照的,删除临时文件
                                temFile = new File(imgPath);
                                if (temFile.exists()) {
                                    temFile.delete();
                                }

                                //进行压缩
                                mScaleImgPath = FileUtils.saveBitmapByQuality(bm, 80);
                                LogUtils.i(TAG, "压缩后图片的路径为：" + mScaleImgPath);
                                hasImage = true;

                                Glide
                                        .with(UIUtils.getContext())
                                        .load(new File(mScaleImgPath))
                                        .into(mPerfectCivPhoto);
                                //mPerfectCivPhoto.setImageBitmap(bm);
                            }
                        } else {
                            ToastUtils.showShort(UIUtils.getContext(), "选择图片发生错误，图片可能已经移位或删除");
                        }
                        break;
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        permissionListener.onGranted();
                    } else {
                        permissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
