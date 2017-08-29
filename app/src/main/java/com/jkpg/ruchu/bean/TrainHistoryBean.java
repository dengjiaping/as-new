package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/8/2.
 */

public class TrainHistoryBean  implements Serializable{



    public int state;
    public String mess;
    public List<ItemsBean> items;

    public static class ItemsBean implements Serializable {


        public String level;
        public String currenttime;
        public String sumtime;
        public List<RecordBean> record;

        public static class RecordBean implements Serializable {

            public String practice_time;
            public String bigintime;
        }
    }
}
