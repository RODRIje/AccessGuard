package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.AccessEventDao;
import com.tp.accessguard.model.AccessEvent;
import com.tp.accessguard.model.enums.EventResult;
import com.tp.accessguard.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcAccessEventDao implements AccessEventDao {
    @Override
    public void save(AccessEvent ev) {
        String sql = "INSERT INTO access_event(person_id, sector_id, ts, result, reason) VALUES (?,?,?,?,?)";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, ev.getPersonId());
            ps.setLong(2, ev.getSectorId());
            ps.setTimestamp(3, Timestamp.valueOf(ev.getTs()));
            ps.setString(4, ev.getResult() == null ? EventResult.DENY.name() : ev.getResult().name());
            ps.setString(5, ev.getReason());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
