package com.oxchains.themis.notice.common;

import org.apache.commons.lang3.StringUtils;

/**
 * @author luoxuri
 * @create 2017-11-03 19:11
 **/
public interface NoticeConst {
    /**
     * 公告类型
     */
    enum NoticeType{
        BUY(1L, "购买"), SELL(2L, "出售");
        private Long status;
        private String name;
        NoticeType(Long status, String name){
            this.status = status;
            this.name = name;
        }
        public Long getStatus() {
            return status;
        }
        public String getName() {
            return name;
        }
    }

    /**
     * 搜索类型
     */
    enum SearchType{
        ONE(1L, "搜公告"), TWO(2L, "搜用户");
        private Long status;
        private String name;

        SearchType(Long status, String name) {
            this.status = status;
            this.name = name;
        }

        public Long getStatus() {
            return status;
        }

        public String getName() {
            return name;
        }
    }
}
