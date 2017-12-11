package com.oxchains.themis;

import com.oxchains.themis.zuul.AccessFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * @author ccl
 * @time 2017-11-09 16:45
 * @name ZuulApplication
 * @desc:
 */

@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableEurekaClient
@EnableZuulProxy
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class,args);
    }

    @Bean
    public AccessFilter accessFilter(){
        return new AccessFilter();
    }
}
