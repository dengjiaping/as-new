package com.jkpg.ruchu.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.jkpg.ruchu.R;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;

import static com.jkpg.ruchu.utils.UIUtils.getResources;

/**
 * 获取地址数据并显示地址选择器
 *
 */
public class AddressPickTask extends AsyncTask<String, Void, ArrayList<Province>> {
    private Activity activity;
    private ProgressDialog dialog;
    private Callback callback;
    private String selectedProvince = "", selectedCity = "", selectedCounty = "";
    private boolean hideProvince = false;
    private boolean hideCounty = false;

    public AddressPickTask(Activity activity) {
        this.activity = activity;
    }

    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
       // dialog = ProgressDialog.show(activity, null, "正在初始化数据...", true, true);
    }

    @Override
    protected ArrayList<Province> doInBackground(String... params) {
        if (params != null) {
            switch (params.length) {
                case 1:
                    selectedProvince = params[0];
                    break;
                case 2:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    break;
                case 3:
                    selectedProvince = params[0];
                    selectedCity = params[1];
                    selectedCounty = params[2];
                    break;
                default:
                    break;
            }
        }
        ArrayList<Province> data = new ArrayList<>();
        try {
            String json = ConvertUtils.toString(activity.getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json, Province.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<Province> result) {
       // dialog.dismiss();
        if (result.size() > 0) {
            AddressPicker picker = new AddressPicker(activity, result);
            picker.setHideProvince(hideProvince);
            picker.setHideCounty(hideCounty);
            picker.setTextColor(getResources().getColor(R.color.colorPink));
            picker.setDividerColor(Color.parseColor("#ffffff"));
            picker.setSubmitTextColor(Color.parseColor("#000000"));
            picker.setCancelTextColor(Color.parseColor("#000000"));
            picker.setTopLineColor(Color.parseColor("#ffffff"));
            picker.setPressedTextColor(getResources().getColor(R.color.colorPink));
            //picker.setCycleDisable(false);//不禁用循环

            if (hideCounty) {
                picker.setColumnWeight(0.8f, 1.0f);
            } else if (hideProvince) {
                picker.setColumnWeight(1.0f, 0.8f);
            } else {
                picker.setColumnWeight(0.8f, 1.0f, 1.0f);
            }
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(callback);
            picker.show();
        } else {
            callback.onAddressInitFailed();
        }
    }

    public interface Callback extends AddressPicker.OnAddressPickListener {

        void onAddressInitFailed();

    }

}
