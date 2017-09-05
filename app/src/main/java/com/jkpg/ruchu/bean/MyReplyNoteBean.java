package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/20.
 */

public class MyReplyNoteBean {


    public int state;
    public String mess;
    public List<MyReplyBean> myReply;

    public static class MyReplyBean {


        public String content;
        public String title;
        public String headImg;
        public int BBSId;
        public String replytime;
        public String ct;
    }
}
