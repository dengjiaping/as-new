package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/7.
 */

public class HotNoteBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * createtime : 2017-08-03 17:22:49
         * title : 我是帖子，我先发出来的
         * nick : 昵称
         * zan : 8
         * headimg : headImg/100343110.png
         * userid : d523f793-3ee1-11e7-aebf-fa163e534242
         * reply : 180
         * tid : 1
         * plateid : 1
         */

        public String createtime;
        public String title;
        public String nick;
        public int zan;
        public String headimg;
        public String userid;
        public int reply;
        public int tid;
        public int plateid;
    }
}
