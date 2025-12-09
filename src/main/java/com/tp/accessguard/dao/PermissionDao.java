package com.tp.accessguard.dao;

import com.tp.accessguard.model.Permission;

import java.time.LocalDateTime;
import java.util.List;

public interface PermissionDao {
    boolean hasDirectPermission(long personId, long sectorId, LocalDateTime ts);

    boolean hasValidPermission(long personId, long sectorId, LocalDateTime at);

    List<Permission> findAll();

    void insert(Permission p);

    void delete(long id);
}
