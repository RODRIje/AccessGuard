package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.PersonDao;
import com.tp.accessguard.model.Person;
import com.tp.accessguard.model.enums.PersonStatus;
import com.tp.accessguard.util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPersonDao implements PersonDao {

    @Override
    public List<Person> findAll() {
        String sql = "SELECT id, full_name, document_id, badge_id, status FROM person ORDER BY id";
        List<Person> result = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar personas", e);
        }
        return result;
    }

    @Override
    public Optional<Person> findById(long id) {
        String sql = "SELECT id, full_name, document_id, badge_id, status FROM person WHERE id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar persona por id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Person> findByBadgeId(String badgeId) {
        String sql = "SELECT id, full_name, document_id, badge_id, status FROM person WHERE badge_id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, badgeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar persona por badge", e);
        }
        return Optional.empty();
    }

    @Override
    public void insert(Person p) {
        String sql = "INSERT INTO person(full_name, document_id, badge_id, status) VALUES (?,?,?,?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getFullName());
            ps.setString(2, p.getDocumentId());
            ps.setString(3, p.getBadgeId());
            ps.setString(4, p.getStatus().name().toLowerCase()); // ACTIVE -> "active"

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar persona", e);
        }
    }

    @Override
    public void update(Person p) {
        String sql = "UPDATE person SET full_name=?, document_id=?, badge_id=?, status=? WHERE id=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getFullName());
            ps.setString(2, p.getDocumentId());
            ps.setString(3, p.getBadgeId());
            ps.setString(4, p.getStatus().name().toLowerCase());
            ps.setLong(5, p.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar persona", e);
        }
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM person WHERE id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar persona", e);
        }
    }

    // ---- Helper ----
    private Person mapRow(ResultSet rs) throws SQLException {
        Person p = new Person();
        p.setId(rs.getLong("id"));
        p.setFullName(rs.getString("full_name"));
        p.setDocumentId(rs.getString("document_id"));
        p.setBadgeId(rs.getString("badge_id"));
        String statusStr = rs.getString("status"); // "active"/"blocked"
        p.setStatus(PersonStatus.valueOf(statusStr.toUpperCase()));
        return p;
    }
}

