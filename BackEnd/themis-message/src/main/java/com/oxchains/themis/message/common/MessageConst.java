package com.oxchains.themis.message.common;

/**
 * @author luoxuri
 * @create 2017-11-07 17:13
 **/
public interface MessageConst {

    /**
     * 集合大小
     */
    enum ListSize{
        ZERO(0, "0"), ONE(1, "1"), TWO(2, "2");
        private Integer value;
        private String name;
        ListSize(Integer value, String name) {
            this.value = value;
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public Integer getValue() {
            return value;
        }
    }

    /**
     * 用于判断的常量
     */
    enum Constant{
        ZERO(0), ONE(1), TWO(2), FIVE(5), TEN(10), FIFTEEN(15), HUNDRED(100), TWO_THOUSAND(2000), FIVE_THOUSAND(5000);
        private Integer value;

        Constant(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    enum ReadStatus{
        ONE(1, "未读"), TWO(2, "已读"), THREE(3, "删除");
        private Integer status;
        private String name;

        ReadStatus(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }
    }

    enum MessageType{
        ONE(1, "系统信息"), TWO(2, "私信"), THREE(3, "公告");
        private Integer status;
        private String name;
        MessageType(Integer status, String name) {
            this.status = status;
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }
    }

}
