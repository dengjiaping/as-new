package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/7/21.
 */

public class MyTestBean {


    /**
     * istest : 1
     * state : 200
     * report : {"createtime":"2017-08-03 18:02:30.0","content":";;;;;;。","userHeight":"168","level":"Level-3","count":"81.13","userWeight":"45","userAge":"28","userNike":"A星越"}
     * mess : 查询成功,返回报告
     */

    public String istest;
    public int state;
    public ReportBean report;
    public String mess;

    public static class ReportBean {
        /**
         * createtime : 2017-08-03 18:02:30.0
         * content : ;;;;;;。
         * userHeight : 168
         * level : Level-3
         * count : 81.13
         * userWeight : 45
         * userAge : 28
         * userNike : A星越
         */

        public String createtime;
        public String content;
        public String userHeight;
        public String level;
        public String count;
        public String userWeight;
        public String userAge;
        public String userNike;
    }
}
