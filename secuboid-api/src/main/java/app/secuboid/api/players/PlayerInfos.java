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
package app.secuboid.api.players;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Contains the player informations.
 */
public interface PlayerInfos {

    /**
     * Gets the information for a command sender.
     *
     * @param sender the command sender
     * @return the command sender information
     */
    @Nullable CommandSenderInfo get(CommandSender sender);

    /**
     * Gets the information for a specific player.
     *
     * @param player the player
     * @return the player information
     */
    @Nullable PlayerInfo getPlayerInfo(Player player);

    /**
     * Gets the information for all command senders.
     *
     * @return the player information
     */
    @NotNull Collection<CommandSenderInfo> getAll();
}
