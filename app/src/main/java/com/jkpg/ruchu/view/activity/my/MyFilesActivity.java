package com.jkpg.ruchu.view.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.VipManageVPAdapter;
import com.jkpg.ruchu.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DoublePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by qindi on 2017/5/25.
 */

public class MyFilesActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_files_ctv_photo)
    CircleImageView mMyFilesCtvPhoto;
    @BindView(R.id.my_files_tv_name)
    TextView mMyFilesTvName;
    @BindView(R.id.my_files_iv_vip)
    ImageView mMyFilesIvVip;
    @BindView(R.id.my_files_tv_uid)
    TextView mMyFilesTvUid;
    @BindView(R.id.my_files_cb)
    CheckBox mMyFilesCb;
    @BindView(R.id.my_files_tab_layout)
    TabLayout mMyFilesTabLayout;
    @BindView(R.id.my_files_view_pager)
    ViewPager mMyFilesViewPager;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;

    private List<View> viewList;//数据源
    private List<String> viewTitle;
    private View mBearing;
    private View mBearingBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);
        ButterKnife.bind(this);
        initData();
        initHeader();
        initTabLayout();
        initViewPager();

        initBearing();
        initBearingBack();
    }


    private void initData() {
        viewList = new ArrayList<>();
        mBearing = View.inflate(UIUtils.getContext(), R.layout.view_bearing, null);
        viewList.add(mBearing);
        mBearingBack = View.inflate(UIUtils.getContext(), R.layout.view_bearing_back, null);
        viewList.add(mBearingBack);
        viewTitle = new ArrayList<>();
        viewTitle.add("生产情况");
        viewTitle.add("产后情况");
    }

    private void initTabLayout() {
        mMyFilesTabLayout.setupWithViewPager(mMyFilesViewPager);
    }

    private void initViewPager() {
        mMyFilesViewPager.setAdapter(new VipManageVPAdapter(viewList, viewTitle));
    }


    private void initHeader() {
        mHeaderTvTitle.setText("我的档案");
        mHeaderTvRight.setText("保存");
    }

    @OnClick({R.id.header_iv_left, R.id.header_tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.header_tv_right:
                break;
        }
    }

    private RelativeLayout mView_bearing_chsj;
    private TextView mView_bearing_tv_chsj;
    private RelativeLayout mView_bearing_mqtz;
    private TextView mView_bearing_tv_mqtz;
    private RelativeLayout mView_bearing_sg;
    private TextView mView_bearing_tv_sg;
    private RelativeLayout mView_bearing_42jc;
    private TextView mView_bearing_tv_42jc;
    private RelativeLayout mView_bearing_pdjl;
    private TextView mView_bearing_tv_pdjl;
    private RelativeLayout mView_bearing_gnza;
    private TextView mView_bearing_tv_gnza;

    private void initBearingBack() {
        mView_bearing_chsj = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_chsj);
        mView_bearing_tv_chsj = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_chsj);
        mView_bearing_chsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> firstData = new ArrayList<>();
                for (int i = 0; i < 37; i++) {
                    firstData.add(i + "月");
                }
                final ArrayList<String> secondData = new ArrayList<>();
                for (int i = 0; i < 31; i++) {
                    secondData.add(i + "天");
                }
                final DoublePicker picker = new DoublePicker(MyFilesActivity.this, firstData, secondData);
                picker.setDividerVisible(false);
                picker.setShadowColor(Color.WHITE, 80);
                picker.setSelectedIndex(2, 1);
                picker.setTextColor(getResources().getColor(R.color.colorPink));
                picker.setDividerColor(Color.parseColor("#ffffff"));
                picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));

                picker.setSubmitTextColor(Color.parseColor("#000000"));
                picker.setCancelTextColor(Color.parseColor("#000000"));
                picker.setTopLineColor(Color.parseColor("#ffffff"));
                picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
                picker.setOnPickListener(new DoublePicker.OnPickListener() {
                    @Override
                    public void onPicked(int selectedFirstIndex, int selectedSecondIndex) {
                        mView_bearing_tv_chsj.setText(firstData.get(selectedFirstIndex) + "零" + secondData.get(selectedSecondIndex));
                    }
                });
                picker.show();
            }
        });

        mView_bearing_mqtz = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_mqtz);
        mView_bearing_tv_mqtz = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_mqtz);
        mView_bearing_mqtz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker(40, 120, 50, "kg", mView_bearing_tv_mqtz);

            }
        });

        mView_bearing_sg = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_sg);
        mView_bearing_tv_sg = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_sg);
        mView_bearing_sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker(120, 220, 168, "cm", mView_bearing_tv_sg);
            }
        });

        mView_bearing_42jc = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_42jc);
        mView_bearing_tv_42jc = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_42jc);
        mView_bearing_42jc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionPicker picker = new OptionPicker(MyFilesActivity.this, new String[]{"已做", "未做"});
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
                        mView_bearing_tv_42jc.setText(item);
                        if (item.equals("未做")) {
                            mView_bearing_pdjl.setVisibility(View.GONE);
                            mView_bearing_gnza.setVisibility(View.GONE);
                        } else {
                            mView_bearing_pdjl.setVisibility(View.VISIBLE);
                            mView_bearing_gnza.setVisibility(View.VISIBLE);
                        }
                    }
                });
                picker.show();
            }
        });

        mView_bearing_pdjl = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_pdjl);
        mView_bearing_tv_pdjl = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_pdjl);
        mView_bearing_pdjl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinglePicker(new String[]{"0级", "1级", "2级", "3级", "4级", "5级"}, mView_bearing_tv_pdjl);

            }
        });

        mView_bearing_gnza = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_gnza);
        mView_bearing_tv_gnza = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_gnza);
        mView_bearing_gnza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinglePicker(new String[]{"器官膨出", "器官拖垂", "漏尿"}, mView_bearing_tv_gnza);


            }
        });

    }

    private RelativeLayout mView_bearing_tc;
    private TextView mView_bearing_tv_tc;
    private RelativeLayout mView_bearing_fmfs;
    private TextView mView_bearing_tv_fmfs;
    private RelativeLayout mView_bearing_fmtz;
    private TextView mView_bearing_tv_fmtz;
    private RelativeLayout mView_bearing_scsc;
    private TextView mView_bearing_tv_scsc;
    private RelativeLayout mView_bearing_fmss;
    private TextView mView_bearing_tv_fmss;
    private RelativeLayout mView_bearing_xstz;
    private TextView mView_bearing_tv_xstz;

    private void initBearing() {
        mView_bearing_tc = (RelativeLayout) mBearing.findViewById(R.id.view_bearing_tc);
        mView_bearing_tv_tc = (TextView) mBearing.findViewById(R.id.view_bearing_tv_tc);
        mView_bearing_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinglePicker(new String[]{"无", "头胎", "二胎", "三胎及以上"}, mView_bearing_tv_tc);
            }
        });

        mView_bearing_fmfs = (RelativeLayout) mBearing.findViewById(R.id.view_bearing_fmfs);
        mView_bearing_tv_fmfs = (TextView) mBearing.findViewById(R.id.view_bearing_tv_fmfs);
        mView_bearing_fmfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinglePicker(new String[]{"顺产", "刨宫产"}, mView_bearing_tv_fmfs);

            }
        });

        mView_bearing_fmtz = (RelativeLayout) mBearing.findViewById(R.id.view_bearing_fmtz);
        mView_bearing_tv_fmtz = (TextView) mBearing.findViewById(R.id.view_bearing_tv_fmtz);
        mView_bearing_fmtz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker(40, 120, 50, "kg", mView_bearing_tv_fmtz);
            }
        });

        mView_bearing_scsc = (RelativeLayout) mBearing.findViewById(R.id.view_bearing_scsc);
        mView_bearing_tv_scsc = (TextView) mBearing.findViewById(R.id.view_bearing_tv_scsc);
        mView_bearing_scsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker(1, 48, 2, "h", mView_bearing_tv_scsc);

            }
        });
        mView_bearing_fmss = (RelativeLayout) mBearing.findViewById(R.id.view_bearing_fmss);
        mView_bearing_tv_fmss = (TextView) mBearing.findViewById(R.id.view_bearing_tv_fmss);

        mView_bearing_fmss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinglePicker(new String[]{"无损伤", "有侧切", "有撕裂", "侧切并撕裂"}, mView_bearing_tv_fmss);
            }
        });
        mView_bearing_xstz = (RelativeLayout) mBearing.findViewById(R.id.view_bearing_xstz);
        mView_bearing_tv_xstz = (TextView) mBearing.findViewById(R.id.view_bearing_tv_xstz);
        mView_bearing_xstz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker(1, 10, 4, "kg", mView_bearing_tv_xstz);
            }
        });
    }

    private void showSinglePicker(String[] strings, final TextView text) {
        OptionPicker picker = new OptionPicker(MyFilesActivity.this, strings);
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.WRAP);
        picker.setShadowColor(Color.WHITE, 40);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setTextSize(14);
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));

        picker.setTextColor(getResources().getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                text.setText(item);
            }
        });
        picker.show();
    }

    private void showNumberPicker(int x, int y, int z, final String label, final TextView textView) {
        NumberPicker picker = new NumberPicker(this);
//        picker.setWidth(picker.getScreenWidthPixels());
        picker.setCanceledOnTouchOutside(true);
        picker.setDividerVisible(false);
        picker.setCycleDisable(true);//不禁用循环
        //picker.setOffset(2);//偏移量
        picker.setRange(x, y, 1);//数字范围
        picker.setSelectedItem(z);
        picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));

        picker.setLabel(label);
        picker.setTextColor(getResources().getColor(R.color.colorPink));
        picker.setDividerColor(Color.parseColor("#ffffff"));
        picker.setSubmitTextColor(Color.parseColor("#000000"));
        picker.setCancelTextColor(Color.parseColor("#000000"));
        picker.setTopLineColor(Color.parseColor("#ffffff"));
        picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                textView.setText(item.intValue() + " " + label);
            }
        });
        picker.show();
    }
}
