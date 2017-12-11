package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/11/24.
 */

public class TrainMainBean2 {


    public int everytimes;
    public int todayXltimes;
    public String getgifturl;
    public String tid;
    public String windowflag;
    public int currDaytime;
    public int levelid;
    public int everyweektimes;
    public String uIsfirst;
    public int weekxltimes;
    public int alltime;
    public String uIsstest;
    public String uLevel;
    public String uzidifficulty;
    public String levelname;
    public String excisedays;
    public String endtime;
    public String statime;
    public List<TuozhanxlBean> tuozhanxl;
    public List<ListShoppingBean> listShopping;
    public List<ListArticleBean> listArticle;

    public static class TuozhanxlBean {

        public String title;
        public String video_url;
        public String times;
        public String video_time;
        public String videoid;
        public String imgurl;
    }

    public static class ListShoppingBean {
        public String tuijianimg;
        public String title;
        public String shoppingurl;
    }

    public static class ListArticleBean {
        public String tuijianimg;
        public String title;
        public String ID;
    }
}
