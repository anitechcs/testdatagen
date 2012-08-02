/**
 * Copyright (C) 2012 Enterprise System Solutions (P) Ltd. All rights reserved.
 *
 * This file is part of DATA Gen. http://testdatagen.sourceforge.net/
 *
 * DATA Gen is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DATA Gen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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
