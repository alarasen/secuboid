/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.secuboid.core.selection;

import me.tabinol.secuboid.api.exceptions.SecuboidRuntimeException;
import me.tabinol.secuboid.api.lands.areas.AreaForm;
import me.tabinol.secuboid.api.lands.areas.CuboidAreaForm;
import me.tabinol.secuboid.api.lands.areas.CylinderAreaForm;
import me.tabinol.secuboid.core.lands.areas.CuboidAreaFormImpl;
import me.tabinol.secuboid.core.lands.areas.CylinderAreaFormImpl;
import me.tabinol.secuboid.core.selection.active.VisualSelection;
import me.tabinol.secuboid.core.selection.active.VisualSelectionCuboid;
import me.tabinol.secuboid.core.selection.active.VisualSelectionCylinder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static me.tabinol.secuboid.core.config.Config.config;

public class PlayerSelection extends SenderSelection {

    protected static final Material SEL_ACTIVE = Material.SPONGE;
    protected static final Material SEL_COLLISION = Material.REDSTONE_BLOCK;
    protected static final Material SEL_PASSIVE = Material.IRON_BLOCK;

    private final Player player;

    private SelectionMoveType moveType;
    private SelectionScoreboard selectionScoreboard;

    public PlayerSelection(@NotNull Player player) {
        super();
        this.player = player;

        selectionScoreboard = null;
        moveType = null;
    }

    public void createVisualSelection(@NotNull Class<? extends AreaForm> areaFormClass, @NotNull SelectionMoveType newMoveType) {
        moveType = newMoveType;

        int selectionDefaultStartDiameter = config().selectionDefaultStartDiameter();
        Location loc = player.getLocation();
        int playerX = loc.getBlockX();
        int playerZ = loc.getBlockZ();

        // TODO Change to customizable limit y1/y2
        World world = player.getWorld();
        int y1 = world.getMinHeight();
        int y2 = world.getMaxHeight();
        int x1 = playerX - (selectionDefaultStartDiameter / 2);
        int x2 = x1 + selectionDefaultStartDiameter;
        int z1 = playerZ - (selectionDefaultStartDiameter / 2);
        int z2 = z1 + selectionDefaultStartDiameter;

        AreaForm areaForm;
        if (areaFormClass.isAssignableFrom(CuboidAreaForm.class)) {
            areaForm = new CuboidAreaFormImpl(x1, y1, z1, x2, y2, z2);
            activeSelection = new VisualSelectionCuboid(world, (CuboidAreaForm) areaForm, null, player);
        } else if (areaFormClass.isAssignableFrom(CylinderAreaForm.class)) {
            areaForm = new CylinderAreaFormImpl(x1, y1, z1, x2, y2, z2);
            activeSelection = new VisualSelectionCylinder(world, (CylinderAreaForm) areaForm, null, player);
        } else {
            throw new SecuboidRuntimeException("Area class not yet implemented: " + areaFormClass.getSimpleName());
        }

        // TODO passive

        selectionScoreboard = new SelectionScoreboard(player, areaForm, moveType);
    }

    public void updateSelectionFromLocation() {
        if (activeSelection instanceof VisualSelection visualSelection) {
            visualSelection.playerMoveSelection(moveType);

            if (moveType != SelectionMoveType.PASSIVE) {
                selectionScoreboard.update();
            }
        }
    }

    @Nullable
    public AreaForm getAreaForm() {
        if (activeSelection instanceof VisualSelection visualSelection) {
            return visualSelection.getAreaForm();
        }

        return null;
    }

    @Override
    public boolean removeSelection() {
        if (activeSelection instanceof VisualSelection visualSelection) {
            visualSelection.removeSelection();
        }

        if (selectionScoreboard != null) {
            selectionScoreboard.hide();
            selectionScoreboard = null;
        }

        return super.removeSelection();
    }
}