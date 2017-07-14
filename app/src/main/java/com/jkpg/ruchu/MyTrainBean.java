package com.jkpg.ruchu;

/**
 * Created by qindi on 2017/7/13.
 */

public class MyTrainBean {
    public int backgroundRes;
    public String difficulty;
    public String difficultyNum;
    public String time;
    public boolean isSelect;
    public String introduce;

    public MyTrainBean(int backgroundRes, String difficulty, String difficultyNum, String time, boolean isSelect, String introduce) {
        this.backgroundRes = backgroundRes;
        this.difficulty = difficulty;
        this.difficultyNum = difficultyNum;
        this.time = time;
        this.isSelect = isSelect;
        this.introduce = introduce;
    }
}
