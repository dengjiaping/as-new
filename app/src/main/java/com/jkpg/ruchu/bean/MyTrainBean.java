package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/7/13
 */

public class MyTrainBean implements Serializable {

    public String ulevel;
    public String advisemsg;

    public boolean success;
    public List<ListBean> list;

    public static class ListBean implements Serializable {

        public String level;
        public boolean isSelect;
        public int backgroundRes;
        public String advise;
        public String introduction;
    }
}
