package com.tp.accessguard.model;

import com.tp.accessguard.model.enums.SystemRole;

public class SysUser {
    private long id;
    private String username;
    private String passwordHash;
    private SystemRole systemRole;
    private boolean active = true;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public SystemRole getSystemRole() { return systemRole; }
    public void setSystemRole(SystemRole systemRole) { this.systemRole = systemRole; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
