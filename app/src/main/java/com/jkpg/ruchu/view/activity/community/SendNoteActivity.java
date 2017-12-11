package com.jkpg.ruchu.view.activity.community;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.LocationBaiDuBean;
import com.jkpg.ruchu.bean.PlateBean;
import com.jkpg.ruchu.bean.SendNoteMess;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.ImageTools;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.PhotoAdapter;
import com.jkpg.ruchu.view.adapter.PlateNameRVAdapter;
import com.jkpg.ruchu.view.adapter.RecyclerItemClickListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Response;

import static android.widget.Toast.LENGTH_LONG;
import static com.jkpg.ruchu.R.id.send_note_cb_no_name;

/**
 * Created by qindi on 2017/6/6.
 */

public class SendNoteActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.send_note_tv_plate)
    TextView mSendNoteTvPlate;
    @BindView(R.id.send_note_et_title)
    EditText mSendNoteEtTitle;
    @BindView(R.id.send_note_et_body)
    EditText mSendNoteEtBody;
    @BindView(R.id.send_note_cb_position)
    CheckBox mSendNoteCbPosition;
    @BindView(send_note_cb_no_name)
    CheckBox mSendNoteCbNoName;
    @BindView(R.id.send_note_iv_image)
    ImageView mSendNoteIvImage;
    @BindView(R.id.send_note_tv_plate_content)
    TextView mSendNoteTvPlateContent;
    @BindView(R.id.send_note_recycler_view)
    RecyclerView mSendNoteRecyclerView;
    @BindView(R.id.send_note_view)
    LinearLayout mSendNoteView;
    private String mTitle;
    private BottomSheetDialog mPopupWindow;
    private List<PlateBean> data;

    private LocationManager mLocationManager;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private String mLocality = "";
    private String mPlateid;
    private String[] mPermissions;
    private double mLatitude = 0.0;
    private double mLongitude = 0.0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_note);
        ButterKnife.bind(this);
        initHeader();
        init();
        initCheckBox();
        initPhotoPicker();

    }


    private void initPhotoPicker() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos);

        mSendNoteRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        mSendNoteRecyclerView.setAdapter(photoAdapter);
        mSendNoteRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == 1) {
                            PhotoPicker.builder()
                                    .setPhotoCount(9)
                                    .setGridColumnCount(4)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(SendNoteActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(SendNoteActivity.this);
                        }
                    }
                }));
    }

    private void initCheckBox() {
        mSendNoteCbPosition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    initPermission();
                    if (!mLocality.equals("")) {
                        ToastUtils.showShort(UIUtils.getContext(), "你的位置:" + mLocality);
                    }
                }
            }
        });
    }

    private void initPermission() {
        mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtils.requestPermissions(this, 200, mPermissions, new PermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    initLocation();
                    LogUtils.i("initLocation");
                }

                @Override
                public void onPermissionDenied(String[] deniedPermissions) {
                    mSendNoteCbPosition.setChecked(false);
                }
            });
        } else {
            initLocation();
            LogUtils.i("initLocation");

        }
//        }
    }

    private void initLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 判断网络定位是否可用，可替换成 GPS 定位。
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "请检查位置权限是否打开",
                        LENGTH_LONG).show();
                return;
            }
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            //位置发生改变时回调该函数
                            //纬度
                            if (mLatitude == 0.0 || mLongitude == 0.0) {

                                mLatitude = location.getLatitude();
                                //经度
                                mLongitude = location.getLongitude();


                                OkGo
//                                        .post("http://gc.ditu.aliyun.com/regeocoding?l="+mLatitude+","+mLongitude+"&type=010")
                                        .post("http://api.map.baidu.com/geocoder/v2/?location=" + mLatitude + "," + mLongitude + "&output=json&pois=1&ak=CluAFhAwQB6EftNweYXlFtRy9ZrKqupB")
//                                        .params("location",mLatitude + "," + mLongitude)
//                                        .params("ak","2RCMLhMvC1As1KaNoNOb0GVNhwGLtSk4")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                LocationBaiDuBean locationBaiDuBean = new Gson().fromJson(s, LocationBaiDuBean.class);
                                                LogUtils.i(locationBaiDuBean.result.addressComponent.city);
                                                mLocality = locationBaiDuBean.result.addressComponent.city;
                                                ToastUtils.showLong(UIUtils.getContext(), "你的位置:" + mLocality);

                                            }
                                        });
                                mLocationManager.removeUpdates(this);
                            }

                            LogUtils.i("latitude:" + mLatitude);
                            LogUtils.i("longitude:" + mLongitude);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            //状态改变回调
                            //provider：定位器名称（NetWork,Gps等）
                            //status: 3中状态，超出服务范围，临时不可用，正常可用
                            //extras: 包含定位器一些细节信息
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            //定位开启回调
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            //定位关闭回调
                        }
                    });

        }
    }

    private void init() {
        mTitle = getIntent().getStringExtra("title");
        mPlateid = getIntent().getStringExtra("plateid");
        ArrayList<String> plate = getIntent().getStringArrayListExtra("plate");
        ArrayList<String> plateId = getIntent().getStringArrayListExtra("plateId");
        mSendNoteTvPlateContent.setText(mTitle);
        data = new ArrayList<>();
        for (int i = 0; i < plate.size(); i++) {
            data.add(new PlateBean(plate.get(i), plateId.get(i)));
        }
//        for (String s : plate) {
//            data.add(new PlateBean(s,));
//        }
        if (!SPUtils.getBoolean(UIUtils.getContext(), "notice", false)) {
            View view = View.inflate(UIUtils.getContext(), R.layout.view_plate_notice, null);
            final AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            SPUtils.saveBoolean(UIUtils.getContext(), "notice", true);

                        }
                    })
                    .show();
            view.findViewById(R.id.plate_notice_tv_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
        }
    }

    private void initHeader() {
        mHeaderTvTitle.setText("发帖");
        mHeaderTvRight.setText("发布");
    }

    @OnClick({R.id.header_iv_left, R.id.header_tv_right, R.id.send_note_tv_plate, R.id.send_note_iv_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_tv_right:
                send();
                break;
            case R.id.send_note_tv_plate:
                mSendNoteEtTitle.clearFocus();
                //隐藏键盘
                assert ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)) != null;
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(SendNoteActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPopupWindow();
                    }
                }, 300);
                break;
            case R.id.send_note_iv_image:
