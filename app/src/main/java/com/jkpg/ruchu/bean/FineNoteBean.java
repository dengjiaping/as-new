package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/6/5.
 */

public class FineNoteBean {
    public String title;
    public String time;
    public String eulogy;
    public String reply;
    public String image;
    public String body;
    public String url;

    public FineNoteBean(String title, String time, String eulogy, String reply, String image, String body, String url) {
        this.title = title;
        this.time = time;
        this.eulogy = eulogy;
        this.reply = reply;
        this.image = image;
        this.body = body;
        this.url = url;
    }
}
