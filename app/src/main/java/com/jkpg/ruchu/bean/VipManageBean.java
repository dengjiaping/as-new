package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/21.
 */

public class VipManageBean {

    /**
     * headImg : headImg/243R7K8LQW7M.jpg
     * nick : successds
     * VIPTime : 2017-07-17
     * list : [{"content":"这是关于训练特权的描述！！！","title":"训练特权"},{"content":"这是关于咨询特权的描述。。。。","title":"咨询特权"},{"content":"这是关于社区特权的描述？？？？？？？","title":"社区特权"}]
     * isVIP : 0
     */

    public String headImg;
    public String nick;
    public String VIPTime;
    public String isVIP;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * content : 这是关于训练特权的描述！！！
         * title : 训练特权
         */

        public String content;
        public String title;
    }
}
