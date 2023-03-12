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

package app.secuboid.api.messages;

import app.secuboid.api.services.Service;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the Secuboid plugin and extensions message service.
 */
public interface MessageService extends Service {

    /**
     * Grab a message manager for a specific Secuboid plugin. {@link Service#onEnable(boolean)} loads and must be
     * used before accessing the messages.
     *
     * @param plugin the Secuboid plugin
     * @return the message manager
     */
    @NotNull MessageManagerService grab(@NotNull Plugin plugin);
}