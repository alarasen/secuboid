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
package app.secuboid.api.parameters.values;

import app.secuboid.api.exceptions.ParameterValueException;
import app.secuboid.api.lands.Land;
import app.secuboid.api.reflection.ParameterValueRegistered;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

/**
 * Represents a specific entity type.
 *
 * @param id         the database id
 * @param entityType the entity type
 */
@ParameterValueRegistered(name = "entity-type", shortName = "et", chatColor = "\u00A75", priority = 60)
public record ParameterValueEntityType(
        long id,
        @NotNull EntityType entityType
) implements ParameterValue {

    private static final String NAME = ParameterValueEntityType.class.getAnnotation(ParameterValueRegistered.class)
            .name();
    private static final String SHORT_NAME = ParameterValueEntityType.class
            .getAnnotation(ParameterValueRegistered.class).shortName();
    private static final String CHAT_COLOR = ParameterValueEntityType.class
            .getAnnotation(ParameterValueRegistered.class).chatColor();
    private static final int PRIORITY = ParameterValueEntityType.class.getAnnotation(ParameterValueRegistered.class)
            .priority();


    // Needed for load from database
    public static ParameterValueEntityType newInstance(long id, @NotNull String value) throws ParameterValueException {
        EntityType entityType;

        try {
            String entityTypeStr = value.toUpperCase();
            entityType = EntityType.valueOf(entityTypeStr);
        } catch (IllegalArgumentException | NullPointerException e) {
            String msg = format(
                    "Entity type not found: Wrong name in the database or Bukkit API is changed? [entityType=%s]",
                    value);
            throw new ParameterValueException(msg, e);
        }

        return new ParameterValueEntityType(id, entityType);
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
        return entityType.name();
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity) {
        return entity.getType() == entityType;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand) {
        return hasAccess(entity);
    }
}
