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
package com.esspl.datagen.common;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.esspl.datagen.config.ConnectionProfile;
import com.esspl.datagen.util.JdbcUtils;

/**
 *@author Tapas
 * 
 */
@SuppressWarnings("serial")
public class DatabaseSessionManager implements Serializable {

	private static final Logger log = Logger.getLogger(DatabaseSessionManager.class);
	
    private ConnectionProfile connectionProfile;
    private BasicDataSource dataSource;

    public void destroy() {
        disconnect();
    }

    public void test(String driver, String url, String user, String password) throws Exception {
        Connection conn = JdbcUtils.getConnection(driver, url, user, password);
        JdbcUtils.close(conn);
    }

    public void connect(ConnectionProfile profile) throws Exception {
        test(profile.getDriver(), profile.getUrl(), profile.getUser(), profile.getPassword());
        disconnect();

        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(profile.getDriver());
        dataSource.setUrl(profile.getUrl());
        dataSource.setUsername(profile.getUser());
        dataSource.setPassword(profile.getPassword());

        dataSource.setMaxActive(32);
        dataSource.setMaxIdle(4);
        dataSource.setMaxWait(20 * 1000);
        dataSource.setMaxOpenPreparedStatements(8);

        connectionProfile = profile;
    }

    public void disconnect() {
    	log.info("cleaning up...");
        connectionProfile = null;
        if (null != dataSource)
            try {
                dataSource.close();
                dataSource = null;
            } catch (Exception ex) {
            	log.error("failed to close the data source", ex);
            }
    }

    public ConnectionProfile getConnectionProfile() {
        return connectionProfile;
    }

    public Connection getConnection() throws SQLException {
        return (dataSource != null)?dataSource.getConnection():null;
    }
}
