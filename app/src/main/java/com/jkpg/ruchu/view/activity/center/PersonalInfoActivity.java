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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.PersonallInfoBean;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.ImageTools;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.AddressPickTask;
import com.jkpg.ruchu.widget.CircleImageView;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

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
import cn.qqtheme.framework.picker.LinkagePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;
import okhttp3.Response;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by qindi on 2017/5/15.
 */

public class PersonalInfoActivity extends BaseActivity {
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
    /* @BindView(R.id.personal_ll_ok)
     LinearLayout mPersonalLlOk;*/
   /* @BindView(R.id.personal_success)
    LinearLayout mPersonalSuccess;*/
    @BindView(R.id.personal_tv_parity)
    TextView mPersonalTvParity;
    @BindView(R.id.personal_rl_parity)
    RelativeLayout mPersonalRlParity;
    @BindView(R.id.personal_tv_time)
    TextView mPersonalTvTime;
    @BindView(R.id.personal_rl_time)
    RelativeLayout mPersonalRlTime;

    private PermissionListener permissionListener;
    private Uri outputUri;
    private String imgPath;//拍照完图片保存的路径
    private static final String TAG = "PerfectInfoActivity";

    private static final String FILE_PROVIDER_AUTHORITY = UIUtils.getPackageName() + ".provider";

