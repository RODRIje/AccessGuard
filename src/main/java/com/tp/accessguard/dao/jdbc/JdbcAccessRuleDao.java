package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.AccessRuleDao;

import java.time.LocalDateTime;

public class JdbcAccessRuleDao implements AccessRuleDao {
    @Override
    public boolean isAllowedByRoleAndTime(long personId, long sectorId, LocalDateTime ts) {
        return false;
    }
}
