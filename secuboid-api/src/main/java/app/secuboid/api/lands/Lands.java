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
package app.secuboid.api.lands;

import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.parameters.values.ParameterValue;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface Lands {

    /**
     * Creates the land with parent.
     *
     * @param parent   the parent or world
     * @param landName the land name
     * @param owner    the owner
     * @param areaForm the area form
     * @param callback the method to call back if success or not
     */
    void create(@NotNull Land parent, @NotNull String landName, @NotNull ParameterValue owner,
                @NotNull AreaForm areaForm, @Nullable Consumer<LandResult> callback);

    /**
     * Removes the land and force if the land has children. The children will be
     * orphan.
     *
     * @param land     the land
     * @param callback the method to call back if success or not
     */
    void removeForce(AreaLand land, Consumer<LandResult> callback);

    /**
     * Removes the land and children recursively.
     *
     * @param land     the land
     * @param callback the method to call back if success or not
     */
    void removeRecursive(AreaLand land, Consumer<LandResult> callback);

    /**
     * Removes the land.
     *
     * @param land     the land
     * @param callback the method to call back if success or not
     */
    void remove(AreaLand land, Consumer<LandResult> callback);

    /**
     * Rename land.
     *
     * @param land     the land
     * @param newName  the new name
     * @param callback the method to call back if success or not
     */
    void rename(AreaLand land, String newName, Consumer<LandResult> callback);

    /**
     * Gets the land.
     *
     * @param landName the land name
     * @return the land
     */
    AreaLand get(String landName);

    /**
     * Gets the land.
     *
     * @param id the id
     * @return the land
     */
    AreaLand get(int id);

    /**
     * Gets the land.
     *
     * @param loc the loc
     * @return the land
     */
    AreaLand get(Location loc);

    /**
     * Gets the lands.
     *
     * @return the lands
     */
    Collection<AreaLand> getLands();

    /**
     * Gets all lands available from a 2D location.
     *
     * @param world the world
     * @param x     the x
     * @param z     the z
     * @return the lands
     */
    Set<AreaLand> getLands(World world, int x, int z);

    /**
     * Gets all lands available from a location.
     *
     * @param loc the location
     * @return the lands
     */
    Set<AreaLand> getLands(Location loc);

    /**
     * Gets all lands from an owner.
     *
     * @param owner the owner
     * @return the lands
     */
    Set<AreaLand> getLands(ParameterValue owner);

    /**
     * Gets the areas. This method ignore Y value.
     *
     * @param world the world
     * @param x     the x
     * @param z     the z
     * @return the areas
     */
    List<Area> getAreas(World world, int x, int z);

    /**
     * Gets all areas from a location.
     *
     * @param loc the loc
     * @return the areas
     */
    List<Area> getAreas(Location loc);

    /**
     * Gets the active area from the location.
     *
     * @param loc the loc
     * @return the area
     */
    Area getArea(Location loc);
}
