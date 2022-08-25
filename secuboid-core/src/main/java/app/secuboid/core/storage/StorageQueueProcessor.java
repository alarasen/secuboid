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
import app.secuboid.api.storage.tables.Row;
import app.secuboid.api.storage.tables.Table;
import app.secuboid.api.thread.QueueProcessor;
import app.secuboid.core.messages.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;

import static java.lang.String.format;

public class StorageQueueProcessor implements QueueProcessor<StorageElement, Row> {

    @Override
    @SuppressWarnings("unchecked")
    public Row process(StorageElement element) {
        SQLRequestType requestType = element.requestType();
        Table<Row> table = (Table<Row>) element.table();
        Row row = element.row();

        try (Connection conn = ConnectionManager.getConnection()) {
            return processDatabase(conn, requestType, table, row);

        } catch (SQLException e) {
            Log.log().log(Level.SEVERE, e,
                    () -> format(
                            "Possible data loss! Unable to access to the storage [requestType=%s, table=%s, row=%s]",
                            requestType, table.getClass().getName(), row));
        } catch (RuntimeException e) {
            Log.log().log(Level.SEVERE, e,
                    () -> format(
                            "Possible data loss! Something is wrong with the storage access [requestType=%s, table=%s, row=%s]",
                            requestType, table.getClass().getName(), row));
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Row> processMultipleSync(StorageElement element) {
        SQLRequestType requestType = element.requestType();
        Table<Row> table = (Table<Row>) element.table();

        try (Connection conn = ConnectionManager.getConnection()) {
            return processDatabaseMultiple(conn, requestType, table);

        } catch (SQLException e) {
            Log.log().log(Level.SEVERE, e,
                    () -> format(
                            "Possible data loss! Unable to access to the storage [requestType=%s, table=%s]",
                            requestType, table.getClass().getName()));
        } catch (RuntimeException e) {
            Log.log().log(Level.SEVERE, e,
                    () -> format(
                            "Possible data loss! Something is wrong with the storage access [requestType=%s, table=%s]",
                            requestType, table.getClass().getName()));
        }

        return Collections.emptySet();
    }

    private Row processDatabase(Connection conn, SQLRequestType requestType, Table<Row> table, Row row)
            throws SQLException {
        switch (requestType) {
            case INSERT -> {
                return table.insert(conn, row);
            }
            case UPDATE -> {
                return table.update(conn, row);
            }
            case DELETE -> {
                return table.delete(conn, row);
            }
            default -> throw new SecuboidRuntimeException("No storage instruction");
        }
    }

    private Set<Row> processDatabaseMultiple(Connection conn, SQLRequestType requestType, Table<Row> table)
            throws SQLException {
        switch (requestType) {
            case SELECT_ALL_SYNC -> {
                return table.selectAll(conn);
            }
            default -> throw new SecuboidRuntimeException("No storage instruction");
        }
    }
}
