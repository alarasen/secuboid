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

package app.secuboid.core.parameters.values;

import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.parameters.values.ParameterValueResult;
import app.secuboid.api.parameters.values.ParameterValues;
import app.secuboid.api.reflection.ParameterValueRegistered;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.reflection.PluginLoader;
import app.secuboid.core.storage.rows.ParameterValueRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static app.secuboid.api.parameters.values.ParameterValueResultCode.INVALID_PARAMETER;
import static app.secuboid.api.parameters.values.ParameterValueResultCode.SUCCESS;
import static app.secuboid.api.parameters.values.ParameterValues.PLAYER;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ParameterValuesTest {

    private final AtomicLong atomicIndexId = new AtomicLong();

    private ParameterValues parameterValues;

    @BeforeEach
    void beforeEach() {
        ParameterValuesImpl parameterValuesImpl = spy(new ParameterValuesImpl());

        StorageManager storageManager = mock(StorageManager.class);
        doAnswer(this::storageInsertAnswer).when(storageManager).insert(any(), any());
        doReturn(storageManager).when(parameterValuesImpl).getStorageManager();

        PluginLoader pluginLoader = mock(PluginLoader.class);
        ParameterValueRegistered parameterValueRegistered =
                ParameterValuePlayer.class.getAnnotation(ParameterValueRegistered.class);
        when(pluginLoader.getClassToAnnotation(ParameterValueRegistered.class, ParameterValue.class))
                .thenReturn(Collections.singletonMap(ParameterValuePlayer.class, parameterValueRegistered));

        parameterValuesImpl.init(pluginLoader);
        parameterValuesImpl.load();

        parameterValues = parameterValuesImpl;
    }

    @Test
    void when_grab_invalid_parameter_send_error_code() {
        String value = UUID.randomUUID().toString();
        AtomicReference<ParameterValueResult> atomicResult = new AtomicReference<>(null);
        Consumer<ParameterValueResult> callback = atomicResult::set;

        parameterValues.grab("INVALID", value, callback);
        await().atMost(Duration.ofSeconds(10)).until(() -> atomicResult.get() != null);

        ParameterValueResult result = atomicResult.get();

        assertNotNull(result);
        assertEquals(INVALID_PARAMETER, result.code());
    }

    @Test
    void when_grab_new_value_add_it() {
        String value = UUID.randomUUID().toString();
        AtomicReference<ParameterValueResult> atomicResult = new AtomicReference<>(null);
        Consumer<ParameterValueResult> callback = atomicResult::set;

        parameterValues.grab(PLAYER, value, callback);
        await().atMost(Duration.ofSeconds(10)).until(() -> atomicResult.get() != null);

        ParameterValueResult result = atomicResult.get();

        assertNotNull(result);
        assertEquals(SUCCESS, result.code());
        ParameterValue parameterValue = result.parameterValue();
        assertNotNull(parameterValue);
        assertEquals(1, parameterValue.id());
    }

    @Test
    void when_grab_twice_same_value_add_one_and_get_the_second() {
        String value = UUID.randomUUID().toString();
        AtomicReference<ParameterValueResult> atomicResult = new AtomicReference<>(null);
        Consumer<ParameterValueResult> callback = atomicResult::set;

        parameterValues.grab(PLAYER, value, callback);
        await().atMost(Duration.ofSeconds(10)).until(() -> atomicResult.get() != null);

        atomicResult.set(null);
        parameterValues.grab(PLAYER, value, callback);
        await().atMost(Duration.ofSeconds(10)).until(() -> atomicResult.get() != null);

        ParameterValueResult result = atomicResult.get();

        assertNotNull(result);
        assertEquals(SUCCESS, result.code());
        ParameterValue parameterValue = result.parameterValue();
        assertNotNull(parameterValue);
        assertEquals(1, parameterValue.id());
    }

    private Object storageInsertAnswer(InvocationOnMock invocation) {
        ParameterValueRow previousRow = invocation.getArgument(0);
        Consumer<ParameterValueRow> callback = invocation.getArgument(1);

        ParameterValueRow row = new ParameterValueRow(atomicIndexId.incrementAndGet(), PLAYER, previousRow.value());
        callback.accept(row);

        return null;
    }
}
