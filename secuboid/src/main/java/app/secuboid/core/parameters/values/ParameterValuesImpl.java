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

import app.secuboid.api.exceptions.ParameterValueException;
import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.parameters.values.ParameterValueResult;
import app.secuboid.api.parameters.values.ParameterValueResultCode;
import app.secuboid.api.parameters.values.ParameterValues;
import app.secuboid.api.reflection.ParameterValueRegistered;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.reflection.PluginLoader;
import app.secuboid.core.storage.rows.ParameterValueRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static app.secuboid.api.parameters.values.ParameterValueResultCode.*;
import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public class ParameterValuesImpl implements ParameterValues {

    private final Map<String, Class<? extends ParameterValue>> nameLowerToClass;

    private final Map<Class<? extends ParameterValue>, ParameterValueRegistered> classToAnnotation;
    private final Map<Class<? extends ParameterValue>, Map<String, ParameterValue>> classToValueToParameterValue;

    public ParameterValuesImpl() {
        nameLowerToClass = new HashMap<>();
        classToAnnotation = new HashMap<>();
        classToValueToParameterValue = new HashMap<>();
    }

    public void init(@NotNull PluginLoader pluginLoader) {
        // TODO unit test
        if (!nameLowerToClass.isEmpty()) {
            return;
        }

        classToAnnotation.putAll(pluginLoader.getClassToAnnotation(ParameterValueRegistered.class,
                ParameterValue.class));
        classToAnnotation.forEach((c, a) -> {
            nameLowerToClass.put(a.shortName(), c);
            nameLowerToClass.put(a.name(), c);
        });
    }

    public void load() {
        // TODO unit test
        nameLowerToClass.clear();
        classToAnnotation.clear();
        classToValueToParameterValue.clear();

        Set<ParameterValueRow> parameterValueRows = getStorageManager().selectAllSync(ParameterValueRow.class);

        for (ParameterValueRow parameterValueRow : parameterValueRows) {
            loadParameterValueRow(parameterValueRow);
        }
    }


    @Override
    public void grab(@NotNull String name, @Nullable String value, @Nullable Consumer<ParameterValueResult> callback) {
        // TODO implement code
        return;
    }

    private void loadParameterValueRow(@NotNull ParameterValueRow parameterValueRow) {
        ParameterValueResult parameterValueResult = createInstanceWithResult(parameterValueRow.id(), parameterValueRow.shortName(),
                parameterValueRow.value());
        ParameterValueResultCode code = parameterValueResult.code();
        ParameterValue parameterValue = parameterValueResult.parameterValue();

        if (parameterValue == null || code != SUCCESS) {
            String msg = format("Unable to load the parameter value [parameterValueResult=%s]", parameterValueResult);
            log().log(SEVERE, () -> msg);
            return;
        }

        classToValueToParameterValueAdd(parameterValue);
    }

    private @NotNull ParameterValueResult createInstanceWithResult(long id, @NotNull String name,
                                                                   @Nullable String value) {
        String nameLower = name.toLowerCase();
        Class<? extends ParameterValue> clazz = nameLowerToClass.get(nameLower);

        if (clazz == null) {
            return new ParameterValueResult(INVALID_PARAMETER, null);
        }

        ParameterValueRegistered annotation = classToAnnotation.get(clazz);

        boolean needsValue = annotation.needsValue();
        if ((needsValue && value == null) || (!needsValue && value != null)) {
            return new ParameterValueResult(INVALID_VALUE, null);
        }

        String modifiedValue;
        if (!needsValue) {
            modifiedValue = null;
        } else {
            modifiedValue = switch (annotation.characterCase()) {
                case LOWERCASE -> value.toLowerCase();
                case UPPERCASE -> value.toUpperCase();
                case CASE_SENSITIVE -> value;
            };
        }

        try {
            return new ParameterValueResult(SUCCESS, createInstance(clazz, id, modifiedValue));
        } catch (ParameterValueException e) {
            return new ParameterValueResult(INVALID_VALUE, null);
        }
    }

    @SuppressWarnings("java:S2139")
    private @NotNull ParameterValue createInstance(@NotNull Class<? extends ParameterValue> clazz, long id,
                                                   @Nullable String value) throws ParameterValueException {
        try {
            Method newInstance = clazz.getMethod("newInstance", long.class, String.class);
            return (ParameterValue) newInstance.invoke(null, id, value);
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException |
                 InvocationTargetException e) {
            if (e instanceof InvocationTargetException invocationTargetException) {
                Throwable cause = invocationTargetException.getCause();
                if (cause instanceof ParameterValueException parameterValueException) {
                    throw parameterValueException;
                }
            }
            String msg = format("Unable to create the parameter value instance: [clazz=%s, id=%s, value=%s]", clazz,
                    id, value);
            log().log(SEVERE, e, () -> msg);
            throw new ParameterValueException(msg, e);
        }
    }

    private void classToValueToParameterValueAdd(@NotNull ParameterValue parameterValue) {
        classToValueToParameterValue.computeIfAbsent(parameterValue.getClass(), k -> new HashMap<>()).put(parameterValue.getValue(), parameterValue);
    }

    private StorageManager getStorageManager() {
        return SecuboidImpl.instance().getStorageManager();
    }
}
