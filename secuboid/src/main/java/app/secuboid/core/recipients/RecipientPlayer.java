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
import app.secuboid.api.lands.Land;
import app.secuboid.api.recipients.Recipient;
import app.secuboid.api.recipients.RecipientType;
import app.secuboid.api.reflection.RecipientRegistered;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static java.lang.String.format;

@RecipientRegistered(name = "player", shortName = "p", chatColor = "\u00A76", needsValue = true, priority = 80)
public record RecipientPlayer(@NotNull RecipientType type,
                              long id,
                              @NotNull UUID uuid
) implements Recipient {

    // Needed for load from database
    public static RecipientPlayer newInstance(@NotNull RecipientType type, long id, @NotNull String value) throws RecipientException {
        UUID uuid;

        try {
            uuid = UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            String msg = format(
                    "Not a player UUID: Wrong name in the database or Bukkit API is changed? [uuid=%s]",
                    value);
            throw new RecipientException(msg, e);
        }

        return new RecipientPlayer(type, id, uuid);
    }

    public String getPlayerName() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        if (!offlinePlayer.hasPlayedBefore()) {
            return uuid.toString();
        }

        return offlinePlayer.getName();
    }

    @Override
    public @NotNull String getValue() {
        return uuid.toString();
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity) {
        if (entity instanceof Player player) {
            return player.getUniqueId().equals(uuid);
        }

        return false;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand) {
        return hasAccess(entity);
    }
}
