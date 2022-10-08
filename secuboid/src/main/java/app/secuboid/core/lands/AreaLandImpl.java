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
package app.secuboid.core.lands;

import app.secuboid.api.lands.AreaLand;
import app.secuboid.api.lands.Land;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.lands.areas.AreaResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class AreaLandImpl extends LandImpl implements AreaLand {

    private final Map<Integer, Area> idToArea;

    private Land parent;

    // IMPORTANT: Please use createLand in Lands class to create a Land or it will
    // not be accessible and saved.
    public AreaLandImpl(long id, @NotNull String name, @Nullable Land parent) {
        super(id, name);
        this.parent = parent;

        idToArea = new HashMap<>();
    }

    @Override
    public void addArea(@NotNull AreaForm areaForm, @Nullable Consumer<AreaResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeArea(int key, @Nullable Consumer<AreaResult> callback) {
        // TODO Auto-generated method stub
    }

    @Override
    public void removeArea(@NotNull Area area, @Nullable Consumer<AreaResult> callback) {
        // TODO Auto-generated method stub
    }

    @Override
    public void replaceArea(int key, @NotNull AreaForm newAreaForm, @Nullable Consumer<AreaResult> callback) {
        // TODO Auto-generated method stub
    }

    @Override
    public @Nullable Area getArea(int key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @Nullable Integer getAreaKey(@NotNull Area area) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull Set<Integer> getAreaKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull Map<Integer, Area> getIdToArea() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull Collection<Area> getAreas() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @Nullable Land getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isParentOrAncestor(@NotNull Land land) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPlayerInLand(@NotNull Player player) {
        // TODO Auto-generated method stub
        return false;
    }

}
