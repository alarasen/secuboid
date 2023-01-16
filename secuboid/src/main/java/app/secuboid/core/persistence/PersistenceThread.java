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

package app.secuboid.core.persistence;

import app.secuboid.core.SecuboidImpl;
import org.bukkit.scheduler.BukkitScheduler;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;

import static app.secuboid.core.messages.Log.log;
import static java.util.logging.Level.SEVERE;

public class PersistenceThread extends Thread {

    private static final String THREAD_NAME = "Secuboid Persistence";

    private final @NotNull BukkitScheduler scheduler;
    private final @NotNull Persistence persistence;
    private final @NotNull BlockingQueue<PersistenceElement<?>> queue;

    private final @NotNull BlockingQueue<Object> threadSyncQueue;

    // TODO unit tests

    public PersistenceThread(@NotNull BukkitScheduler scheduler, @NotNull Persistence persistence) {
        this.scheduler = scheduler;
        this.persistence = persistence;
        queue = new LinkedBlockingQueue<>();
        threadSyncQueue = new LinkedBlockingQueue<>(1);
        setName(THREAD_NAME);
    }

    public void init() {
        persistence.init();
        start();
    }

    public void shutdown() {
        interrupt();
        try {
            join();
        } catch (InterruptedException e) {
            log().severe("Unwanted interruption on persistence shutdown. Possible data loss!");
            Thread.currentThread().interrupt();
        }
        persistence.shutdown();
    }

    public <R> void exec(@NotNull Function<Session, R> sessionFunction, @Nullable Consumer<R> callback) {
        PersistenceElement<R> element = new PersistenceElement<>(sessionFunction, callback, false);
        offer(element);
    }

    @SuppressWarnings("unchecked")
    public <R> @Nullable R execSync(@NotNull Function<Session, R> sessionFunction) {
        PersistenceElement<R> element = new PersistenceElement<>(sessionFunction, null, true);
        offer(element);

        Object result;
        try {
            result = threadSyncQueue.take();
        } catch (InterruptedException e) {
            log().log(SEVERE, "Interrupted! Possible data loss!", e);
            Thread.currentThread().interrupt();
            return null;
        }

        try {
            return (R) result;
        } catch (ClassCastException e) {
            log().log(SEVERE, e, () -> "Wrong return type: " + result);
            return null;
        }
    }

    @Override
    public void run() {
        while (!(Thread.currentThread().isInterrupted())) {
            try {
                PersistenceElement<?> element = queue.take();
                process(element);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log().info("Flushing the persistence queue...");
        List<PersistenceElement<?>> elements = new ArrayList<>();
        queue.drainTo(elements);

        for (PersistenceElement<?> element : elements) {
            process(element);
        }

        log().info("Flushing the persistence queue done!");
    }

    private <R> void process(@NotNull PersistenceElement<R> element) {
        Function<Session, R> sessionFunction = element.sessionFunction();

        R result;
        try (Session session = persistence.getSession()) {
            result = sessionFunction.apply(session);
        }

        Consumer<R> callback = element.callback();
        if (callback != null) {
            callMainThread(callback, result);
        }

        if (element.isSync() && !threadSyncQueue.offer(result)) {
            log().severe(() -> "Unable to send the persistence result: " + result);
        }
    }

    private <R> void offer(@NotNull PersistenceElement<R> element) {
        if (!queue.offer(element)) {
            log().severe("The persistence queue is full. Possible data loss!");
        }
    }

    private <R> void callMainThread(@NotNull Consumer<R> callback, R result) {
        scheduler.callSyncMethod(SecuboidImpl.getJavaPLugin(), () -> {
            try {
                callback.accept(result);
            } catch (RuntimeException e) {
                log().log(SEVERE, "Exception while call back main thread", e);
            }
            return null;
        });
    }
}
