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
package app.secuboid.core.commands.exec;

import app.secuboid.api.Secuboid;
import app.secuboid.api.SecuboidCorePlugin;
import app.secuboid.api.commands.CommandExec;
import app.secuboid.api.lands.areas.CuboidAreaForm;
import app.secuboid.api.players.CommandSenderInfo;
import app.secuboid.api.reflection.CommandRegistered;
import app.secuboid.core.players.PlayerInfoImpl;
import app.secuboid.core.selection.PlayerSelection;
import app.secuboid.core.selection.SelectionMoveType;

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