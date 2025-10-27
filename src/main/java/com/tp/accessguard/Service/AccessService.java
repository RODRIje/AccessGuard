package com.tp.accessguard.Service;

import java.time.LocalDateTime;

public interface AccessService {
    Result checkAccess(String badgeId, String sectorCode, LocalDateTime ts);

    class Result {
        public final boolean allow;
        public final String reason;
        public final long personId;
        public final long sectorId;

        public Result(boolean allow, String reason, long personId, long sectorId) {
            this.allow = allow; this.reason = reason; this.personId = personId; this.sectorId = sectorId;
        }
    }
}
