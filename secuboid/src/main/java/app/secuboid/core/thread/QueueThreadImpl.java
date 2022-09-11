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
package app.secuboid.core.thread;

import app.secuboid.api.thread.QueueProcessor;
import app.secuboid.api.thread.QueueThread;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;
import static java.util.Collections.emptySet;

public class QueueThreadImpl<T, R> implements QueueThread<T, R> {

    private static final long TIME_WAITING_THREAD_MILLIS = Duration.ofSeconds(10).toMillis();

    private final Plugin plugin;
    private final QueueProcessor<T, R> queueProcessor;
    private final String threadName;
    private final BlockingQueue<Element<T, R>> taskQueue;

    private ThreadExt thread;
    private BukkitScheduler scheduler;

    public QueueThreadImpl(Plugin plugin, String threadName, QueueProcessor<T, R> queueProcessor) {
        this.plugin = plugin;
        this.threadName = threadName;
        this.queueProcessor = queueProcessor;
        taskQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void start() {
        scheduler = plugin.getServer().getScheduler();

        if (isAlive()) {
            log().log(Level.WARNING,
                    () -> format("Thread \"%s\" is already started and it shouldn't!", threadName));
        }

        thread = new ThreadExt();
        thread.setName(threadName);

        thread.start();
    }

    @Override
    public boolean isAlive() {
        return thread != null && thread.isAlive();
    }

    @Override
    public void addElement(@NotNull T t) {
        Element<T, R> element = new Element<>(t, null, null, null, false);
        taskQueue.add(element);
    }

    @Override
    public void addElement(@NotNull T t, @NotNull BlockingQueue<Object> resultQueue, boolean isSet) {
        Element<T, R> element = new Element<>(t, null, resultQueue, null, isSet);
        taskQueue.add(element);
    }

    @Override
    public void addElement(@NotNull T t, CommandSender sender, BiConsumer<CommandSender, R> callback) {
        Element<T, R> element = new Element<>(t, sender, null, callback, false);
        taskQueue.add(element);
    }

    @Override
    public void stop() {
        // Check if the thread is not death
        if (thread == null || !thread.isAlive()) {
            log().log(Level.SEVERE, () -> format("Thread \"%s\" is already stopped!", threadName));
            return;
        }

        // Send shutdown thread request
        Element<T, R> element = new Element<>(null, null, null, null, false);
        taskQueue.add(element);

        // Waiting for thread
        waitForThread();

        // If the thread is not stopped
        if (thread.isAlive()) {
            log().log(Level.WARNING, () -> format("Unable to stop gracefully the thread \"%s\"!", threadName));
            thread.interrupt();
            waitForThread();
        }

        thread = null;
    }

    private static record Element<T, R>(@Nullable T t,
                                        @Nullable CommandSender sender,
                                        @Nullable BlockingQueue<Object> resultQueue,
                                        @Nullable BiConsumer<CommandSender, R> callback,
                                        boolean isSet
    ) {
    }

    private void waitForThread() {
        try {
            thread.join(TIME_WAITING_THREAD_MILLIS);
        } catch (InterruptedException e) {
            log().log(Level.WARNING, e, () -> format("Thread \"%s\" interrupted!", threadName));
            Thread.currentThread().interrupt();
        }
    }

    private class ThreadExt extends Thread {

        @Override
        public void run() {
            try {
                loopQueue();
            } catch (InterruptedException e) {
                log().log(Level.WARNING, e, () -> format("Interruption requested for thread \"%s\".", getName()));
                Thread.currentThread().interrupt();
            }
        }

        private void loopQueue() throws InterruptedException {
            Element<T, R> element;

            // Loop unit there is no empty (stop thread request) queueProcessor.
            while ((element = taskQueue.take()).t != null) {

                Object result;
                if (element.isSet) {
                    result = processElements(element);
                } else {
                    result = processElement(element);
                }


                if (result != null && element.resultQueue != null) {
                    element.resultQueue.put(result);
                }
            }
        }

        private @Nullable R processElement(@NotNull Element<T, R> element) {
            CommandSender sender = element.sender;
            T t = element.t;
            BiConsumer<CommandSender, R> callback = element.callback;

            if (t == null) {
                log().log(Level.SEVERE, "An element in thread has a 't' null: {}", element);
                return null;
            }

            R r = queueProcessor.process(t);

            if (callback != null) {

                Callable<Void> callableCallback = () -> {
                    callback.accept(sender, r);
                    return null;
                };

                scheduler.callSyncMethod(plugin, callableCallback);
            }

            return r;
        }

        private @NotNull Set<R> processElements(Element<T, R> element) throws InterruptedException {
            T t = element.t;

            if (t == null) {
                log().log(Level.SEVERE, "An element in thread has a 't' null: {}", element);
                return emptySet();
            }

            return queueProcessor.processMultiple(t);
        }
    }
}
