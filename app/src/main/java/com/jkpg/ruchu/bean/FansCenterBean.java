package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/4.
 */

public class FansCenterBean {

    /**
     * chanhoutime : 3个月
     * headImg : headImg/243QVGHZB7M8.jpg
     * nick : 嘟噜噜啦啦啦
     * address : 山东省  济南市
     * bbslist : [{"content":"鼎湖","createtime":"2017-08-03 18:36:05","title":"李婷婷","headImg":"headImg/243QVGHZB7M8.jpg","nick":"A星越","zan":"0","userId":"d523f793-3ee1-11e7-aebf-fa163e534242","reply":"0","isGood":"0","tid":"82"}]
     * fansNum : 1
     * attNum : 1
     * isAtt : 1
     * isVIP : 1
     * levelname : 新手宝妈
     */

    public String chanhoutime;
    public String headImg;
    public String nick;
    public String address;
    public String fansNum;
    public String attNum;
    public String isAtt;
    public String isVIP;
    public String levelname;
    public String nameid;
    public String bbsnum;
    public String userid;
    public List<BbslistBean> bbslist;

    public static class BbslistBean {
        /**
         * content : 鼎湖
         * createtime : 2017-08-03 18:36:05
         * title : 李婷婷
         * headImg : headImg/243QVGHZB7M8.jpg
         * nick : A星越
         * zan : 0
         * userId : d523f793-3ee1-11e7-aebf-fa163e534242
         * reply : 0
         * isGood : 0
         * tid : 82
         */

        public String content;
        public String createtime;
        public String title;
        public String headImg;
        public String nick;
        public String zan;
        public String userId;
        public String reply;
        public String isGood;
        public String tid;
    }
}
