package com.jkpg.ruchu.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qindi on 2017/8/2.
 */

public class TestResultBean implements Parcelable {

    /**
     * content : ;;;;;;;;;;;;;。
     * weight : 45
     * height : 168
     * level : Level-1
     * count : 62.97
     * usernick : A星越
     * age : 28
     * userid : d523f793-3ee1-11e7-aebf-fa163e534242
     * testtime : 2017-08-02
     */

    public String content;
    public int weight;
    public int height;
    public String level;
    public String count;
    public String usernick;
    public int age;
    public String userid;
    public String testtime;
    public int levelid;
    public boolean ischange;


    protected TestResultBean(Parcel in) {
        content = in.readString();
        weight = in.readInt();
        height = in.readInt();
        level = in.readString();
        count = in.readString();
        usernick = in.readString();
        age = in.readInt();
        userid = in.readString();
        testtime = in.readString();
        levelid = in.readInt();
        ischange = in.readByte() != 0;
    }

    public static final Creator<TestResultBean> CREATOR = new Creator<TestResultBean>() {
        @Override
        public TestResultBean createFromParcel(Parcel in) {
            return new TestResultBean(in);
        }

        @Override
        public TestResultBean[] newArray(int size) {
            return new TestResultBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeInt(weight);
        dest.writeInt(height);
        dest.writeString(level);
        dest.writeString(count);
        dest.writeString(usernick);
        dest.writeInt(age);
        dest.writeString(userid);
        dest.writeString(testtime);
        dest.writeInt(levelid);
        dest.writeByte((byte) (ischange ? 1 : 0));
    }
}
