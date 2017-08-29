package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/12.
 */

public class MySmsLoveBean {


    /**
     * state : 200
     * mess : 查找成功有值
     * backMess : [{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","text":"贊了你的帖子","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"14","retime":"2017-08-09 13:45:26","ftnick":"123","fttime":"2017-06-01 10:51:00","renick":"帆HqX7nJEah","fttitle":"我的天老爷!!!!"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","text":"贊了你的帖子","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"1","retime":"2017-08-09 09:50:42","ftnick":"昵称","fttime":"2017-08-03 17:22:49","renick":"帆HqX7nJEah","fttitle":"我是帖子，我先发出来的"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"10","ftnick":"123","imgurl3":"","fttime":"2017-07-20 10:16:59","replycontent":"GHG","fttitle":"哦啦啦"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"1","ftnick":"昵称","imgurl3":"","fttime":"2017-08-03 17:22:49","replycontent":"额jar","fttitle":"我是帖子，我先发出来的"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"10","ftnick":"123","imgurl3":"","fttime":"2017-07-20 10:16:59","replycontent":"FCC","fttitle":"哦啦啦"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"91","ftnick":"A星越","imgurl3":"","fttime":"2017-08-08 17:11:44","replycontent":"GHG","fttitle":"FCC to"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"1","ftnick":"昵称","imgurl3":"","fttime":"2017-08-03 17:22:49","replycontent":"","fttitle":"我是帖子，我先发出来的"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"6","ftnick":"123","imgurl3":"","fttime":"2017-06-01 10:51:00","replycontent":"TT","fttitle":"我的天老爷!!!!"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"1","ftnick":"昵称","imgurl3":"","fttime":"2017-08-03 17:22:49","replycontent":"","fttitle":"我是帖子，我先发出来的"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"16","ftnick":"123","imgurl3":"","fttime":"2017-06-22 15:35:52","replycontent":"GHG","fttitle":"测试一下"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"1","ftnick":"昵称","imgurl3":"","fttime":"2017-08-03 17:22:49","replycontent":"try","fttitle":"我是帖子，我先发出来的"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"1","ftnick":"昵称","imgurl3":"","fttime":"2017-08-03 17:22:49","replycontent":"Iggy","fttitle":"我是帖子，我先发出来的"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"2","ftnick":"123","imgurl3":"","fttime":"2017-05-27 09:54:37","replycontent":"hhah","fttitle":"谢天谢地，终于康复了"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"90","ftnick":"A星越","imgurl3":"","fttime":"2017-08-08 17:04:39","replycontent":"FCC","fttitle":"FCC"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"2","ftnick":"123","imgurl3":"","fttime":"2017-05-27 09:54:37","replycontent":"哈哈","fttitle":"谢天谢地，终于康复了"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"2","ftnick":"123","imgurl3":"","fttime":"2017-05-27 09:54:37","replycontent":"hhaa","fttitle":"谢天谢地，终于康复了"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"1","ftnick":"昵称","imgurl3":"","fttime":"2017-08-03 17:22:49","replycontent":"","fttitle":"我是帖子，我先发出来的"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"10","ftnick":"123","imgurl3":"","fttime":"2017-07-20 10:16:59","replycontent":"day","fttitle":"哦啦啦"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"1","ftnick":"昵称","imgurl3":"","fttime":"2017-08-03 17:22:49","replycontent":"哈哈","fttitle":"我是帖子，我先发出来的"},{"nick3":"","text":"贊了你的帖子","userid3":"","flag":"2","retime3":"","BBSId":"12","ftnick":"123","imgurl3":"","fttime":"2017-06-01 10:28:29","replycontent":"GHG","fttitle":"欧耶"}]
     */

    public int state;
    public String mess;
    public List<BackMessBean> backMess;

    public static class BackMessBean {
        /**
         * ruserid : 587a253d-3d07-11e7-aebf-fa163e547655
         * text : 贊了你的帖子
         * flag : 1
         * reimgurl : headImg/243XPCAR9JCG.jpg
         * BBSId : 14
         * retime : 2017-08-09 13:45:26
         * ftnick : 123
         * fttime : 2017-06-01 10:51:00
         * renick : 帆HqX7nJEah
         * fttitle : 我的天老爷!!!!
         * nick3 :
         * userid3 :
         * retime3 :
         * imgurl3 :
         * replycontent : GHG
         */

        public String ruserid;
        public String text;
        public String flag;
        public String reimgurl;
        public String BBSId;
        public String retime;
        public String ftnick;
        public String fttime;
        public String renick;
        public String fttitle;
        public String nick3;
        public String userid3;
        public String retime3;
        public String imgurl3;
        public String replycontent;
    }
}
