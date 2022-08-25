/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.secuboid.core.storage;

import static java.util.logging.Level.SEVERE;
import static me.tabinol.secuboid.core.config.Config.config;
import static me.tabinol.secuboid.core.messages.Log.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.bukkit.plugin.java.JavaPlugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import me.tabinol.secuboid.api.exceptions.SecuboidRuntimeException;
import me.tabinol.secuboid.core.SecuboidImpl;
import me.tabinol.secuboid.core.config.Config;

public class ConnectionManager {

    // Also used in Config class
    public static final String DRIVER_HSQLDB = "hsqldb";
    public static final String DRIVER_MARIADB = "mariadb";

    private static final String DATASOURCE_CLASS_NAME_HSQLDB = "org.hsqldb.jdbc.JDBCDataSource";
    private static final String DATASOURCE_DRIVER_CLASS_NAME_MARIADB = "org.mariadb.jdbc.Driver";

    private static final String POOL_NAME_HSQLDB = "Secuboid-HikariPool-HSQL";
    private static final String POOL_NAME_MARIADB = "Secuboid-HikariPool-ariaDB";

    private static final String PROPERTY_NAME_POOL_NAME = "poolName";
    private static final String PROPERTY_NAME_CLASS_NAME = "dataSourceClassName";
    private static final String PROPERTY_NAME_USER = "dataSource.user";
    private static final String PROPERTY_NAME_PASSWORD = "dataSource.password";
    private static final String PROPERTY_NAME_DATABASE_NAME = "dataSource.databaseName";
    private static final String PROPERTY_NAME_SERVER_NAME = "dataSource.serverName";
    private static final String PROPERTY_NAME_PORT_NUMBER = "dataSource.portNumber";

    private static final String TAG_PLUGIN_PATH = "{{plugin-path}}";

    private static HikariDataSource dataSource;
    private static boolean isLocalHSQL;

    private ConnectionManager() {
    }

    protected static void init() {
        Config config = config();
        String driver = config.databaseDriver();
        String user = config.databaseUser();
        String password = config.databasePassword();
        JavaPlugin javaPlugin = SecuboidImpl.getJavaPLugin();
        String dataFolderStr = javaPlugin.getDataFolder().getAbsolutePath();
        String database = config.databaseDatabase().replace(TAG_PLUGIN_PATH, dataFolderStr);
        String portStr = Integer.toString(config.databasePortNumber());
        String server = config.databaseServerName();

        switch (driver) {

            case DRIVER_HSQLDB -> {
                Properties props = new Properties();
                props.setProperty(PROPERTY_NAME_POOL_NAME, POOL_NAME_HSQLDB);
                props.setProperty(PROPERTY_NAME_CLASS_NAME, DATASOURCE_CLASS_NAME_HSQLDB);
                props.setProperty(PROPERTY_NAME_DATABASE_NAME, database);
                props.setProperty(PROPERTY_NAME_USER, user);
                props.setProperty(PROPERTY_NAME_PASSWORD, password);

                if (database.startsWith("file:")) {
                    isLocalHSQL = true;
                } else {
                    isLocalHSQL = false;
                    props.setProperty(PROPERTY_NAME_SERVER_NAME, server);
                    props.setProperty(PROPERTY_NAME_PORT_NUMBER, portStr);
                }

                HikariConfig hikariConfig = new HikariConfig(props);
                dataSource = new HikariDataSource(hikariConfig);
            }

            case DRIVER_MARIADB -> {
                isLocalHSQL = false;
                dataSource = new HikariDataSource();
                dataSource.setPoolName(POOL_NAME_MARIADB);
                dataSource.setDriverClassName(DATASOURCE_DRIVER_CLASS_NAME_MARIADB);
                String url = String.format("jdbc:mariadb://%s:%s/%s", server, portStr, database);
                dataSource.setJdbcUrl(url);
                dataSource.addDataSourceProperty("user", user);
                dataSource.addDataSourceProperty("password", password);
                dataSource.setAutoCommit(true);
            }

            default -> {
                isLocalHSQL = false;
                throw new SecuboidRuntimeException(
                        "Database driver \"mariadb\" (also for MySQL support) and \"hsqldb\" are the only supported");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (!dataSource.isClosed()) {
            if (isLocalHSQL) {
                try (Connection conn = getConnection()) {
                    shutdownLocalHSQL(conn);
                } catch (SQLException e) {
                    log().log(SEVERE, "Unable to shutdown the local database", e);
                }
            }
            dataSource.close();
        }
    }

    private static void shutdownLocalHSQL(Connection conn) throws SQLException {
        String sql = "SHUTDOWN";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
    }
}
