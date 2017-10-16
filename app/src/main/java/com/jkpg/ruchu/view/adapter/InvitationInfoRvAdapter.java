package com.jkpg.ruchu.view.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.InvitationBean;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/10/12.
 */

public class InvitationInfoRvAdapter extends BaseQuickAdapter<InvitationBean.ArrayBean,BaseViewHolder> {
    public InvitationInfoRvAdapter(@LayoutRes int layoutResId, @Nullable List<InvitationBean.ArrayBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, InvitationBean.ArrayBean item) {
        helper.setText(R.id.item_invitation_name,item.nick);
        helper.setText(R.id.item_invitation_time,item.createtime);
        if (item.flag.equals("0")){
            helper.setVisible(R.id.item_invitation_ok,true);
            helper.setVisible(R.id.item_invitation_no,false);
        } else {
            helper.setVisible(R.id.item_invitation_ok,false);
            helper.setVisible(R.id.item_invitation_no,true);
        }
        Glide
                .with(UIUtils.getContext())
                .load(item.imageurl)
                .centerCrop()
                .crossFade()
                .error(R.drawable.icon_default)
                .into((ImageView) helper.getView(R.id.item_invitation_image));
    }
}
