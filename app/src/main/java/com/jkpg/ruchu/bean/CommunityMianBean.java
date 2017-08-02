package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/6/8.
 */

public class CommunityMianBean {


    public List<List2Bean> list2;
    public List<List3Bean> list3;
    public List<List1Bean> list1;

    public static class List2Bean {
        /**
         * zongshu : 5
         * plateimg : headImg/100343110.png
         * platename : 尴尬体验
         * tid : 1
         */

        public int zongshu;
        public String plateimg;
        public String remark;
        public String platename;
        public int tid;
    }

    public static class List3Bean {
        /**
         * createtime : 2017-06-22 15:35:52.0
         * title : 测试一下
         * nick : null
         * zan : 0
         * headimg : null
         * userid : 39
         * reply : 0
         * tid : 9
         * plateid : 3
         */

        public String createtime;
        public String title;
        public String  nick;
        public int zan;
        public String headimg;
        public String userid;
        public int reply;
        public int tid;
        public int plateid;
    }

    public static class List1Bean {
        /**
         * title : 我的天老爷!!!!
         * images : bbsImg/156484564.png
         * tid : 5
         */

        public String title;
        public String images;
        public int tid;
    }
}
