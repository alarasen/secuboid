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
package app.secuboid.api.thread;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;

import static app.secuboid.api.SecuboidPlugin.secuboid;

/**
 * This is a class to create a waiting queue thread.
 */
public interface QueueThread<T, R> {

    /**
     * Instantiates a new Secuboid queue thread.
     *
     * @param plugin         the actual plugin
     * @param threadName     the name of the thread
     * @param queueProcessor the queue processor
     */
    public static <T, R> @NotNull QueueThread<T, R> newQueueThread(@NotNull Plugin plugin, @NotNull String threadName, @NotNull QueueProcessor<T, R> queueProcessor) {
        return secuboid().getNewInstance().newQueueThread(plugin, threadName, queueProcessor);
    }

    /**
     * Starts the thread.
     */
    void start();

    /**
     * Checks if the queue is alive.
     *
     * @return true if the queue is alive
     */
    boolean isAlive();

    /**
     * Adds an element without callback.
     *
     * @param t the element to send
     */
    void addElement(@NotNull T t);

    /**
     * Adds an element and blocking queue for sync.
     *
     * @param t           the element to send
     * @param resultQueue the blocking queue for result
     * @param isSet       is only one or a set?
     */
    void addElement(@NotNull T t, @NotNull BlockingQueue<Object> resultQueue, boolean isSet);

    /**
     * Adds an element with callback.
     *
     * @param t        the element to send
     * @param sender   the sender or null
     * @param callback the callback or null
     */
    void addElement(@NotNull T t, @Nullable CommandSender sender, @Nullable BiConsumer<CommandSender, R> callback);

    /**
     * Waits for the last element and stop.
     */
    void stop();
}
