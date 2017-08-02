package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/21.
 */

public class OpenVipBean {

    /**
     * headImg : headImg/243R7K8LQW7M.jpg
     * nick : successds
     * VIPTime : 2017-07-17
     * WXStatus : false
     * list : [{"cardprice":"15.00","cardname":"月卡(30天)","cardtime":"30"},{"cardprice":"45.00","cardname":"季卡(90天)","cardtime":"90"},{"cardprice":"75.00","cardname":"半年卡(180天)","cardtime":"180"},{"cardprice":"135.00","cardname":"年卡(365天)","cardtime":"365"}]
     * isVIP : 0
     */

    public boolean WXStatus;
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
    }
}
