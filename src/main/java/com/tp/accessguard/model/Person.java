package com.tp.accessguard.model;

import com.tp.accessguard.model.enums.PersonStatus;

public class Person {
    private long id;
    private String fullName;
    private String documentId;
    private String badgeId;
    private PersonStatus status = PersonStatus.ACTIVE;

    public Person(long id, String fullName, String documentId, String badgeId, PersonStatus status) {
        this.id = id;
        this.fullName = fullName;
        this.documentId = documentId;
        this.badgeId = badgeId;
        this.status = status;
    }

    public Person() {
    }

    public long getId() { return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getBadgeId() {
        return badgeId;
    }
    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }
    public PersonStatus getStatus() {
        return status;
    }
    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public void block() {
        this.status = PersonStatus.BLOCKED;
    }
    public void unblock() {
        this.status = PersonStatus.ACTIVE;
    }

    @Override
    public String toString(){
        return fullName;
    }
}
