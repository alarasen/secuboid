/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.secuboid.api.parameters.values;

import org.bukkit.entity.Entity;

import me.tabinol.secuboid.api.lands.Land;
import me.tabinol.secuboid.api.reflection.ParameterValueRegistered;
import me.tabinol.secuboid.api.storage.tables.RowWithId;

/**
 * Represents a parameter value (ex: entity, mob, players, ...) If you
 * implements a new parameter value, you need to add the annotation
 * ParameterValueRegistered and add it to secuboid-plugin.yml. You need also to
 * create a static method for a new instance with a throw if something goes
 * wrong:
 * 
 * <pre>
 * public static ParamaterValueMy newInstance(String value) throws ParameterValueException {
 *     // return INSTANCE; // If single instance with no value
 *     return new ParameterValueMy(value);
 *     // SecuboidRuntimeException will be catched be the requester
 * }
 * </pre>
 */
public interface ParameterValue extends RowWithId {

    /**
     * Gets the parameter name (not the value). Ex: PLAYER, ENTITYTYPE, etc. It's
     * recommended to override this method with a static value for speed.
     * 
     * @return the parameter name
     */
    default String getName() {
        return this.getClass().getAnnotation(ParameterValueRegistered.class).name();
    }

    /**
     * Gets the short name for chat and save. Must be unique. It's recommended to
     * override this method with a static value for speed. Max 10 but please, keep 3
     * MAX when there is a value parameter.
     * 
     * @return the short name
     */
    default String getShortName() {
        return this.getClass().getAnnotation(ParameterValueRegistered.class).shortName();
    }

    /**
     * Gets the chat color for this parameter value. It's recommended to override
     * this method with a static value for speed.
     * 
     * @return the chat color
     */
    default String getChatColor() {
        return this.getClass().getAnnotation(ParameterValueRegistered.class).chatColor();
    }

    /**
     * Gets the priority of this parameter value. It's recommended to override this
     * method with a static value for speed.
     * 
     * @return the priority
     */
    default int getPriority() {
        return this.getClass().getAnnotation(ParameterValueRegistered.class).priority();
    }

    /**
     * Gets the extra value parameter in String format. If there is no extra value
     * parameter, null will be returned
     * 
     * @return the extra value parameter or null
     */
    String getValue();

    /**
     * Return if the player has access. This command does not look for
     * inheritance. If this is land relative, it will return false.
     *
     * @param entity the entity
     * @return true if the entity has access
     */
    boolean hasAccess(Entity entity);

    /**
     * Return if the player has access from a land.
     *
     * @param entity     the entity
     * @param originLand the land where we are looking from
     * @return true if the entity has access
     */
    boolean hasAccess(Entity entity, Land originLand);
}
