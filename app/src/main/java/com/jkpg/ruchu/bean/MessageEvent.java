package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/7/21.
 */

public class MessageEvent {
    /* Additional fields if needed */
    public String message;
    public int num;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, int num) {
        this.message = message;
        this.num = num;
    }
}
