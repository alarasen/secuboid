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

import app.secuboid.api.storage.StorageManager;
import app.secuboid.api.storage.tables.Row;
import app.secuboid.api.storage.tables.Table;
import app.secuboid.api.thread.QueueThread;
import app.secuboid.core.messages.Log;
import app.secuboid.core.reflection.PluginLoader;
import app.secuboid.core.thread.QueueThreadImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiConsumer;
import java.util.logging.Level;

public class StorageManagerImpl implements StorageManager {

    private static final String THREAD_NAME = "Secuboid Storage";

    private final StorageInit storageInit;
    private final StorageQueueProcessor queueProcessor;
    private final QueueThread<StorageElement, Row> queueThread;
    private final BlockingQueue<Set<Row>> resultQueue;

    public StorageManagerImpl(Plugin plugin) {
        storageInit = new StorageInit();
        queueProcessor = new StorageQueueProcessor();
        queueThread = new QueueThreadImpl<>(plugin, THREAD_NAME, queueProcessor);
        resultQueue = new LinkedBlockingDeque<>(1);
    }

    public void init(PluginLoader pluginLoader) {
        storageInit.init(pluginLoader);
    }

    public void start() {
        queueThread.start();
    }

    public void stop() {
        queueThread.stop();
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <R extends Row> Set<R> selectAllSync(@NotNull Class<R> classRow) {
        Table<Row> table = storageInit.getTableFromClassRow(classRow);
        StorageElement element = new StorageElement(table, null, SQLRequestType.SELECT_ALL_SYNC);
        queueThread.addElement(element, resultQueue);

        try {
            return (Set<R>) resultQueue.take();
        } catch (InterruptedException e) {
            Log.log().log(Level.SEVERE, "Thread \"%s\" interrupted!", e);
            Thread.currentThread().interrupt();
        }

        return Collections.emptySet();
    }

    @Override
    public void insert(@NotNull Row row, @Nullable CommandSender sender, @Nullable BiConsumer<CommandSender, Row> callback) {
        Table<Row> table = storageInit.getTableFromClassRow(row.getClass());
        StorageElement element = new StorageElement(table, row, SQLRequestType.INSERT);
        queueThread.addElement(element, sender, callback);
    }

    @Override
    public void update(@NotNull Row row, @Nullable CommandSender sender, @Nullable BiConsumer<CommandSender, Row> callback) {
        Table<Row> table = storageInit.getTableFromClassRow(row.getClass());
        StorageElement element = new StorageElement(table, row, SQLRequestType.UPDATE);
        queueThread.addElement(element, sender, callback);
    }

    @Override
    public void delete(@NotNull Row row, @Nullable CommandSender sender, @Nullable BiConsumer<CommandSender, Row> callback) {
        Table<Row> table = storageInit.getTableFromClassRow(row.getClass());
        StorageElement element = new StorageElement(table, row, SQLRequestType.DELETE);
        queueThread.addElement(element, sender, callback);
    }
}
