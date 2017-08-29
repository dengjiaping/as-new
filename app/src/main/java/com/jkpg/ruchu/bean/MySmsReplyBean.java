package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/11.
 */

public class MySmsReplyBean {


    /**
     * state : 200
     * mess : 查找成功有值
     * backMess : [{"ruserid":"","flag":"1","reimgurl":"","BBSId":"1","reply_content":"GHG","replytime":"2017-08-11 15:22:46","ftnick":"昵称","fttime":"2017-08-03 17:22:49","renick":"","fttitle":"我是帖子，我先发出来的"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"7","reply_content":"The app is ","replytime":"2017-08-09 17:32:29","ftnick":"123","fttime":"2017-06-01 10:54:30","renick":"帆HqX7nJEah","fttitle":"我的天老爷!!!!"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"7","reply_content":"The app is ","replytime":"2017-08-09 17:31:48","ftnick":"123","fttime":"2017-06-01 10:54:30","renick":"帆HqX7nJEah","fttitle":"我的天老爷!!!!"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"7","reply_content":"The app is ","replytime":"2017-08-09 17:31:05","ftnick":"123","fttime":"2017-06-01 10:54:30","renick":"帆HqX7nJEah","fttitle":"我的天老爷!!!!"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"13","reply_content":"The fact is ","replytime":"2017-08-09 17:29:44","ftnick":"123","fttime":"2017-06-01 10:38:45","renick":"帆HqX7nJEah","fttitle":"我的天老爷!!!!"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"14","reply_content":"The fact is ","replytime":"2017-08-09 17:29:04","ftnick":"123","fttime":"2017-06-01 10:51:00","renick":"帆HqX7nJEah","fttitle":"我的天老爷!!!!"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"14","reply_content":"The fact is ","replytime":"2017-08-09 17:25:02","ftnick":"123","fttime":"2017-06-01 10:51:00","renick":"帆HqX7nJEah","fttitle":"我的天老爷!!!!"},{"nick3":"buzhunlala","userid3":"d523f793-3ee1-11e7-aebf-fa163e534242","flag":"2","BBSId":"9","reply_content":"FCC","replytime":"2017-08-09 14:16:56","ftnick":"123","imgurl3":"headImg/243XDCLN4TCG.jpg","content3":"GH","fttime":"2017-06-22 15:35:52","fttitle":"测试一下"},{"ruserid":"d523f793-3ee1-11e7-aebf-fa163e534242","flag":"1","reimgurl":"headImg/243XDCLN4TCG.jpg","BBSId":"9","reply_content":"FCC","replytime":"2017-08-09 14:16:51","ftnick":"123","fttime":"2017-06-22 15:35:52","renick":"buzhunlala","fttitle":"测试一下"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"1","reply_content":"你好","replytime":"2017-08-09 09:04:00","ftnick":"昵称","fttime":"2017-08-03 17:22:49","renick":"帆HqX7nJEah","fttitle":"我是帖子，我先发出来的"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"1","reply_content":"","replytime":"2017-08-09 09:03:53","ftnick":"昵称","fttime":"2017-08-03 17:22:49","renick":"帆HqX7nJEah","fttitle":"我是帖子，我先发出来的"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"1","reply_content":"","replytime":"2017-08-09 09:03:52","ftnick":"昵称","fttime":"2017-08-03 17:22:49","renick":"帆HqX7nJEah","fttitle":"我是帖子，我先发出来的"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"1","reply_content":"","replytime":"2017-08-09 09:02:51","ftnick":"昵称","fttime":"2017-08-03 17:22:49","renick":"帆HqX7nJEah","fttitle":"我是帖子，我先发出来的"},{"ruserid":"587a253d-3d07-11e7-aebf-fa163e547655","flag":"1","reimgurl":"headImg/243XPCAR9JCG.jpg","BBSId":"1","reply_content":"","replytime":"2017-08-09 09:02:41","ftnick":"昵称","fttime":"2017-08-03 17:22:49","renick":"帆HqX7nJEah","fttitle":"我是帖子，我先发出来的"},{"nick3":"buzhunlala","userid3":"d523f793-3ee1-11e7-aebf-fa163e534242","flag":"2","BBSId":"91","reply_content":"GHG","replytime":"2017-08-08 17:12:35","ftnick":"A星越","imgurl3":"headImg/243XDCLN4TCG.jpg","content3":"GHG","fttime":"2017-08-08 17:11:44","fttitle":"FCC to"}]
     */

    public int state;
    public String mess;
    public List<BackMessBean> backMess;

    public static class BackMessBean {
        /**
         * ruserid :
         * flag : 1
         * reimgurl :
         * BBSId : 1
         * reply_content : GHG
         * replytime : 2017-08-11 15:22:46
         * ftnick : 昵称
         * fttime : 2017-08-03 17:22:49
         * renick :
         * fttitle : 我是帖子，我先发出来的
         * nick3 : buzhunlala
         * userid3 : d523f793-3ee1-11e7-aebf-fa163e534242
         * imgurl3 : headImg/243XDCLN4TCG.jpg
         * content3 : GH
         */

        public String flag;
        public String BBSId;
        public String ruserid;
        public String reimgurl;
        public String renick;
        public String reply_content;
        public String replytime;
        public String ftnick;
        public String fttime;
        public String fttitle;
        public String nick3;
        public String userid3;
        public String imgurl3;
        public String content3;
    }
}
