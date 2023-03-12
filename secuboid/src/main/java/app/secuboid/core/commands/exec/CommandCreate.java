/*
 *  Secuboid: LandService and Protection plugin for Minecraft server
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

import app.secuboid.api.commands.CommandExec;
import app.secuboid.api.lands.LandResult;
import app.secuboid.api.lands.LandResultCode;
import app.secuboid.api.lands.LandService;
import app.secuboid.api.lands.WorldLand;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.messages.MessageManagerService;
import app.secuboid.api.messages.MessageType;
import app.secuboid.api.players.CommandSenderInfo;
import app.secuboid.api.players.ConsoleCommandSenderInfo;
import app.secuboid.api.players.PlayerInfo;
import app.secuboid.api.recipients.RecipientExec;
import app.secuboid.api.recipients.RecipientResult;
import app.secuboid.api.recipients.RecipientResultCode;
import app.secuboid.api.recipients.RecipientService;
import app.secuboid.api.registration.CommandRegistered;
import app.secuboid.api.selection.SenderSelection;
import app.secuboid.api.selection.active.ActiveSelection;
import app.secuboid.api.selection.active.ActiveSelectionModify;
import app.secuboid.core.messages.ChatGetterService;
import app.secuboid.core.messages.MessagePaths;
import app.secuboid.core.players.CommandSenderInfoImpl;
import app.secuboid.core.selection.active.ActiveSelectionModifyImpl;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static app.secuboid.api.recipients.RecipientService.NOBODY;
import static app.secuboid.api.recipients.RecipientService.PLAYER;

@CommandRegistered(
        name = "create",
        sourceActionFlags = "land-create"
)
public class CommandCreate implements CommandExec {

    private static final String COMMAND_SELECT = "/sd select";

    private final @NotNull ChatGetterService chatGetterService;
    private final @NotNull LandService landService;
    private final @NotNull MessageManagerService messageManagerService;
    private final @NotNull RecipientService recipientService;

    public CommandCreate(@NotNull ChatGetterService chatGetterService, @NotNull LandService landService,
                         @NotNull MessageManagerService messageManagerService,
                         @NotNull RecipientService recipientService) {
        this.chatGetterService = chatGetterService;
        this.landService = landService;
        this.messageManagerService = messageManagerService;
        this.recipientService = recipientService;
    }

    @Override
    public void commandExec(@NotNull CommandSenderInfo commandSenderInfo, @NotNull String[] subArgs) {
        SenderSelection selection = ((CommandSenderInfoImpl) commandSenderInfo).getSelection();
        ActiveSelection activeSelection = selection.getActiveSelection();
        CommandSender sender = commandSenderInfo.sender();

        // TODD Remove this check and use annotation check
        if (!(activeSelection instanceof ActiveSelectionModify activeSelectionModify)) {
            messageManagerService.sendMessage(sender, MessageType.ERROR, MessagePaths.selectionCreateNeedActiveSelection(COMMAND_SELECT));
            return;
        }

        if (subArgs.length == 0) {
            if (commandSenderInfo instanceof ConsoleCommandSenderInfo) {
                messageManagerService.sendMessage(sender, MessageType.ERROR, MessagePaths.generalNeedParameter());
                return;
            }

            messageManagerService.sendMessage(sender, MessageType.NORMAL, MessagePaths.selectionCreateEnterName());
            chatGetterService.put(commandSenderInfo, s -> landNameCallback(commandSenderInfo, activeSelectionModify,
                    s));
            return;
        }

        if (subArgs.length > 1) {
            messageManagerService.sendMessage(sender, MessageType.ERROR, MessagePaths.selectionCreateNoSpace());
            return;
        }

        landNameCallback(commandSenderInfo, activeSelectionModify, subArgs[0]);
    }

    private void landNameCallback(@NotNull CommandSenderInfo commandSenderInfo,
                                  @NotNull ActiveSelectionModify activeSelectionModify, @NotNull String landName) {
        if (landName.contains(" ")) {
            CommandSender sender = commandSenderInfo.sender();
            messageManagerService.sendMessage(sender, MessageType.ERROR, MessagePaths.selectionCreateNoSpace());
            return;
        }

        if (!commandSenderInfo.isAdminMode() && commandSenderInfo instanceof PlayerInfo playerInfo) {
            UUID uuid = playerInfo.getUUID();
            recipientService.grab(PLAYER, uuid.toString(), r -> landOwnerCallback(commandSenderInfo,
                    activeSelectionModify, landName, r));
        } else {
            recipientService.grab(NOBODY, null, r -> landOwnerCallback(commandSenderInfo, activeSelectionModify,
                    landName, r));
        }
    }

    private void landOwnerCallback(@NotNull CommandSenderInfo commandSenderInfo,
                                   @NotNull ActiveSelectionModify activeSelectionModify, @NotNull String landName,
                                   @NotNull RecipientResult result) {
        RecipientExec owner = result.recipientExec();

        if (result.code() != RecipientResultCode.SUCCESS || owner == null) {
            CommandSender sender = commandSenderInfo.sender();
            messageManagerService.sendMessage(sender, MessageType.ERROR, MessagePaths.generalError(result.code()));
            return;
        }

        // TODO get parent
        WorldLand worldLand = activeSelectionModify.getWorldLand();
        AreaForm areaForm = ((ActiveSelectionModifyImpl) activeSelectionModify).getSelectionForm().getAreaForm();
        landService.create(worldLand, landName, owner, areaForm, r -> landCreateCallback(commandSenderInfo, r));
    }

    public void landCreateCallback(@NotNull CommandSenderInfo commandSenderInfo, @NotNull LandResult landResult) {
        CommandSender sender = commandSenderInfo.sender();

        if (landResult.code() != LandResultCode.SUCCESS || landResult.areaLand() == null) {
            messageManagerService.sendMessage(sender, MessageType.ERROR, MessagePaths.generalError(landResult.code()));
            return;
        }

        messageManagerService.sendMessage(sender, MessageType.NORMAL,
                MessagePaths.selectionCreateCreated(landResult.areaLand().getName()));
        SenderSelection senderSelection = commandSenderInfo.getSelection();
        senderSelection.removeSelection();
    }
}
