package com.tp.accessguard.dao.jdbc;

import com.tp.accessguard.dao.PersonDao;
import com.tp.accessguard.model.Person;
import com.tp.accessguard.model.enums.PersonStatus;
import com.tp.accessguard.util.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcPersonDao implements PersonDao {

    @Override
    public Optional<Person> findByBadgeId(String badgeId) {
        String sql = "SELECT id, full_name, document_id, badge_id, status FROM person WHERE badge_id=?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, badgeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Person p = new Person();
                    p.setId(rs.getLong("id"));
                    p.setFullName(rs.getString("full_name"));
                    p.setDocumentId(rs.getString("document_id"));
                    p.setBadgeId(rs.getString("badge_id"));
                    p.setStatus(PersonStatus.valueOf(rs.getString("status").toUpperCase()));
                    return Optional.of(p);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }
}
