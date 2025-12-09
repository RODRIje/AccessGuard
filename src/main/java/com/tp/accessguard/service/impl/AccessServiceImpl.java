package com.tp.accessguard.service.impl;

import com.tp.accessguard.dao.AccessEventDao;
import com.tp.accessguard.dao.PermissionDao;
import com.tp.accessguard.dao.PersonDao;
import com.tp.accessguard.dao.SectorDao;
import com.tp.accessguard.model.Person;
import com.tp.accessguard.model.Sector;
import com.tp.accessguard.model.enums.PersonStatus;
import com.tp.accessguard.service.AccessResult;
import com.tp.accessguard.service.AccessService;

import java.time.LocalDateTime;
import java.util.Optional;

public class AccessServiceImpl implements AccessService {

    private final PersonDao personDao;
    private final SectorDao sectorDao;
    private final PermissionDao permissionDao;
    private final AccessEventDao eventDao;

    public AccessServiceImpl(PersonDao personDao,
                             SectorDao sectorDao,
                             PermissionDao permissionDao,
                             AccessEventDao eventDao) {
        this.personDao = personDao;
        this.sectorDao = sectorDao;
        this.permissionDao = permissionDao;
        this.eventDao = eventDao;
    }

    @Override
    public AccessResult checkAccess(String badgeId, String sectorCode, LocalDateTime timestamp) {

        String trimmedBadge = badgeId == null ? "" : badgeId.trim();
        String trimmedSector = sectorCode == null ? "" : sectorCode.trim();

        if (trimmedBadge.isEmpty() || trimmedSector.isEmpty()) {
            return new AccessResult(false, "Badge y sector son obligatorios.");
        }

        Optional<Person> op = personDao.findByBadgeId(trimmedBadge);
        Optional<Sector> os = sectorDao.findByCode(trimmedSector);

        // Si no existe persona
        if (op.isEmpty()) {
            return new AccessResult(false, "Badge no registrado en el sistema.");
        }
        Person person = op.get();

        // Si no existe sector
        if (os.isEmpty()) {
            return new AccessResult(false, "Sector no registrado en el sistema.");
        }
        Sector sector = os.get();

        // Persona bloqueada
        if (person.getStatus() == PersonStatus.BLOCKED) {
            eventDao.logEvent(person.getId(), sector.getId(), timestamp, false, "Persona bloqueada.");
            return new AccessResult(false, "Acceso denegado: la persona está bloqueada.");
        }

        // Sector inactivo
        if (!sector.isActive()) {
            eventDao.logEvent(person.getId(), sector.getId(), timestamp, false, "Sector inactivo.");
            return new AccessResult(false, "Acceso denegado: el sector está inactivo.");
        }

        // 🔐 NUEVO: verificacion de permisos persona–sector
        boolean hasPermission = permissionDao.hasValidPermission(person.getId(), sector.getId(), timestamp);
        if (!hasPermission) {
            eventDao.logEvent(person.getId(), sector.getId(), timestamp, false, "Sin permiso para este sector.");
            return new AccessResult(false, "Acceso denegado: la persona no tiene permiso para este sector.");
        }

        // Si paso todos los checkeos
        eventDao.logEvent(person.getId(), sector.getId(), timestamp, true, "Acceso permitido (reglas básicas).");
        return new AccessResult(true, "Acceso permitido.");
    }
}


