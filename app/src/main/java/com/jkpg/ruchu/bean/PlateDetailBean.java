package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/6/5.
 */

public class PlateDetailBean implements Serializable {
    /**
     * list : [{"content":"我的帖子的内容，我是内容展示出来的","createtime":"2017-05-08 09:54:06.0","title":"我是帖子，我先发出来的","headImg":"headImg/243QVGHZB7M8.jpg","nick":"昵称","zan":"6","reply":"24","isGood":"1","tid":1},{"content":"产后合理的运动，快速恢复了！","createtime":"2017-05-27 09:54:37.0","title":"谢天谢地，终于康复了","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"48","reply":"2","isGood":"1","tid":2},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:38:45.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"5","reply":"2","isGood":"0","tid":5},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:51:00.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"9","reply":"2","isGood":"1","tid":6},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:54:30.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"9","reply":"2","isGood":"1","tid":7},{"content":"产后合理的运动，快速恢复了！","createtime":"2017-05-27 09:54:37.0","title":"谢天谢地，终于康复了","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"0","reply":"2","isGood":"1","tid":11},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:38:45.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"0","reply":"2","isGood":"0","tid":13},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:51:00.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"0","reply":"2","isGood":"1","tid":14},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:54:30.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"0","reply":"2","isGood":"1","tid":15},{"content":"产后合理的运动，快速恢复了！","createtime":"2017-05-27 09:54:37.0","title":"谢天谢地，终于康复了","headImg":"headImg/243QVGHZB7M8.jpg","nick":"123","zan":"0","reply":"2","isGood":"1","tid":18}]
     * notice : [{"title":"【公告标题】","notice":"阿萨大所多11sadfdfds1d11阿萨德撒"},{"title":"【公告标题】","notice":"阿萨大所多11sadfdfds1d11阿萨德撒"},{"title":"【公告标题】","notice":"阿萨大所多11sadfdfds1d11阿萨德撒"}]
     * success : true
     */

    public boolean success;
    public List<ListBean> list;
    public List<NoticeBean> notice;

    public static class ListBean implements Serializable{
        /**
         * content : 我的帖子的内容，我是内容展示出来的
         * createtime : 2017-05-08 09:54:06.0
         * title : 我是帖子，我先发出来的
         * headImg : headImg/243QVGHZB7M8.jpg
         * nick : 昵称
         * zan : 6
         * reply : 24
         * isGood : 1
         * tid : 1
         */

        public String content;
        public String createtime;
        public String title;
        public String headImg;
        public String nick;
        public String zan;
        public String reply;
        public String isGood;
        public int tid;
    }

    public static class NoticeBean implements Serializable{
        /**
         * title : 【公告标题】
         * notice : 阿萨大所多11sadfdfds1d11阿萨德撒
         */

        public String title;
        public String notice;
    }
    /*public String id;
    public String image;
    public String name;
    public boolean fine;
    public String time;
    public String eulogy;
    public String reply;
    public String title;
    public String body;
    public String url;

    */

}
