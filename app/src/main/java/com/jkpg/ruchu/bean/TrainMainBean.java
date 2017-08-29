package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/7/17.
 */

public class TrainMainBean implements Serializable {
    /**
     * headerLunBoImage : [{"title":"一人我饮酒醉","imageUrl":"lunboimg/111111.jpg","allcontent":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170323/14902272684314.png","tid":"1"},{"title":"醉把那家人成双对","imageUrl":"lunboimg/222222.jpg","allcontent":"video/12334567.mp4","tid":"2"},{"title":"方法将阿斯蒂芬","imageUrl":"lunboimg/333333.jpg","allcontent":"密苏里新闻帮的崛起","tid":"3"}]
     * uIsfirst : 1
     * userInfos : {"tele":"18364195986","istest":"1","chanhoutime":"2个月01天","birth":"1990-05-20","isfirst":"1","weight":"45 kg","flag_status":"1","userid":"d523f793-3ee1-11e7-aebf-fa163e534242","taici":"二胎","height":"168 cm","level":"Level-5","headImg":"headImg/243XDCLN4TCG.jpg","nick":"buzhunlala","address":"山东省  济南市","introduction":"不介绍了不介绍了不介绍了不介绍了"}
     * state : 200
     * mess : 查询成功，收到用户名
     * uIsstest : 1
     * uLevel : Level-5
     */

    public String uIsfirst;
    public UserInfosBean userInfos;
    public int state;
    public String mess;
    public String uIsstest;
    public String uLevel;
    public List<HeaderLunBoImageBean> headerLunBoImage;

    public static class UserInfosBean implements Serializable {
        /**
         * tele : 18364195986
         * istest : 1
         * chanhoutime : 2个月01天
         * birth : 1990-05-20
         * isfirst : 1
         * weight : 45 kg
         * flag_status : 1
         * userid : d523f793-3ee1-11e7-aebf-fa163e534242
         * taici : 二胎
         * height : 168 cm
         * level : Level-5
         * headImg : headImg/243XDCLN4TCG.jpg
         * nick : buzhunlala
         * address : 山东省  济南市
         * introduction : 不介绍了不介绍了不介绍了不介绍了
         */

        public String tele;
        public String istest;
        public String chanhoutime;
        public String birth;
        public String isfirst;
        public String weight;
        public String flag_status;
        public String userid;
        public String taici;
        public String height;
        public String level;
        public String headImg;
        public String nick;
        public String address;
        public String introduction;
    }

    public static class HeaderLunBoImageBean implements Serializable {
        /**
         * title : 一人我饮酒醉
         * imageUrl : lunboimg/111111.jpg
         * allcontent : http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170323/14902272684314.png
         * tid : 1
         */

        public String title;
        public String imageUrl;
        public String allcontent;
        public String tid;
    }

//    /**
//     * headerLunBoImage : [{"title":"一人我饮酒醉","imageUrl":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170317/14897197236419.jpg","allcontent":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170323/14902272684314.png","tid":"1"},{"title":"醉把那家人成双对","imageUrl":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170317/14897208203765.jpg","allcontent":"video/12334567.mp4","tid":"2"},{"title":"方法将阿斯蒂芬","imageUrl":"http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170323/14902272684314.png","allcontent":"密苏里新闻帮的崛起","tid":"3"}]
//     * uIsfirst :
//     * state : 201
//     * mess : 查询成功，没有用户名
//     * uIsstest :
//     */
//
//    public String uIsfirst;
//    public int state;
//    public String mess;
//    public String uIsstest;
//    public List<HeaderLunBoImageBean> headerLunBoImage;
//
//    public static class HeaderLunBoImageBean {
//        /**
//         * title : 一人我饮酒醉
//         * imageUrl : http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170317/14897197236419.jpg
//         * allcontent : http://jkpg.com.cn/HealthCity/Public/Plugin/umeditor/php/upload/20170323/14902272684314.png
//         * tid : 1
//         */
//
//        public String title;
//        public String imageUrl;
//        public String allcontent;
//        public String tid;
//    }


}
