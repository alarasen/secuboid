/*
 *  Secuboid: Lands and Protection plugin for Minecraft server
 *  Copyright (C) 2014 Tabinol
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package app.secuboid.core.storage;

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.config.Config;
import app.secuboid.core.messages.Log;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static app.secuboid.core.config.Config.config;
import static java.util.logging.Level.SEVERE;

public class ConnectionManager {

    // Also used in Config class
    public static final String DRIVER_HSQLDB = "jdbc:hsqldb";
    public static final String DRIVER_MARIADB = "jdbc:mariadb";

    private static final String DRIVER_CLASS_NAME_HSQLDB = "org.hsqldb.jdbc.JDBCDriver";
    private static final String DRIVER_CLASS_NAME_MARIADB = "org.mariadb.jdbc.Driver";

    private static final String POOL_NAME_HSQLDB = "Secuboid-HikariPool-HSQL";
    private static final String POOL_NAME_MARIADB = "Secuboid-HikariPool-ariaDB";

    private static final String TAG_PLUGIN_PATH = "{{plugin-path}}";

    private static HikariDataSource dataSource;
    private static boolean isLocalHSQL = false;

    private ConnectionManager() {
    }

    protected static void init() {
        Config config = config();
        JavaPlugin javaPlugin = SecuboidImpl.getJavaPLugin();
        String dataFolderStr = javaPlugin.getDataFolder().getAbsolutePath();
        String url = config.databaseUrl().replace(TAG_PLUGIN_PATH, dataFolderStr);
        String user = config.databaseUser();
        String password = config.databasePassword();
        HikariConfig hikariConfig = new HikariConfig();

        if (url.contains(DRIVER_HSQLDB)) {
            if (url.startsWith("jdbc:hsqldb:file:")) {
                isLocalHSQL = true;
            }

            hikariConfig.setPoolName(POOL_NAME_HSQLDB);
            hikariConfig.setDriverClassName(DRIVER_CLASS_NAME_HSQLDB);
            hikariConfig.addDataSourceProperty("sql.syntax_mys", true);

        } else if (url.contains(DRIVER_MARIADB)) {
            hikariConfig.setPoolName(POOL_NAME_MARIADB);
            hikariConfig.setDriverClassName(DRIVER_CLASS_NAME_MARIADB);
            hikariConfig.setAutoCommit(true);
        } else {
            isLocalHSQL = false;
            throw new SecuboidRuntimeException("Database driver \"mariadb\" (also for MySQL support) and \"hsqldb\" are the only supported");
        }

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        dataSource = new HikariDataSource(hikariConfig);
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
                    Log.log().log(SEVERE, "Unable to shutdown the local database", e);
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
