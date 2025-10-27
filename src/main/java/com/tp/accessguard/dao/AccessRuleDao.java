package com.tp.accessguard.dao;

import java.time.LocalDateTime;

public interface AccessRuleDao {
     boolean isAllowedByRoleAndTime(long personId, long sectorId, LocalDateTime ts);
}
