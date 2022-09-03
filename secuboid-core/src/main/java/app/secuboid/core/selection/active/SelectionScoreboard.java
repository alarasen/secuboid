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

import static app.secuboid.core.messages.Message.message;

class SelectionScoreboard {

    private static final String MESSAGE_PATH_MOVE_TYPE_PREFIX = "selection.scoreboard.selection-types.";

    private static final String COMMAND_CREATE = "/sd create";

    private static final Map<Class<? extends ActiveSelectionModify>, String> CLASS_TO_MESSAGE_TAG = Map.of(
            ActiveSelectionModifyExpand.class, "expand",
            ActiveSelectionModifyMove.class, "move",
            ActiveSelectionModifyPassive.class, "passive",
            ActiveSelectionModifyRetract.class, "retract");

    private final SecuboidScoreboard scoreboard;
    private final AreaForm areaForm;

    SelectionScoreboard(@NotNull Player player, @NotNull AreaForm areaForm, @NotNull Class<? extends ActiveSelectionModify> activeSelectionModifyClass) {
        this.areaForm = areaForm;

        String title = message().get(MessageType.TITLE, MessagePaths.selectionScoreboardTitleCreate());
        String selectionTypeMsg = getSelectionTypeMsg(activeSelectionModifyClass);
        String[] lines = new String[5];
        lines[0] = message().get(MessageType.NORMAL,
                MessagePaths.selectionScoreboardSelectionType(selectionTypeMsg));
        lines[1] = message().get(MessageType.NORMAL, areaForm.getMessagePath());
        long volume = areaForm.getVolume();
        lines[2] = message().get(MessageType.NORMAL, MessagePaths.selectionScoreboardVolume(volume));
        lines[3] = "";
        lines[4] = message().get(MessageType.NORMAL, MessagePaths.selectionScoreboardTypeWhenDone(COMMAND_CREATE));

        scoreboard = new SecuboidScoreboard(player, title, lines);
    }

    void update() {
        long volume = areaForm.getVolume();
        String line1 = message().get(MessageType.NORMAL, areaForm.getMessagePath());
        scoreboard.changeline(1, line1);
        String line2 = message().get(MessageType.NORMAL, MessagePaths.selectionScoreboardVolume(volume));
        scoreboard.changeline(2, line2);
    }

    void hide() {
        scoreboard.hide();
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
