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
package me.tabinol.secuboid.core.lands;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;

import me.tabinol.secuboid.api.lands.AreaLand;
import me.tabinol.secuboid.api.lands.Land;
import me.tabinol.secuboid.api.lands.LandComponent;
import me.tabinol.secuboid.api.lands.LandResult;
import me.tabinol.secuboid.api.lands.LandResultCode;
import me.tabinol.secuboid.api.lands.Lands;
import me.tabinol.secuboid.api.lands.WorldLand;
import me.tabinol.secuboid.api.lands.areas.Area;
import me.tabinol.secuboid.api.parameters.values.ParameterValue;
import me.tabinol.secuboid.api.players.CommandSenderInfo;
import me.tabinol.secuboid.api.storage.StorageManager;
import me.tabinol.secuboid.core.SecuboidImpl;
import me.tabinol.secuboid.core.utilities.NameUtil;

public class LandsImpl implements Lands {

    private final Map<String, WorldLand> worldNameToWorldLand;
    private final Map<Integer, LandComponent> idToLandComponent;

    public LandsImpl() {
        worldNameToWorldLand = new HashMap<>();
        idToLandComponent = new HashMap<>();
    }

    public void load() {
        Collection<LandComponent> landComponents = getStorageManager().selectAllSync(LandComponent.class);

        for (LandComponent landComponent : landComponents) {

            if (landComponent instanceof WorldLand worldLand) {
                worldNameToWorldLand.put(worldLand.getName(), worldLand);
            }

            idToLandComponent.put(landComponent.getId(), landComponent);
        }
    }

    public void loadWorld(String worldName) {
        // TODO Load config
    }

    @Override
    public void createLand(Land parent, String landName, ParameterValue owner, Area area,
            CommandSenderInfo commandSenderInfo, Consumer<LandResult> callback) {
        String nameLower = landName;

        LandResultCode code = validateName(parent, nameLower);
        if (code != null) {
            if (callback != null) {
                LandResult landResult = new LandResult(commandSenderInfo, code, null, area);
                callback.accept(landResult);
            }
            return;
        }

        // TODO create area
        // AreaLandImpl land = new AreaLandImpl(uuid, nameLower, parent);
        // land.addArea(area);

        // TODO land init
    }

    @Override
    public void removeLandForce(AreaLand land, CommandSenderInfo commandSenderInfo, Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeLandRecursive(AreaLand land, CommandSenderInfo commandSenderInfo, Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeLand(AreaLand land, CommandSenderInfo commandSenderInfo, Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void renameLand(AreaLand land, String newName, CommandSenderInfo commandSenderInfo,
            Consumer<LandResult> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public AreaLand getLand(String landName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AreaLand getLand(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AreaLand getLand(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<AreaLand> getLands() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AreaLand> getLands(World world, int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AreaLand> getLands(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<AreaLand> getLands(ParameterValue owner) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Area> getAreas(World world, int x, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Area> getAreas(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Area getArea(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

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

    private StorageManager getStorageManager() {
        return SecuboidImpl.instance().getStorageManager();
    }
}
