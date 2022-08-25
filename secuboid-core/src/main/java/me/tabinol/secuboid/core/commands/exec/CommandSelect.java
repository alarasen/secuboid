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
package me.tabinol.secuboid.core.commands.exec;

import me.tabinol.secuboid.api.Secuboid;
import me.tabinol.secuboid.api.SecuboidCorePlugin;
import me.tabinol.secuboid.api.commands.CommandExec;
import me.tabinol.secuboid.api.players.CommandSenderInfo;
import me.tabinol.secuboid.api.reflection.CommandRegistered;

@CommandRegistered( //
        pluginClass = SecuboidCorePlugin.class, //
        name = "select", //
        aliases = "sel" //
)
public class CommandSelect implements CommandExec {

    Secuboid secuboid;

    public CommandSelect(Secuboid secuboid) {
        this.secuboid = secuboid;
    }

    @Override
    public void commandExec(CommandSenderInfo commandSenderInfo, String[] subArgs) {
        secuboid.getCommands().executeCommandClass(CommandSelectCuboid.class, commandSenderInfo, subArgs);
    }
}
