package com.oxchains.themis.user.domain;

/**
 * @author ccl
 * @time 2017-10-20 10:38
 * @name UserToken
 * @desc:
 */
public class UserToken {

    public UserToken(){}
    public UserToken(String username,String token){
        this.username=username;
        this.token=token;
    }

    String username;
    String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
