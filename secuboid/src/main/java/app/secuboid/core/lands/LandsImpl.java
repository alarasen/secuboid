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

import app.secuboid.api.lands.*;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.lands.areas.AreaResult;
import app.secuboid.api.lands.areas.AreaResultCode;
import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.messages.Log;
import app.secuboid.core.storage.rows.AreaRow;
import app.secuboid.core.storage.rows.LandRow;
import app.secuboid.core.storage.types.LandType;
import app.secuboid.core.utilities.NameUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

import static app.secuboid.api.lands.LandResultCode.SUCCESS;
import static app.secuboid.api.lands.LandResultCode.UNKNOWN;
import static app.secuboid.api.storage.rows.RowWithId.NON_EXISTING_ID;
import static app.secuboid.core.storage.types.LandType.AREA_LAND;
import static app.secuboid.core.storage.types.LandType.WORLD_LAND;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public class LandsImpl implements Lands {

    private final Map<String, WorldLand> worldNameToWorldLand;
    private final Map<Long, LandComponent> idToLandComponent;

    public LandsImpl() {
        worldNameToWorldLand = new HashMap<>();
        idToLandComponent = new HashMap<>();
    }

    public void load() {
        worldNameToWorldLand.clear();
        idToLandComponent.clear();

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
            Log.log().log(SEVERE, "Unable to create the world \"{}\" because there is no answer from the database",
                    worldName);
            return;
        }

        WorldLand worldLand = new WorldLandImpl(landRow.id(), worldName);
        worldNameToWorldLand.put(worldName, worldLand);
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
    public AreaLand get(@NotNull String landName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AreaLand get(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @Nullable AreaLand get(@NotNull Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull Collection<AreaLand> getLands() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull Set<AreaLand> getLands(@NotNull World world, int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull Set<AreaLand> getLands(@NotNull Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull Set<AreaLand> getLands(@NotNull ParameterValue owner) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull List<Area> getAreas(@NotNull World world, int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public @NotNull List<Area> getAreas(@NotNull Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Area getArea(@NotNull Location loc) {
        // TODO Auto-generated method stub
        return null;
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
                Log.log().log(SEVERE, "Unable to create the land \"{}\" because it has no parent", landRow.name());
                return false;
            }

            parent = (Land) idToLandComponent.get(parentId);

            if (parent == null) {
                return true;
            }
        }

        LandComponent landComponent = LandType.newLandComponent(landRow, parent);
        idToLandComponent.put(landComponent.id(), landComponent);

        if (landComponent instanceof WorldLand worldLand) {
            worldNameToWorldLand.put(worldLand.getName(), worldLand);
        }

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
            Log.log().log(SEVERE, msg);
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
            Log.log().warning(() -> format("This land cannot be create because an error with the area [id=%s, " +
                    "name=%s]", areaLand.id(), areaLand.getName()));
            return;
        }

        idToLandComponent.put(areaLand.id(), areaLand);
        ((LandImpl) areaLand.getParent()).setChild(areaLand);

        if (callback != null) {
            callback.accept(new LandResult(SUCCESS, areaLand, areaResult.area()));
        }
    }

    private StorageManager getStorageManager() {
        return SecuboidImpl.instance().getStorageManager();
    }
}
