package com.jkpg.ruchu.view.adapter;

import java.util.List;

/**
 * Created by qindi on 2017/7/20.
 */

public class MyReplyNoteBean {

    /**
     * mySpeak : [{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":21,"replytime":"2017-06-12 11:00:46.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":20,"replytime":"2017-06-12 11:00:45.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":19,"replytime":"2017-06-12 11:00:44.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":18,"replytime":"2017-06-12 11:00:42.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":17,"replytime":"2017-06-12 11:00:41.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":16,"replytime":"2017-06-12 11:00:39.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":15,"replytime":"2017-06-12 11:00:38.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":14,"replytime":"2017-06-12 11:00:36.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":13,"replytime":"2017-06-12 11:00:34.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":12,"replytime":"2017-06-12 11:00:33.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":11,"replytime":"2017-06-12 11:00:31.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":10,"replytime":"2017-06-12 11:00:30.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":9,"replytime":"2017-06-12 11:00:29.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":8,"replytime":"2017-06-12 11:00:27.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":7,"replytime":"2017-06-12 11:00:26.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":6,"replytime":"2017-06-12 11:00:25.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":5,"replytime":"2017-06-12 11:00:23.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":4,"replytime":"2017-06-12 11:00:20.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":22,"replytime":"2017-06-07 11:01:50.0","ct":"56"},{"content":"产后合理的运动，快速恢复了！","title":"谢天谢地，终于康复了","BBSId":1,"replytime":"2017-06-07 11:01:50.0","ct":"784"}]
     * state : 200
     * mess : 查找成功有回复过的帖子
     */

    public int state;
    public String mess;
    public List<MySpeakBean> mySpeak;

    public static class MySpeakBean {
        /**
         * content : 产后合理的运动，快速恢复了！
         * title : 谢天谢地，终于康复了
         * BBSId : 21
         * replytime : 2017-06-12 11:00:46.0
         * ct : 56
         */

        public String content;
        public String title;
        public int BBSId;
        public String replytime;
        public String ct;
        public String headImg;
    }
}
