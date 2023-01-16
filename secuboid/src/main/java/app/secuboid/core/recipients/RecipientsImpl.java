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

import app.secuboid.api.exceptions.RecipientException;
import app.secuboid.api.recipients.*;
import app.secuboid.api.reflection.RecipientRegistered;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.reflection.PluginLoader;
import app.secuboid.core.storage.rows.RecipientRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static app.secuboid.api.recipients.RecipientResultCode.*;
import static app.secuboid.api.storage.rows.RowWithId.NON_EXISTING_ID;
import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

public class RecipientsImpl implements Recipients {

    private final Map<String, RecipientType> nameLowerToType;

    private final Map<RecipientType, RecipientRegistered> typeToAnnotation;
    private final Map<RecipientType, Map<String, Recipient>> typeToValueToRecipient;

    public RecipientsImpl() {
        nameLowerToType = new HashMap<>();
        typeToAnnotation = new HashMap<>();
        typeToValueToRecipient = new HashMap<>();
    }

    public void init(@NotNull PluginLoader pluginLoader) {
        if (!nameLowerToType.isEmpty() || !typeToAnnotation.isEmpty()) {
            return;
        }

        Map<Class<? extends Recipient>, RecipientRegistered> classToAnnotation = pluginLoader.getClassToAnnotation(RecipientRegistered.class, Recipient.class);

        classToAnnotation.forEach((c, a) -> {
            RecipientType type = new RecipientType(c, a);
            typeToAnnotation.put(type, a);
            nameLowerToType.put(a.shortName(), type);
            nameLowerToType.put(a.name(), type);
        });
    }

    public void load() {
        typeToValueToRecipient.clear();

        // TODO Set<RecipientRow> recipientRows = getStorageManager().selectAllSync(RecipientRow.class);
        Set<RecipientRow> recipientRows = Collections.emptySet();

        for (RecipientRow recipientRow : recipientRows) {
            loadRecipientRow(recipientRow);
        }
    }

    public void grab(@NotNull String name, @Nullable String value, @Nullable Consumer<RecipientResult> callback) {
        String nameLower = name.toLowerCase();

        // Pre-validation
        RecipientResult result = grabInstanceWithResult(NON_EXISTING_ID, nameLower, value);
        if (result.code() != SUCCESS || result.recipient() == null) {
            if (callback != null) {
                callback.accept(result);
            } else {
                log().log(WARNING, () -> format("Non success parameter Value [name=%s, value=%s, " +
                        "result=%s]", name, value, result));
            }
            return;
        }

        Recipient recipient = result.recipient();
        long id = recipient.id();

        if (id != NON_EXISTING_ID) {
            if (callback != null) {
                callback.accept(result);
            }
            return;
        }

        RecipientType type = recipient.type();
        RecipientRegistered info = type.info();
        String shortName = info.shortName();
        String valueTrans = recipient.getValue();
        RecipientRow recipientRow = new RecipientRow(NON_EXISTING_ID, shortName, valueTrans);

        getStorageManager().insert(recipientRow, r -> insertCallback(r, callback));
    }

    private void insertCallback(@NotNull RecipientRow recipientRow, @Nullable Consumer<RecipientResult> callback) {
        RecipientResult result = grabInstanceWithResult(recipientRow.id(),
                recipientRow.shortName(), recipientRow.value());

        if (callback != null) {
            callback.accept(result);
        }
    }

    private void loadRecipientRow(@NotNull RecipientRow recipientRow) {
        RecipientResult result = grabInstanceWithResult(recipientRow.id(),
                recipientRow.shortName(), recipientRow.value());
        RecipientResultCode code = result.code();
        Recipient recipient = result.recipient();

        if (recipient == null || code != SUCCESS) {
            String msg = format("Unable to load the recipient [result=%s]", result);
            log().log(SEVERE, () -> msg);
        }
    }

    private @NotNull RecipientResult grabInstanceWithResult(long id, @NotNull String nameLower,
                                                            @Nullable String value) {
        RecipientType type = nameLowerToType.get(nameLower);

        if (type == null) {
            return new RecipientResult(INVALID_PARAMETER, null);
        }

        RecipientRegistered annotation = typeToAnnotation.get(type);

        boolean needsValue = annotation.needsValue();
        if ((needsValue && value == null) || (!needsValue && value != null)) {
            return new RecipientResult(INVALID_VALUE, null);
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

        Recipient recipient;

        if (id == NON_EXISTING_ID) {
            recipient = typeToValueToRecipientGet(type, modifiedValue);
            if (recipient != null) {
                return new RecipientResult(SUCCESS, recipient);
            }
        }

        try {
            recipient = createInstance(type, id, modifiedValue);
        } catch (RecipientException e) {
            return new RecipientResult(INVALID_VALUE, null);
        }

        if (id != NON_EXISTING_ID) {
            typeToValueToRecipientAdd(recipient);
        }

        return new RecipientResult(SUCCESS, recipient);
    }

    @SuppressWarnings("java:S2139")
    private @NotNull Recipient createInstance(RecipientType type, long id,
                                              @Nullable String value) throws RecipientException {
        Class<? extends Recipient> clazz = type.clazz();
        try {
            Method newInstance = clazz.getMethod("newInstance", RecipientType.class, long.class, String.class);
            return (Recipient) newInstance.invoke(null, type, id, value);
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException |
                 InvocationTargetException e) {
            if (e instanceof InvocationTargetException invocationTargetException) {
                Throwable cause = invocationTargetException.getCause();
                if (cause instanceof RecipientException recipientException) {
                    throw recipientException;
                }
            }
            String msg = format("Unable to create the recipient instance: [clazz=%s, id=%s, value=%s]", clazz,
                    id, value);
            log().log(SEVERE, e, () -> msg);
            throw new RecipientException(msg, e);
        }
    }

    private void typeToValueToRecipientAdd(@NotNull Recipient recipient) {
        typeToValueToRecipient.computeIfAbsent(recipient.type(), k -> new HashMap<>()).put(recipient.getValue(), recipient);
    }

    private @Nullable Recipient typeToValueToRecipientGet(@NotNull RecipientType type,
                                                          @Nullable String modifiedValue) {
        Map<String, Recipient> valueToRecipient = typeToValueToRecipient.get(type);
        if (valueToRecipient != null) {
            return valueToRecipient.get(modifiedValue);
        }

        return null;
    }

    @NotNull StorageManager getStorageManager() {
        // TODO return SecuboidImpl.instance().getStorageManager();
        return null;
    }
}
