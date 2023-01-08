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

import app.secuboid.api.lands.WorldLand;
import app.secuboid.api.lands.areas.Area;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class WorldLandImpl extends LandImpl implements WorldLand {

    private final @NotNull Map<Long, Set<Area>> regionXZToLandAreas;

    public WorldLandImpl(long id, @NotNull String worldName) {
        super(id, worldName);

        regionXZToLandAreas = new HashMap<>();
    }

    @Override
    public @NotNull String getPathName() {
        return SEPARATOR_LAND + name;
    }

    @Override
    public @NotNull WorldLand getWorldLand() {
        return this;
    }

    @Override
    public boolean isLocationInside(int x, int z) {
        return true;
    }

    @Override
    public boolean isLocationInside(int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isLocationInside(@NotNull Location loc) {
        World locWorld = loc.getWorld();

        if (locWorld != null) {
            return name.equalsIgnoreCase(locWorld.getName());
        }

        return true;
    }

    @Override
    public @NotNull Set<Area> get(int x, int z) {
        return get(x, 0, z, false);
    }

    @Override
    public @NotNull Set<Area> get(int x, int y, int z) {
        return get(x, y, z, true);
    }

    private Set<Area> get(int x, int y, int z, boolean isY) {
        long regionXZ = getRegionXZFromLocation(x, z);
        Set<Area> areas = regionXZToLandAreas.get(regionXZ);

        if (areas == null) {
            return Collections.emptySet();
        }

        Set<Area> result = new HashSet<>();
        for (Area area : areas) {
            if (isY ? area.isLocationInside(x, y, z) : area.isLocationInsideSquare(x, z)) {
                result.add(area);
            }
        }
        return result;
    }

    public void add(Area area) {
        performInRegions(area, (rx, rz) -> addInRegion(rx, rz, area));
    }

    public void remove(Area area) {
        performInRegions(area, (rx, rz) -> removeInRegion(rx, rz, area));
    }

    private void performInRegions(Area area, BiConsumer<Integer, Integer> bc) {
        int regionX1 = area.getX1() >> 9;
        int regionX2 = area.getX2() >> 9;
        int regionZ1 = area.getZ1() >> 9;
        int regionZ2 = area.getZ2() >> 9;

        for (int regionX = regionX1; regionX <= regionX2; regionX++) {
            for (int regionZ = regionZ1; regionZ <= regionZ2; regionZ++) {
                bc.accept(regionX, regionZ);
            }
        }
    }

    private void addInRegion(int regionX, int regionZ, Area area) {
        long regionXZ = getRegionXZFromRegion(regionX, regionZ);
        regionXZToLandAreas.computeIfAbsent(regionXZ, k -> new HashSet<>()).add(area);
    }

    private void removeInRegion(int regionX, int regionZ, Area area) {
        long regionXZ = getRegionXZFromRegion(regionX, regionZ);
        Set<Area> areas = regionXZToLandAreas.get(regionXZ);

        if (areas == null) {
            return;
        }

        areas.remove(area);

        if (areas.isEmpty()) {
            regionXZToLandAreas.remove(regionXZ);
        }
    }

    private long getRegionXZFromLocation(int x, int z) {
        int regionX = x >> 9;
        int regionZ = z >> 9;
        return getRegionXZFromRegion(regionX, regionZ);
    }

    private long getRegionXZFromRegion(int regionX, int regionZ) {
        return (((long) regionX) << 32) | (regionZ & 0xFFFFFFFFL);
    }
}
