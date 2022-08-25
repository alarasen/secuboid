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
package me.tabinol.secuboid.api.players;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Contains the player informations.
 */
public interface PlayerInfos {

    /**
     * Gets the informations for a command sender.
     *
     * @param sender the command sender
     * @return the command sender information
     */
    CommandSenderInfo get(CommandSender sender);

    /**
     * Gets the informations for a specific player.
     *
     * @param player the player
     * @return the player information
     */
    PlayerInfo getPlayerInfo(Player player);

    /**
     * Gets the informations for all command senders.
     *
     * @return the player informations
     */
    Collection<CommandSenderInfo> getAll();
}
