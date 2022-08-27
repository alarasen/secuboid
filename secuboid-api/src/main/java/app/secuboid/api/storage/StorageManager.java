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
package app.secuboid.api.storage;

import app.secuboid.api.storage.tables.Row;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * The sync/async class where everything is loaded/saved.
 */
public interface StorageManager {

    /**
     * Bi functional for SQL.
     */
    @FunctionalInterface
    public interface BiFunctionSQL<T, U, R> {
        @NotNull R apply(@NotNull T t, @NotNull U u) throws SQLException;
    }

    /**
     * Get (select) multiple values sync.
     *
     * @param <R>      the row
     * @param classRow the row class
     * @return the result set
     */
    <R extends Row> @NotNull Set<R> selectAllSync(@NotNull Class<R> classRow);

    /**
     * Insert to the database.
     *
     * @param row      the row
     * @param sender   the sender or null
     * @param callback the callback or null
     */
    void insert(@NotNull Row row, @Nullable CommandSender sender, @Nullable BiConsumer<CommandSender, Row> callback);

    /**
     * Update to the database.
     *
     * @param row      the row
     * @param sender   the sender or null
     * @param callback the callback or null
     */
    void update(@NotNull Row row, @Nullable CommandSender sender, @Nullable BiConsumer<CommandSender, Row> callback);

    /**
     * Delete from the database.
     *
     * @param row      the row
     * @param sender   the sender or null
     * @param callback the callback or null
     */
    void delete(@NotNull Row row, @Nullable CommandSender sender, @Nullable BiConsumer<CommandSender, Row> callback);
}
