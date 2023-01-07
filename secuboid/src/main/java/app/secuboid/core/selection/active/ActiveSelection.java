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
package app.secuboid.core.selection.active;

import app.secuboid.api.lands.WorldLand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class ActiveSelection {

    protected final @NotNull WorldLand worldLand;

    protected final @NotNull CommandSender commandSender;

    protected ActiveSelection(@NotNull WorldLand worldLand, @NotNull CommandSender commandSender) {
        this.worldLand = worldLand;
        this.commandSender = commandSender;
    }

    public final @NotNull WorldLand getWorldLand() {
        return worldLand;
    }

    public final @NotNull CommandSender getCommandSender() {
        return commandSender;
    }

    public void init() {
        // Override if needed
    }

    public void playerMoveSelection() {
        // Override if needed
    }

    public void removeSelection() {
        // Override if needed
    }
}
