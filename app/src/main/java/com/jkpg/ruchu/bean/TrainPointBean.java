package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/5/23.
 */

public class TrainPointBean implements Serializable {


    /**
     * time : 55
     * arr : [[2,3,4,5,2,4,1,2,3,2,3],[1,2,3,4,5,6,6,4,0,0,0],[2,3,4,5,2,4,0,0,2,0,2],[2,3,4,5,2,4,1,1,1,1,1],[2,3,4,5,2,4,0,0,2,3,1]]
     * number : 5
     * success : true
     */

    public int time;
    public int number;
    public boolean success;
    public List<int[]> arr;
}
