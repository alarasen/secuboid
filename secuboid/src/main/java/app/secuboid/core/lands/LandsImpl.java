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

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.api.lands.*;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.lands.areas.AreaResult;
import app.secuboid.api.lands.areas.AreaResultCode;
import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.storage.rows.AreaRow;
import app.secuboid.core.storage.rows.LandRow;
import app.secuboid.core.storage.types.LandType;
import app.secuboid.core.utilities.NameUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static app.secuboid.api.lands.LandResultCode.SUCCESS;
import static app.secuboid.api.lands.LandResultCode.UNKNOWN;
import static app.secuboid.api.storage.rows.RowWithId.NON_EXISTING_ID;
import static app.secuboid.core.messages.Log.log;
import static app.secuboid.core.storage.types.LandType.AREA_LAND;
import static app.secuboid.core.storage.types.LandType.WORLD_LAND;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public class LandsImpl implements Lands {

    private static final String DEFAULT_WORLD_NAME = "world";

    private final Map<String, WorldLand> worldNameToWorldLand;
    private final Map<Long, LandComponent> idToLandComponent;
    private final Map<String, Set<LandComponent>> nameToLandComponents;

    public LandsImpl() {
        worldNameToWorldLand = new HashMap<>();
        idToLandComponent = new HashMap<>();
        nameToLandComponents = new HashMap<>();
    }

    public void load() {
        worldNameToWorldLand.clear();
        idToLandComponent.clear();
        nameToLandComponents.clear();

        loadLandComponents();
        loadAreas();
    }


    public void loadWorldSync(@NotNull World world) {
        String worldName = world.getName();

        if (worldNameToWorldLand.containsKey(worldName)) {
            return;
        }

        LandRow inputLandRow = new LandRow(NON_EXISTING_ID, worldName, WORLD_LAND, null);
        LandRow landRow = getStorageManager().insertSync(inputLandRow);
        if (landRow == null) {
            log().log(SEVERE, "Unable to create the world \"{}\" because there is no answer from the database",
                    worldName);
            return;
        }

        WorldLand worldLand = new WorldLandImpl(landRow.id(), worldName);
        putLandComponentToMap(worldLand);
    }

    @Override
    public void create(@NotNull Land parent, @NotNull String landName, @NotNull ParameterValue owner,
                       @NotNull AreaForm areaForm, @Nullable Consumer<LandResult> callback) {
        String nameLower = landName.toLowerCase();

        LandResultCode code = validateName(parent, nameLower);
        if (code != null) {
            if (callback != null) {
                LandResult landResult = new LandResult(code, null, null);
                callback.accept(landResult);
            }
            return;
        }

        LandRow landRow = new LandRow(NON_EXISTING_ID, nameLower, AREA_LAND, parent.id());
        getStorageManager().insert(landRow, r -> createInsertCallback(r, parent, areaForm, callback));
    }

    @Override
    public void removeForce(@NotNull AreaLand land, @Nullable Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeRecursive(@NotNull AreaLand land, @Nullable Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(@NotNull AreaLand land, @Nullable Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rename(@NotNull AreaLand land, @NotNull String newName, @Nullable Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setParent(@NotNull AreaLand land, @NotNull Land newParent, @Nullable Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public @Nullable LandComponent getLandComponent(long id) {
        return idToLandComponent.get(id);
    }

    @Override
    public @NotNull Land get(@NotNull Location loc) {
        Area area = getArea(loc);

        if (area != null) {
            return area.getLand();
        }

        return getWorldLand(loc);
    }

    @Override
    public @NotNull Set<AreaLand> getAreaLands(@NotNull World world, int x, int z) {
        WorldLand worldLand = getWorldLand(world);
        Set<Area> areas = worldLand.get(x, z);

        return getAreaLandsFromAreas(areas);
    }

    @Override
    public @NotNull Set<AreaLand> getAreaLands(@NotNull Location loc) {
        Set<Area> areas = getAreas(loc);

        return getAreaLandsFromAreas(areas);
    }

    @Override
    public @NotNull Set<Area> getAreas(@NotNull World world, int x, int z) {
        WorldLand worldLand = getWorldLand(world);

        return worldLand.get(x, z);
    }

    @Override
    public @NotNull Set<Area> getAreas(@NotNull Location loc) {
        WorldLand worldLand = getWorldLand(loc);

        return worldLand.get(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public @Nullable Area getArea(@NotNull Location loc) {
        WorldLand worldLand = getWorldLand(loc);
        Set<Area> areas = worldLand.get(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        // TODO Area priority/child system
        return areas.stream().findAny().orElse(null);
    }

    @Override
    public @NotNull LocationPath getLocationPath(@NotNull Location loc) {
        return Optional.ofNullable((LocationPath) getArea(loc)).orElse(get(loc));
    }

    @Override
    public @NotNull WorldLand getWorldLand(@NotNull Location loc) {
        return getWorldLand(getWorldFromLocation(loc));
    }

    @Override
    public @NotNull WorldLand getWorldLand(@NotNull World world) {
        return worldNameToWorldLand.get(world.getName());
    }

    private void loadLandComponents() {
        Set<LandRow> landRows = getStorageManager().selectAllSync(LandRow.class);

        while (!landRows.isEmpty()) {
            Set<LandRow> nextLandRows = new HashSet<>();

            for (LandRow landRow : landRows) {
                if (loadLandComponentAndIsToRetry(landRow)) {
                    nextLandRows.add(landRow);
                }
            }
            landRows = nextLandRows;
        }

        for (World world : SecuboidImpl.getJavaPLugin().getServer().getWorlds()) {
            loadWorldSync(world);
        }
    }

    private boolean loadLandComponentAndIsToRetry(@NotNull LandRow landRow) {
        Long parentId = landRow.parentId();
        Land parent = null;

        if (landRow.type() == AREA_LAND) {
            if (parentId == null) {
                log().log(SEVERE, "Unable to create the land \"{}\" because it has no parent", landRow.name());
                return false;
            }

            parent = (Land) idToLandComponent.get(parentId);

            if (parent == null) {
                return true;
            }
        }

        LandComponent landComponent = LandType.newLandComponent(landRow, parent);
        putLandComponentToMap(landComponent);

        return false;
    }

    private void loadAreas() {
        Set<AreaRow> areaRows = getStorageManager().selectAllSync(AreaRow.class);

        for (AreaRow areaRow : areaRows) {
            loadArea(areaRow);
        }
    }

    private void loadArea(@NotNull AreaRow areaRow) {
        LandComponent landComponent = idToLandComponent.get(areaRow.landId());

        if (!(landComponent instanceof AreaLand)) {
            String msg = format("Unable to add area id \"%s\" because the associated land is the wrong type [%s]"
                    , areaRow.id(), landComponent);
            log().log(SEVERE, msg);
            return;
        }
        ((AreaLandImpl) landComponent).addAreaToLand(areaRow);
    }

    private @Nullable LandResultCode validateName(@NotNull Land parent, @NotNull String nameLower) {
        if (!NameUtil.validateName(nameLower)) {
            return LandResultCode.NAME_INVALID;
        }

        parent.getChild(nameLower);
        if (!NameUtil.validateName(nameLower)) {
            return LandResultCode.NAME_EXIST;
        }

        return null;
    }

    private void createInsertCallback(@NotNull LandRow landRow, @NotNull Land parent, @NotNull AreaForm areaForm,
                                      @Nullable Consumer<LandResult> callback) {
        AreaLandImpl areaLand = new AreaLandImpl(landRow.id(), landRow.name(), parent);
        areaLand.addArea(areaForm, r -> createAddAreaCallback(areaLand, r, callback));
    }

    private void createAddAreaCallback(@NotNull AreaLand areaLand, @NotNull AreaResult areaResult,
                                       @Nullable Consumer<LandResult> callback) {
        if (areaResult.code() != AreaResultCode.SUCCESS) {
            if (callback != null) {
                callback.accept(new LandResult(UNKNOWN, null, null));
            }
            log().warning(() -> format("This land cannot be create because an error with the area [id=%s, " +
                    "name=%s]", areaLand.id(), areaLand.getName()));
            return;
        }

        putLandComponentToMap(areaLand);
        ((LandImpl) areaLand.getParent()).setChild(areaLand);

        if (callback != null) {
            callback.accept(new LandResult(SUCCESS, areaLand, areaResult.area()));
        }
    }

    private void putLandComponentToMap(@NotNull LandComponent landComponent) {
        String name = landComponent.getName();

        idToLandComponent.put(landComponent.id(), landComponent);
        nameToLandComponents.computeIfAbsent(name, k -> new HashSet<>()).add(landComponent);

        if (landComponent instanceof WorldLand worldLand) {
            worldNameToWorldLand.put(name, worldLand);
        }
    }

    private void removeLandComponentFromMap(@NotNull LandComponent landComponent) {
        String name = landComponent.getName();

        idToLandComponent.remove(landComponent.id());

        nameToLandComponents.computeIfPresent(name, (k, v) -> {
            v.remove(landComponent);
            return !v.isEmpty() ? v : null;
        });

        if (landComponent instanceof WorldLand) {
            worldNameToWorldLand.remove(name);
        }
    }

    private @NotNull Set<AreaLand> getAreaLandsFromAreas(@NotNull Set<Area> areas) {
        return areas.stream().map(Area::getLand).collect(Collectors.toSet());
    }

    private @NotNull StorageManager getStorageManager() {
        return SecuboidImpl.instance().getStorageManager();
    }

    private @NotNull World getWorldFromLocation(@NotNull Location loc) {
        World world = loc.getWorld();

        if (world != null)
            return world;

        world = Bukkit.getWorld(DEFAULT_WORLD_NAME);

        if (world == null) {
            world = Bukkit.getWorlds().get(0);
        }

        if (world == null) {
            throw new SecuboidRuntimeException("A location is sent without any world. No world is available");
        }

        log().log(Level.WARNING, "A location is sent without any world. Assuming: {}", world.getName());

        return world;
    }
}
