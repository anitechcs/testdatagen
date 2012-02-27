package com.esspl.datagen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * @author Tapas
 *
 */
public class JdbcUtils {

	private static final Logger log = Logger.getLogger(JdbcUtils.class);

    public static Connection getConnection(String driver, String url, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        log.debug("JdbcUtils - getConnection() - Driver loaded. Getting connection...");
        return DriverManager.getConnection(url, user, password);
    }

    public static void close(Connection connection) {
        try {
        	log.debug("JdbcUtils - close() - Closing connection...");
            connection.close();
        } catch (Exception ex) {
        	log.error(ex.getMessage());
        }
    }

    public static void close(Statement statement) {
        try {
        	log.debug("JdbcUtils - close() - Closing Statement...");
            statement.close();
        } catch (Exception ex) {
        	log.error(ex.getMessage());
        }
    }

    public static void close(ResultSet rs) {
        try {
        	log.debug("JdbcUtils - close() - Closing ResultSet...");
            rs.close();
        } catch (Exception ex) {
        	log.error(ex.getMessage());
        }
    }
}
