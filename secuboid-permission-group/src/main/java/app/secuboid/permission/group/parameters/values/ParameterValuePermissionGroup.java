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

/**
 * Represents any players member of the specific Bukkit permission system group.
 */
@ParameterValueRegistered(name = "permission-group", shortName = "pg", chatColor = "\u00A77", needsValue = true,
        priority = 70)
public record ParameterValuePermissionGroup(
        long id,
        @NotNull String group
) implements ParameterValue {

    // Needed for load from database
    @SuppressWarnings("java:S1130")
    public static ParameterValuePermissionGroup newInstance(long id, @NotNull String value) throws ParameterValueException {
        return new ParameterValuePermissionGroup(id, value);
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
}
