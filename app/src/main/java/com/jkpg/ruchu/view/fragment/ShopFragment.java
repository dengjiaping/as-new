package com.jkpg.ruchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkpg.ruchu.utils.UIUtils;
import com.youzan.sdk.web.plugin.YouzanBrowser;

/**
 * Created by qindi on 2017/5/16.
 */

public class ShopFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        YouzanBrowser view = new YouzanBrowser(UIUtils.getContext());
//        YouzanHybrid view = new YouzanHybrid(getActivity());
        view.loadUrl("https://h5.youzan.com/v2/showcase/homepage?alias=juhos0");
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
