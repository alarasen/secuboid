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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionManager {

    // Also used in Config class
    public static final String DRIVER_HSQLDB = "jdbc:hsqldb";
    public static final String DRIVER_MARIADB = "jdbc:mariadb";

    private static final String DRIVER_CLASS_NAME_HSQLDB = "org.hsqldb.jdbc.JDBCDriver";
    private static final String DRIVER_CLASS_NAME_MARIADB = "org.mariadb.jdbc.Driver";

    private static final String POOL_NAME_HSQLDB = "Secuboid-HikariPool-HSQL";
    private static final String POOL_NAME_MARIADB = "Secuboid-HikariPool-MariaDB";

    private static final String TAG_PLUGIN_PATH = "{{plugin-path}}";

    private static @Nullable DataSource dataSource = null;
    private static boolean isLocalHSQL = false;

    private ConnectionManager() {
    }

    public static void init() {
    }

    public static @NotNull Connection getConnection() throws SQLException {
        assert dataSource != null : "The Secuboid datasource is closed or net yet available";

        return dataSource.getConnection();
    }

    private static void shutdownLocalHSQL(@NotNull Connection conn) throws SQLException {
        String sql = "SHUTDOWN";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }
    }
}
