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
package app.secuboid.permission.group.recipients;

import app.secuboid.api.exceptions.RecipientException;
import app.secuboid.api.lands.Land;
import app.secuboid.api.recipients.Recipient;
import app.secuboid.api.recipients.RecipientType;
import app.secuboid.api.reflection.RecipientRegistered;
import app.secuboid.permission.group.SecuboidPermissionGroupPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents any players member of the specific Bukkit permission system group.
 */
@RecipientRegistered(name = "permission-group", shortName = "pg", chatColor = "\u00A77", needsValue = true,
        priority = 70)
public record RecipientPermissionGroup(
        @NotNull RecipientType type,
        long id,
        @NotNull String group
) implements Recipient {

    // Needed for load from database
    @SuppressWarnings("java:S1130")
    public static RecipientPermissionGroup newInstance(@NotNull RecipientType type, long id,
                                                       @NotNull String value) throws RecipientException {
        return new RecipientPermissionGroup(type, id, value);
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
