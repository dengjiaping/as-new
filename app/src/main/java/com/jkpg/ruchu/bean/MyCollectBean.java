package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/22.
 */

public class MyCollectBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * content : 测试
         * createtime : 2017-08-29 17:08:30
         * title : 你好
         * headImg : headImg/24437SXV9CHC.jpg
         * nick : 帆♡峰
         * zan : 2
         * images : []
         * reply : 16
         * tid : 81
         * type : 1
         * bbsid : 15
         */

        public String content;
        public String createtime;
        public String title;
        public String headImg;
        public String nick;
        public String zan;
        public String images;
        public String reply;
        public int tid;
        public String type;
        public int bbsid;
    }
}
