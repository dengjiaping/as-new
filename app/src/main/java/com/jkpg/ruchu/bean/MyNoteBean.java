package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/20.
 */

public class MyNoteBean {



    public int state;
    public String mess;
    public List<MySpeakBean> mySpeak;

    public static class MySpeakBean {

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
