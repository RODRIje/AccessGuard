package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.AccessEventDao;
import com.tp.accessguard.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class JdbcAccessEventDao implements AccessEventDao {

    @Override
    public void logEvent(long personId, long sectorId, LocalDateTime ts, boolean allowed, String reason) {
        String sql = "INSERT INTO access_event(person_id, sector_id, ts, result, reason) " +
                "VALUES (?,?,?,?,?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, personId);
            ps.setLong(2, sectorId);
            ps.setTimestamp(3, Timestamp.valueOf(ts));
            ps.setString(4, allowed ? "allow" : "deny");
            ps.setString(5, reason);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

            throw new RuntimeException("Error al registrar evento de acceso", e);
        }
    }
}

