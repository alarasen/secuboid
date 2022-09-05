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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents every players en entities.
 *
 * @param id the database id
 */
@ParameterValueRegistered(name = "everything", shortName = "everything", chatColor = "\u00A7F", priority = 30)
public record ParameterValueEverything(
        long id
) implements ParameterValue {

    private static final String NAME = ParameterValueEverything.class.getAnnotation(ParameterValueRegistered.class)
            .name();
    private static final String SHORT_NAME = ParameterValueEverything.class
            .getAnnotation(ParameterValueRegistered.class)
            .shortName();
    private static final String CHAT_COLOR = ParameterValueEverything.class
            .getAnnotation(ParameterValueRegistered.class)
            .chatColor();
    private static final int PRIORITY = ParameterValueEverything.class.getAnnotation(ParameterValueRegistered.class)
            .priority();

    // Needed for load from database
    @SuppressWarnings({"java:S1172", "java:S1130"})
    public static ParameterValueEverything newInstance(long id, @Nullable String value) throws ParameterValueException {
        return new ParameterValueEverything(id);
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
    public @Nullable String getValue() {
        return null;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity) {
        return true;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand) {
        return true;
    }
}
