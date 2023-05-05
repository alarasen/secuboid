/*
 *  Secuboid: LandService and Protection plugin for Minecraft server
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
import org.bukkit.Location;

import java.util.*;
import java.util.function.Consumer;

public class AreaLandImpl extends LandImpl implements AreaLand {

    private final WorldLand worldLand;
    private final Land parent;
    private final Map<Long, Area> idToArea;


    public AreaLandImpl(long id, String name, Land parent) {
        super(id, name);

        worldLand = parent.getWorldLand();
        this.parent = parent;

        idToArea = new HashMap<>();
    }

    @Override
    public String getPathName() {
        return parent.getPathName() + SEPARATOR_LAND + name;
    }

    @Override
    public WorldLand getWorldLand() {
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
    public boolean isLocationInside(Location loc) {
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
    public void addArea(AreaForm areaForm, Consumer<AreaResult> callback) {
        // TODO add area
        //AreaRow areaRow = new AreaRow(NON_EXISTING_ID, id(), ((AreaFormImpl) areaForm).getAreaType(),
        //        areaForm.getX1(), areaForm.getY1(), areaForm.getZ1(), areaForm.getX2(), areaForm.getY2(),
        //        areaForm.getZ2());
        //getStorageManager().insert(areaRow, r -> addAreaCallback(r, callback));

    }

    @Override
    public void removeArea(int key, Consumer<AreaResult> callback) {
        // TODO Auto-generated method stub
    }

    @Override
    public void removeArea(Area area, Consumer<AreaResult> callback) {
        // TODO Auto-generated method stub
    }

    @Override
    public void replaceArea(int key, AreaForm newAreaForm, Consumer<AreaResult> callback) {
        // TODO Auto-generated method stub
    }

    @Override
    public Area getArea(long id) {
        return idToArea.get(id);
    }

    @Override
    public Set<Long> getAreaIds() {
        return idToArea.keySet();
    }

    @Override
    public Map<Long, Area> getIdToArea() {
        return Collections.unmodifiableMap(idToArea);
    }

    @Override
    public Collection<Area> getAreas() {
        return idToArea.values();
    }

    @Override
    public Land getParent() {
        return parent;
    }

//    private void addAreaCallback(AreaRow areaRow, Consumer<AreaResult> callback) {
//        Area area = addAreaToLand(areaRow);
//
//        if (callback != null) {
//            callback.accept(new AreaResult(SUCCESS, area));
//        }
//    }

//    public Area addAreaToLand(AreaRow areaRow) {
//        Area area = AreaType.newArea(areaRow, this);
//        idToArea.put(area.id(), area);
//        ((WorldLandImpl) getWorldLand()).add(area);
//
//        return area;
//    }
}
