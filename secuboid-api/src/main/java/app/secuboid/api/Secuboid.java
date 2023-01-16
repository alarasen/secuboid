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
package app.secuboid.api;

import app.secuboid.api.commands.Commands;
import app.secuboid.api.flagtypes.FlagTypes;
import app.secuboid.api.lands.Lands;
import app.secuboid.api.players.PlayerInfos;
import app.secuboid.api.recipients.Recipients;
import org.jetbrains.annotations.NotNull;

/**
 * Secuboid main api interface.
 */
public interface Secuboid {

    /**
     * Gets online player information.
     *
     * @return the player information
     */
    @NotNull PlayerInfos getPlayerInfos();

    /**
     * Gets the commands class instance.
     *
     * @return the commands class instance
     */
    @NotNull Commands getCommands();

    /**
     * Gets the flag definitions instance.
     *
     * @return the flags instance
     */
    @NotNull FlagTypes getFlagTypes();

    /**
     * Gets the recipients instance.
     *
     * @return the recipients instance
     */
    @NotNull Recipients getRecipients();

    /**
     * Gets lands.
     *
     * @return the lands
     */
    @NotNull Lands getLands();

    /**
     * For internal use only.
     *
     * @return the new instance
     */
    @NotNull NewInstance getNewInstance();
}