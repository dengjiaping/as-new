package com.jkpg.ruchu.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by qindi on 2017/8/23.
 */

public class MySmsPrivsteBean {

    /**
     * managerImg : headImg/100343110.png
     * list : [{"createtime":"2017-08-23 10:36:59","content":"2","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:43:39","content":"你好","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:43:45","content":"逗你玩","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:43:50","content":"helloqqq","site":"0","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:44:59","content":"好好","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:46:08","content":"你们","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:46:20","content":"hello","site":"0","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:52:06","content":"光棍节快乐济南","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:52:27","content":"🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄🙄","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:52:48","content":"😏😏😏😏😏😏😏😏😏😏😏","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:52:50","content":"🤓","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:52:51","content":"😅","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:52:59","content":"😋😜😝","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"},{"createtime":"2017-08-23 10:53:02","content":"☹️😤😶😟🤡🤠😡😡😡","site":"1","userImg":"headImg/243Z86UZOPV6.jpg"}]
     * success : true
     */


    public boolean success;
    public List<ListBean> list;


    public static class ListBean implements MultiItemEntity {
        public static final int FROM_USER_MSG = 0;//接收消息类型
        public static final int TO_USER_MSG = 1;//发送消息类型
        /**
         * createtime : 2017-08-23 10:36:59
         * content : 2
         * site : 1
         * userImg : headImg/243Z86UZOPV6.jpg
         */
        public String managerImg;
        public String userImg;
        public String createtime;
        public String content;
        public String site;

        @Override
        public int getItemType() {
            return Integer.parseInt(site);
        }

        public ListBean(String managerImg, String userImg, String createtime, String content, String site) {
            this.managerImg = managerImg;
            this.userImg = userImg;
            this.createtime = createtime;
            this.content = content;
            this.site = site;
        }
    }
}
