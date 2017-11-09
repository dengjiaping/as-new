package com.jkpg.ruchu.bean;

/**
 * Created by qindi on 2017/7/17.
 */

public class LoginQQBean {

    /**
     * uid : E7633F9EF841CE21ABCB1CC61F2DE8CA
     * state : 200
     * mess : 查询成功
     * u_id : 48
     * backMess : {"appqquid":"E7633F9EF841CE21ABCB1CC61F2DE8CA","flagStatus":"1","userId":"5fcff1a6-6906-11e7-b296-883fd3cfd36b","uNick":"庄逍","loginCount":"2","uImgurl":"https://q.qlogo.cn/qqapp/1106182299/E7633F9EF841CE21ABCB1CC61F2DE8CA/100","uId":"48"}
     */

    public String uid;
    public int state;
    public String mess;
    public String u_id;
    public BackMessBean backMess;
    public String nameid;
    public String usersign;

    public static class BackMessBean {
        /**
         * appqquid : E7633F9EF841CE21ABCB1CC61F2DE8CA
         * flagStatus : 1
         * userId : 5fcff1a6-6906-11e7-b296-883fd3cfd36b
         * uNick : 庄逍
         * loginCount : 2
         * uImgurl : https://q.qlogo.cn/qqapp/1106182299/E7633F9EF841CE21ABCB1CC61F2DE8CA/100
         * uId : 48
         */

        public String appqquid;
        public String flagStatus;
        public String userId;
        public String uNick;
        public String loginCount;
        public String uImgurl;
        public String uId;
    }
}
