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
import app.secuboid.core.storage.rows.RecipientRow;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@TableRegistered(
        row = RecipientRow.class,
        createTable = ""
                + "CREATE TABLE IF NOT EXISTS secuboid_parameter_value ("
                + " id BIGINT NOT NULL AUTO_INCREMENT,"
                + " short_name VARCHAR(10) NOT NULL,"
                + " value VARCHAR(45) NULL,"
                + " PRIMARY KEY (id),"
                + " CONSTRAINT parameter_value_short_name_value_unique UNIQUE (short_name, value)"
                + ")"
)
public class RecipientTable implements Table<RecipientRow> {

    @Override
    public @NotNull Set<RecipientRow> selectAll(@NotNull Connection conn) throws SQLException {
        String sql = "SELECT id, short_name, value FROM secuboid_parameter_value";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Set<RecipientRow> result = new HashSet<>();

                while (rs.next()) {
                    long id = rs.getLong("id");
                    String shortName = rs.getString("short_name");
                    String value = rs.getString("value");
                    result.add(new RecipientRow(id, shortName, value));
                }

                return result;
            }
        }
    }

    @Override
    public @NotNull RecipientRow insert(@NotNull Connection conn, @NotNull RecipientRow recipientRow) throws SQLException {
        String sql = "INSERT INTO secuboid_parameter_value(short_name, value) VALUES(?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, recipientRow.shortName());
            stmt.setString(2, recipientRow.value());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();
                return new RecipientRow(rs.getLong(1), recipientRow.shortName(), recipientRow.value());
            }
        }
    }
}
