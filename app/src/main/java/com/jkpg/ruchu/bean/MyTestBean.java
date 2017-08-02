package com.jkpg.ruchu.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qindi on 2017/7/21.
 */

public class MyTestBean {


    /**
     * istest : 1
     * state : 200
     * report : {"createtime":"2017-07-12 14:04:33.0","content":"16","userHeight":"179","class":"A级","userWeight":"80","userAge":"25","userNike":"a星月"}
     * mess : 查询成功,返回报告
     */

    public String istest;
    public int state;
    public ReportBean report;
    public String mess;

    public static class ReportBean {
        /**
         * createtime : 2017-07-12 14:04:33.0
         * content : 16
         * userHeight : 179
         * class : A级
         * userWeight : 80
         * userAge : 25
         * userNike : a星月
         */

        public String createtime;
        public String content;
        public String userHeight;
        @SerializedName("class")
        public String classX;
        public String userWeight;
        public String userAge;
        public String userNike;
    }
}
