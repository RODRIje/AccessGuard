package com.tp.accessguard.dao;

import java.time.LocalDateTime;

public interface PermissionDao {
    boolean hasDirectPermission(long personId, long sectorId, LocalDateTime ts);
}
