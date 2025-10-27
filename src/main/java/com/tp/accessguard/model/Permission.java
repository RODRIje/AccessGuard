package com.tp.accessguard.model;

import java.time.LocalDateTime;

public class Permission {
    private long id;
    private long personId;
    private long sectorId;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private String createdBy;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getPersonId() { return personId; }
    public void setPersonId(long personId) { this.personId = personId; }
    public long getSectorId() { return sectorId; }
    public void setSectorId(long sectorId) { this.sectorId = sectorId; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public boolean isActiveAt(LocalDateTime ts) {
        boolean afterStart = !ts.isBefore(validFrom);
        boolean beforeEnd = (validTo == null) || !ts.isAfter(validTo);
        return afterStart && beforeEnd;
    }
    }
