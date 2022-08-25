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
import org.awaitility.Awaitility;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QueueThreadTest {

    private static class TestQueueProcessor implements QueueProcessor<Duration, Integer> {

        @Override
        public Integer process(Duration duration) {
            if (!duration.isZero()) {
                Awaitility.await().during(duration);
            }
            return 1;
        }

        @Override
        public Set<Integer> processMultipleSync(Duration duration) {
            if (!duration.isZero()) {
                Awaitility.await().during(duration);
            }
            return Collections.singleton(2);
        }
    }

    private TestQueueProcessor testQueueProcessor;
    private QueueThread<Duration, Integer> queueThread;

    @BeforeEach
    void init() {
        Plugin plugin = mock(Plugin.class);
        Logger logger = mock(Logger.class);
        Server server = mock(Server.class);
        BukkitScheduler scheduler = mock(BukkitScheduler.class);

        when(plugin.getLogger()).thenReturn(logger);
        when(plugin.getServer()).thenReturn(server);
        when(server.getScheduler()).thenReturn(scheduler);
        when(scheduler.callSyncMethod(any(), any())).thenAnswer(a -> {
            Callable<?> callable = a.getArgument(1, Callable.class);
            callable.call();
            return null;
        });

        testQueueProcessor = spy(new TestQueueProcessor());
        queueThread = new QueueThreadImpl<>(plugin, "Test Queue Thread", testQueueProcessor);

    }

    @Test
    void when_stop_signal_then_queue_stop() {
        queueThread.start();

        assertTrue(queueThread.isAlive());
        queueThread.stop();
        assertFalse(queueThread.isAlive());
    }

    @Test
    void when_stop_signal_then_wait_for_queue() {
        queueThread.start();
        queueThread.addElement(Duration.ofSeconds(2));

        assertTrue(queueThread.isAlive());
        queueThread.stop();
        assertFalse(queueThread.isAlive());
    }

    @Test
    void when_stop_signal_on_sleep_then_interrupt() {
        queueThread.start();
        queueThread.addElement(Duration.ofSeconds(1));

        assertTrue(queueThread.isAlive());
        queueThread.stop();
        assertFalse(queueThread.isAlive());
    }

    @Test
    void when_stop_on_death_then_do_nothing() {
        queueThread.stop();

        assertFalse(queueThread.isAlive());
    }

    @Test
    void when_add_element_then_do_element_one_time() throws InterruptedException {
        Duration element1 = Duration.ofMillis(0L);
        queueThread.start();
        queueThread.addElement(element1);
        queueThread.stop();

        verify(testQueueProcessor, times(1)).process(element1);
    }

    @Test
    void when_callback_is_added_then_callback() throws InterruptedException {
        Duration element1 = Duration.ofMillis(0L);
        AtomicLong atomicLong = new AtomicLong(0);
        BiConsumer<CommandSender, Integer> callback = (s, l) -> atomicLong.addAndGet(l);

        queueThread.start();
        queueThread.addElement(element1, null, callback);

        Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> atomicLong.get() == 1);
        queueThread.stop();

        verify(testQueueProcessor, times(1)).process(element1);
        assertEquals(1, atomicLong.get());
    }

    @Test
    void when_sync_then_return_result() throws InterruptedException {
        Duration element1 = Duration.ofMillis(0L);
        BlockingQueue<Set<Integer>> resultQueue = new LinkedBlockingDeque<>(1);

        queueThread.start();
        queueThread.addElement(element1, resultQueue);
        Set<Integer> result = resultQueue.take();
        queueThread.stop();

        assertEquals(1, result.size());
        assertEquals(2, result.iterator().next());
    }
}
