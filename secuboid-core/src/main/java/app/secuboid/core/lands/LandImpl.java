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
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class LandImpl extends LandComponentImpl implements Land {

    private final Map<String, AreaLand> nameLowerToChild;

    // IMPORTANT: Please use createLand in Lands class to create a Land or it will
    // not be accessible and saved.
    LandImpl(long id, String name) {
        super(id, name);
        nameLowerToChild = new HashMap<>();
    }

    public void setChild(AreaLand areaLand) {
        nameLowerToChild.put(areaLand.getName(), areaLand);
    }

    @Override
    public boolean isLocationInside(int x, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLocationInside(Location loc) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isLocationInside(int x, int y, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isDescendants(AreaLand areaLand) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public @Nullable AreaLand getChild(String name) {
        return nameLowerToChild.get(name.toLowerCase());
    }

    @Override
    public @NotNull Collection<AreaLand> getChildren() {
        return nameLowerToChild.values();
    }
}
