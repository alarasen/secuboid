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

import app.secuboid.api.services.Service;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class PersistenceService implements Service {

    private static final String PERSISTENCE_THREAD_NOT_RUNNING_MSG = "PersistenceSessionService thread not running";

    private final @NotNull JavaPlugin javaPlugin;
    private final @NotNull BukkitScheduler scheduler;
    private final @NotNull PersistenceSessionService persistenceSessionService;

    private @Nullable PersistenceThread persistenceThread;

    public PersistenceService(@NotNull JavaPlugin javaPlugin, @NotNull BukkitScheduler scheduler,
                              @NotNull PersistenceSessionService persistenceSessionService) {
        this.javaPlugin = javaPlugin;
        this.scheduler = scheduler;
        this.persistenceSessionService = persistenceSessionService;
        persistenceThread = null;
    }

    @Override
    public void onEnable(boolean isServerBoot) {
        persistenceThread = new PersistenceThread(javaPlugin, scheduler, persistenceSessionService);
        persistenceThread.start();
    }

    @Override
    public void onDisable(boolean isServerShutdown) {
        assert persistenceThread != null : PERSISTENCE_THREAD_NOT_RUNNING_MSG;
        persistenceThread.shutdown();
        persistenceThread = null;
    }

    public <R> void exec(@NotNull Function<Session, R> sessionFunction, @Nullable Consumer<R> callback) {
        assert persistenceThread != null : PERSISTENCE_THREAD_NOT_RUNNING_MSG;
        PersistenceElement<R> element = new PersistenceElement<>(sessionFunction, callback, false);
        persistenceThread.offer(element);
    }

    public <R> @Nullable R execSync(@NotNull Function<Session, R> sessionFunction) {
        assert persistenceThread != null : PERSISTENCE_THREAD_NOT_RUNNING_MSG;
        PersistenceElement<R> element = new PersistenceElement<>(sessionFunction, null, true);
        persistenceThread.offer(element);

        return persistenceThread.take();
    }
}
