package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/11/24.
 */

public class TrainInfoListBean {

    /**
     * xhtext : [{"content":"跑马灯one"},{"content":"跑马灯1"},{"content":"跑马灯2"},{"content":"跑马灯4"},{"content":"跑马灯3"}]
     * mess : 查询到要显示的文字
     */

    public String mess;
    public List<XhtextBean> xhtext;

    public static class XhtextBean {
        /**
         * content : 跑马灯one
         */

        public String content;
    }
}
