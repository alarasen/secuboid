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
import app.secuboid.api.parameters.values.*;
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
import static app.secuboid.api.storage.rows.RowWithId.NON_EXISTING_ID;
import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

public class ParameterValuesImpl implements ParameterValues {

    private final Map<String, ParameterValueType> nameLowerToType;

    private final Map<ParameterValueType, ParameterValueRegistered> typeToAnnotation;
    private final Map<ParameterValueType, Map<String, ParameterValue>> typeToValueToParameterValue;

    public ParameterValuesImpl() {
        nameLowerToType = new HashMap<>();
        typeToAnnotation = new HashMap<>();
        typeToValueToParameterValue = new HashMap<>();
    }

    public void init(@NotNull PluginLoader pluginLoader) {
        if (!nameLowerToType.isEmpty() || !typeToAnnotation.isEmpty()) {
            return;
        }

        Map<Class<? extends ParameterValue>, ParameterValueRegistered> classToAnnotation = pluginLoader.getClassToAnnotation(ParameterValueRegistered.class, ParameterValue.class);

        classToAnnotation.forEach((c, a) -> {
            ParameterValueType type = new ParameterValueType(c, a);
            typeToAnnotation.put(type, a);
            nameLowerToType.put(a.shortName(), type);
            nameLowerToType.put(a.name(), type);
        });
    }

    public void load() {
        typeToValueToParameterValue.clear();

        Set<ParameterValueRow> parameterValueRows = getStorageManager().selectAllSync(ParameterValueRow.class);

        for (ParameterValueRow parameterValueRow : parameterValueRows) {
            loadParameterValueRow(parameterValueRow);
        }
    }

    public void grab(@NotNull String name, @Nullable String value, @Nullable Consumer<ParameterValueResult> callback) {
        String nameLower = name.toLowerCase();

        // Pre-validation
        ParameterValueResult result = grabInstanceWithResult(NON_EXISTING_ID, nameLower, value);
        if (result.code() != SUCCESS || result.parameterValue() == null) {
            if (callback != null) {
                callback.accept(result);
            } else {
                log().log(WARNING, () -> format("Non success parameter Value [name=%s, value=%s, " +
                        "result=%s]", name, value, result));
            }
            return;
        }


        ParameterValue parameterValue = result.parameterValue();
        ParameterValueRegistered info = parameterValue.type().info();
        String shortName = info.shortName();
        String valueTrans = parameterValue.getValue();
        ParameterValueRow parameterValueRow = new ParameterValueRow(NON_EXISTING_ID, shortName, valueTrans);

        getStorageManager().insert(parameterValueRow, r -> insertCallback(r, callback));
    }

    private void insertCallback(@NotNull ParameterValueRow parameterValueRow, @Nullable Consumer<ParameterValueResult> callback) {
        ParameterValueResult result = grabInstanceWithResult(parameterValueRow.id(),
                parameterValueRow.shortName(), parameterValueRow.value());

        if (callback != null) {
            callback.accept(result);
        }
    }

    private void loadParameterValueRow(@NotNull ParameterValueRow parameterValueRow) {
        ParameterValueResult result = grabInstanceWithResult(parameterValueRow.id(),
                parameterValueRow.shortName(), parameterValueRow.value());
        ParameterValueResultCode code = result.code();
        ParameterValue parameterValue = result.parameterValue();

        if (parameterValue == null || code != SUCCESS) {
            String msg = format("Unable to load the parameter value [result=%s]", result);
            log().log(SEVERE, () -> msg);
        }
    }

    private @NotNull ParameterValueResult grabInstanceWithResult(long id, @NotNull String nameLower,
                                                                 @Nullable String value) {
        ParameterValueType type = nameLowerToType.get(nameLower);

        if (type == null) {
            return new ParameterValueResult(INVALID_PARAMETER, null);
        }

        ParameterValueRegistered annotation = typeToAnnotation.get(type);

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

        ParameterValue parameterValue;

        if (id != NON_EXISTING_ID) {
            parameterValue = classToValueToParameterValueGet(type, modifiedValue);
            if (parameterValue != null) {
                return new ParameterValueResult(SUCCESS, parameterValue);
            }
        }

        try {
            parameterValue = createInstance(type, id, modifiedValue);
        } catch (ParameterValueException e) {
            return new ParameterValueResult(INVALID_VALUE, null);
        }

        if (id != NON_EXISTING_ID) {
            typeToValueToParameterValueAdd(parameterValue);
        }

        return new ParameterValueResult(SUCCESS, parameterValue);
    }

    @SuppressWarnings("java:S2139")
    private @NotNull ParameterValue createInstance(ParameterValueType type, long id,
                                                   @Nullable String value) throws ParameterValueException {
        Class<? extends ParameterValue> clazz = type.clazz();
        try {
            Method newInstance = clazz.getMethod("newInstance", ParameterValueType.class, long.class, String.class);
            return (ParameterValue) newInstance.invoke(null, type, id, value);
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

    private void typeToValueToParameterValueAdd(@NotNull ParameterValue parameterValue) {
        typeToValueToParameterValue.computeIfAbsent(parameterValue.type(), k -> new HashMap<>()).put(parameterValue.getValue(), parameterValue);
    }

    private @Nullable ParameterValue classToValueToParameterValueGet(ParameterValueType type,
                                                                     @Nullable String modifiedValue) {
        Map<String, ParameterValue> valueToParameterValue = typeToValueToParameterValue.get(type);
        if (valueToParameterValue != null) {
            return valueToParameterValue.get(modifiedValue);
        }

        return null;
    }

    StorageManager getStorageManager() {
        return SecuboidImpl.instance().getStorageManager();
    }
}
