package com.jkpg.ruchu.bean;

import java.util.List;

/**
 * Created by qindi on 2017/10/12.
 */

public class InvitationBean {


    /**
     * zfx : 4
     * state : 200
     * mess : 还没有邀请用户
     * array : [{"createtime":"2017-10-11 15:25:06.0","flag":"0","nick":"A星越","imageurl":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIHG0uDdQ0yJaO4JU1bofMo57AmZxjXVuXISvTKSibhtnEzujwAMsicbLU07oYvTePSgr9wytqLSkGQ/0"},{"createtime":"2017-10-11 15:25:06.0","flag":"0","nick":"A星越","imageurl":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIHG0uDdQ0yJaO4JU1bofMo57AmZxjXVuXISvTKSibhtnEzujwAMsicbLU07oYvTePSgr9wytqLSkGQ/0"},{"createtime":"2017-10-11 15:25:06.0","flag":"0","nick":"A星越","imageurl":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIHG0uDdQ0yJaO4JU1bofMo57AmZxjXVuXISvTKSibhtnEzujwAMsicbLU07oYvTePSgr9wytqLSkGQ/0"},{"createtime":"2017-10-11 15:25:06.0","flag":"0","nick":"A星越","imageurl":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIHG0uDdQ0yJaO4JU1bofMo57AmZxjXVuXISvTKSibhtnEzujwAMsicbLU07oYvTePSgr9wytqLSkGQ/0"}]
     * zzc : 0
     */

    public String zfx;
    public int state;
    public String mess;
    public String zzc;
    public List<ArrayBean> array;

    public static class ArrayBean {
        /**
         * createtime : 2017-10-11 15:25:06.0
         * flag : 0
         * nick : A星越
         * imageurl : http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIHG0uDdQ0yJaO4JU1bofMo57AmZxjXVuXISvTKSibhtnEzujwAMsicbLU07oYvTePSgr9wytqLSkGQ/0
         */

        public String createtime;
        public String flag;
        public String nick;
        public String imageurl;
    }
}
