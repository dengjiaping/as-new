package com.jkpg.ruchu.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by qindi on 2017/6/23.
 */

public class ChatListBean implements MultiItemEntity {
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_USER_IMG = 2;//接收消息类型
    public static final int TO_USER_IMG = 3;//发送消息类型
    public static final int NOTICE = 4;//发送消息类型
    private int itemType;
    public String content;
    public String image;
    public String time;


    public ChatListBean(int itemType, String time, String image, String content) {
        this.itemType = itemType;
        this.time = time;
        this.image = image;
        this.content = content;
    }


    public ChatListBean(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
