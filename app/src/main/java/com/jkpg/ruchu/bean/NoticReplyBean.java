package com.jkpg.ruchu.bean;


import com.jkpg.ruchu.widget.nineview.ImageInfo;

import java.util.List;

/**
 * Created by qindi on 2017/6/15.
 */

public class NoticReplyBean {
    public String id;
    public String image;
    public String name;
    public String floor;
    public String baby;
    public String body;
    public List<ImageInfo> imageInfos;
    public String time;
    public boolean isLove;
    public String love;
    public String reply;
    public boolean isReply;
    public String toName;
    public String toFloor;
    public String toTime;
    public String toBody;
    public List<ImageInfo> toImageInfos;

    public NoticReplyBean(String id, String image, String name, String floor, String baby, String body, List<ImageInfo> imageInfos, String time,boolean isLove, String love, String reply, boolean isReply, String toName, String toFloor, String toTime, String toBody, List<ImageInfo> toImageInfos) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.floor = floor;
        this.baby = baby;
        this.body = body;
        this.imageInfos = imageInfos;
        this.time = time;
        this.isLove = isLove;
        this.love = love;
        this.reply = reply;
        this.isReply = isReply;
        this.toName = toName;
        this.toFloor = toFloor;
        this.toTime = toTime;
        this.toBody = toBody;
        this.toImageInfos = toImageInfos;
    }
}
