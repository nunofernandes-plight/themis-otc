package com.oxchains.themis.notice.domain;

import javax.persistence.*;

/**
 * @author luoxuri
 * @create 2017-10-24 10:43
 **/
@Entity
@Table(name = "notice_type")
public class NoticeType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Override
    public String toString() {
        return "NoticeType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
