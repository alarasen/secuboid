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

import app.secuboid.api.lands.*;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.lands.areas.AreaResult;
import app.secuboid.api.lands.areas.AreaResultCode;
import app.secuboid.api.recipients.RecipientExec;
import app.secuboid.core.utilities.NameUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static app.secuboid.api.lands.LandResultCode.SUCCESS;
import static app.secuboid.api.lands.LandResultCode.UNKNOWN;
import static app.secuboid.core.messages.Log.log;
import static java.lang.String.format;

public class LandServiceImpl implements LandService {

    private static final String DEFAULT_WORLD_NAME = "world";

    private final Server server;

    private final Map<String, WorldLand> worldNameToWorldLand;
    private final Map<Long, LandComponent> idToLandComponent;
    private final Map<String, Set<LandComponent>> nameToLandComponents;

    public LandServiceImpl(Server server) {
        this.server = server;

        worldNameToWorldLand = new HashMap<>();
        idToLandComponent = new HashMap<>();
        nameToLandComponents = new HashMap<>();
    }

    @Override
    public void onEnable(boolean isServerBoot) {
        worldNameToWorldLand.clear();
        idToLandComponent.clear();
        nameToLandComponents.clear();

        loadLandComponents();
        loadAreas();
    }


    public void loadWorldSync(World world) {
//        String worldName = world.getName();
//
//        if (worldNameToWorldLand.containsKey(worldName)) {
//            return;
//        }
//
//        LandRow inputLandRow = new LandRow(NON_EXISTING_ID, worldName, WORLD_LAND, null);
//        LandRow landRow = getStorageManager().insertSync(inputLandRow);
//        if (landRow == null) {
//            log().log(SEVERE, "Unable to create the world \"{}\" because there is no answer from the database",
//                    worldName);
//            return;
//        }
//
//        WorldLand worldLand = new WorldLandImpl(landRow.id(), worldName);
//        putLandComponentToMap(worldLand);
    }

    @Override
    public void create(Land parent, String landName, RecipientExec owner,
                       AreaForm areaForm, Consumer<LandResult> callback) {
        String nameLower = landName.toLowerCase();

        LandResultCode code = validateName(parent, nameLower);
        if (code != null) {
            if (callback != null) {
                LandResult landResult = new LandResult(code, null, null);
                callback.accept(landResult);
            }
            return;
        }

//        LandRow landRow = new LandRow(NON_EXISTING_ID, nameLower, AREA_LAND, parent.id());
//        getStorageManager().insert(landRow, r -> createInsertCallback(r, parent, areaForm, callback));
    }

