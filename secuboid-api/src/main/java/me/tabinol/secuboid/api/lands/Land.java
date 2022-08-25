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
package me.tabinol.secuboid.api.lands;

import java.util.Collection;

import org.bukkit.Location;

/**
 * All lands world relative.
 */
public interface Land extends LandComponent {

    /**
     * Check if the location is inside the land. This method does not check for Y
     * value.
     *
     * @param x the x
     * @param z the z
     * @return true if inside the land
     */
    boolean isLocationInside(int x, int z);

    /**
     * Check if the location is inside the land.
     *
     * @param loc the location
     * @return true if inside the land
     */
    boolean isLocationInside(Location loc);

    /**
     * Check if the location is inside the land.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return true if inside the land
     */
    boolean isLocationInside(int x, int y, int z);

    /**
     * Checks if is descendants.
     *
     * @param areaLand the area land
     * @return true, if is descendants
     */
    boolean isDescendants(AreaLand areaLand);

    /**
     * Gets the child.
     *
     * @param name the land name
     * @return the child
     */
    AreaLand getChild(String name);

    /**
     * Gets the children.
     *
     * @return the children
     */
    Collection<AreaLand> getChildren();
}
