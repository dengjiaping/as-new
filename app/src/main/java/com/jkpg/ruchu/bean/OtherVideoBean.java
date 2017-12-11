package com.jkpg.ruchu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/7/18.
 */

public class OtherVideoBean implements Serializable{
    /**
     * item : [{"title":"大神1","imageUrl":"bbsImg/156484564.png","video_time":"1:30","orderid":"1","tid":"8"},{"title":"大神2","imageUrl":"bbsImg/156484564.png","video_time":"1:30","orderid":"2","tid":"9"},{"title":"大神3","imageUrl":"bbsImg/156484564.png","video_time":"1:30","orderid":"3","tid":"10"},{"title":"大神4","imageUrl":"bbsImg/156484564.png","video_time":"1:30","orderid":"4","tid":"11"}]
     * state : 200
     * mess : 拓展列表请求成功
     */

    public int state;
    public String mess;
    public List<ItemBean> item;

    public static class ItemBean implements Serializable {

        public String title;
        public String imageUrl;
        public String video_time;
        public String orderid;
        public String tid;
        public String content;
        public String level;
        public String video_url;
        public String times;
    }

}
