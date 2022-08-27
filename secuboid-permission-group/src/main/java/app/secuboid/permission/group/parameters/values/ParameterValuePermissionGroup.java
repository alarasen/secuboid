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
package app.secuboid.permission.group.parameters.values;

import app.secuboid.api.exceptions.ParameterValueException;
import app.secuboid.api.lands.Land;
import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.reflection.ParameterValueRegistered;
import app.secuboid.permission.group.SecuboidPermissionGroupPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents any players member of the specific Bukkit permission system group.
 */
@ParameterValueRegistered(name = "permission-group", shortName = "pg", chatColor = "\u00A77", priority = 70)
public class ParameterValuePermissionGroup implements ParameterValue {

    private static final String NAME = ParameterValuePermissionGroup.class.getAnnotation(ParameterValueRegistered.class)
            .name();
    private static final String SHORT_NAME = ParameterValuePermissionGroup.class
            .getAnnotation(ParameterValueRegistered.class).shortName();
    private static final String CHAT_COLOR = ParameterValuePermissionGroup.class
            .getAnnotation(ParameterValueRegistered.class).chatColor();
    private static final int PRIORITY = ParameterValuePermissionGroup.class
            .getAnnotation(ParameterValueRegistered.class).priority();

    private final String group;

    private long id;

    public ParameterValuePermissionGroup(@NotNull String group) {
        this.group = group;
        id = ID_NON_CREATED_VALUE;
    }

    // Needed for load from database
    public static ParameterValuePermissionGroup newInstance(@NotNull String value) throws ParameterValueException {
        return new ParameterValuePermissionGroup(value);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
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
        return group;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity) {
        if (entity instanceof Player player) {
            return SecuboidPermissionGroupPlugin.getPermission().playerInGroup(player, group);
        }

        return false;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand) {
        return hasAccess(entity);
    }

    @Override
    public String toString() {
        return "{" +
                " group='" + group + "'" +
                ", id='" + getId() + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParameterValuePermissionGroup parameterValuePermissionGroup)) {
            return false;
        }

        return Objects.equals(group, parameterValuePermissionGroup.group) && id == parameterValuePermissionGroup.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, id);
    }
}
