package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.SectorDao;
import com.tp.accessguard.model.Sector;
import com.tp.accessguard.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcSectorDao implements SectorDao {

    @Override
    public Optional<Sector> findByCodeActive(String code) {
        String sql = "SELECT id, name, code, is_active FROM sector WHERE code=? AND is_active=1";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Sector s = new Sector();
                    s.setId(rs.getLong("id"));
                    s.setName(rs.getString("name"));
                    s.setCode(rs.getString("code"));
                    s.setActive(rs.getBoolean("is_active"));
                    return Optional.of(s);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
}
