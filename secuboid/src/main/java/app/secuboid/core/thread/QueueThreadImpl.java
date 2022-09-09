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
import app.secuboid.core.messages.Log;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import static java.lang.String.format;

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
            Log.log().log(Level.WARNING,
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
    public void addElement(T t) {
        Element<T, R> element = new Element<>(t, null, null, null);
        taskQueue.add(element);
    }

    @Override
    public void addElement(T t, @NotNull BlockingQueue<Set<R>> resultQueue) {
        Element<T, R> element = new Element<>(t, null, resultQueue, null);
        taskQueue.add(element);
    }

    @Override
    public void addElement(T t, CommandSender sender, BiConsumer<CommandSender, R> callback) {
        Element<T, R> element = new Element<>(t, sender, null, callback);
        taskQueue.add(element);
    }

    @Override
    public void stop() {
        // Check if the thread is not death
        if (thread == null || !thread.isAlive()) {
            Log.log().log(Level.SEVERE, () -> format("Thread \"%s\" is already stopped!", threadName));
            return;
        }

        // Send shutdown thread request
        addElement(null);

        // Waiting for thread
        waitForThread();

        // If the thread is not stopped
        if (thread.isAlive()) {
            Log.log().log(Level.WARNING, () -> format("Unable to stop gracefully the thread \"%s\"!", threadName));
            thread.interrupt();
            waitForThread();
        }

        thread = null;
    }

    private static record Element<T, R>(T t, CommandSender sender, BlockingQueue<Set<R>> resultQueue,
                                        BiConsumer<CommandSender, R> callback) {
    }

    private void waitForThread() {
        try {
            thread.join(TIME_WAITING_THREAD_MILLIS);
        } catch (InterruptedException e) {
            Log.log().log(Level.WARNING, e, () -> format("Thread \"%s\" interrupted!", threadName));
            Thread.currentThread().interrupt();
        }
    }

    private class ThreadExt extends Thread {

        @Override
        public void run() {
            try {
                loopQueue();
            } catch (InterruptedException e) {
                Log.log().log(Level.WARNING, e, () -> format("Interruption requested for thread \"%s\".", getName()));
                Thread.currentThread().interrupt();
            }
        }

        private void loopQueue() throws InterruptedException {
            Element<T, R> element;

            // Loop unit there is no empty (stop thread request) queueProcessor.
            while ((element = taskQueue.take()).t != null) {
                if (element.resultQueue != null) {
                    processElementsSync(element);
                } else {
                    processElement(element);
                }
            }
        }

        private void processElement(Element<T, R> element) {
            CommandSender sender = element.sender;
            T t = element.t;
            BiConsumer<CommandSender, R> callback = element.callback;

            R r = queueProcessor.process(t);

            if (callback == null) {
                return;
            }

            Callable<Void> callableCallback = () -> {
                callback.accept(sender, r);
                return null;
            };

            scheduler.callSyncMethod(plugin, callableCallback);
        }

        private void processElementsSync(Element<T, R> element) throws InterruptedException {
            T t = element.t;

            Set<R> result = queueProcessor.processMultipleSync(t);

            element.resultQueue.put(result);
        }
    }
}
