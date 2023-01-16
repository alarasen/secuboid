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

package app.secuboid.core.recipients;

import app.secuboid.api.recipients.Recipient;
import app.secuboid.api.recipients.RecipientResult;
import app.secuboid.api.recipients.Recipients;
import app.secuboid.api.reflection.RecipientRegistered;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.reflection.PluginLoader;
import app.secuboid.core.storage.rows.RecipientRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static app.secuboid.api.recipients.RecipientResultCode.INVALID_PARAMETER;
import static app.secuboid.api.recipients.RecipientResultCode.SUCCESS;
import static app.secuboid.api.recipients.Recipients.PLAYER;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RecipientsTest {

    private final AtomicLong atomicIndexId = new AtomicLong();

    private Recipients recipients;

    @BeforeEach
    void beforeEach() {
        RecipientsImpl recipientsImpl = spy(new RecipientsImpl());

        StorageManager storageManager = mock(StorageManager.class);
        doAnswer(this::storageInsertAnswer).when(storageManager).insert(any(), any());
        doReturn(storageManager).when(recipientsImpl).getStorageManager();

        PluginLoader pluginLoader = mock(PluginLoader.class);
        RecipientRegistered recipientRegistered =
                RecipientPlayer.class.getAnnotation(RecipientRegistered.class);
        when(pluginLoader.getClassToAnnotation(RecipientRegistered.class, Recipient.class))
                .thenReturn(Collections.singletonMap(RecipientPlayer.class, recipientRegistered));

        recipientsImpl.init(pluginLoader);
        recipientsImpl.load();

        recipients = recipientsImpl;
    }

    @Test
    void when_grab_invalid_parameter_send_error_code() {
        String value = UUID.randomUUID().toString();
        AtomicReference<RecipientResult> atomicResult = new AtomicReference<>(null);
        Consumer<RecipientResult> callback = atomicResult::set;

        recipients.grab("INVALID", value, callback);
        await().atMost(Duration.ofSeconds(10)).until(() -> atomicResult.get() != null);

        RecipientResult result = atomicResult.get();

        assertNotNull(result);
        assertEquals(INVALID_PARAMETER, result.code());
    }

    @Test
    void when_grab_new_value_add_it() {
        String value = UUID.randomUUID().toString();
        AtomicReference<RecipientResult> atomicResult = new AtomicReference<>(null);
        Consumer<RecipientResult> callback = atomicResult::set;

        recipients.grab(PLAYER, value, callback);
        await().atMost(Duration.ofSeconds(10)).until(() -> atomicResult.get() != null);

        RecipientResult result = atomicResult.get();

        assertNotNull(result);
        assertEquals(SUCCESS, result.code());
        Recipient recipient = result.recipient();
        assertNotNull(recipient);
        assertEquals(1, recipient.id());
    }

    @Test
    void when_grab_twice_same_value_add_one_and_get_the_second() {
        String value = UUID.randomUUID().toString();
        AtomicReference<RecipientResult> atomicResult = new AtomicReference<>(null);
        Consumer<RecipientResult> callback = atomicResult::set;

        recipients.grab(PLAYER, value, callback);
        await().atMost(Duration.ofSeconds(10)).until(() -> atomicResult.get() != null);

        atomicResult.set(null);
        recipients.grab(PLAYER, value, callback);
        await().atMost(Duration.ofSeconds(10)).until(() -> atomicResult.get() != null);

        RecipientResult result = atomicResult.get();

        assertNotNull(result);
        assertEquals(SUCCESS, result.code());
        Recipient recipient = result.recipient();
        assertNotNull(recipient);
        assertEquals(1, recipient.id());
    }

    private Object storageInsertAnswer(InvocationOnMock invocation) {
        RecipientRow previousRow = invocation.getArgument(0);
        Consumer<RecipientRow> callback = invocation.getArgument(1);

        RecipientRow row = new RecipientRow(atomicIndexId.incrementAndGet(), PLAYER, previousRow.value());
        callback.accept(row);

        return null;
    }
}
