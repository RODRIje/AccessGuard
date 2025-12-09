package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.PermissionDao;
import com.tp.accessguard.model.Permission;
import com.tp.accessguard.util.Db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean hasValidPermission(long personId, long sectorId, LocalDateTime at) {
        String sql = """
            SELECT 1
            FROM permission
            WHERE person_id = ?
              AND sector_id = ?
              AND valid_from <= ?
              AND (valid_to IS NULL OR valid_to >= ?)
            LIMIT 1
            """;

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            Timestamp ts = Timestamp.valueOf(at);
            ps.setLong(1, personId);
            ps.setLong(2, sectorId);
            ps.setTimestamp(3, ts);
            ps.setTimestamp(4, ts);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // si hay al menos un registro, tiene permiso
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al verificar permisos de acceso", e);
        }
    }

    @Override
    public List<Permission> findAll() {
        String sql = """
        SELECT p.id, pe.full_name, s.name, p.valid_from, p.valid_to,
               p.person_id, p.sector_id
        FROM permission p
        JOIN person pe ON p.person_id = pe.id
        JOIN sector s ON p.sector_id = s.id
        ORDER BY p.id
    """;

        List<Permission> list = new ArrayList<>();

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Permission p = new Permission();
                p.setId(rs.getLong("id"));
                p.setPersonId(rs.getLong("person_id"));
                p.setSectorId(rs.getLong("sector_id"));
                p.setPersonName(rs.getString("full_name"));
                p.setSectorName(rs.getString("name"));
                p.setValidFrom(rs.getTimestamp("valid_from").toLocalDateTime());

                Timestamp to = rs.getTimestamp("valid_to");
                if (to != null) {
                    p.setValidTo(to.toLocalDateTime());
                }

                list.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando permisos", e);
        }

        return list;
    }

    @Override
    public void insert(Permission p) {
        String sql = """
        INSERT INTO permission (person_id, sector_id, valid_from, valid_to, created_by)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, p.getPersonId());
            ps.setLong(2, p.getSectorId());
            ps.setTimestamp(3, Timestamp.valueOf(p.getValidFrom()));

            if (p.getValidTo() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(p.getValidTo()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            ps.setString(5, "admin");

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error insertando permiso", e);
        }
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM permission WHERE id = ?";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando permiso", e);
        }
    }

}
