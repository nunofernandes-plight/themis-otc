package com.oxchains.themis.user.domain;

import com.oxchains.themis.repo.entity.User;
import com.oxchains.themis.repo.entity.UserRelation;

/**
 * @author oxchains
 * @time 2017-11-28 10:20
 * @name UserRelationInfo
 * @desc:
 */
public class UserRelationInfo extends User {
    private UserRelation userRelation;

    public UserRelationInfo(User user) {
        super(user);
    }

    public UserRelationInfo() {
    }

    public UserRelation getUserRelation() {
        return userRelation;
    }

    public void setUserRelation(UserRelation userRelation) {
        this.userRelation = userRelation;
    }
}
