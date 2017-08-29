package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/8/22.
 */

public class YouZanBean {

    /**
     * code : 0
     * msg : 登录成功
     * data : {"access_token":"52609a8d91143271ab5d5ceede046363","cookie_key":"open_cookie_2d230f061c23006b4e","cookie_value":"YZf5d18811de064b363a4a13f4ed"}
     */

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * access_token : 52609a8d91143271ab5d5ceede046363
         * cookie_key : open_cookie_2d230f061c23006b4e
         * cookie_value : YZf5d18811de064b363a4a13f4ed
         */

        public String access_token;
        public String cookie_key;
        public String cookie_value;
    }
}
