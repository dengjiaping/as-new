package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/6/6.
 */

public class NoticeBean {

    /**
     * json : {"remark":"板块说明","platename":"尴尬体验"}
     * list : [{"title":"【公告标题】","notice":"阿萨大所多11sadfdfds1d11阿萨德撒"},{"title":"【公告标题】","notice":"阿萨大所多11sadfdfds1d11阿萨德撒"},{"title":"【公告标题】","notice":"阿萨大所多11sadfdfds1d11阿萨德撒"}]
     */

    public JsonBean json;
    public List<ListBean> list;

    public static class JsonBean {
        /**
         * remark : 板块说明
         * platename : 尴尬体验
         */

        public String remark;
        public String platename;
    }

    public static class ListBean {
        /**
         * title : 【公告标题】
         * notice : 阿萨大所多11sadfdfds1d11阿萨德撒
         */

        public String title;
        public String notice;
    }
}
