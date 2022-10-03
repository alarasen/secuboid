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

import app.secuboid.api.lands.Land;
import app.secuboid.api.storage.rows.WithId;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a parameter value (ex: entity, mob, players, ...) If you
 * implements a new parameter value, you need to add the annotation
 * ParameterValueRegistered and add it to secuboid-plugin.yml. You need also to
 * create a static method for a new instance from the database with a throw if something goes
 * wrong:
 *
 * <pre>
 * public static ParameterValueMy newInstance(ParameterValueType type, long id, String value)
 *         throws ParameterValueException {
 *     // return ParameterValueMy(type); // If single instance with no value
 *     return new ParameterValueMy(type, value);
 *     // ParameterValueException will be caught be the requester
 * }
 * </pre>
 */
public interface ParameterValue extends WithId {

    /**
     * Gets the parameter value type.
     *
     * @return the parameter value type
     */
    @NotNull ParameterValueType type();

    /**
     * Gets the extra value parameter in String format. If there is no extra value
     * parameter, null will be returned.
     *
     * @return the extra value parameter or null
     */
    @Nullable String getValue();

    /**
     * Return if the player has access. This command does not look for
     * inheritance. If this is land relative, it will return false.
     *
     * @param entity the entity
     * @return true if the entity has access
     */
    boolean hasAccess(@NotNull Entity entity);

    /**
     * Return if the player has access from a land.
     *
     * @param entity     the entity
     * @param originLand the land where we are looking from
     * @return true if the entity has access
     */
    boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand);
}
