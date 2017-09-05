package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/7/13
 */

public class MyTrainBean implements Serializable {
    /**
     * ulevel : 4
     * list : [{"level":"Level-1","strength":"4","advise":"每日3-4次","totaltime":"8","introduction":"此难度适合盆底肌受损较重，肌力偏弱的宝妈，没有锻炼经验的也可以从这里开始。适合作为盆底功能障碍者的日常康复训练。","levelname":"零基础入门"},{"level":"Level-2","strength":"6","advise":"每日2-3次","totaltime":"10","introduction":"此难度是零基础入门训练的提升版，适合有一定锻炼基础，能够完成5秒收紧的宝妈。","levelname":"新手提升"},{"level":"Level-3","strength":"7","advise":"每日2次","totaltime":"7","introduction":"随着难度增大，单次训练时间缩短。此难度适合盆底肌肌力和控制力均有稳定提升的宝妈。","levelname":"稳定进阶"},{"level":"Level-4","strength":"8","advise":"每日2次","totaltime":"6","introduction":"随着难度增大，单次训练时间缩短。此难度适合盆底肌肌力和控制力均有稳定提升的宝妈。","levelname":"坚持强化"},{"level":"Level-5","strength":"9","advise":"每日1-2次","totaltime":"3","introduction":"此难度适合有一定锻炼经验，盆底肌肌力较强，能很好控制\u201c分步收紧\u201d的宝妈。锻炼高手，可选择此难度作为日常的盆底保养，进行长期练习。","levelname":"日常巩固"}]
     * success : true
     */

    public String ulevel;
    public boolean success;
    public List<ListBean> list;

    public static class ListBean implements Serializable {
        /**
         * level : Level-1
         * strength : 4
         * advise : 每日3-4次
         * totaltime : 8
         * introduction : 此难度适合盆底肌受损较重，肌力偏弱的宝妈，没有锻炼经验的也可以从这里开始。适合作为盆底功能障碍者的日常康复训练。
         * levelname : 零基础入门
         */

        public String level;
        public boolean isSelect;
        public int backgroundRes;
        public String strength;
        public String advise;
        public String totaltime;
        public String introduction;
        public String levelname;
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
