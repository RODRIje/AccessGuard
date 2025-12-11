package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.SysUserDao;
import com.tp.accessguard.model.SysUser;
import com.tp.accessguard.model.enums.SystemRole;
import com.tp.accessguard.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcSysUserDao implements SysUserDao {
    @Override
    public Optional<SysUser> findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, system_role, active FROM sys_user WHERE username = ?";

        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SysUser u = new SysUser();
                    u.setId(rs.getLong("id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setSystemRole(SystemRole.valueOf(rs.getString("system_role")));
                    u.setActive(rs.getBoolean("active"));
                    return Optional.of(u);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario del sistema", e);
        }
        return Optional.empty();
    }
}
