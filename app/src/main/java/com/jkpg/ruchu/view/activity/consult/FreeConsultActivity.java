package com.jkpg.ruchu.view.activity.consult;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.PhotoAdapter;
import com.jkpg.ruchu.view.adapter.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by qindi on 2017/6/13.
 */

public class FreeConsultActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.free_consult_et)
    EditText mFreeConsultEt;
    @BindView(R.id.free_consult_rv)
    RecyclerView mFreeConsultRv;
    @BindView(R.id.free_consult_cb)
    CheckBox mFreeConsultCb;
    @BindView(R.id.free_consult_btn)
    Button mFreeConsultBtn;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_consult);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        photoAdapter = new PhotoAdapter(this, selectedPhotos);

        mFreeConsultRv.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        mFreeConsultRv.setAdapter(photoAdapter);
        mFreeConsultRv.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == 1) {
                            PhotoPicker.builder()
                                    .setPhotoCount(3)
                                    .setGridColumnCount(4)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(FreeConsultActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(FreeConsultActivity.this);
                        }
                    }
                }));
    }

    @OnClick({R.id.header_iv_left, R.id.free_consult_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.free_consult_btn:
                showWaiting();
                break;
        }
    }

    private void showWaiting() {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_show_waiting, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        view.findViewById(R.id.view_waiting_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.view_waiting_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreeConsultActivity.this, SelectDoctorActivity.class));
                finish();
            }
        });
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
