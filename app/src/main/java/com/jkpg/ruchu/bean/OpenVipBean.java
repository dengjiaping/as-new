package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/21.
 */

public class OpenVipBean {


    /**
     * headImg : headImg/244355AKY7CJ.jpg
     * nick : 哈哈
     * VIPTime : 2017-09-09
     * WXStatus : true
     * list : [{"cardprice":"29.00","cardname":"月卡（30天）","cardtime":"30","cardid":"1","oldprice":""},{"cardprice":"49.00","cardname":"季卡（90天）","cardtime":"90","cardid":"2","oldprice":"87.00"},{"cardprice":"199.00","cardname":"年卡（365天）","cardtime":"365","cardid":"3","oldprice":"348.00"}]
     * isVIP : 1
     * msg : 绑定微信,即送15天会员叠加包!
     * isgivenVIP : 1
     */

    public String headImg;
    public String nick;
    public String VIPTime;
    public boolean WXStatus;
    public String isVIP;
    public String information;
    public String msg;
    public String isgivenVIP;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * cardprice : 29.00
         * cardname : 月卡（30天）
         * cardtime : 30
         * cardid : 1
         * oldprice :
         */

        public String cardprice;
        public String cardname;
        public String cardtime;
        public String cardid;
        public String oldprice;
    }
}
