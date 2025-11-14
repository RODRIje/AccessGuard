package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.SectorDao;
import com.tp.accessguard.model.Sector;
import com.tp.accessguard.util.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSectorDao implements SectorDao {

    @Override
    public List<Sector> findAll() {
        String sql = "SELECT id, name, code, is_active FROM sector ORDER BY id";
        List<Sector> result = new ArrayList<>();
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar sectores", e);
        }
        return result;
    }

    @Override
    public Optional<Sector> findById(long id) {
        String sql = "SELECT id, name, code, is_active FROM sector WHERE id = ?";
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
            throw new RuntimeException("Error al buscar sector por id", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Sector> findByCode(String code) {
        String sql = "SELECT id, name, code, is_active FROM sector WHERE code = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar sector por código", e);
        }
        return Optional.empty();
    }

    @Override
    public void insert(Sector s) {
        String sql = "INSERT INTO sector(name, code, is_active) VALUES (?,?,?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getCode());
            ps.setBoolean(3, s.isActive());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    s.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al insertar sector", e);
        }
    }

    @Override
    public void update(Sector s) {
        String sql = "UPDATE sector SET name=?, code=?, is_active=? WHERE id=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getCode());
            ps.setBoolean(3, s.isActive());
            ps.setLong(4, s.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar sector", e);
        }
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM sector WHERE id = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar sector", e);
        }
    }

    private Sector mapRow(ResultSet rs) throws SQLException {
        Sector s = new Sector();
        s.setId(rs.getLong("id"));
        s.setName(rs.getString("name"));
        s.setCode(rs.getString("code"));
        s.setActive(rs.getBoolean("is_active"));
        return s;
    }
}
