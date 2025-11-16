package com.tp.accessguard.service;

public class AccessResult {
    private final boolean allowed;
    private final String message;

    public AccessResult(boolean allowed, String message) {
        this.allowed = allowed;
        this.message = message;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getMessage() {
        return message;
    }
}
