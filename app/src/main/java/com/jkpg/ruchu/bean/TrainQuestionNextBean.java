package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/21.
 */

public class TrainQuestionNextBean {


    public List<ListBean> list;

    public static class ListBean {

        public String kuohao;
        public String tz_titlesum;
        public String title;
        public String level;
        public String tid;
        public String tiaozhuanselect;
        public String questionid;
        public String fromquestionid;
        public List<AdminquestionBean> adminquestion;
        public List<QuestionBean> question;

        public static class AdminquestionBean {

            public String count;
            public String answer;
            public String key;
        }

        public static class QuestionBean {
            public String A;
            public String B;
            public String C;
            public String D;
        }
    }
}
