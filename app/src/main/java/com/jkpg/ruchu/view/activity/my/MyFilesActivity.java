package com.jkpg.ruchu.view.activity.my;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.MyFilesBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.ViewPagerAdapter;
import com.jkpg.ruchu.widget.CircleImageView;
import com.jkpg.ruchu.widget.MultiSelectPicker;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.LinkagePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.DateUtils;
import cn.qqtheme.framework.widget.WheelView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/25.
 */

public class MyFilesActivity extends BaseActivity {
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

        OkGo
                .post(AppUrl.UPDATE_MYARCHIVE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("ispublic", "")
                .params("taici", "")
                .params("mode", "")
                .params("oldweight", "")
                .params("chantime", "")
                .params("hurt", "")
                .params("kidweight", "")
                .params("chanhoutime", "")
                .params("weight", "")
                .params("height", "")
                .params("test42", "")
                .params("power", "")
                .params("obstacle", "")
                .cacheKey("UPDATE_MYARCHIVE")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new StringDialogCallback(MyFilesActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        // LogUtils.i(s);
                        MyFilesBean myFilesBean = new Gson().fromJson(s, MyFilesBean.class);
                        init(myFilesBean);
                    }

                    @Override
                    public void onCacheSuccess(String s, Call call) {
                        super.onCacheSuccess(s, call);
                        MyFilesBean myFilesBean = new Gson().fromJson(s, MyFilesBean.class);
                        init(myFilesBean);
                    }
                });


        viewList = new ArrayList<>();
        mBearing = View.inflate(UIUtils.getContext(), R.layout.view_bearing, null);
        viewList.add(mBearing);
        mBearingBack = View.inflate(UIUtils.getContext(), R.layout.view_bearing_back, null);
        viewList.add(mBearingBack);
        viewTitle = new ArrayList<>();
        viewTitle.add("生产情况");
        viewTitle.add("产后情况");
    }

    private void init(MyFilesBean myFilesBean) {
        Glide
                .with(UIUtils.getContext())

                .load(AppUrl.BASEURL + myFilesBean.headImg)
                .crossFade()
                .centerCrop()
                .error(R.drawable.icon_default)
                .into(mMyFilesCtvPhoto);
        mMyFilesTvName.setText(myFilesBean.nick);
        if (myFilesBean.isVIP.equals("1"))
            mMyFilesIvVip.setImageResource(R.drawable.icon_vip1);
        else
            mMyFilesIvVip.setImageResource(R.drawable.icon_vip2);

        if (myFilesBean.ispublic.equals("0"))
            mMyFilesCb.setChecked(false);
        else
            mMyFilesCb.setChecked(true);

        mMyFilesTvUid.setText("UID:" + myFilesBean.uid);

        mView_bearing_tv_tc.setText(myFilesBean.taici);
        mView_bearing_tv_fmfs.setText(myFilesBean.mode);
        mView_bearing_tv_fmtz.setText(myFilesBean.oldweight);
        mView_bearing_tv_scsc.setText(myFilesBean.chantime);
        mView_bearing_tv_fmss.setText(myFilesBean.hurt);
        mView_bearing_tv_xstz.setText(myFilesBean.kidweight);

        mView_bearing_tv_chsj.setText(myFilesBean.chanhoutime);
        mView_bearing_tv_mqtz.setText(myFilesBean.weight);
        mView_bearing_tv_sg.setText(myFilesBean.height);
        mView_bearing_tv_42jc.setText(myFilesBean.test42);
        mView_bearing_tv_pdjl.setText(myFilesBean.power);
        mView_bearing_tv_gnza.setText(myFilesBean.obstacle);
        if (myFilesBean.test42.equals("未做") || myFilesBean.test42.equals("")) {
            mView_bearing_pdjl.setVisibility(View.GONE);
            mView_bearing_gnza.setVisibility(View.GONE);
        } else {
            mView_bearing_pdjl.setVisibility(View.VISIBLE);
            mView_bearing_gnza.setVisibility(View.VISIBLE);
        }


    }

    private void initTabLayout() {
        mMyFilesTabLayout.setupWithViewPager(mMyFilesViewPager);
    }

    private void initViewPager() {
        mMyFilesViewPager.setAdapter(new ViewPagerAdapter(viewList, viewTitle));
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
                if (
                        mView_bearing_tv_tc.getText().equals("") ||
                                mView_bearing_tv_fmfs.getText().equals("") ||
                                mView_bearing_tv_fmtz.getText().equals("") ||
                                mView_bearing_tv_scsc.getText().equals("") ||
                                mView_bearing_tv_fmss.getText().equals("") ||
                                mView_bearing_tv_xstz.getText().equals("") ||

                                mView_bearing_tv_chsj.getText().equals("") ||
                                mView_bearing_tv_mqtz.getText().equals("") ||
                                mView_bearing_tv_sg.getText().equals("") ||
                                mView_bearing_tv_42jc.getText().equals("")
                        ) {

                    if (!mView_bearing_tv_42jc.getText().equals("未做")) {
                        mView_bearing_tv_pdjl.getText().equals("");
                        mView_bearing_tv_gnza.getText().equals("");
                    }
                    ToastUtils.showShort(UIUtils.getContext(), "请完善信息");
                } else {
                    if (!NetworkUtils.isConnected()) {
                        ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
                        return;
                    }
                    OkGo
                            .post(AppUrl.UPDATE_MYARCHIVE)
                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                            .params("type", "1")
                            .params("ispublic", mMyFilesCb.isChecked() ? 1 : 0)
                            .params("taici", mView_bearing_tv_tc.getText().toString())
                            .params("mode", mView_bearing_tv_fmfs.getText().toString())
                            .params("oldweight", mView_bearing_tv_fmtz.getText().toString())
                            .params("chantime", mView_bearing_tv_scsc.getText().toString())
                            .params("hurt", mView_bearing_tv_fmss.getText().toString())
                            .params("kidweight", mView_bearing_tv_xstz.getText().toString())
                            .params("chanhoutime", mView_bearing_tv_chsj.getText().toString())
                            .params("weight", mView_bearing_tv_mqtz.getText().toString())
                            .params("height", mView_bearing_tv_sg.getText().toString())
                            .params("test42", mView_bearing_tv_42jc.getText().toString())
                            .params("power", mView_bearing_tv_pdjl.getText().toString())
                            .params("obstacle", mView_bearing_tv_gnza.getText().toString())
                            .execute(new StringDialogCallback(MyFilesActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    // LogUtils.i(s);
                                    final AlertDialog dialog = new AlertDialog.Builder(MyFilesActivity.this)
                                            .setView(View.inflate(MyFilesActivity.this, R.layout.view_save_success, null))
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
//                                            dialog.dismiss();
                                            finish();
                                        }
                                    }, 3000);
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
                                }
                            });
                }
                break;
        }
    }


    private void initBearingBack() {
        mView_bearing_chsj = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_chsj);
        mView_bearing_tv_chsj = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_chsj);
        mView_bearing_chsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                            secondList.add("");
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
                LinkagePicker picker = new LinkagePicker(MyFilesActivity.this, provider);


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
                        if (first.equals("0个月") && second.equals("")) {
                            mView_bearing_tv_chsj.setText("无");

                        } else {
                            mView_bearing_tv_chsj.setText(first + " " + second);
                        }

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
                showNumberPicker(30, 120, 60, "kg", mView_bearing_tv_mqtz);

            }
        });

        mView_bearing_sg = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_sg);
        mView_bearing_tv_sg = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_sg);
        mView_bearing_sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPicker(140, 200, 160, "cm", mView_bearing_tv_sg);
            }
        });

        mView_bearing_42jc = (RelativeLayout) mBearingBack.findViewById(R.id.view_bearing_42jc);
        mView_bearing_tv_42jc = (TextView) mBearingBack.findViewById(R.id.view_bearing_tv_42jc);
        mView_bearing_42jc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionPicker picker = new OptionPicker(MyFilesActivity.this, new String[]{"已做", "未做"});
                picker.setCanceledOnTouchOutside(true);
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
//                showSinglePicker(new String[]{"器官膨出", "器官脱垂", "漏尿"}, mView_bearing_tv_gnza);
                final List<String> f = new ArrayList<>();
                f.add("");
                f.add("器官膨出");
                final List<String> s = new ArrayList<>();
                s.add("");
                s.add("器官脱垂");
                final List<String> t = new ArrayList<>();
                t.add("");
                t.add("漏尿");
                final MultiSelectPicker picker = new MultiSelectPicker(MyFilesActivity.this, f, s, t);
                picker.setCanceledOnTouchOutside(true);
                picker.setShadowColor(Color.WHITE, 40);
                picker.setTextSize(14);
                picker.setTopPadding(ConvertUtils.toPx(UIUtils.getContext(), 20));
                picker.setTextColor(getResources().getColor(R.color.colorPink));
                picker.setDividerColor(Color.parseColor("#ffffff"));
                picker.setSubmitTextColor(Color.parseColor("#000000"));
                picker.setCancelTextColor(Color.parseColor("#000000"));
                picker.setTopLineColor(Color.parseColor("#ffffff"));
                picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
                picker.setOnPickListener(new MultiSelectPicker.OnPickListener() {
                    @Override
                    public void onPicked(int selectedFirstIndex, int selectedSecondIndex, int selectedThirdIndex) {
                        mView_bearing_tv_gnza.setText(f.get(selectedFirstIndex) + " " + s.get(selectedSecondIndex) + " " + t.get(selectedThirdIndex));
                    }
                });
                picker.show();
            }
        });

    }


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
                showNumberPicker(30, 120, 60, "kg", mView_bearing_tv_fmtz);
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
        picker.setCanceledOnTouchOutside(true);
        picker.setDividerRatio(WheelView.DividerConfig.WRAP);
        picker.setShadowColor(Color.WHITE, 40);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setTextSize(14);
        picker.setCanceledOnTouchOutside(true);
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
