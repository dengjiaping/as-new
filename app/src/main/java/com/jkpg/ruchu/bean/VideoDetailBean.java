package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/8/14.
 */

public class VideoDetailBean {

    /**
     * state : 200
     * videoMS : {"videourl":"video/123456789.mp4","title":"大神1","discuss":[{"createtime":"2017-07-08 12:24:53.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"1"},{"createtime":"2017-07-08 12:25:03.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"2"},{"createtime":"2017-07-08 14:01:50.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"3"},{"createtime":"2017-07-08 14:02:10.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"4"},{"createtime":"2017-07-08 14:04:48.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"5"},{"createtime":"2017-07-15 13:44:08.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"6"},{"createtime":"2017-07-15 13:48:03.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"7"}],"detailsUrl":"html/one.html"}
     * mess : 请求成功
     */

    public int state;
    public VideoMSBean videoMS;
    public String mess;

    public static class VideoMSBean {
        /**
         * videourl : video/123456789.mp4
         * title : 大神1
         * discuss : [{"createtime":"2017-07-08 12:24:53.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"1"},{"createtime":"2017-07-08 12:25:03.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"2"},{"createtime":"2017-07-08 14:01:50.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"3"},{"createtime":"2017-07-08 14:02:10.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"4"},{"createtime":"2017-07-08 14:04:48.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"5"},{"createtime":"2017-07-15 13:44:08.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"6"},{"createtime":"2017-07-15 13:48:03.0","username":"buzhunlala","userimageurl":"headImg/243XDCLN4TCG.jpg","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","discussContent":"dhasdh","tid":"7"}]
         * detailsUrl : html/one.html
         */

        public String videourl;
        public String title;
        public String detailsUrl;
        public String image_url;
        public List<DiscussBean> discuss;

        public static class DiscussBean {
            /**
             * createtime : 2017-07-08 12:24:53.0
             * username : buzhunlala
             * userimageurl : headImg/243XDCLN4TCG.jpg
             * userid : d523f793-3ee1-11e7-aebf-fa163e534242
             * discussContent : dhasdh
             * tid : 1
             */

            public String createtime;
            public String username;
            public String userimageurl;
            public String userid;
            public String discussContent;
            public String tid;
        }
    }
}
