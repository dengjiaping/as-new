package com.jkpg.ruchu.view.activity.train;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jkpg.ruchu.MyTrainBean;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.view.adapter.MyTrainRVAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/18.
 */

public class MyTrainActivity extends AppCompatActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_train_recycler_view)
    RecyclerView mMyTrainRecyclerView;

    private List<MyTrainBean> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("我的训练");
        initData();
        initRecyclerView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        mDatas.add(new MyTrainBean(R.drawable.grade_1,"我不听","我不听","我不听",true,"便利店可以办会员了？\n" +
                "\n" +
                "意思是买瓶2块钱的饮料还要办个会员卡？\n" +
                "\n" +
                "说好的轻便、快捷、用完即走呢？\n" +
                "\n" +
                "我不听…"));
           mDatas.add(new MyTrainBean(R.drawable.grade_2,"我不听","我不听","我不听",true,"便利店可以办会员了？\n" +
                "\n" +
                "意思是买瓶2块钱的饮料还要办个会员卡？\n" +
                "\n" +
                "说好的轻便、快捷、用完即走呢？\n" +
                "\n" +
                "我不听…"));
           mDatas.add(new MyTrainBean(R.drawable.grade_3,"我不听","我不听","我不听",true,"便利店可以办会员了？\n" +
                "\n" +
                "意思是买瓶2块钱的饮料还要办个会员卡？\n" +
                "\n" +
                "说好的轻便、快捷、用完即走呢？\n" +
                "\n" +
                "我不听…"));
           mDatas.add(new MyTrainBean(R.drawable.grade_4,"我不听","我不听","我不听",true,"便利店可以办会员了？\n" +
                "\n" +
                "意思是买瓶2块钱的饮料还要办个会员卡？\n" +
                "\n" +
                "说好的轻便、快捷、用完即走呢？\n" +
                "\n" +
                "我不听…"));
           mDatas.add(new MyTrainBean(R.drawable.grade_5,"我不听","我不听","我不听",true,"便利店可以办会员了？\n" +
                "\n" +
                "意思是买瓶2块钱的饮料还要办个会员卡？\n" +
                "\n" +
                "说好的轻便、快捷、用完即走呢？\n" +
                "\n" +
                "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "\n" +
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"+
                   "我不听…"));


    }

    private void initRecyclerView() {
        mMyTrainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyTrainRVAdapter myTrainRVAdapter = new MyTrainRVAdapter(R.layout.item_my_train, mDatas);
        mMyTrainRecyclerView.setAdapter(myTrainRVAdapter);
        myTrainRVAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                new AlertDialog.Builder(MyTrainActivity.this)
                        .setMessage(mDatas.get(position).introduce)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("启用", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setTitle("要启用 V"+(position+1)+" 难度训练吗？")
                        .show();
            }
        });
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}
