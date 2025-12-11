package com.tp.accessguard.service;

import com.tp.accessguard.model.SysUser;

import java.util.Optional;

public interface AuthService {
    Optional<SysUser> login(String username, String passwordHash);
}
