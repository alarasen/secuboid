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

import app.secuboid.api.lands.Land;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SelectionForm {

    protected static final Material MATERIAL_ACTIVE = Material.SPONGE;
    protected static final Material MATERIAL_COLLISION = Material.REDSTONE_BLOCK;
    protected static final Material MATERIAL_AREA = Material.IRON_BLOCK;

    protected final @NotNull AreaForm areaForm;
    protected final @NotNull Player player;
    protected final boolean isResizeable;
    protected final @Nullable Land originLand;
    protected final @Nullable Area originArea;
    protected final @NotNull World world;

    protected @NotNull ChangedBlocks changedBlocks;
    protected boolean hasCollision;

    SelectionForm(@NotNull AreaForm areaForm, @NotNull Player player, boolean isResizeable, @Nullable Land originLand,
                  @Nullable Area originArea) {
        this.areaForm = areaForm;
        this.player = player;
        this.isResizeable = isResizeable;
        this.originLand = originLand;
        this.originArea = originArea;
        world = player.getWorld();

        changedBlocks = new ChangedBlocks(player);
        hasCollision = false;
    }

    public final @NotNull AreaForm getAreaForm() {
        return areaForm;
    }

    public final @NotNull Player getPlayer() {
        return player;
    }

    public final boolean isResizeable() {
        return isResizeable;
    }

    public final @Nullable Land getOriginLand() {
        return originLand;
    }

    public final @Nullable Area getOriginArea() {
        return originArea;
    }

    public final boolean hasCollision() {
        return hasCollision;
    }

    public final void removeSelection() {
        changedBlocks.resetBlocks();
    }

    abstract void refreshVisualSelection();

    protected int getStepX() {
        int result = ((areaForm.getX2() - areaForm.getX1()) / 32);
        return Math.max(result, 1);
    }

    protected int getStepZ() {
        int result = ((areaForm.getZ2() - areaForm.getZ1()) / 32);
        return Math.max(result, 1);
    }
}
