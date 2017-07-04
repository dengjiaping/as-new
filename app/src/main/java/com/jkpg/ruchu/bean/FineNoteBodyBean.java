package com.jkpg.ruchu.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by qindi on 2017/6/17.
 */

public class FineNoteBodyBean implements MultiItemEntity {
    public static final int TITLE = 0;
    public static final int BODY = 1;
    public static final int IMG = 2;
    private int itemType;
    public String content;


    public FineNoteBodyBean(int itemType, String content) {
        this.itemType = itemType;
        this.content = content;
    }


    public FineNoteBodyBean(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
