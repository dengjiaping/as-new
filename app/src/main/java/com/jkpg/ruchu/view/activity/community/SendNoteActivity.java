package com.jkpg.ruchu.view.activity.community;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.PlateBean;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.FileUtils;
import com.jkpg.ruchu.utils.ImageTools;
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.center.PersonalInfoActivity;
import com.jkpg.ruchu.view.adapter.PhotoAdapter;
import com.jkpg.ruchu.view.adapter.PlateNameRVAdapter;
import com.jkpg.ruchu.view.adapter.RecyclerItemClickListener;
import com.lzy.okgo.OkGo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

public class SendNoteActivity extends AppCompatActivity {
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
    private String mTitle;
    private PopupWindow mPopupWindow;
    private List<PlateBean> data;

    private LocationManager locationManager;
    private String provider;
    private PersonalInfoActivity.PermissionListener permissionListener;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private String mLocality = "";
    private String mPlateid;
    private String[] mPermissions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_note);
        ButterKnife.bind(this);
        initHeader();
        init();
        initCheckBox();
        initPhotoPicker();

        initShare();
    }

    private void initShare() {
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        String sharedTitle = intent.getStringExtra(Intent.EXTRA_TITLE);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            mSendNoteEtTitle.setText(sharedTitle);
            mSendNoteEtBody.setText(sharedText);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            selectedPhotos.add(imageUri.getPath());
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
            for (int i = 0; i < imageUris.size(); i++) {
                if (i > 8) {
                    return;
                }
                selectedPhotos.add(imageUris.get(i).getPath());
            }
        }
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
                }
            }
        });
    }

    private void initPermission() {
        mPermissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
//        if (Build.VERSION.SDK_INT >= M) {
           /* requestRuntimePermission(permissions, new PersonalInfoActivity.PermissionListener() {
                @Override
                public void onGranted() {
                    initLocation();
                }

                @Override
                public void onDenied(List<String> deniedPermissions) {
                }
            });
            return;*/
            PermissionUtils.requestPermissions(this, 200, mPermissions, new PermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    initLocation();
                }

                @Override
                public void onPermissionDenied(String[] deniedPermissions) {
                    mSendNoteCbPosition.setChecked(false);
                }
            });
//        }
    }

    /**
     * 申请运行时权限
     *//*
    public void requestRuntimePermission(String[] permissions, PersonalInfoActivity.PermissionListener listener) {
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
    }*/
    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(this, "请检查位置权限是否打开",
                    LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            /*String string = "纬度为：" + location.getLatitude() + ",经度为："
                    + location.getLongitude();*/
            Geocoder gc = new Geocoder(this, Locale.getDefault());
            List<Address> locationList = null;
            try {
                locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (locationList == null){
                ToastUtils.showShort(UIUtils.getContext(),"获取地址失败");
                return;
            }
            Address address = locationList.get(0);//得到Address实例

            mLocality = address.getLocality();
            ToastUtils.showShort(UIUtils.getContext(), mLocality);
        }
    }

    private void init() {
        mTitle = getIntent().getStringExtra("title");
        mPlateid = getIntent().getStringExtra("plateid");
        ArrayList<String> plate = getIntent().getStringArrayListExtra("plate");
        mSendNoteTvPlateContent.setText(mTitle);
        data = new ArrayList<>();
        for (String s : plate) {
            data.add(new PlateBean(s));
        }
        View view = View.inflate(UIUtils.getContext(), R.layout.view_plate_notice, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        view.findViewById(R.id.plate_notice_tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
                showPopupWindow();
                //隐藏键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(SendNoteActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.send_note_iv_image:
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setGridColumnCount(4)
                        .start(SendNoteActivity.this);
                break;
        }
    }

    private void send() {
        if (selectedPhotos.size() == 0) {
            OkGo
                    .post(AppUrl.BBS_POST
                            + "?plateid=" + mPlateid
                            + "&title=" + mSendNoteEtTitle.getText().toString()
                            + "&content=" + mSendNoteEtBody.getText().toString()
                            + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                            + "&issite=" + (mSendNoteCbPosition.isChecked() ? 1 : 0)
                            + "&ishidename=" + (mSendNoteCbNoName.isChecked() ? 1 : 0)
                            + "&site=" + mLocality)
                    .execute(new StringDialogCallback(SendNoteActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                            if (successBean.success){
                                finish();
                            }
                        }
                    });
        } else {

            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < selectedPhotos.size(); i++) {
                Uri uri = Uri.fromFile(new File(selectedPhotos.get(i)));
                Bitmap bm = ImageTools.decodeUriAsBitmap(uri);
                String s = FileUtils.saveBitmapByQuality(bm, 10);
                files.add(new File(s));
            }
            OkGo
                    .post(AppUrl.BBS_POST
                            + "?plateid=" + mPlateid
                            + "&title=" + mSendNoteEtTitle.getText().toString()
                            + "&content=" + mSendNoteEtBody.getText().toString()
                            + "&userid=" + SPUtils.getString(UIUtils.getContext(), Constants.USERID, "")
                            + "&issite=" + (mSendNoteCbPosition.isChecked() ? 1 : 0)
                            + "&ishidename=" + (mSendNoteCbNoName.isChecked() ? 1 : 0)
                            + "&site=" + mLocality)
                    .isMultipart(true)
                    .addFileParams("upload", files)
                    .execute(new StringDialogCallback(SendNoteActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                            if (successBean.success){
                                finish();
                            }
                        }
                    });
        }
    }

    private void showPopupWindow() {

        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(UIUtils.getContext(), R.layout.view_recycler_view, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.view_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        PlateNameRVAdapter adapter = new PlateNameRVAdapter(R.layout.item_plate_name, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSendNoteTvPlateContent.setText(data.get(position).title);
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setContentView(view);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAsDropDown(getLayoutInflater().inflate(R.layout.activity_send_note, null), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*switch (requestCode) {
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
        }*/
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
