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
import app.secuboid.api.SecuboidPlugin;
import app.secuboid.api.commands.CommandExec;
import app.secuboid.api.lands.LandResult;
import app.secuboid.api.lands.Lands;
import app.secuboid.api.players.CommandSenderInfo;
import app.secuboid.api.players.ConsoleCommandSenderInfo;
import app.secuboid.api.reflection.CommandRegistered;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.players.CommandSenderInfoImpl;
import app.secuboid.core.selection.SenderSelection;
import app.secuboid.core.selection.active.ActiveSelection;
import org.jetbrains.annotations.NotNull;

@CommandRegistered( //
        pluginClass = SecuboidPlugin.class, //
        name = "create", //
        sourceActionFlags = "land-create" //
)
public class CommandCreate implements CommandExec {

    Secuboid secuboid;

    public CommandCreate(Secuboid secuboid) {
        this.secuboid = secuboid;
    }

    @Override
    public void commandExec(@NotNull CommandSenderInfo commandSenderInfo, @NotNull String[] subArgs) {
        SenderSelection selection = ((CommandSenderInfoImpl) commandSenderInfo).getSelection();
        ActiveSelection activeSelection = selection.getActiveSelection();

        if (activeSelection == null) {
            // TODO message need active selection
            return;
        }

        if (subArgs.length == 0) {
            if (commandSenderInfo instanceof ConsoleCommandSenderInfo) {
                // TODO message you need parameter
                return;
            }

            ((SecuboidImpl) secuboid).getChatGetter().put(commandSenderInfo, s -> createLand(commandSenderInfo,
                    activeSelection, s));
            return;
        }

        if (subArgs.length > 1) {
            // TODO message no space in name
            return;
        }

        createLand(commandSenderInfo, activeSelection, subArgs[0]);
    }

    private void createLand(@NotNull CommandSenderInfo commandSenderInfo,
                            @NotNull ActiveSelection activeSelection, @NotNull String landName) {
        if (landName.contains(" ")) {
            // TODO message no space in name
            return;
        }

        Lands lands = secuboid.getLands();
        //lands.createLand(worldLand, landName, owner, area, this::landCreationCallBack);

    }

    public void landCreationCallBack(@NotNull LandResult landResult) {
    }
}
