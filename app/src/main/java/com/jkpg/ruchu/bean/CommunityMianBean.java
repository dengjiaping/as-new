package com.jkpg.ruchu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qindi on 2017/6/8.
 */

public class CommunityMianBean implements Serializable {


    public List<List2Bean> list2;
    public List<List3Bean> list3;
    public List<List1Bean> list1;

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

    public static class List3Bean implements Serializable {
        /**
         * createtime : 2017-06-22 15:35:52.0
         * title : 测试一下
         * nick : null
         * zan : 0
         * headimg : null
         * userid : 39
         * reply : 0
         * tid : 9
         * plateid : 3
         */

        public String createtime;
        public String title;
        public String nick;
        public int zan;
        public String headimg;
        public String userid;
        public String type;
        public int reply;
        public int tid;
        public int plateid;
    }

    public static class List1Bean implements Serializable {
        /**
         * title : 我的天老爷!!!!
         * images : bbsImg/156484564.png
         * tid : 5
         */

        public String title;
        public String images;
        public String type;
        public int tid;
    }
}
