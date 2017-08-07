package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/3.
 */

public class TrainYearCountBean {

    /**
     * arraypoint : [["01","02","03","04","05","06","07","08","09","10","11","12"],["0","0","0","0","0","0","5310","3717","2124","0","0","0"]]
     * max : 5310
     * state : 200
     * mess : 鏌ユ壘鎴愬姛
     * avmonth : 929
     * countyear : 11151
     */

    public String max;
    public int state;
    public String mess;
    public int avmonth;
    public int countyear;
    public List<List<String>> arraypoint;
}
