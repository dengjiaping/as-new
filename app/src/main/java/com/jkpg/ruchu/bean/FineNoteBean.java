package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/6/5.
 */

public class FineNoteBean {


    /**
     * list : [{"createtime":"2017-08-24 11:29:19.0","author":"如初康复","title":"常见盆底障碍治疗手段分析，看你适合那种","simplecontent":"关于文章3333的介绍！！","articleid":"4","zan":"4","reply":"19","images":"img/timg.jpg"},{"createtime":"2017-08-24 10:59:46.0","author":"如初康复","title":"盆底脱垂千万不能这样做！","simplecontent":"关于文章2222的介绍！！","articleid":"3","zan":"3","reply":"4","images":"img/timgURLQLTTF.jpg"},{"createtime":"2017-08-24 10:34:01.0","author":"如初康复","title":"如何定位盆底肌？","simplecontent":"关于文章1111的介绍！！","articleid":"2","zan":"2","reply":"5","images":"img/u=3945284222,1175023860&amp;fm=26&amp;gp=0.jpg"}]
     * success : true
     */

    public boolean success;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * createtime : 2017-08-24 11:29:19.0
         * author : 如初康复
         * title : 常见盆底障碍治疗手段分析，看你适合那种
         * simplecontent : 关于文章3333的介绍！！
         * articleid : 4
         * zan : 4
         * reply : 19
         * images : img/timg.jpg
         */

        public String createtime;
        public String author;
        public String title;
        public String simplecontent;
        public String articleid;
        public String zan;
        public String reply;
        public String images;
    }
}
