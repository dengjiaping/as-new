package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/7/17.
 */

public class TrainMainBean {

    /**
     * headerLunBoImage : [{"title":"一人我饮酒醉","imageUrl":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170317/14897197236419.jpg","allcontent":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170323/14902272684314.png","tid":"1"},{"title":"醉把那家人成双对","imageUrl":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170317/14897208203765.jpg","allcontent":"video/12334567.mp4","tid":"2"},{"title":"方法将阿斯蒂芬","imageUrl":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170323/14902272684314.png","allcontent":"密苏里新闻帮的崛起","tid":"3"}]
     * uIsfirst :
     * state : 201
     * mess : 查询成功，没有用户名
     * uIsstest :
     */

    public String uIsfirst;
    public int state;
    public String mess;
    public String uIsstest;
    public List<HeaderLunBoImageBean> headerLunBoImage;

    public static class HeaderLunBoImageBean {
        /**
         * title : 一人我饮酒醉
         * imageUrl : http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170317/14897197236419.jpg
         * allcontent : http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170323/14902272684314.png
         * tid : 1
         */

        public String title;
        public String imageUrl;
        public String allcontent;
        public String tid;
    }
}
