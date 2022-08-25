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
import me.tabinol.secuboid.api.lands.areas.CuboidAreaForm;
import me.tabinol.secuboid.api.players.CommandSenderInfo;
import me.tabinol.secuboid.api.reflection.CommandRegistered;
import me.tabinol.secuboid.core.players.PlayerInfoImpl;
import me.tabinol.secuboid.core.selection.PlayerSelection;
import me.tabinol.secuboid.core.selection.SelectionMoveType;

@CommandRegistered( //
        pluginClass = SecuboidCorePlugin.class, //
        name = "cuboid", //
        aliases = "cub", //
        allowConsole = false, //
        sourceActionFlags = "land-create" //
)
public class CommandSelectCuboid implements CommandExec {

    Secuboid secuboid;

    public CommandSelectCuboid(Secuboid secuboid) {
        this.secuboid = secuboid;
    }

    @Override
    public void commandExec(CommandSenderInfo commandSenderInfo, String[] subArgs) {
        PlayerSelection playerSelection = ((PlayerInfoImpl) commandSenderInfo).getPlayerSelection();
        playerSelection.createVisualSelection(CuboidAreaForm.class, SelectionMoveType.EXPAND);
    }
}
