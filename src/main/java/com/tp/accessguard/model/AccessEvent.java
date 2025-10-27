package com.tp.accessguard.model;

import com.tp.accessguard.model.enums.EventResult;

import java.time.LocalDateTime;

public class AccessEvent {
    private long id;
    private long personId;
    private long sectorId;
    private LocalDateTime ts;
    private EventResult result;
    private String reason;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getPersonId() { return personId; }
    public void setPersonId(long personId) { this.personId = personId; }
    public long getSectorId() { return sectorId; }
    public void setSectorId(long sectorId) { this.sectorId = sectorId; }
    public LocalDateTime getTs() { return ts; }
    public void setTs(LocalDateTime ts) { this.ts = ts; }
    public EventResult getResult() { return result; }
    public void setResult(EventResult result) { this.result = result; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