//                PhotoPicker.builder()
//                        .setPhotoCount(9)
//                        .setGridColumnCount(4)
//                        .start(SendNoteActivity.this);
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setGridColumnCount(4)
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setSelected(selectedPhotos)
                        .start(SendNoteActivity.this);
                break;
        }
    }
    @SuppressWarnings("deprecation")
    private void send() {
        if (StringUtils.isEmpty(mSendNoteEtTitle.getText().toString())) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入标题");
            return;
        }
        if (mSendNoteEtBody.getText().toString().length() < 10) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入内容长度不能少于10");
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(SendNoteActivity.this, R.style.dialog).
                setView(View.inflate(SendNoteActivity.this.getApplicationContext(), R.layout.view_animation, null));
        final AlertDialog show = builder.show();
        mHeaderTvRight.setClickable(false);
        if (selectedPhotos.size() == 0) {
            OkGo
                    .post(AppUrl.BBS_POST
                            + "?plateid=" + mPlateid
                            + "&title=" + mSendNoteEtTitle.getText().toString().trim()
                            + "&content=" + URLEncoder.encode(mSendNoteEtBody.getText().toString().trim())
                            + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                            + "&issite=" + (StringUtils.isEmpty(mLocality) ? 0 : 1)
                            + "&ishidename=" + (mSendNoteCbNoName.isChecked() ? 1 : 0)
                            + "&site=" + mLocality)
                    .execute(new StringDialogCallback(SendNoteActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            mHeaderTvRight.setClickable(true);

//                            EventBus.getDefault().post("send");
                            SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                            if (successBean.success) {
                                showSuccess();
                            } else {
                                ToastUtils.showShort(UIUtils.getContext(), "发帖失败");
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            mHeaderTvRight.setClickable(true);

                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            show.dismiss();
                        }
                    });
        } else {

            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < selectedPhotos.size(); i++) {
                File file = new File(selectedPhotos.get(i));

                Uri uri = Uri.fromFile(file);
                Bitmap bm = ImageTools.decodeUriAsBitmap(uri);
                String s = FileUtils.saveBitmapByQuality(bm, 40);
                files.add(new File(s));
            }
            OkGo
                    .post(AppUrl.BBS_POST
                            + "?plateid=" + mPlateid
                            + "&title=" + mSendNoteEtTitle.getText().toString().trim()
                            + "&content=" +URLEncoder.encode(mSendNoteEtBody.getText().toString().trim())
                            + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                            + "&issite=" + (StringUtils.isEmpty(mLocality) ? 0 : 1)
                            + "&ishidename=" + (mSendNoteCbNoName.isChecked() ? 1 : 0)
                            + "&site=" + mLocality)
//                    .params("plateid",mPlateid)
//                    .params("title",mSendNoteEtTitle.getText().toString())
//                    .params("content",mSendNoteEtBody.getText().toString())
//                    .params("userid",SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
//                    .params("issite",(mSendNoteCbPosition.isChecked() ? 1 : 0))
//                    .params("ishidename",(mSendNoteCbNoName.isChecked() ? 1 : 0))
//                    .params("site",mLocality)
                    .isMultipart(true)
                    .addFileParams("upload", files)
                    .execute(new StringDialogCallback(SendNoteActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            mHeaderTvRight.setClickable(true);
                            SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                            if (successBean.success) {
                                showSuccess();

                            } else {
                                ToastUtils.showShort(UIUtils.getContext(), "发帖失败");
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            mHeaderTvRight.setClickable(true);

                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            show.dismiss();
                        }
                    });
        }
    }

    private void showSuccess() {
        EventBus.getDefault().post("send");

        if (getIntent().getStringExtra("plateid").equals(mPlateid)) {
            new AlertDialog.Builder(SendNoteActivity.this)
                    .setTitle("帖子发布成功咯，快喊小伙伴来围观吧~")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            EventBus.getDefault().post("send");
                            finish();
                        }
                    }).show();
            MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 3000);
        } else {
            new AlertDialog.Builder(SendNoteActivity.this)
                    .setTitle("帖子在【" + mSendNoteTvPlateContent.getText() + "】已经发布成功啦，快过去看看吧！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EventBus.getDefault().post(new SendNoteMess(mSendNoteTvPlateContent.getText().toString(), mPlateid + "", "sendSkip"));
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();

        }
    }

    private void showPopupWindow() {


//        mPopupWindow = new PopupWindow(this);
//        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
//        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(UIUtils.getContext(), R.layout.view_recycler_view, null);
        view.findViewById(R.id.view_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.view_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        PlateNameRVAdapter adapter = new PlateNameRVAdapter(R.layout.item_plate_name, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSendNoteTvPlateContent.setText(data.get(position).title);
                mPlateid = data.get(position).plateid;
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow = new BottomSheetDialog(SendNoteActivity.this);
        mPopupWindow.setContentView(view);
        mPopupWindow.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mPopupWindow.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this, 200, mPermissions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();

            if (photos != null) {

                selectedPhotos.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

}
