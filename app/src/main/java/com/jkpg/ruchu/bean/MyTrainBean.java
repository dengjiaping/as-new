package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/7/13
 */

public class MyTrainBean implements Serializable{
    /**
     * ulevel : 5
     * list : [{"level":"Level-1","strength":"0~20","totaltime":"2:03"},{"level":"Level-2","strength":"20~40","totaltime":"02:12"},{"level":"Level-3","strength":"40~60","totaltime":"02:12"},{"level":"Level-4","strength":"60~80","totaltime":"02:12"},{"level":"Level-5","strength":"80~100","totaltime":"02:05"}]
     * success : true
     */

    public String ulevel;
    public boolean success;
    public List<ListBean> list;

    public static class ListBean implements Serializable {
        /**
         * level : Level-1
         * strength : 0~20
         * totaltime : 2:03
         */

        public int backgroundRes;
        public String level;
        public String strength;
        public String totaltime;
        public String introduction;
        public boolean isSelect;
    }
   /* public String difficulty;
    public String difficultyNum;
    public String time;
    public String introduce;

    public MyTrainBean(int backgroundRes, String difficulty, String difficultyNum, String time, boolean isSelect, String introduce) {
        this.backgroundRes = backgroundRes;
        this.difficulty = difficulty;
        this.difficultyNum = difficultyNum;
        this.time = time;
        this.isSelect = isSelect;
        this.introduce = introduce;
    }*/

}
