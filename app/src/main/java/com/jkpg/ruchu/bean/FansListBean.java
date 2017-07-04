package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/5/27.
 */

public class FansListBean {
    public String photo;
    public String name;
    public String body;
    public boolean isFollow;

    public FansListBean(String photo, String name, String body, boolean isFollow) {
        this.photo = photo;
        this.name = name;
        this.body = body;
        this.isFollow = isFollow;
    }
}
