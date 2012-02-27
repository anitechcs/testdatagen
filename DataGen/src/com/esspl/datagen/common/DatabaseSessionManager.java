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
 * session scoped
 */
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
