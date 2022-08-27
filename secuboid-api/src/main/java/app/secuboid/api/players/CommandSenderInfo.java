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

import app.secuboid.api.parameters.values.ParameterValuePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Contains informations for a command sender.
 */
public interface CommandSenderInfo {

    /**
     * Gets the command sender for this player.
     *
     * @return the command sender
     */
    @NotNull CommandSender getSender();

    /**
     * Gets the player name.
     *
     * @return the player name
     */
    @NotNull String getName();

    /**
     * Gets the parameter value for this player.
     *
     * @return the player container
     */
    @NotNull ParameterValuePlayer getParameterValue();

    /**
     * Is the player admin mode?
     *
     * @return true if the player is admin mode
     */
    boolean isAdminMode();

}
