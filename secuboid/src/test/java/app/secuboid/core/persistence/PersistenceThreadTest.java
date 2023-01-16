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

import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PersistenceThreadTest {

    PersistenceThread persistenceThread;

    @BeforeEach
    void init() {
        BukkitScheduler scheduler = mock(BukkitScheduler.class);
        Persistence persistence = mock(Persistence.class);
        persistenceThread = new PersistenceThread(scheduler, persistence);
    }

    @Test
    void when_stop_signal_then_queue_stop() {
        persistenceThread.init();

        assertTrue(persistenceThread.isAlive());
        persistenceThread.shutdown();
        assertFalse(persistenceThread.isAlive());
    }

    @Test
    void when_stop_signal_then_wait_for_queue() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        persistenceThread.init();
        persistenceThread.exec(s -> {
            await().during(Duration.ofSeconds(10));
            atomicBoolean.set(true);
            return null;
        }, null);

        assertTrue(persistenceThread.isAlive());
        persistenceThread.shutdown();
        assertFalse(persistenceThread.isAlive());
        assertTrue(atomicBoolean.get());
    }

    @Test
    void when_stop_signal_on_sleep_then_interrupt() {
        persistenceThread.init();
        persistenceThread.exec(s -> null, null);

        assertTrue(persistenceThread.isAlive());
        persistenceThread.shutdown();
        assertFalse(persistenceThread.isAlive());
    }

    @Test
    void when_stop_on_death_then_do_nothing() {
        persistenceThread.shutdown();

        assertFalse(persistenceThread.isAlive());
    }

    @Test
    void when_request_result_async_wait_for_callback() {
        persistenceThread.init();
        Long result = persistenceThread.execSync(s -> {
            await().during(Duration.ofMillis(100));
            return 10L;
        });
        persistenceThread.shutdown();

        assertNotNull(result);
        assertEquals(10, result);
    }

}