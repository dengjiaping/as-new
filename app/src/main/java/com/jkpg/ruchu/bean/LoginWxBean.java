package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/7/17.
 */

public class LoginWxBean {

    /**
     * state : 200
     * mess : 查询成功
     * backMess : {"flagStatus":"1","unionid":"oqoufvzHv0pSoMk99VKwpFz2JIbk","userId":"770ce08b-6a95-11e7-b296-883fd3cfd36b","uNick":"Heigoo啦","loginCount":"6","uImgurl":"https://wx.qlogo.cn/mmhead/PiajxSqBRaELRaXKHibTgHicOico4t5FRjHBXRbQHZSibbfNmHfG4MqtqhQ/0","uId":"49"}
     */

    public int state;
    public String mess;
    public BackMessBean backMess;
    public boolean giveVIP;

    public static class BackMessBean {
        /**
         * flagStatus : 1
         * unionid : oqoufvzHv0pSoMk99VKwpFz2JIbk
         * userId : 770ce08b-6a95-11e7-b296-883fd3cfd36b
         * uNick : Heigoo啦
         * loginCount : 6
         * uImgurl : https://wx.qlogo.cn/mmhead/PiajxSqBRaELRaXKHibTgHicOico4t5FRjHBXRbQHZSibbfNmHfG4MqtqhQ/0
         * uId : 49
         */

        public String flagStatus;
        public String unionid;
        public String userId;
        public String uNick;
        public String loginCount;
        public String uImgurl;
        public String uId;
    }
}
