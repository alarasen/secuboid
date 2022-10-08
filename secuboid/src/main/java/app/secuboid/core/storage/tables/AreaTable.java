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
import app.secuboid.core.storage.rows.AreaRow;
import app.secuboid.core.storage.types.AreaType;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

@TableRegistered(row = AreaRow.class, dependsOn = LandTable.class)
@SuppressWarnings({"unused", "java:S1118"})
public class AreaTable implements Table<AreaRow> {

    // Needed for automatic table create
    public static final String CREATE_TABLE_SQL = ""
            + "CREATE TABLE IF NOT EXISTS secuboid_area ("
            + " id BIGINT NOT NULL AUTO_INCREMENT,"
            + " land_id BIGINT NOT NULL,"
            + " type CHAR(1) NOT NULL,"
            + " x1 BIGINT NOT NULL,"
            + " y1 BIGINT NOT NULL,"
            + " z1 BIGINT NOT NULL,"
            + " x2 BIGINT NOT NULL,"
            + " y2 BIGINT NOT NULL,"
            + " z2 BIGINT NOT NULL,"
            + " PRIMARY KEY (id),"
            + " CONSTRAINT fk_area_land_id FOREIGN KEY (land_id) REFERENCES secuboid_land (id)"
            + ")";

    @Override
    public @NotNull Set<AreaRow> selectAll(@NotNull Connection conn) throws SQLException {
        String sql = "SELECT id, land_id, type, x1, y1, z1, x2, y2, z2 FROM secuboid_area";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Set<AreaRow> result = new HashSet<>();

                while (rs.next()) {
                    long id = rs.getLong("id");
                    long landId = rs.getLong("land_id");

                    try {
                        AreaType type = AreaType.fromValue(rs.getString("type"));
                        int x1 = rs.getInt("x1");
                        int y1 = rs.getInt("y1");
                        int z1 = rs.getInt("z1");
                        int x2 = rs.getInt("x2");
                        int y2 = rs.getInt("y2");
                        int z2 = rs.getInt("z2");
                        result.add(new AreaRow(id, landId, type, x1, y1, z1, x2, y2, z2));
                    } catch (IllegalArgumentException e) {
                        log().log(SEVERE, e, () -> format("Unable to load an area from the database [id=%s, " +
                                "landId=%s]", id, landId));
                    }
                }

                return result;
            }
        }
    }

    @Override
    public @NotNull AreaRow insert(@NotNull Connection conn, @NotNull AreaRow areaRow) throws SQLException {
        String sql = "INSERT INTO secuboid_area(land_id, type, x1, y1, z1, x2, y2, z2) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, areaRow.landId());
            stmt.setString(2, areaRow.type().value);
            stmt.setInt(3, areaRow.x1());
            stmt.setInt(4, areaRow.y1());
            stmt.setInt(5, areaRow.z1());
            stmt.setInt(6, areaRow.x2());
            stmt.setInt(7, areaRow.y2());
            stmt.setInt(8, areaRow.z2());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                return new AreaRow(rs.getLong(1), areaRow.landId(), areaRow.type(), areaRow.x1(), areaRow.y1(),
                        areaRow.z1(), areaRow.x2(), areaRow.y2(), areaRow.z2());
            }
        }
    }

    @Override
    public @NotNull AreaRow delete(@NotNull Connection conn, @NotNull AreaRow areaRow) throws SQLException {
        String sql = "DELETE FROM secuboid_area WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, areaRow.id());
            stmt.executeUpdate();
        }

        return areaRow;
    }
}
