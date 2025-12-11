package com.tp.accessguard.dao;

import com.tp.accessguard.model.SysUser;

import java.util.Optional;

public interface SysUserDao {
    Optional<SysUser> findByUsername(String username);
}
