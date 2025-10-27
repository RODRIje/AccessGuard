package com.tp.accessguard.dao;

import com.tp.accessguard.model.Person;

import java.util.Optional;

public interface PersonDao {
    Optional<Person> findByBadgeId(String badgeId);
}


