/*
 *  Secuboid: LandService and Protection plugin for Minecraft server
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
package app.secuboid.api.storage;

import app.secuboid.api.storage.rows.Row;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The sync/async class where everything is loaded/saved.
 */
public interface StorageManager {

    /**
     * Get (select) multiple values sync.
     *
     * @param classRow the row class
     * @param <R>      a row
     * @return the result set
     */
    <R extends Row> Set<R> selectAllSync(Class<R> classRow);

    /**
     * Insert to the database sync.
     *
     * @param row the row
     * @param <R> a row
     */
    <R extends Row> R insertSync(R row);

    /**
     * Insert to the database.
     *
     * @param row      the row
     * @param callback the callback or null
     * @param <R>      a row
     */
    <R extends Row> void insert(R row, Consumer<R> callback);

    /**
     * Update to the database.
     *
     * @param row      the row
     * @param callback the callback or null
     * @param <R>      a row
     */
    <R extends Row> void update(R row, Consumer<R> callback);

    /**
     * Delete from the database.
     *
     * @param row      the row
     * @param callback the callback or null
     * @param <R>      a row
     */
    <R extends Row> void delete(R row, Consumer<R> callback);

    /**
     * Bi function for SQL.
     */
    @FunctionalInterface
    public interface BiFunctionSQL<T, U, R> {
        R apply(T t, U u) throws SQLException;
    }
}