    private static final int REQ_TAKE_PHOTO = 100;
    private static final int REQ_ALBUM = 101;
    private static final int REQ_ZOOM = 102;
    private String mScaleImgPath; //裁剪图片保存的路径


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        initData();
        initHeader();
    }

    private void initData() {


        OkGo
                .post(AppUrl.UPDATE_INFO)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(PersonalInfoActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.i(s);
                        PersonallInfoBean personallInfoBean = new Gson().fromJson(s, PersonallInfoBean.class);
                        init(personallInfoBean);

                    }
                });
    }

    private void init(PersonallInfoBean personallInfoBean) {
        Glide
                .with(UIUtils.getContext())
                .load(AppUrl.BASEURL + personallInfoBean.headImg)
                .crossFade()
                .error(R.drawable.icon_photo)
                .into(mPersonalCivPhoto);

        mPersonalTvName.setText(personallInfoBean.nick);
        mPersonalTvBirth.setText(personallInfoBean.birth);
        mPersonalTvHeight.setText(personallInfoBean.height);
        mPersonalTvWeight.setText(personallInfoBean.weight);
        mPersonalTvParity.setText(personallInfoBean.taici);
        mPersonalTvTime.setText(personallInfoBean.chanhoutime);
        mPersonalTvAddress.setText(personallInfoBean.address);
    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的资料");
    }

    @OnClick({R.id.personal_rl_photo, R.id.personal_rl_parity, R.id.personal_rl_time, R.id.personal_rl_name, R.id.personal_rl_birth, R.id.personal_rl_height, R.id.personal_rl_weight, R.id.personal_rl_address, R.id.personal_btn_save, R.id.header_iv_left})
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
            case R.id.personal_rl_parity:
                setParity();
                break;
            case R.id.personal_rl_time:
                setTime();
                break;
            case R.id.personal_btn_save:
                showOK();
                break;
            case R.id.header_iv_left:
                finish();
                break;
        }
    }

    private void setTime() {
//        final ArrayList<String> firstData = new ArrayList<>();
//        for (int i = 0; i < 37; i++) {
//            firstData.add(i + "个月");
//        }
//        final ArrayList<String> secondData = new ArrayList<>();
//        for (int i = 0; i < 31; i++) {
//            secondData.add(i + "天");
//        }
//        final DoublePicker picker = new DoublePicker(PersonalInfoActivity.this, firstData, secondData);
//        picker.setDividerVisible(false);
//        picker.setShadowColor(Color.WHITE, 80);
//        picker.setSelectedIndex(2, 1);
//
//        picker.setTextColor(getResources().getColor(R.color.colorPink));
//        picker.setDividerColor(Color.parseColor("#ffffff"));
//        picker.setSubmitTextColor(Color.parseColor("#000000"));
//        picker.setCancelTextColor(Color.parseColor("#000000"));
//        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
//
//        picker.setTopLineColor(Color.parseColor("#ffffff"));
//        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
//        picker.setOnPickListener(new DoublePicker.OnPickListener() {
//            @Override
//            public void onPicked(int selectedFirstIndex, int selectedSecondIndex) {
//                mPersonalTvTime.setText(firstData.get(selectedFirstIndex) + " " + secondData.get(selectedSecondIndex));
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
                for (int i = 0; i < 12; i++) {
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
                    for (int i = 0; i <= 30; i++) {
                        String str = DateUtils.fillZero(i);
                        secondList.add(str + "天");
                    }
                } else if (firstIndex < 16) {
                    for (int i = 0; i <= 12; i++) {
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
        LinkagePicker picker = new LinkagePicker(PersonalInfoActivity.this, provider);


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
        picker.setTextColor(getResources().getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
//                picker.setOnPickListener(new DoublePicker.OnPickListener() {
//                    @Override
//                    public void onPicked(int selectedFirstIndex, int selectedSecondIndex) {
//                        if (firstData.get(selectedFirstIndex).equals("0个月") && secondData.get(selectedSecondIndex).equals("0天")) {
//                            mView_bearing_tv_chsj.setText("无");
//                        } else {
//                            mView_bearing_tv_chsj.setText(firstData.get(selectedFirstIndex) + " " + secondData.get(selectedSecondIndex));
//                        }
//                    }
//                });
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                if (first.equals("0个月") && second.equals("00天")) {
                    mPersonalTvTime.setText("无");

                } else {
                    mPersonalTvTime.setText(first + " " + second);
                }

            }
        });
        picker.show();
    }

    private void setParity() {
        OptionPicker picker = new OptionPicker(PersonalInfoActivity.this, new String[]{"无", "头胎", "二胎", "三胎及以上"});
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.WRAP);
        picker.setShadowColor(Color.WHITE, 40);
        picker.setSelectedIndex(0);
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
        picker.setCycleDisable(true);
        picker.setTextSize(14);
        picker.setTextColor(getResources().getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                mPersonalTvParity.setText(item);
            }
        });
        picker.show();
    }

    private void showOK() {
/*
        if (!NetworkUtils.isAvailableByPing()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }*/
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

        // TODO: 2017/5/16
        OkGo
                .post(AppUrl.UPDATE_INFO)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("type", "1")
                .params("nick", mPersonalTvName.getText().toString())
                .params("birth", mPersonalTvBirth.getText().toString())
                .params("weight", mPersonalTvWeight.getText().toString())
                .params("height", mPersonalTvHeight.getText().toString())
                .params("address", mPersonalTvAddress.getText().toString())
                .params("chanhoutime", mPersonalTvTime.getText().toString())
                .params("taici", mPersonalTvParity.getText().toString())
                .execute(new StringDialogCallback(PersonalInfoActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                        if (!successBean.success) {
                            ToastUtils.showShort(UIUtils.getContext(), "昵称已存在");
                        } else {
//                            EventBus.getDefault().post();
                            EventBus.getDefault().post(new MessageEvent("MyFragment"));

                            AlertDialog dialog = new AlertDialog.Builder(PersonalInfoActivity.this)
                                    .setView(View.inflate(PersonalInfoActivity.this, R.layout.view_save_success, null))
                                    .show();
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    finish();
                                }
                            });
                            MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 3000);
                        }
                    }
                });

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
        //picker.setOffset(2);//偏移量
        picker.setRange(30, 120, 1);//数字范围
        picker.setSelectedItem(60);
        picker.setLabel("Kg");
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
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
        picker.setCycleDisable(false);//不禁用循环
        //picker.setOffset(2);//偏移量
        picker.setRange(140, 200, 1);//数字范围
        picker.setSelectedItem(160);
        picker.setLabel("cm");
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));

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
        picker.setUseWeight(false);

        picker.setCycleDisable(false);//不禁用循环
        picker.setDividerVisible(false);
        picker.setTopPadding(ConvertUtils.toPx(this, 20));
        picker.setRangeStart(1970, 1, 1);
        picker.setRangeEnd(2002, 12, 31);
        picker.setSelectedItem(1990, 5, 31);
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
                if (RegexUtils.isMatch(RegexUtils.REGEX_NAME, s.toString()) && RegexUtils.getStrlength(s.toString()) <= 20) {
//                if (s.length() <= 12) {
                    //^[a-zA-Z][a-zA-Z0-9_]{0,11}$
                    textInputLayout.setError("");
                    isOk[0] = true;
                } else {
                    textInputLayout.setError("不能超过20个字符和特殊字符");
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
                        if (Build.VERSION.SDK_INT >= M) {
                            //如果是6.0或6.0以上，则要申请运行时权限，这里需要申请拍照和写入SD卡的权限
                            requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    pickPictureFromSystem();
                                }

                                @Override
                                public void onDenied(List<String> deniedPermissions) {
                                }
                            });
                        } else {
                            pickPictureFromSystem();
                        }
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
                               /* temFile = new File(imgPath);
                                if (temFile.exists()) {
                                    temFile.delete();
                                }*/

                                //进行压缩
                                mScaleImgPath = FileUtils.saveBitmapByQuality(bm, 80);
                                LogUtils.i(TAG, "压缩后图片的路径为：" + mScaleImgPath);
                                Glide
                                        .with(UIUtils.getContext())
                                        .load(new File(mScaleImgPath))
                                        .into(mPersonalCivPhoto);
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

