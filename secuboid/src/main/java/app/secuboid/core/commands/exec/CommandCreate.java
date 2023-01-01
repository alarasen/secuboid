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
import app.secuboid.api.lands.LandResultCode;
import app.secuboid.api.messages.MessageType;
import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.parameters.values.ParameterValueResult;
import app.secuboid.api.parameters.values.ParameterValueResultCode;
import app.secuboid.api.parameters.values.ParameterValues;
import app.secuboid.api.players.CommandSenderInfo;
import app.secuboid.api.players.ConsoleCommandSenderInfo;
import app.secuboid.api.players.PlayerInfo;
import app.secuboid.api.reflection.CommandRegistered;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.messages.MessagePaths;
import app.secuboid.core.players.CommandSenderInfoImpl;
import app.secuboid.core.selection.SenderSelection;
import app.secuboid.core.selection.active.ActiveSelection;
import app.secuboid.core.selection.active.ActiveSelectionModify;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static app.secuboid.api.parameters.values.ParameterValues.NOBODY;
import static app.secuboid.api.parameters.values.ParameterValues.PLAYER;
import static app.secuboid.core.messages.Message.message;

@CommandRegistered(
        pluginClass = SecuboidPlugin.class,
        name = "create",
        sourceActionFlags = "land-create"
)
public class CommandCreate implements CommandExec {

    private static final String COMMAND_SELECT = "/sd select";

    private final Secuboid secuboid;

    public CommandCreate(@NotNull Secuboid secuboid) {
        this.secuboid = secuboid;
    }

    @Override
    public void commandExec(@NotNull CommandSenderInfo commandSenderInfo, @NotNull String[] subArgs) {
        SenderSelection selection = ((CommandSenderInfoImpl) commandSenderInfo).getSelection();
        ActiveSelection activeSelection = selection.getActiveSelection();
        CommandSender sender = commandSenderInfo.sender();

        if (!(activeSelection instanceof ActiveSelectionModify activeSelectionModify)) {
            message().sendMessage(sender, MessageType.ERROR, MessagePaths.selectionCreateNeedActiveSelection(COMMAND_SELECT));
            return;
        }

        if (subArgs.length == 0) {
            if (commandSenderInfo instanceof ConsoleCommandSenderInfo) {
                message().sendMessage(sender, MessageType.ERROR, MessagePaths.generalNeedParameter());
                return;
            }

            message().sendMessage(sender, MessageType.NORMAL, MessagePaths.selectionCreateEnterName());
            ((SecuboidImpl) secuboid).getChatGetter().put(commandSenderInfo, s -> landNameCallback(commandSenderInfo,
                    activeSelectionModify, s));
            return;
        }

        if (subArgs.length > 1) {
            message().sendMessage(sender, MessageType.ERROR, MessagePaths.selectionCreateNoSpace());
            return;
        }

        landNameCallback(commandSenderInfo, activeSelectionModify, subArgs[0]);
    }

    private void landNameCallback(@NotNull CommandSenderInfo commandSenderInfo,
                                  @NotNull ActiveSelectionModify activeSelectionModify, @NotNull String landName) {
        if (landName.contains(" ")) {
            CommandSender sender = commandSenderInfo.sender();
            message().sendMessage(sender, MessageType.ERROR, MessagePaths.selectionCreateNoSpace());
            return;
        }

        ParameterValues parameterValues = secuboid.getParameterValues();
        if (!commandSenderInfo.isAdminMode() && commandSenderInfo instanceof PlayerInfo playerInfo) {
            UUID uuid = playerInfo.getUUID();
            parameterValues.grab(PLAYER, uuid.toString(), r -> landOwnerCallback(commandSenderInfo,
                    activeSelectionModify, landName, r));
        } else {
            parameterValues.grab(NOBODY, null, r -> landOwnerCallback(commandSenderInfo, activeSelectionModify,
                    landName, r));
        }
    }

    private void landOwnerCallback(@NotNull CommandSenderInfo commandSenderInfo,
                                   @NotNull ActiveSelectionModify activeSelectionModify, @NotNull String landName,
                                   @NotNull ParameterValueResult result) {
        ParameterValue owner = result.parameterValue();

        if (result.code() != ParameterValueResultCode.SUCCESS || owner == null) {
            CommandSender sender = commandSenderInfo.sender();
            message().sendMessage(sender, MessageType.ERROR, MessagePaths.generalError(result.code()));
            return;
        }

        // TODO get parent
        secuboid.getLands().create(activeSelectionModify.getWorldLand(), landName, owner,
                activeSelectionModify.getSelectionForm().getAreaForm(), r -> landCreateCallback(commandSenderInfo, r));
    }

    public void landCreateCallback(@NotNull CommandSenderInfo commandSenderInfo, @NotNull LandResult landResult) {
        CommandSender sender = commandSenderInfo.sender();

        if (landResult.code() != LandResultCode.SUCCESS || landResult.areaLand() == null) {
            message().sendMessage(sender, MessageType.ERROR, MessagePaths.generalError(landResult.code()));
            return;
        }

        message().sendMessage(sender, MessageType.NORMAL,
                MessagePaths.selectionCreateCreated(landResult.areaLand().getName()));
        SenderSelection senderSelection = ((CommandSenderInfoImpl) commandSenderInfo).getSelection();
        senderSelection.removeSelection();
    }
}
