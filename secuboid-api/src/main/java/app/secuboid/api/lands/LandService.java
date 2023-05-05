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
package app.secuboid.api.lands;

import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.recipients.RecipientExec;
import app.secuboid.api.services.Service;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Set;
import java.util.function.Consumer;

public interface LandService extends Service {

    /**
     * Creates the land with parent.
     *
     * @param parent   the parent or world
     * @param landName the land name
     * @param owner    the owner
     * @param areaForm the area form
     * @param callback the method to call back if success or not
     */
    void create(Land parent, String landName, RecipientExec owner,
                AreaForm areaForm, Consumer<LandResult> callback);

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
     * Sets the land parent.
     *
     * @param land      the land
     * @param newParent the land parent
     * @param callback  the method to call back if success or not
     */
    void setParent(AreaLand land, Land newParent, Consumer<LandResult> callback);

    /**
     * Gets the land component.
     *
     * @param id the id
     * @return the land component
     */
    LandComponent getLandComponent(long id);

    /**
     * Gets the land.
     *
     * @return the land
     */
    Land get(Location loc);

    /**
     * Gets all area lands available from a 2D location.
     *
     * @param world the world
     * @param x     the x
     * @param z     the z
     * @return the area lands
     */
    Set<AreaLand> getAreaLands(World world, int x, int z);

    /**
     * Gets all area lands available from a location.
     *
     * @param loc the location
     * @return the area lands
     */
    Set<AreaLand> getAreaLands(Location loc);

    /**
     * Gets the areas. This method ignore Y value.
     *
     * @param world the world
     * @param x     the x
     * @param z     the z
     * @return the areas
     */
    Set<Area> getAreas(World world, int x, int z);

    /**
     * Gets all areas from a location.
     *
     * @param loc the loc
     * @return the areas
     */
    Set<Area> getAreas(Location loc);

    /**
     * Gets the active area from the location.
     *
     * @param loc the loc
     * @return the last area or null if the location is outside an area
     */
    Area getArea(Location loc);

    /**
     * Gets the active location path (area or world land) from the location.
     *
     * @return the last location path (area or world land instance)
     */
    LocationPath getLocationPath(Location loc);

    /**
     * Gets the world land from the location.
     *
     * @param loc the loc
     * @return the world land
     */
    WorldLand getWorldLand(Location loc);

    /**
     * Gets the world land from the world.
     *
     * @param world the world
     * @return the world land
     */
    WorldLand getWorldLand(World world);
}
