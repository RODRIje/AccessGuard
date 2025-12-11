package com.tp.accessguard.service.impl;

import com.tp.accessguard.dao.SysUserDao;
import com.tp.accessguard.dao.jdbc.JdbcSysUserDao;
import com.tp.accessguard.model.SysUser;
import com.tp.accessguard.service.AuthService;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private final SysUserDao sysUserDao;

    public AuthServiceImpl(){
        this.sysUserDao = new JdbcSysUserDao();
    }

    @Override
    public Optional<SysUser> login(String username, String passwordHash) {
        if (username == null || username.isBlank() || passwordHash == null || passwordHash.isBlank()) {
            return Optional.empty();
        }

        Optional<SysUser> ou = sysUserDao.findByUsername(username.trim());
        if (ou.isEmpty()) {
            return Optional.empty();
        }

        SysUser user = ou.get();

        if (!user.isActive()) {
            return Optional.empty();
        }

        if (!user.getPasswordHash().equals(passwordHash)) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
}
