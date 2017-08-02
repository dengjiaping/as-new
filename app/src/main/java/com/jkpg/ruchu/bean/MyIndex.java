package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/7/19.
 */

public class MyIndex {

    /**
     * mymess : {"amount":null,"fens":"0","ftiecount":"0","uNick":"sdfsdfsd","mygz":"0","experience":null,"uImgurl":"headImg/4567553433.png","isVIP":"0"}
     * state : 200
     * mess : 查询成功
     */

    public MymessBean mymess;
    public int state;
    public String mess;

    public static class MymessBean {
        /**
         * amount : null
         * fens : 0
         * ftiecount : 0
         * uNick : sdfsdfsd
         * mygz : 0
         * experience : null
         * uImgurl : headImg/4567553433.png
         * isVIP : 0
         */

        public String uImgurl;
        public String uNick;
        public String isVIP;
        public String experience;
        public String levelname;
        public String amount;
        public String ftiecount;
        public String mygz;
        public String fens;
    }
}
