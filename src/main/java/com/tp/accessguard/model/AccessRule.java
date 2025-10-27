package com.tp.accessguard.model;

public class AccessRule {
    private long id;
    private long roleId;
    private long sectorId;
    private String timeWindow; // "Mon-Fri 06:00-22:00"
    private boolean allowed;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getRoleId() { return roleId; }
    public void setRoleId(long roleId) { this.roleId = roleId; }
    public long getSectorId() { return sectorId; }
    public void setSectorId(long sectorId) { this.sectorId = sectorId; }
    public String getTimeWindow() { return timeWindow; }
    public void setTimeWindow(String timeWindow) { this.timeWindow = timeWindow; }
    public boolean isAllowed() { return allowed; }
    public void setAllowed(boolean allowed) { this.allowed = allowed; }
}
