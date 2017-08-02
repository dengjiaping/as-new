package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/20.
 */

public class MyNoteBean {

    /**
     * mySpeak : [{"content":"欧耶","createtime":"2017-07-20 10:16:59.0","title":"哦啦啦","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"0","tid":"10"},{"content":"欧耶","createtime":"2017-07-20 10:16:57.0","title":"哦啦啦","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"0","tid":"17"},{"content":"测试一下行不行","createtime":"2017-06-22 15:35:52.0","title":"测试一下","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"0","tid":"9"},{"content":"测试一下行不行","createtime":"2017-06-22 15:35:52.0","title":"测试一下","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"0","tid":"16"},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:54:30.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":"9","userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"1","tid":"7"},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:54:30.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"1","tid":"15"},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:51:00.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"1","tid":"14"},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:51:00.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":"9","userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"1","tid":"6"},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:38:45.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":"5","userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"0","tid":"5"},{"content":"嘟噜噜噜噜噜噜噜噜！！！！！！","createtime":"2017-06-01 10:38:45.0","title":"我的天老爷!!!!","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"0","tid":"13"},{"content":"安达市大所","createtime":"2017-06-01 10:28:29.0","title":"欧耶","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":"7","userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"1","tid":"4"},{"content":"安达市大所","createtime":"2017-06-01 10:28:29.0","title":"欧耶","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"1","tid":"12"},{"content":"产后合理的运动，快速恢复了！","createtime":"2017-05-27 09:54:37.0","title":"谢天谢地，终于康复了","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":"48","userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"1","tid":"2"},{"content":"产后合理的运动，快速恢复了！","createtime":"2017-05-27 09:54:37.0","title":"谢天谢地，终于康复了","headImg":"headImg/243QVGHZB7M8.jpg","nick":"haha","zan":null,"userId":"ed6cdb60-3eb2-11e7-aebf-fa163e547655","reply":null,"isGood":"1","tid":"11"}]
     * state : 200
     * mess : 查找成功有发过帖子
     */

    public int state;
    public String mess;
    public List<MySpeakBean> mySpeak;

    public static class MySpeakBean {
        /**
         * content : 欧耶
         * createtime : 2017-07-20 10:16:59.0
         * title : 哦啦啦
         * headImg : headImg/243QVGHZB7M8.jpg
         * nick : haha
         * zan : null
         * userId : ed6cdb60-3eb2-11e7-aebf-fa163e547655
         * reply : null
         * isGood : 0
         * tid : 10
         */

        public String content;
        public String createtime;
        public String title;
        public String headImg;
        public String nick;
        public Object zan;
        public String userId;
        public Object reply;
        public String isGood;
        public String tid;
    }
}
