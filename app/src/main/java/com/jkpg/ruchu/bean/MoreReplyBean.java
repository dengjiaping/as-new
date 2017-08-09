package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/8.
 */

public class MoreReplyBean {

    public List<ItemsBean> items;

    public static class ItemsBean {
        /**
         * content : 方法很不简单,望有用
         * userid2 : ed6cdb60-3eb2-11e7-aebf-fa163e547655
         * headImg : headImg/100343110.png
         * nick : haha
         * replytime : 2017-06-12 11:00:20
         * userid : ed6cdb60-3eb2-11e7-aebf-fa163e547655
         * parentid : 2
         * tid : 4
         * nick2 : haha
         */
        public String ftid;
        public String content;
        public String userid2;
        public String headImg;
        public String nick;
        public String replytime;
        public String userid;
        public String parentid;
        public int tid;
        public String nick2;
    }
}
