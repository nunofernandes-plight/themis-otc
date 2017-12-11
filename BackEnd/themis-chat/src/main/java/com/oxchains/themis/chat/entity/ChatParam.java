package com.oxchains.themis.chat.entity;

/**
 * Created by xuqi on 2017/12/8.
 */
public interface ChatParam {
    enum MsgType{
        USER_CHAT(1,"用户聊天"),HEALTH_CHECK(2,"健康监测"),SYSTEM_INFO(3,"系统消息"),CUSTOMER_SERVICE(4,"客服");
        private Integer status;
        private String desc;
        MsgType(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }
        public Integer getStatus() {

            return status;
        }
        public String getDesc() {
            return desc;
        }
    }
    enum HealthType{
        CHAT_HEALTH(1,"聊天心跳"),UPLOAD_TXID_HEALTH(2,"上传交易id通知心跳");
        private Integer status;
        private String desc;

        HealthType(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }
    }
}
