package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/8/16.
 */

public class WxPayBean {

    /**
     * info : {"sign":"568BD061C91D181F116C41195CEA5F5A","timestamp":"1503104790","noncestr":"ryb9pnrTOLA5SVnJ","partnerid":"20170819090630","prepayid":"wx201708190906291c30a8e33d0938922293","package":"Sign=WXPay","appid":"wx1db22a34a1a63fa9"}
     */

    public InfoBean info;

    public static class InfoBean {
        /**
         * sign : 568BD061C91D181F116C41195CEA5F5A
         * timestamp : 1503104790
         * noncestr : ryb9pnrTOLA5SVnJ
         * partnerid : 20170819090630
         * prepayid : wx201708190906291c30a8e33d0938922293
         * package : Sign=WXPay
         * appid : wx1db22a34a1a63fa9
         */

        public String sign;
        public String timestamp;
        public String noncestr;
        public String partnerid;
        public String prepayid;
        public String appid;
    }
}
