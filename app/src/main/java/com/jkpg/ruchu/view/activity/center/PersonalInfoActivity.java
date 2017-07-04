package com.jkpg.ruchu.view.activity.center;

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
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.AnimationUtil;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.ImageTools;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.AddressPickTask;
import com.jkpg.ruchu.widget.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.util.ConvertUtils;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by qindi on 2017/5/15.
 */

public class PersonalInfoActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.personal_civ_photo)
    CircleImageView mPersonalCivPhoto;
    @BindView(R.id.personal_rl_photo)
    RelativeLayout mPersonalRlPhoto;
    @BindView(R.id.personal_tv_name)
    TextView mPersonalTvName;
    @BindView(R.id.personal_rl_name)
    RelativeLayout mPersonalRlName;
    @BindView(R.id.personal_tv_birth)
    TextView mPersonalTvBirth;
    @BindView(R.id.personal_rl_birth)
    RelativeLayout mPersonalRlBirth;
    @BindView(R.id.personal_tv_height)
    TextView mPersonalTvHeight;
    @BindView(R.id.personal_rl_height)
    RelativeLayout mPersonalRlHeight;
    @BindView(R.id.personal_tv_weight)
    TextView mPersonalTvWeight;
    @BindView(R.id.personal_rl_weight)
    RelativeLayout mPersonalRlWeight;
    @BindView(R.id.personal_tv_address)
    TextView mPersonalTvAddress;
    @BindView(R.id.personal_rl_address)
    RelativeLayout mPersonalRlAddress;
    @BindView(R.id.personal_btn_save)
    Button mPersonalBtnSave;
    @BindView(R.id.personal_view)
    ScrollView mPersonalView;
    @BindView(R.id.personal_ll_ok)
    LinearLayout mPersonalLlOk;
    @BindView(R.id.personal_success)
    LinearLayout mPersonalSuccess;

    private PermissionListener permissionListener;
    private Uri outputUri;
    private String imgPath;//拍照完图片保存的路径
    private static final String TAG = "PerfectInfoActivity";

    private static final String FILE_PROVIDER_AUTHORITY = UIUtils.getPackageName() + ".provider";

    private static final int REQ_TAKE_PHOTO = 100;
    private static final int REQ_ALBUM = 101;
    private static final int REQ_ZOOM = 102;
    private String mScaleImgPath; //裁剪图片保存的路径

    private static String URL = "https://www.ruchuapp.com/AppServer/Update_InfoServlet";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        initHeader();
        initPhoto();
    }

    private void initPhoto() {
        String imgPath = SPUtils.getString(UIUtils.getContext(), Constants.PERSONAIMAGE, "");
        if (!StringUtils.isEmpty(imgPath)) {
            Glide
                    .with(UIUtils.getContext())
                    .load(new File(imgPath))
                    .error(R.drawable.icon_photo)
                    .into(mPersonalCivPhoto);
        }
    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的资料");
    }

    @OnClick({R.id.personal_rl_photo, R.id.personal_rl_name, R.id.personal_rl_birth, R.id.personal_rl_height, R.id.personal_rl_weight, R.id.personal_rl_address, R.id.personal_btn_save,R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personal_rl_photo:
                setPhoto();
                break;
            case R.id.personal_rl_name:
                setName();
                break;
            case R.id.personal_rl_birth:
                setBirth();
                break;
            case R.id.personal_rl_height:
                setHeight();
                break;
            case R.id.personal_rl_weight:
                setWeight();
                break;
            case R.id.personal_rl_address:
                setAddress();
                break;
            case R.id.personal_btn_save:
                showOK();
                break;
            case R.id.header_iv_left:
                finish();
                break;
        }
    }

    private void showOK() {
//        URL = URL + "?username=" + mPersonalTvName.getText().toString() + "&shengao=" + mPersonalTvHeight.getText().toString() + "&tizhong" + mPersonalTvWeight.getText().toString() +
//                "&birth=" + mPersonalTvBirth.getText().toString() + "&address=" + mPersonalTvAddress.getText().toString() + "&u_tel=00000000000";
//        OkGo.post(URL)
//                .tag(this)
//                .isMultipart(true)
//                .params("image", new File(mScaleImgPath))
//                .execute(new StringCallback() {
//
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                    }
//                });


        // TODO: 2017/5/16


        mPersonalSuccess.setVisibility(View.VISIBLE);
        mPersonalLlOk.setAnimation(AnimationUtil.createPanInAnim(2000));

    }

    private void setAddress() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                ToastUtils.showShort(UIUtils.getContext(), "城市数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                mPersonalTvAddress.setText(province.getAreaName() + "  " + city.getAreaName());
            }
        });
        task.execute("山东", "济南");
    }

    private void setWeight() {
        NumberPicker picker = new NumberPicker(this);
//        picker.setWidth(picker.getScreenWidthPixels());
        picker.setCanceledOnTouchOutside(true);
        picker.setDividerVisible(false);
        picker.setCycleDisable(false);//不禁用循环
        picker.setItemWidth(picker.getScreenWidthPixels());
        //picker.setOffset(2);//偏移量
        picker.setRange(30, 150, 1);//数字范围
        picker.setSelectedItem(45);
        picker.setLabel("Kg");
        picker.setTextColor(getResources().getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                mPersonalTvWeight.setText(item.intValue() + " kg");
            }
        });
        picker.show();
    }

    private void setHeight() {
        NumberPicker picker = new NumberPicker(this);
        picker.setWidth(picker.getScreenWidthPixels());
        picker.setDividerVisible(false);
        picker.setCanceledOnTouchOutside(true);
        picker.setItemWidth(picker.getScreenWidthPixels());
        picker.setCycleDisable(false);//不禁用循环
        //picker.setOffset(2);//偏移量
        picker.setRange(145, 200, 1);//数字范围
        picker.setSelectedItem(168);
        picker.setLabel("Cm");
        picker.setTextColor(getResources().getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                mPersonalTvHeight.setText(item.intValue() + " cm");
            }
        });
        picker.show();
    }

    private void setBirth() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setCycleDisable(false);//不禁用循环
        picker.setDividerVisible(false);
        picker.setTopPadding(ConvertUtils.toPx(this, 20));
        picker.setRangeStart(1970, 1, 1);
        picker.setRangeEnd(2017, 1, 1);
        picker.setSelectedItem(1990, 5, 20);
        picker.setTextColor(getResources().getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                mPersonalTvBirth.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

    private void setName() {
        final boolean[] isOk = new boolean[1];
        final LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.view_edittext, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text_name);
        final TextInputLayout textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 12) {
                    textInputLayout.setError("");
                    isOk[0] = true;
                } else {
                    textInputLayout.setError("不能超过12个字符");
                    isOk[0] = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // final EditText editText = new EditText(this);
        editText.setText(mPersonalTvName.getText());
        editText.setSelection(mPersonalTvName.getText().length());


        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("修改昵称")
                .setView(view/*, UIUtils.dip2Px(20), UIUtils.dip2Px(20), UIUtils.dip2Px(20), UIUtils.dip2Px(20)*/)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOk[0])
                            mPersonalTvName.setText(editText.getText());
                        else {

                        }
                    }
                })
                .show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOk[0]) {
                    mPersonalTvName.setText(editText.getText());
                    dialog.dismiss();
                }
            }
        });
    }

    private void setPhoto() {
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
        })/*.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })*/.show();
    }

    private void pickPictureFromCamera() {
        if (Build.VERSION.SDK_INT >= M) {
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
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, REQ_ALBUM);
    }


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
                                Glide
                                        .with(UIUtils.getContext())
                                        .load(new File(mScaleImgPath))
                                        .into(mPersonalCivPhoto);
                                SPUtils.saveString(UIUtils.getContext(), Constants.PERSONAIMAGE, mScaleImgPath);
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
}
