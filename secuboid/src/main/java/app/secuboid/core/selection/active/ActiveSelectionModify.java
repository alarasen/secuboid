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
import app.secuboid.api.players.PlayerInfo;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ActiveSelectionModify extends ActiveSelection {

    protected final @NotNull PlayerInfo playerInfo;
    protected final @NotNull Player player;
    protected final @NotNull SelectionForm selectionForm;

    private @Nullable SelectionScoreboard selectionScoreboard;
    private @Nullable Location playerLastLoc;

    protected ActiveSelectionModify(@NotNull WorldLand worldLand, @NotNull PlayerInfo playerInfo,
                                    @NotNull SelectionForm selectionForm) {
        super(worldLand, playerInfo.getPlayer());
        this.playerInfo = playerInfo;
        player = playerInfo.getPlayer();
        this.selectionForm = selectionForm;

        selectionScoreboard = null;
        playerLastLoc = null;
    }

    @Override
    public void init() {
        playerLastLoc = player.getLocation();
        selectionForm.refreshVisualSelection();
        selectionScoreboard = new SelectionScoreboardActive(player, selectionForm.areaForm, this.getClass());
        selectionScoreboard.init();
    }

    public @NotNull SelectionForm getSelectionForm() {
        return selectionForm;
    }

    @Override
    public final void playerMoveSelection() {
        Location playerLoc = player.getLocation();
        boolean isChanged = playerMoveSelectionCheckChanged(playerLoc);

        if (!isChanged && playerLastLoc != null && playerLoc.distanceSquared(playerLastLoc) >= 4D) {
            isChanged = true;
        }

        if (isChanged) {
            playerLastLoc = playerLoc;
            selectionForm.refreshVisualSelection();


            if (!(this instanceof ActiveSelectionModifyPassive) && selectionScoreboard != null) {
                selectionScoreboard.update();
            }
        }
    }

    @Override
    public final void removeSelection() {
        selectionForm.removeSelection();

        if (selectionScoreboard != null) {
            selectionScoreboard.hide();
        }
    }

    protected abstract boolean playerMoveSelectionCheckChanged(@NotNull Location playerLoc);
}
