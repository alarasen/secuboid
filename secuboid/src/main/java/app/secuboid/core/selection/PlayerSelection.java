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
package app.secuboid.core.selection;

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.api.lands.WorldLand;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.lands.areas.CuboidAreaForm;
import app.secuboid.api.lands.areas.CylinderAreaForm;
import app.secuboid.api.players.PlayerInfo;
import app.secuboid.core.lands.areas.CuboidAreaFormImpl;
import app.secuboid.core.lands.areas.CylinderAreaFormImpl;
import app.secuboid.core.selection.active.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static app.secuboid.core.config.Config.config;

public class PlayerSelection extends SenderSelection {

    private final PlayerInfo playerInfo;
    private final Player player;

    public PlayerSelection(@NotNull PlayerInfo playerInfo) {
        super();
        this.playerInfo = playerInfo;
        this.player = playerInfo.getPlayer();
    }

    public void createActiveSelectionModifyExpand(@NotNull WorldLand worldLand, @NotNull Class<? extends AreaForm> areaFormClass) {
        AreaForm areaForm = createAreaForm(areaFormClass);
        createActiveSelectionModifyExpand(worldLand, areaForm);
    }

    public void createActiveSelectionModifyExpand(@NotNull WorldLand worldLand, @NotNull AreaForm areaForm) {
        SelectionForm selectionForm = createSelectionForm(areaForm);
        activeSelection = new ActiveSelectionModifyExpand(worldLand, playerInfo, selectionForm);
    }

    public void updateSelectionFromLocation() {
        if (activeSelection instanceof ActiveSelectionModify activeSelectionModify) {
            activeSelectionModify.playerMoveSelection();
        }
    }

    @Override
    public boolean removeSelection() {
        if (activeSelection instanceof ActiveSelectionModify activeSelectionModify) {
            activeSelectionModify.removeSelection();
        }

        return super.removeSelection();
    }

    private @NotNull AreaForm createAreaForm(@NotNull Class<? extends AreaForm> areaFormClass) {
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

        if (areaFormClass.isAssignableFrom(CuboidAreaForm.class)) {
            return new CuboidAreaFormImpl(x1, y1, z1, x2, y2, z2);
        } else if (areaFormClass.isAssignableFrom(CylinderAreaForm.class)) {
            return new CylinderAreaFormImpl(x1, y1, z1, x2, y2, z2);
        }

        throw new SecuboidRuntimeException("Area class not yet implemented: " + areaFormClass.getSimpleName());
    }

    private @NotNull SelectionForm createSelectionForm(@NotNull AreaForm areaForm) {
        if (areaForm instanceof CuboidAreaForm cuboidAreaForm) {
            return new SelectionFormCuboid(cuboidAreaForm, player, null, null);
        } else if (areaForm instanceof CylinderAreaForm cylinderAreaForm) {
            return new SelectionFormCylinder(cylinderAreaForm, player, null, null);
        }

        throw new SecuboidRuntimeException("Selection form not yet implemented: " + areaForm);
    }

}