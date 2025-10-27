package com.tp.accessguard.Service.impl;

import com.tp.accessguard.Service.AccessService;
import com.tp.accessguard.dao.*;
import com.tp.accessguard.model.*;
import com.tp.accessguard.model.enums.*;


import java.time.LocalDateTime;
import java.util.Optional;


public class AccessServiceImpl implements AccessService {
    private final PersonDao personDao;
    private final SectorDao sectorDao;
    private final PermissionDao permissionDao;
    private final AccessRuleDao accessRuleDao;
    private final AccessEventDao accessEventDao;

    public AccessServiceImpl(PersonDao p, SectorDao s, PermissionDao perm, AccessRuleDao rule, AccessEventDao ev) {
        this.personDao = p; this.sectorDao = s; this.permissionDao = perm; this.accessRuleDao = rule; this.accessEventDao = ev;
    }

    @Override
    public Result checkAccess(String badgeId, String sectorCode, LocalDateTime ts) {
        Optional<Person> op = personDao.findByBadgeId(badgeId);
        if (op.isEmpty() || op.get().getStatus() == PersonStatus.BLOCKED) {
            logEvent(op.map(Person::getId).orElse(0L), 0L, ts, EventResult.DENY, "badge not found/blocked");
            return new Result(false, "badge not found/blocked", op.map(Person::getId).orElse(0L), 0L);
        }
        Person p = op.get();

        Optional<Sector> os = sectorDao.findByCodeActive(sectorCode);
        if (os.isEmpty()) {
            logEvent(p.getId(), 0L, ts, EventResult.DENY, "sector not found/inactive");
            return new Result(false, "sector not found/inactive", p.getId(), 0L);
        }
        long sectorId = os.get().getId();

        boolean direct = permissionDao.hasDirectPermission(p.getId(), sectorId, ts);
        boolean byRule = accessRuleDao.isAllowedByRoleAndTime(p.getId(), sectorId, ts);

        boolean allow = direct || byRule;
        String reason = allow ? (direct ? "direct permission" : "role rule") : "no permission";
        logEvent(p.getId(), sectorId, ts, allow ? EventResult.ALLOW : EventResult.DENY, reason);
        return new Result(allow, reason, p.getId(), sectorId);
    }

    private void logEvent(long personId, long sectorId, LocalDateTime ts, EventResult result, String reason) {
        AccessEvent ev = new AccessEvent();
        ev.setPersonId(personId); ev.setSectorId(sectorId);
        ev.setTs(ts); ev.setResult(result); ev.setReason(reason);
        accessEventDao.save(ev);
    }
}

