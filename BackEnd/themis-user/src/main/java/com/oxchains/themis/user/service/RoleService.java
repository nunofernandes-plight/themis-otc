package com.oxchains.themis.user.service;

import com.oxchains.themis.repo.dao.RoleDao;
import com.oxchains.themis.repo.entity.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2017-10-26 10:19
 * @name RoleService
 * @desc:
 */
@Service
public class RoleService {
    @Resource
    private RoleDao roleDao;

    public Role findById(Long id){
        return roleDao.findById(id);
    }
}
