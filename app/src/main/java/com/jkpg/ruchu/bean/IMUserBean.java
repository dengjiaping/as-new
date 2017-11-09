package com.jkpg.ruchu.bean;

import android.support.annotation.NonNull;

/**
 * Created by qindi on 2017/10/18.
 */

public class IMUserBean implements Comparable<IMUserBean> {
    public String identifier;
    public String name;
    public String url;
    public boolean v;
    public long time;

    public void setNum(long num) {
        this.num = num;
    }

    public long num;
    public String content;

    public IMUserBean(String identifier, String name, String url) {
        this.identifier = identifier;
        this.name = name;
        this.url = url;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(@NonNull IMUserBean o) {
        long timeGap = o.time - time;
        if (timeGap > 0) return 1;
        else if (timeGap < 0) return -1;
        return 0;
    }

    public void setV(boolean v) {
        this.v = v;
    }
}
