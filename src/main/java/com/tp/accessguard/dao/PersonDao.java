package com.tp.accessguard.dao;

import com.tp.accessguard.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonDao {

    List<Person> findAll();

    Optional<Person> findById(long id);

    Optional<Person> findByBadgeId(String badgeId);

    void insert(Person person);

    void update(Person person);

    void deleteById(long id);
}


