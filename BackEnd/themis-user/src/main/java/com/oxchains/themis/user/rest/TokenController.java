package com.oxchains.themis.user.rest;

import com.oxchains.themis.common.auth.JwtService;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.repo.entity.User;
import com.oxchains.themis.user.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2017-10-13 10:32
 * @name TokenController
 * @desc:
 */
@RestController
@RequestMapping(value = "/token")
public class TokenController {
    @Resource
    JwtService jwtService;

    @Resource
    UserService userService;

    @PostMapping
    public RestResp token(User user){
        return userService.findUser(user).map(u -> {
          String token = jwtService.generate(u);
          return RestResp.success(token);
        }).orElse(RestResp.fail());
    }
}
