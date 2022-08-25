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

import java.util.UUID;

import org.bukkit.entity.Player;

import app.secuboid.api.lands.Land;

/**
 * Contains informations for a specific player.
 */
public interface PlayerInfo extends CommandSenderInfo {

    /**
     * Gets the Bukkit player instance.
     *
     * @return the player (or null for the console)
     */
    Player getPlayer();

    /**
     * Gets the player UUID.
     * 
     * @return the player UUID
     */
    UUID getUUID();

    /**
     * Sets the player in Admin Mode
     * 
     * @param value true for Admin Mode
     */
    void setAdminMode(boolean value);

    /**
     * Gets the last land where the player was.
     *
     * @return the last land
     */
    Land getLastLand();

    /**
     * Gets the selection in Y top.
     * 
     * @return the Y to
     */
    int getSelectionTop();

    /**
     * Gets the selection in Y bottom.
     * 
     * @return the Y bottom
     */
    int getSelectionBottom();
}
