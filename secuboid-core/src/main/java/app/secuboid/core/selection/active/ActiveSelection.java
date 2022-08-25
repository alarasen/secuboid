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

import app.secuboid.api.lands.areas.Area;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveSelection {

    protected final World world;
    protected final Area originArea;
    protected final CommandSender commandSender;

    public ActiveSelection(@NotNull World world, @Nullable Area originArea, @NotNull CommandSender commandSender) {
        this.world = world;
        this.originArea = originArea;
        this.commandSender = commandSender;
    }

    @Nullable
    public final Area getOriginArea() {
        return originArea;
    }
}
