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
package me.tabinol.secuboid.api.storage;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.BiConsumer;

import org.bukkit.command.CommandSender;

import me.tabinol.secuboid.api.storage.tables.Row;

/**
 * The sync/async class where everything is loaded/saved.
 */
public interface StorageManager {

    /**
     * Bi functional for SQL.
     */
    @FunctionalInterface
    public interface BiFunctionSQL<T, U, R> {
        R apply(T t, U u) throws SQLException;
    }

    /**
     * Get (select) multiple values sync.
     * 
     * @param <R>      the row
     * @param classRow the row class
     * @return the result set
     */
    <R extends Row> Set<R> selectAllSync(Class<R> classRow);

    /**
     * Insert to the database.
     * 
     * @param row      the row
     * @param sender   the sender or null
     * @param callback the callback or null
     */
    void insert(Row row, CommandSender sender, BiConsumer<CommandSender, Row> callback);

    /**
     * Update to the database.
     * 
     * @param row      the row
     * @param sender   the sender or null
     * @param callback the callback or null
     */
    void update(Row row, CommandSender sender, BiConsumer<CommandSender, Row> callback);

    /**
     * Delete from the database.
     * 
     * @param row      the row
     * @param sender   the sender or null
     * @param callback the callback or null
     */
    void delete(Row row, CommandSender sender, BiConsumer<CommandSender, Row> callback);
}
