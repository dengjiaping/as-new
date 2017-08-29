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
        /**
         * title : 大神1
         * imageUrl : bbsImg/156484564.png
         * video_time : 1:30
         * orderid : 1
         * tid : 8
         */

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

//    /**
//     * videoMS2 : [{"videomess":{"videourl":"video/123456789.mp4","title":"大神1","discuss":"","detailsUrl":"html/one.html"},"title":"大神1","videotime":"1:30","imageUrl":"bbsImg/156484564.png","orderid":"1","tid":"8"},{"videomess":{"videourl":"video/123456789.mp4","title":"大神2","discuss":"","detailsUrl":"html/one.html"},"title":"大神2","videotime":"1:30","imageUrl":"bbsImg/156484564.png","orderid":"2","tid":"9"},{"videomess":{"videourl":"video/123456789.mp4","title":"大神3","discuss":"","detailsUrl":"html/one.html"},"title":"大神3","videotime":"1:30","imageUrl":"bbsImg/156484564.png","orderid":"3","tid":"10"},{"videomess":{"videourl":"video/123456789.mp4","title":"大神4","discuss":"","detailsUrl":"html/one.html"},"title":"大神4","videotime":"1:30","imageUrl":"bbsImg/156484564.png","orderid":"4","tid":"11"}]
//     * Mess : []
//     * success : true
//     */
//
//    public boolean success;
//    public List<VideoMS2Bean> videoMS2;
//    public List<?> Mess;
//
//    public static class VideoMS2Bean {
//        public VideomessBean videomess;
//        public String title;
//        public String videotime;
//        public String imageUrl;
//        public String orderid;
//        public String tid;
//        public static class VideomessBean implements Parcelable {
//
//            public String videourl;
//            public String title;
//            public String discuss;
//            public String detailsUrl;
//
//            protected VideomessBean(Parcel in) {
//                videourl = in.readString();
//                title = in.readString();
//                discuss = in.readString();
//                detailsUrl = in.readString();
//            }
//
//            public static final Creator<VideomessBean> CREATOR = new Creator<VideomessBean>() {
//                @Override
//                public VideomessBean createFromParcel(Parcel in) {
//                    return new VideomessBean(in);
//                }
//
//                @Override
//                public VideomessBean[] newArray(int size) {
//                    return new VideomessBean[size];
//                }
//            };
//
//            @Override
//            public int describeContents() {
//                return 0;
//            }
//
//            @Override
//            public void writeToParcel(Parcel dest, int flags) {
//                dest.writeString(videourl);
//                dest.writeString(title);
//                dest.writeString(discuss);
//                dest.writeString(detailsUrl);
//            }
//        }
//    }
}
