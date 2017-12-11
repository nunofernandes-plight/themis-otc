package com.oxchains.themis.zuul.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ccl
 * @time 2017-11-09 16:50
 * @name RestController
 * @desc:
 */
@RestController
public class ZuulController {
    @RequestMapping(value = "/info")
    @ResponseBody
    public String info(){
        return "This is zuul!";
    }
}