    @Override
    public void removeForce(AreaLand land, Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeRecursive(AreaLand land, Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(AreaLand land, Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rename(AreaLand land, String newName, Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setParent(AreaLand land, Land newParent, Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public LandComponent getLandComponent(long id) {
        return idToLandComponent.get(id);
    }

    @Override
    public Land get(Location loc) {
        Area area = getArea(loc);

        if (area != null) {
            return area.getLand();
        }

        return getWorldLand(loc);
    }

    @Override
    public Set<AreaLand> getAreaLands(World world, int x, int z) {
        WorldLand worldLand = getWorldLand(world);
        Set<Area> areas = worldLand.get(x, z);

        return getAreaLandsFromAreas(areas);
    }

    @Override
    public Set<AreaLand> getAreaLands(Location loc) {
        Set<Area> areas = getAreas(loc);

        return getAreaLandsFromAreas(areas);
    }

    @Override
    public Set<Area> getAreas(World world, int x, int z) {
        WorldLand worldLand = getWorldLand(world);

        return worldLand.get(x, z);
    }

    @Override
    public Set<Area> getAreas(Location loc) {
        WorldLand worldLand = getWorldLand(loc);

        return worldLand.get(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public Area getArea(Location loc) {
        WorldLand worldLand = getWorldLand(loc);
        Set<Area> areas = worldLand.get(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        // TODO Area priority/child system
        return areas.stream().findAny().orElse(null);
    }

    @Override
    public LocationPath getLocationPath(Location loc) {
        return Optional.ofNullable((LocationPath) getArea(loc)).orElse(get(loc));
    }

    @Override
    public WorldLand getWorldLand(Location loc) {
        return getWorldLand(getWorldFromLocation(loc));
    }

    @Override
    public WorldLand getWorldLand(World world) {
        return worldNameToWorldLand.get(world.getName());
    }

    private void loadLandComponents() {
//        // TODO Set<LandRow> landRows = getStorageManager().selectAllSync(LandRow.class);
//        Set<LandRow> landRows = Collections.emptySet();
//
//        while (!landRows.isEmpty()) {
//            Set<LandRow> nextLandRows = new HashSet<>();
//
//            for (LandRow landRow : landRows) {
//                if (loadLandComponentAndIsToRetry(landRow)) {
//                    nextLandRows.add(landRow);
//                }
//            }
//            landRows = nextLandRows;
//        }
//
//        for (World world : server.getWorlds()) {
//            loadWorldSync(world);
//        }
    }

//    private boolean loadLandComponentAndIsToRetry(LandRow landRow) {
//        Long parentId = landRow.parentId();
//        Land parent = null;
//
//        if (landRow.type() == AREA_LAND) {
//            if (parentId == null) {
//                log().log(SEVERE, "Unable to create the land \"{}\" because it has no parent", landRow.name());
//                return false;
//            }
//
//            parent = (Land) idToLandComponent.get(parentId);
//
//            if (parent == null) {
//                return true;
//            }
//        }
//
//        LandComponent landComponent = LandType.newLandComponent(landRow, parent);
//        putLandComponentToMap(landComponent);
//
//        return false;
//    }

    private void loadAreas() {
//        // TODO Set<AreaRow> areaRows = getStorageManager().selectAllSync(AreaRow.class);
//        Set<AreaRow> areaRows = Collections.emptySet();
//
//        for (AreaRow areaRow : areaRows) {
//            loadArea(areaRow);
//        }
    }

//    private void loadArea(AreaRow areaRow) {
//        LandComponent landComponent = idToLandComponent.get(areaRow.landId());
//
//        if (!(landComponent instanceof AreaLand)) {
//            String msg = format("Unable to add area id \"%s\" because the associated land is the wrong type [%s]"
//                    , areaRow.id(), landComponent);
//            log().log(SEVERE, msg);
//            return;
//        }
//        ((AreaLandImpl) landComponent).addAreaToLand(areaRow);
//    }

    private LandResultCode validateName(Land parent, String nameLower) {
        if (!NameUtil.validateName(nameLower)) {
            return LandResultCode.NAME_INVALID;
        }

        parent.getChild(nameLower);
        if (!NameUtil.validateName(nameLower)) {
            return LandResultCode.NAME_EXIST;
        }

        return null;
    }

//    private void createInsertCallback(LandRow landRow, Land parent, AreaForm areaForm,
//                                      Consumer<LandResult> callback) {
//        AreaLandImpl areaLand = new AreaLandImpl(landRow.id(), landRow.name(), parent);
//        areaLand.addArea(areaForm, r -> createAddAreaCallback(areaLand, r, callback));
//    }

    private void createAddAreaCallback(AreaLand areaLand, AreaResult areaResult,
                                       Consumer<LandResult> callback) {
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

    private void putLandComponentToMap(LandComponent landComponent) {
        String name = landComponent.getName();

        idToLandComponent.put(landComponent.id(), landComponent);
        nameToLandComponents.computeIfAbsent(name, k -> new HashSet<>()).add(landComponent);

        if (landComponent instanceof WorldLand worldLand) {
            worldNameToWorldLand.put(name, worldLand);
        }
    }

    private void removeLandComponentFromMap(LandComponent landComponent) {
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

    private Set<AreaLand> getAreaLandsFromAreas(Set<Area> areas) {
        return areas.stream().map(Area::getLand).collect(Collectors.toSet());
    }

    private World getWorldFromLocation(Location loc) {
        World world = loc.getWorld();

        if (world != null)
            return world;

        world = Bukkit.getWorld(DEFAULT_WORLD_NAME);

        if (world == null) {
            world = Bukkit.getWorlds().get(0);
        }

        log().log(Level.WARNING, "A location is sent without any world. Assuming: {}", world.getName());

        return world;
    }
}
