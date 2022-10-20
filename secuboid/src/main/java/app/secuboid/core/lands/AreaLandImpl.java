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
import app.secuboid.api.lands.WorldLand;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.lands.areas.AreaResult;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.lands.areas.AreaFormImpl;
import app.secuboid.core.storage.rows.AreaRow;
import app.secuboid.core.storage.types.AreaType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static app.secuboid.api.lands.areas.AreaResultCode.SUCCESS;
import static app.secuboid.api.storage.rows.RowWithId.NON_EXISTING_ID;

public class AreaLandImpl extends LandImpl implements AreaLand {

    private final WorldLand worldLand;
    private final Land parent;
    private final Map<Long, Area> idToArea;


    public AreaLandImpl(long id, @NotNull String name, @NotNull Land parent) {
        super(id, name);

        worldLand = parent.getWorldLand();
        this.parent = parent;

        idToArea = new HashMap<>();
    }

    @Override
    public @NotNull WorldLand getWorldLand() {
        return worldLand;
    }

    @Override
    public boolean isLocationInside(int x, int z) {
        for (Area area : idToArea.values()) {
            if (area.isLocationInside(x, z)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isLocationInside(int x, int y, int z) {
        for (Area area : idToArea.values()) {
            if (area.isLocationInside(x, y, z)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isLocationInside(@NotNull Location loc) {
        if (!getWorldLand().isLocationInside(loc)) {
            return false;
        }

        for (Area area : idToArea.values()) {
            if (area.isLocationInside(loc)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addArea(@NotNull AreaForm areaForm, @Nullable Consumer<AreaResult> callback) {
        AreaRow areaRow = new AreaRow(NON_EXISTING_ID, id(), ((AreaFormImpl) areaForm).getAreaType(),
                areaForm.getX1(), areaForm.getY1(), areaForm.getZ1(), areaForm.getX2(), areaForm.getY2(), areaForm.getZ2());
        getStorageManager().insert(areaRow, r -> addAreaCallback(r, areaForm, callback));

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
    public @NotNull Land getParent() {
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

    private void addAreaCallback(@NotNull AreaRow areaRow, @NotNull AreaForm areaForm,
                                 @Nullable Consumer<AreaResult> callback) {
        Area area = addAreaToLand(areaRow);

        if (callback != null) {
            callback.accept(new AreaResult(SUCCESS, area));
        }
    }

    public @NotNull Area addAreaToLand(@NotNull AreaRow areaRow) {
        Area area = AreaType.newArea(areaRow, this);
        idToArea.put(area.id(), area);
        ((WorldLandImpl) getWorldLand()).add(area);

        return area;
    }

    private StorageManager getStorageManager() {
        return SecuboidImpl.instance().getStorageManager();
    }
}
