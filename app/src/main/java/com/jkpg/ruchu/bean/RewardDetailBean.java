package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/12/3.
 */

public class RewardDetailBean {
    public List<ListBean> list;

    public static class ListBean {
        public String time;
        public String msg;
        public String useflag;
        public String msg2;
        public String gifturl;
        public String userid;

        public ListBean(String time, String msg, String useflag, String msg2, String gifturl) {
            this.time = time;
            this.msg = msg;
            this.useflag = useflag;
            this.msg2 = msg2;
            this.gifturl = gifturl;
        }
    }
}
