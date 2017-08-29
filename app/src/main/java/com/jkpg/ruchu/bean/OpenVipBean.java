package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/21.
 */

public class OpenVipBean {


    /**
     * headImg : headImg/100343110.png
     * nick : A星越
     * VIPTime : 2017-08-19
     * WXStatus : true
     * list : [{"cardprice":"15.00","cardname":"月卡(30天)","cardtime":"30"},{"cardprice":"45.00","cardname":"季卡(90天)","cardtime":"90"},{"cardprice":"75.00","cardname":"半年卡(180天)","cardtime":"180"},{"cardprice":"135.00","cardname":"年卡(365天)","cardtime":"365"}]
     * isVIP : 1
     * msg : 绑定微信,即送15天会员叠加包!
     */

    public String headImg;
    public String nick;
    public String VIPTime;
    public boolean WXStatus;
    public String isVIP;
    public String msg;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * cardprice : 15.00
         * cardname : 月卡(30天)
         * cardtime : 30
         */

        public String cardprice;
        public String cardname;
        public String cardtime;
        public String cardid;
    }
}
