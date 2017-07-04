package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/6/5.
 */

public class PlateBean {
    public String ImageUrl;
    public String title;
    public String body;
    public String number;

    public PlateBean(String imageUrl, String title, String body, String number) {
        ImageUrl = imageUrl;
        this.title = title;
        this.body = body;
        this.number = number;
    }

    public PlateBean(String title) {
        this.title = title;
    }
}
