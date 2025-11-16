package com.tp.accessguard.service;

import java.time.LocalDateTime;

public interface AccessService {

    AccessResult checkAccess(String badgeId, String sectorCode, LocalDateTime timestamp);
}

