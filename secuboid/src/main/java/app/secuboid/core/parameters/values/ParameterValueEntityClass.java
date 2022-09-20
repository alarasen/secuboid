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
import app.secuboid.api.lands.Land;
import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.reflection.ParameterValueRegistered;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

@ParameterValueRegistered(name = "entity-class", shortName = "ec", chatColor = "\u00A75", priority = 40)
public record ParameterValueEntityClass(
        long id,
        @NotNull Class<? extends Entity> entityClass
) implements ParameterValue {

    private static final String ENTITY_PREFIX = "org.bukkit.entity.";

    private static final String NAME = ParameterValueEntityClass.class.getAnnotation(ParameterValueRegistered.class)
            .name();
    private static final String SHORT_NAME = ParameterValueEntityClass.class
            .getAnnotation(ParameterValueRegistered.class).shortName();
    private static final String CHAT_COLOR = ParameterValueEntityClass.class
            .getAnnotation(ParameterValueRegistered.class).chatColor();
    private static final int PRIORITY = ParameterValueEntityClass.class.getAnnotation(ParameterValueRegistered.class)
            .priority();

    // Needed for load from database
    public static ParameterValueEntityClass newInstance(long id, @NotNull String value) throws ParameterValueException {
        String entityClassStr;
        if (value.contains(".")) {
            entityClassStr = value;
        } else {
            entityClassStr = ENTITY_PREFIX + value;
        }

        Class<?> entityClassUnknown;

        try {
            entityClassUnknown = Class.forName(entityClassStr);
        } catch (ClassNotFoundException e) {
            String msg = format(
                    "Entity class not found: Wrong name in the database or Bukkit API is changed? [entityClass=%s]",
                    value);
            throw new ParameterValueException(msg, e);
        }

        Class<? extends Entity> entityClass;

        try {
            entityClass = entityClassUnknown.asSubclass(Entity.class);
        } catch (ClassCastException e) {
            String msg = format(
                    "Not an entity class: Wrong name in the database or Bukkit API is changed? [entityClass=%s]",
                    value);
            throw new ParameterValueException(msg, e);
        }

        return new ParameterValueEntityClass(id, entityClass);
    }

    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public @NotNull String getShortName() {
        return SHORT_NAME;
    }

    @Override
    public @NotNull String getChatColor() {
        return CHAT_COLOR;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public @NotNull String getValue() {
        String className = entityClass.getName();

        if (className.startsWith(ENTITY_PREFIX)) {
            return className.replace(ENTITY_PREFIX, "");
        }

        return className;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity) {
        return entityClass.isAssignableFrom(entity.getClass());
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand) {
        return hasAccess(entity);
    }
}
