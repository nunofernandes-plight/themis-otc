package com.oxchains.themis.notice.domain;/**
 * Created by Luo_xuri on 2017/10/24.
 */
import javax.persistence.*;

/**
 * @author luoxuri
 * @create 2017-10-24 10:44
 **/
@Entity
@Table(name = "country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Override
    public String toString() {
        return "Country{" +
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
