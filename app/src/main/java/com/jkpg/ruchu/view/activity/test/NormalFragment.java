package com.jkpg.ruchu.view.activity.test;

import android.support.v4.app.Fragment;

import com.jkpg.ruchu.bean.TrainQuestionNextBean;

/**
 * Created by qindi on 2017/7/26.
 */

public abstract class NormalFragment extends Fragment {
    public abstract String getFlag();
    public abstract String getTid();
    public abstract TrainQuestionNextBean.ListBean getListBean();
}
