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
package app.secuboid.core.storage.tables;

import app.secuboid.api.reflection.TableRegistered;
import app.secuboid.api.storage.tables.Table;
import app.secuboid.api.utilities.DbUtils;
import app.secuboid.core.storage.rows.LandRow;
import app.secuboid.core.storage.types.LandType;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static app.secuboid.api.utilities.DbUtils.getNullable;
import static app.secuboid.core.config.Config.config;
import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;
import static java.util.logging.Level.WARNING;

@TableRegistered(row = LandRow.class)
public class LandTable implements Table<LandRow> {

    public static final String TYPE_WORLD_LAND = "W";
    public static final String TYPE_AREA_LAND = "L";
    public static final String TYPE_CONFIGURATION_SET = "S";

    // Needed for automatic table create
    public static final String CREATE_TABLE_SQL = ""
            + "CREATE TABLE IF NOT EXISTS %1$sland ("
            + " id BIGINT NOT NULL {{AUTOINCREMENT}},"
            + " name VARCHAR(45) NOT NULL,"
            + " type CHAR(1) NOT NULL,"
            + " parent_id BIGINT NULL,"
            + " PRIMARY KEY (id),"
            + " CONSTRAINT fk_land_parent_id FOREIGN KEY (parent_id) REFERENCES %1$sland (id)"
            + ")";

    @Override
    public @NotNull Set<LandRow> selectAll(@NotNull Connection conn) throws SQLException {
        String prefix = config().databasePrefix();
        String sql = format("SELECT id, name, type, parent_id FROM %1$sland", prefix);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Set<LandRow> result = new HashSet<>();

                while (rs.next()) {
                    long id = rs.getInt("id");
                    String name = rs.getString("name");

                    try {
                        LandType type = LandType.fromValue(rs.getString("type"));
                        Long parentId = getNullable(rs, "parent_id", rs::getLong);
                        result.add(new LandRow(id, name, type, parentId));
                    } catch (IllegalArgumentException e) {
                        log().log(WARNING, e, () -> format("Unable to load a land from the database [id=%s, name=%s]", id, name));
                    }
                }

                return result;
            }
        }
    }

    @Override
    public @NotNull LandRow insert(@NotNull Connection conn, @NotNull LandRow landRow) throws SQLException {
        String prefix = config().databasePrefix();
        String sql = format("INSERT INTO %1$sland(name, type, parent_id) VALUES(?, ?)", prefix);

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, landRow.name());
            stmt.setString(2, landRow.type().value);
            DbUtils.setNullable(stmt, 3, landRow.parentId(), stmt::setLong);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                return new LandRow(rs.getLong(1), landRow.name(), landRow.type(), landRow.parentId());
            }
        }
    }

    @Override
    public @NotNull LandRow update(@NotNull Connection conn, @NotNull LandRow landRow) throws SQLException {
        String prefix = config().databasePrefix();
        String sql = format("UPDATE %1$sland SET name = ?, type = ?, parent_id = ? WHERE id = ?", prefix);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, landRow.name());
            stmt.setString(2, landRow.type().value);
            DbUtils.setNullable(stmt, 3, landRow.parentId(), stmt::setLong);
            stmt.setLong(4, landRow.getId());

            stmt.executeUpdate();
        }

        return landRow;
    }

    @Override
    public @NotNull LandRow delete(@NotNull Connection conn, @NotNull LandRow landRow) throws SQLException {
        String prefix = config().databasePrefix();
        String sql = format("DELETE FROM %1$sland WHERE id = ?", prefix);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, landRow.getId());
            stmt.executeUpdate();
        }

        return landRow;
    }
}
