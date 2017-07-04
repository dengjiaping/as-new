package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/6/5.
 */

public class PlateDetailBean {
    public String id;
    public String image;
    public String name;
    public boolean fine;
    public String time;
    public String eulogy;
    public String reply;
    public String title;
    public String body;
    public String url;

    public PlateDetailBean(String image, String name, boolean fine, String time, String eulogy, String reply, String title, String body, String url) {
        this.image = image;
        this.name = name;
        this.fine = fine;
        this.time = time;
        this.eulogy = eulogy;
        this.reply = reply;
        this.title = title;
        this.body = body;
        this.url = url;
    }
}
