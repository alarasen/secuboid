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

import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.lands.flags.Flags;
import app.secuboid.core.lands.areas.AreaFormImpl;
import app.secuboid.core.selection.SelectionMoveType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class VisualSelection extends ActiveSelection {

    protected static final Material MATERIAL_ACTIVE = Material.SPONGE;
    protected static final Material MATERIAL_COLLISION = Material.REDSTONE_BLOCK;
    protected static final Material MATERIAL_PASSIVE = Material.IRON_BLOCK;

    protected final AreaForm areaForm;
    protected final Player player;

    protected final ChangedBlocks changedBlocks;
    protected boolean isCollision;
    protected Flags parentFlagsDetected;

    private Location lastOutSideLoc; // for land retraction

    protected VisualSelection(World world, AreaForm areaForm, Area originArea, Player player) {
        super(world, originArea, player);

        this.areaForm = areaForm;
        this.player = player;

        changedBlocks = new ChangedBlocks(player);
        isCollision = false;
        parentFlagsDetected = null;

        lastOutSideLoc = null;
    }

    public final AreaForm getAreaForm() {
        return areaForm;
    }

    public abstract boolean hasCollision();

    public abstract void removeSelection();

    @SuppressWarnings("java:S1121")
    public void playerMoveSelection(SelectionMoveType moveType) {
        Location playerLoc = player.getLocation();
        boolean isChanged;

        switch (moveType) {
            case MOVE -> isChanged = playerMoveSelectionMove(playerLoc);
            case EXPAND -> isChanged = playerMoveSelectionExpand(playerLoc);
            case RETRACT -> isChanged = playerMoveSelectionRetract(playerLoc);
            default -> isChanged = false;
        }

        if (isChanged) {
            removeSelection();
            refreshVisualSelection();
        }
    }

    protected abstract void refreshVisualSelection();

    protected int getStepX() {
        int result = ((areaForm.getX2() - areaForm.getX1()) / 32);
        return result > 1 ? result : 1;
    }

    protected int getStepZ() {
        int result = ((areaForm.getZ2() - areaForm.getZ1()) / 32);
        return result > 1 ? result : 1;
    }

    private boolean playerMoveSelectionMove(Location playerLoc) {
        boolean isChanged = false;

        // Move with player
        if (playerLoc.getBlockX() - 1 < areaForm.getX1()) {
            int diffX = areaForm.getX1() - playerLoc.getBlockX() + 1;
            ((AreaFormImpl) areaForm).setX1(areaForm.getX1() - diffX);
            ((AreaFormImpl) areaForm).setX2(areaForm.getX2() - diffX);
            isChanged = true;
        }
        if (playerLoc.getBlockX() + 1 > areaForm.getX2()) {
            int diffX = areaForm.getX2() - playerLoc.getBlockX() - 1;
            ((AreaFormImpl) areaForm).setX1(areaForm.getX1() - diffX);
            ((AreaFormImpl) areaForm).setX2(areaForm.getX2() - diffX);
            isChanged = true;
        }
        if (playerLoc.getBlockZ() - 1 < areaForm.getZ1()) {
            int diffZ = areaForm.getZ1() - playerLoc.getBlockZ() + 1;
            ((AreaFormImpl) areaForm).setZ1(areaForm.getZ1() - diffZ);
            ((AreaFormImpl) areaForm).setZ2(areaForm.getZ2() - diffZ);
            isChanged = true;
        }
        if (playerLoc.getBlockZ() + 1 > areaForm.getZ2()) {
            int diffZ = areaForm.getZ2() - playerLoc.getBlockZ() - 1;
            ((AreaFormImpl) areaForm).setZ1(areaForm.getZ1() - diffZ);
            ((AreaFormImpl) areaForm).setZ2(areaForm.getZ2() - diffZ);
            isChanged = true;
        }

        return isChanged;
    }

    private boolean playerMoveSelectionExpand(Location playerLoc) {
        boolean isChanged = false;

        // Check where the player is outside the land
        if (playerLoc.getBlockX() - 1 < areaForm.getX1()) {
            ((AreaFormImpl) areaForm).setX1(playerLoc.getBlockX() - 1);
            isChanged = true;
        }
        if (playerLoc.getBlockX() + 1 > areaForm.getX2()) {
            ((AreaFormImpl) areaForm).setX2(playerLoc.getBlockX() + 1);
            isChanged = true;
        }
        if (playerLoc.getBlockZ() - 1 < areaForm.getZ1()) {
            ((AreaFormImpl) areaForm).setZ1(playerLoc.getBlockZ() - 1);
            isChanged = true;
        }
        if (playerLoc.getBlockZ() + 1 > areaForm.getZ2()) {
            ((AreaFormImpl) areaForm).setZ2(playerLoc.getBlockZ() + 1);
            isChanged = true;
        }

        return isChanged;
    }

    private boolean playerMoveSelectionRetract(Location playerLoc) {
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
