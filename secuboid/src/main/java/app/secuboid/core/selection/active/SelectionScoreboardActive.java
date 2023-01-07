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

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.messages.MessagePath;
import app.secuboid.api.messages.MessageType;
import app.secuboid.core.messages.MessagePaths;
import app.secuboid.core.scoreboard.SecuboidScoreboard;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static app.secuboid.core.messages.Log.log;
import static app.secuboid.core.messages.Message.message;
import static java.util.logging.Level.WARNING;

class SelectionScoreboardActive extends SelectionScoreboard {

    private static final String MESSAGE_PATH_MOVE_TYPE_PREFIX = "selection.scoreboard.active.selection-types.";
    private static final String COMMAND_CREATE = "/sd create";
    private static final Map<Class<? extends ActiveSelectionModify>, String> CLASS_TO_MESSAGE_TAG = Map.of(
            ActiveSelectionModifyExpand.class, "expand",
            ActiveSelectionModifyMove.class, "move",
            ActiveSelectionModifyPassive.class, "passive",
            ActiveSelectionModifyRetract.class, "retract");

    private final @NotNull AreaForm areaForm;
    private final @NotNull Class<? extends ActiveSelectionModify> activeSelectionModifyClass;

    SelectionScoreboardActive(@NotNull Player player, @NotNull AreaForm areaForm, @NotNull Class<?
            extends ActiveSelectionModify> activeSelectionModifyClass) {
        super(player);
        this.areaForm = areaForm;
        this.activeSelectionModifyClass = activeSelectionModifyClass;
    }

    @Override
    void init() {
        String title = message().get(MessageType.TITLE, MessagePaths.selectionScoreboardActiveTitleCreate());
        String selectionTypeMsg = getSelectionTypeMsg(activeSelectionModifyClass);
        String[] lines = new String[5];
        lines[0] = message().get(MessageType.NORMAL,
                MessagePaths.selectionScoreboardActiveSelectionType(selectionTypeMsg));
        lines[1] = message().get(MessageType.NORMAL, areaForm.getMessagePath());
        long volume = areaForm.getVolume();
        lines[2] = message().get(MessageType.NORMAL, MessagePaths.selectionScoreboardActiveVolume(volume));
        lines[3] = "";
        lines[4] = message().get(MessageType.NORMAL, MessagePaths.selectionScoreboardActiveTypeWhenDone(COMMAND_CREATE));

        scoreboard = new SecuboidScoreboard(player, title, lines);
        scoreboard.init();
    }

    @Override
    void update() {
        if (scoreboard == null) {
            log().log(WARNING, "No scoreboard to update for the player: {}", player.getName());
            return;
        }

        long volume = areaForm.getVolume();
        String line1 = message().get(MessageType.NORMAL, areaForm.getMessagePath());
        scoreboard.changeLine(1, line1);
        String line2 = message().get(MessageType.NORMAL, MessagePaths.selectionScoreboardActiveVolume(volume));
        scoreboard.changeLine(2, line2);
    }

    private @NotNull String getSelectionTypeMsg(@NotNull Class<? extends ActiveSelectionModify> activeSelectionModifyClass) {
        String msgTag = CLASS_TO_MESSAGE_TAG.get(activeSelectionModifyClass);

        if (msgTag == null) {
            throw new SecuboidRuntimeException("Message for this class not implemented: " + activeSelectionModifyClass.getSimpleName());
        }

        String path = MESSAGE_PATH_MOVE_TYPE_PREFIX + msgTag;
        MessagePath messagePath = new MessagePath(path, new String[]{}, new Object[]{});
        return message().get(MessageType.NO_COLOR, messagePath);
    }
}
