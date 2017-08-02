package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/21.
 */

public class TrainQuestionBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * title : 请选择你的出生年月
         * question : [{"A":"出生年月"}]
         * tid : 1
         */

        public String title;
        public String tid;
        public List<QuestionBean> question;

        public static class QuestionBean {
            /**
             * A : 出生年月
             */

            public String A;
            public String B;
            public String C;
            public String D;
        }
    }
}
