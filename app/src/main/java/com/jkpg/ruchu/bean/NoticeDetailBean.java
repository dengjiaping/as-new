package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/7/24.
 */

public class NoticeDetailBean implements Serializable {

    public List<List2Bean> list2;
    public List<List1Bean> list1;

    public static class List2Bean implements Serializable {
        /**
         * chanhoutime : 2月零1天
         * headimg : headImg/243QVGHZB7M8.jpg
         * userid : ed6cdb60-3eb2-11e7-aebf-fa163e547655
         * parentid : -1
         * taici : 无
         * content : 方法很简单，可888888888有用
         * iszan : false
         * nick : haha
         * items : [{"content":"方法很不简单,望有用","userid2":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","nick":"haha","replytime":"2017-06-12 11:00:20.0","userid":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","parentid":"2","tid":4,"nick2":"haha"},{"content":"方法很不简单,望有用","userid2":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","nick":"haha","replytime":"2017-06-12 11:00:27.0","userid":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","parentid":"2","tid":8,"nick2":"haha"}]
         * zan : 8
         * replytime : 2017-05-27 11:01:50.0
         * images : []
         * reply : 0
         * tid : 2
         */

        public String chanhoutime;
        public String headimg;
        public String userid;
        public String parentid;
        public String taici;
        public String content;
        public boolean iszan;
        public String nick;
        public int zan;
        public String replytime;
        public String reply;
        public int tid;
        public List<ItemsBean> items;
        public List<String> images;

        public static class ItemsBean implements Serializable {
            /**
             * content : 方法很不简单,望有用
             * userid2 : ed6cdb60-3eb2-11e7-aebf-fa163e547655
             * nick : haha
             * replytime : 2017-06-12 11:00:20.0
             * userid : ed6cdb60-3eb2-11e7-aebf-fa163e547655
             * parentid : 2
             * tid : 4
             * nick2 : haha
             */

            public String content;
            public String headImg;
            public String userid2;
            public String nick;
            public String replytime;
            public String userid;
            public String parentid;
            public int tid;
            public String nick2;
        }
    }

    public static class List1Bean implements Serializable {
        /**
         * site : 济南市
         * chanhoutime : 2个月01天
         * headimg : headImg/243QVGHZB7M8.jpg
         * userid : d523f793-3ee1-11e7-aebf-fa163e534242
         * iscollect : false
         * content : 我的帖子的内容，我是内容展示出来的
         * taici : 二胎
         * createtime : 2017-05-08 09:54:06.0
         * title : 我是帖子，我先发出来的
         * iszan : false
         * nick : 昵称
         * zan : 0
         * images : ["bbsImg/156484564.png","bbsImg/156484564.png"]
         * reply : 0
         * tid : 1
         * isGood : 1
         */

        public String site;
        public String chanhoutime;
        public String headimg;
        public String userid;
        public boolean iscollect;
        public boolean isHideName;
        public String content;
        public String taici;
        public String createtime;
        public String title;
        public boolean iszan;
        public String nick;
        public String zan;
        public String reply;
        public int tid;
        public String isGood;
        public String shareurl;
        public List<String> images;
    }
}
