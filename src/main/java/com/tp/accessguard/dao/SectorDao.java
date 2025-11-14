package com.tp.accessguard.dao;

import com.tp.accessguard.model.Sector;

import java.util.List;
import java.util.Optional;

public interface SectorDao {

    List<Sector> findAll();

    Optional<Sector> findById(long id);

    Optional<Sector> findByCode(String code);

    void insert(Sector sector);

    void update(Sector sector);

    void deleteById(long id);
}
