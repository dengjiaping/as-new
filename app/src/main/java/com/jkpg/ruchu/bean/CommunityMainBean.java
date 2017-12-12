package com.jkpg.ruchu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/6/8.
 */

public class CommunityMainBean implements Serializable {


    public List<List2Bean> list2;
    public List<List1Bean> list1;
    public List<DarenBean> daren;

    public static class List2Bean implements Parcelable, Serializable {
        /**
         * zongshu : 5
         * plateimg : headImg/100343110.png
         * platename : 尴尬体验
         * tid : 1
         */

        public int zongshu;
        public String plateimg;
        public String remark;
        public String platename;
        public String simpleremark;
        public int tid;

        protected List2Bean(Parcel in) {
            zongshu = in.readInt();
            plateimg = in.readString();
            remark = in.readString();
            platename = in.readString();
            simpleremark = in.readString();
            tid = in.readInt();
        }

        public static final Creator<List2Bean> CREATOR = new Creator<List2Bean>() {
            @Override
            public List2Bean createFromParcel(Parcel in) {
                return new List2Bean(in);
            }

            @Override
            public List2Bean[] newArray(int size) {
                return new List2Bean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(zongshu);
            dest.writeString(plateimg);
            dest.writeString(remark);
            dest.writeString(platename);
            dest.writeString(simpleremark);
            dest.writeInt(tid);
        }
    }


    public static class List1Bean implements Serializable {

        public String title;
        public String image;
        public String type;
        public JsonBean json;
        public String bbsid;
        public String htmlurl;
    }

    public static class DarenBean implements Serializable {
        public String drisgz;
        public String drimgurl;
        public String drnick;
        public String druserid;
    }

    public static class JsonBean implements Serializable {
        public AndroidBean android;
    }

    public static class AndroidBean implements Serializable {
        public String key;
        public String value;
    }
}
