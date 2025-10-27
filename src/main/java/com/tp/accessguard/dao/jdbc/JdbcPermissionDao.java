package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.PermissionDao;
import com.tp.accessguard.util.Db;

import java.sql.*;
import java.time.LocalDateTime;

public class JdbcPermissionDao implements PermissionDao {

    @Override
    public boolean hasDirectPermission(long personId, long sectorId, LocalDateTime ts) {
        String sql ="SELECT 1 FROM permission WHERE person_id=? AND sector_id=? AND valid_from <= ? AND (valid_to IS NULL OR valid_to >= ?) LIMIT 1";
        Timestamp t = Timestamp.valueOf(ts);
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, personId); ps.setLong(2, sectorId);
            ps.setTimestamp(3, t); ps.setTimestamp(4, t);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
