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
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.players.PlayerInfo;
import app.secuboid.core.lands.areas.AreaFormImpl;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class ActiveSelectionModifyRetract extends ActiveSelectionModify {

    private Location lastOutSideLoc;

    public ActiveSelectionModifyRetract(@NotNull WorldLand worldLand, @NotNull PlayerInfo playerInfo, @NotNull SelectionForm selectionForm) {
        super(worldLand, playerInfo, selectionForm);

        lastOutSideLoc = null;
    }

    @Override
    protected boolean playerMoveSelectionCheckChanged(@NotNull Location playerLoc) {
        AreaForm areaForm = selectionForm.getAreaForm();
        boolean isChanged = false;
        boolean isPlayerInside = areaForm.isLocationInsideSquare(playerLoc.getBlockX(), playerLoc.getBlockZ());
        if (!isPlayerInside) {
            lastOutSideLoc = playerLoc;
        }

        // Check where the player is inside the land
        if (isPlayerInside && lastOutSideLoc != null) {
            if (lastOutSideLoc.getBlockX() < areaForm.getX1() && playerLoc.getBlockX() >= areaForm.getX1()) {
                ((AreaFormImpl) areaForm).setX1(playerLoc.getBlockX() + 1);
                isChanged = true;
            } else if (lastOutSideLoc.getBlockX() > areaForm.getX2() && playerLoc.getBlockX() <= areaForm.getX2()) {
                ((AreaFormImpl) areaForm).setX2(playerLoc.getBlockX() - 1);
                isChanged = true;
            }

            if (lastOutSideLoc.getBlockZ() < areaForm.getZ1() && playerLoc.getBlockZ() >= areaForm.getZ1()) {
                ((AreaFormImpl) areaForm).setZ1(playerLoc.getBlockZ() + 1);
                isChanged = true;
            } else if (lastOutSideLoc.getBlockZ() > areaForm.getZ2() && playerLoc.getBlockZ() <= areaForm.getZ2()) {
                ((AreaFormImpl) areaForm).setZ2(playerLoc.getBlockZ() - 1);
                isChanged = true;
            }
        }

        // Negative size, put to player location
        if (areaForm.getX1() > areaForm.getX2()) {
            ((AreaFormImpl) areaForm).setX1(playerLoc.getBlockX());
            ((AreaFormImpl) areaForm).setX2(playerLoc.getBlockX());
        }

        if (areaForm.getZ1() > areaForm.getZ2()) {
            ((AreaFormImpl) areaForm).setZ1(playerLoc.getBlockZ());
            ((AreaFormImpl) areaForm).setZ2(playerLoc.getBlockZ());
        }

        return isChanged;
    }
}
