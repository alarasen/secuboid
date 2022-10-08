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
import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.messages.Log;
import app.secuboid.core.storage.rows.LandRow;
import app.secuboid.core.storage.types.LandType;
import app.secuboid.core.utilities.NameUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

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

        Set<LandRow> landRows = getStorageManager().selectAllSync(LandRow.class);
        Map<LandComponent, Long> landComponentToParentId = new HashMap<>();

        for (LandRow landRow : landRows) {
            LandComponent landComponent = LandType.newLandComponent(landRow);
            idToLandComponent.put(landComponent.id(), landComponent);
            Long parentId = landRow.parentId();

            if (parentId != null) {
                landComponentToParentId.put(landComponent, parentId);
            }
        }

        for (Map.Entry<LandComponent, Long> entry : landComponentToParentId.entrySet()) {
            LandComponent landComponent = entry.getKey();
            long parentId = entry.getValue();
            setParent(idToLandComponent, landComponent, parentId);

            if (landComponent instanceof WorldLand worldLand) {
                worldNameToWorldLand.put(worldLand.getName(), worldLand);
            }
        }

        for (World world : SecuboidImpl.getJavaPLugin().getServer().getWorlds()) {
            loadWorldSync(world);
        }
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
        getStorageManager().insert(landRow, r -> insertCallback(r, parent, callback));
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

    private void setParent(@NotNull Map<Long, LandComponent> idToLandComponent, @NotNull LandComponent landComponent,
                           long parentId) {
        if (landComponent instanceof AreaLand areaLand) {
            LandComponent parent = idToLandComponent.get(parentId);
            if (parent instanceof LandImpl parentLandImpl) {
                parentLandImpl.setChild(areaLand);

                return;
            }
        }

        Log.log().warning(() -> format("This land has no parent and will be unreachable [id=%s, name=%s, parentId=%s]",
                landComponent.id(), landComponent.getName(), parentId));
    }

    private void insertCallback(@NotNull LandRow landRow, @NotNull Land parent,
                                @Nullable Consumer<LandResult> callback) {
        // TODO create area
        AreaLandImpl areaLand = new AreaLandImpl(landRow.id(), landRow.name(), parent);
        // AreaLandImpl land = new AreaLandImpl(uuid, nameLower, parent);
        // land.addArea(area);

        // TODO land init
    }

    private StorageManager getStorageManager() {
        return SecuboidImpl.instance().getStorageManager();
    }
}
