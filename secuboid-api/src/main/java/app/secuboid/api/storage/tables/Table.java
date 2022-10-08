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

package app.secuboid.api.storage.tables;

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.api.storage.rows.Row;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 * Every table classes must extend this class.
 */
public interface Table<R extends Row> {

    /**
     * Select all resources and return it with the column index in the specific
     * type.
     *
     * @param conn the connection
     * @return the set of rows
     * @throws SQLException the SQL exception
     */
    default @NotNull Set<R> selectAll(@NotNull Connection conn) throws SQLException {
        throw new SecuboidRuntimeException("SQL SELECT ALL Not implemented!");
    }

    /**
     * Inserts the resource and return it with column index in the specific
     * type.
     *
     * @param conn the connection
     * @param row  the row
     * @return the row
     * @throws SQLException the SQL exception
     */
    default @NotNull R insert(@NotNull Connection conn, @NotNull R row) throws SQLException {
        throw new SecuboidRuntimeException("SQL INSERT Not implemented!");
    }

    /**
     * Updates the resource from is id.
     *
     * @param conn the connection
     * @param row  the row
     * @return the row
     * @throws SQLException the SQL exception
     */
    default @NotNull R update(@NotNull Connection conn, @NotNull R row) throws SQLException {
        throw new SecuboidRuntimeException("SQL UPDATE Not implemented!");
    }

    /**
     * Deletes the resource from is id.
     *
     * @param conn the connection
     * @param row  the row
     * @return the row
     * @throws SQLException the SQL exception
     */
    default @NotNull R delete(@NotNull Connection conn, @NotNull R row) throws SQLException {
        throw new SecuboidRuntimeException("SQL DELETE Not implemented!");
    }
}
