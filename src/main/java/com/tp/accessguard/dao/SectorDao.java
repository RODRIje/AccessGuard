package com.tp.accessguard.dao;

import com.tp.accessguard.model.Sector;

import java.util.Optional;

public interface SectorDao {
    Optional<Sector> findByCodeActive(String code);
}
