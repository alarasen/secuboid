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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.logging.Level;

import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;
import static java.util.Collections.emptySet;

class QueueThreadRun<T, R> extends Thread {

    private final Plugin plugin;
    private final QueueProcessor<T, R> queueProcessor;
    private final BlockingQueue<QueueThreadElement<T, R>> taskQueue;

    QueueThreadRun(@NotNull Plugin plugin, @NotNull QueueProcessor<T, R> queueProcessor,
                   @NotNull BlockingQueue<QueueThreadElement<T, R>> taskQueue) {
        this.plugin = plugin;
        this.queueProcessor = queueProcessor;
        this.taskQueue = taskQueue;
    }

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
        QueueThreadElement<T, R> element;

        // Loop unit there is no empty (stop thread request) queueProcessor.
        while ((element = taskQueue.take()).t() != null) {

            Object result;
            if (element.isSet()) {
                result = processElements(element);
            } else {
                result = processElement(element);
            }


            if (result != null && element.resultQueue() != null) {
                element.resultQueue().put(result);
            }
        }
    }

    private @Nullable R processElement(@NotNull QueueThreadElement<T, R> element) {
        T t = element.t();
        Consumer<R> callback = element.callback();

        if (t == null) {
            log().log(Level.SEVERE, "An element in thread has a 't' null: {}", element);
            return null;
        }

        R r = queueProcessor.process(t);

        if (callback != null) {

            Callable<Void> callableCallback = () -> {
                callback.accept(r);
                return null;
            };

            BukkitScheduler scheduler = plugin.getServer().getScheduler();
            scheduler.callSyncMethod(plugin, callableCallback);
        }

        return r;
    }

    private @NotNull Set<R> processElements(@NotNull QueueThreadElement<T, R> element) {
        T t = element.t();

        if (t == null) {
            log().log(Level.SEVERE, "An element in thread has a 't' null: {}", element);
            return emptySet();
        }

        return queueProcessor.processMultiple(t);
    }
}
