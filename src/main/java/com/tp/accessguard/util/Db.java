package com.tp.accessguard.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Db {
    private static String URL, USER, PASS;

    static {
        try (InputStream in = Db.class.getResourceAsStream("/config.properties")) {
            Properties p = new Properties();
            p.load(in);
            String host = p.getProperty("db.host","localhost");
            String port = p.getProperty("db.port","3306");
            String db   = p.getProperty("db.name","accessguard");
            USER = p.getProperty("db.user","root");
            PASS = p.getProperty("db.pass","");
            URL  = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false&serverTimezone=UTC";
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar config.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
