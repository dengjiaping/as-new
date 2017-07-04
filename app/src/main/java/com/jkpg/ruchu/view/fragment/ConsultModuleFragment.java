package com.jkpg.ruchu.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.RecommendDoctorBean;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.consult.ConsultHistoryActivity;
import com.jkpg.ruchu.view.activity.consult.DoctorCollectActivity;
import com.jkpg.ruchu.view.activity.consult.DoctorMainActivity;
import com.jkpg.ruchu.view.activity.consult.FreeConsultActivity;
import com.jkpg.ruchu.view.activity.consult.HotQuestionActivity;
import com.jkpg.ruchu.view.activity.consult.SelectDoctorActivity;
import com.jkpg.ruchu.view.activity.my.MyFilesActivity;
import com.jkpg.ruchu.view.adapter.RecommendDoctorAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qindi on 2017/6/5.
 */

public class ConsultModuleFragment extends Fragment {
    @BindView(R.id.consult_ll_online)
    LinearLayout mConsultLlOnline;
    @BindView(R.id.consult_ll_history)
    LinearLayout mConsultLlHistory;
    @BindView(R.id.consult_tv_free)
    TextView mConsultTvFree;
    @BindView(R.id.consult_tv_hot)
    TextView mConsultTvHot;
    @BindView(R.id.consult_ll_files)
    LinearLayout mConsultLlFiles;
    @BindView(R.id.consult_ll_collect)
    LinearLayout mConsultLlCollect;
    @BindView(R.id.consult_rl_doctor)
    RecyclerView mConsultRlDoctor;
    Unbinder unbinder;

    private List<RecommendDoctorBean> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consult, null, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initRecyclerView();
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(new RecommendDoctorBean());
        }
    }

    private void initRecyclerView() {
        mConsultRlDoctor.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
        RecommendDoctorAdapter recommendDoctorAdapter = new RecommendDoctorAdapter(R.layout.item_recommend_doctor, data);
        mConsultRlDoctor.setAdapter(recommendDoctorAdapter);
        recommendDoctorAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), DoctorMainActivity.class));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.consult_ll_online, R.id.consult_ll_history, R.id.consult_tv_free, R.id.consult_tv_hot, R.id.consult_ll_files, R.id.consult_ll_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.consult_ll_online:
                startActivity(new Intent(getActivity(), SelectDoctorActivity.class));
                break;
            case R.id.consult_ll_history:
                startActivity(new Intent(getActivity(), ConsultHistoryActivity.class));
                break;
            case R.id.consult_tv_free:
                startActivity(new Intent(getActivity(), FreeConsultActivity.class));

                break;
            case R.id.consult_tv_hot:
                startActivity(new Intent(getActivity(), HotQuestionActivity.class));

                break;
            case R.id.consult_ll_files:
                startActivity(new Intent(getActivity(), MyFilesActivity.class));
                break;
            case R.id.consult_ll_collect:
                startActivity(new Intent(getActivity(), DoctorCollectActivity.class));

                break;
        }
    }
}
