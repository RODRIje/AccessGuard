package com.tp.accessguard.dao;

import com.tp.accessguard.model.AccessEvent;

import java.time.LocalDateTime;

public interface AccessEventDao {
    void logEvent(long personId, long sectorId, LocalDateTime ts, boolean allowed, String reason);
}
