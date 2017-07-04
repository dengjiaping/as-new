package com.jkpg.ruchu.view.activity.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.FileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jkpg.ruchu.utils.ScreenUtils.getBitmapByView;

/**
 * Created by qindi on 2017/5/20.
 */

public class TestReportActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.report_tv_grade)
    TextView mReportTvGrade;
    @BindView(R.id.report_tv_plan)
    TextView mReportTvPlan;
    @BindView(R.id.report_tv_name)
    TextView mReportTvName;
    @BindView(R.id.report_tv_height)
    TextView mReportTvHeight;
    @BindView(R.id.report_tv_age)
    TextView mReportTvAge;
    @BindView(R.id.report_tv_weight)
    TextView mReportTvWeight;
    @BindView(R.id.report_tv_body)
    TextView mReportTvBody;
    @BindView(R.id.report_tv_time)
    TextView mReportTvTime;
    @BindView(R.id.register_scroll_view)
    ScrollView mRegisterScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        initHeader();

    }

    private void initHeader() {
        mHeaderTvTitle.setText("完整报告");
        mHeaderIvRight.setImageResource(R.drawable.icon_share);
    }


    @OnClick({R.id.header_iv_left, R.id.header_iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_iv_right:
                Bitmap bitmap = getBitmapByView(mRegisterScrollView);
                String pic = FileUtils.saveBitmap(bitmap);
                Intent imageIntent = new Intent(Intent.ACTION_SEND);
                imageIntent.setType("image/jpeg");
                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pic));
                startActivity(Intent.createChooser(imageIntent, "分享我的报告"));

                break;
        }
    }
}
