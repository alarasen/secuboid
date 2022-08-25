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
package me.tabinol.secuboid.core.storage;

import static me.tabinol.secuboid.core.messages.Log.log;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.tabinol.secuboid.api.storage.StorageManager;
import me.tabinol.secuboid.api.storage.tables.Row;
import me.tabinol.secuboid.api.storage.tables.Table;
import me.tabinol.secuboid.api.thread.QueueThread;
import me.tabinol.secuboid.core.reflection.PluginLoader;
import me.tabinol.secuboid.core.thread.QueueThreadImpl;

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
    public <R extends Row> Set<R> selectAllSync(Class<R> classRow) {
        Table<Row> table = storageInit.getTableFromClassRow(classRow);
        StorageElement element = new StorageElement(table, null, SQLRequestType.SELECT_ALL_SYNC);
        queueThread.addElement(element, resultQueue);

        try {
            return (Set<R>) resultQueue.take();
        } catch (InterruptedException e) {
            log().log(Level.SEVERE, "Thread \"%s\" interrupted!", e);
            Thread.currentThread().interrupt();
        }

        return Collections.emptySet();
    }

    @Override
    public void insert(Row row, CommandSender sender, BiConsumer<CommandSender, Row> callback) {
        Table<Row> table = storageInit.getTableFromClassRow(row.getClass());
        StorageElement element = new StorageElement(table, row, SQLRequestType.INSERT);
        queueThread.addElement(element, sender, callback);
    }

    @Override
    public void update(Row row, CommandSender sender, BiConsumer<CommandSender, Row> callback) {
        Table<Row> table = storageInit.getTableFromClassRow(row.getClass());
        StorageElement element = new StorageElement(table, row, SQLRequestType.UPDATE);
        queueThread.addElement(element, sender, callback);
    }

    @Override
    public void delete(Row row, CommandSender sender, BiConsumer<CommandSender, Row> callback) {
        Table<Row> table = storageInit.getTableFromClassRow(row.getClass());
        StorageElement element = new StorageElement(table, row, SQLRequestType.DELETE);
        queueThread.addElement(element, sender, callback);
    }
}
