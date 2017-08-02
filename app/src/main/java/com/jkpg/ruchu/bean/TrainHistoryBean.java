package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/2.
 */

public class TrainHistoryBean {



    public int state;
    public String mess;
    public List<ItemsBean> items;

    public static class ItemsBean {


        public String level;
        public String currenttime;
        public String sumtime;
        public List<RecordBean> record;

        public static class RecordBean {

            public String practice_time;
            public String bigintime;
        }
    }
}
