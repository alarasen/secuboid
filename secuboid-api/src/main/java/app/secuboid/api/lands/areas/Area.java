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
package app.secuboid.api.lands.areas;

import org.bukkit.Location;

import app.secuboid.api.lands.AreaLand;
import app.secuboid.api.messages.MessagePath;

/**
 * Represents an area of any type.
 */
public interface Area {

    /**
     * Gets the area form.
     *
     * @return the area form
     */
    AreaForm getAreaForm();

    /**
     * Gets the area id.
     *
     * @return the area id
     */
    int getID();

    /**
     * Gets the land.
     *
     * @return the land
     */
    AreaLand getLand();

    /**
     * Gets the x1.
     *
     * @return the x1
     */
    int getX1();

    /**
     * Gets the y1.
     *
     * @return the y1
     */
    int getY1();

    /**
     * Gets the z1.
     *
     * @return the z1
     */
    int getZ1();

    /**
     * Gets the x2.
     *
     * @return the x2
     */
    int getX2();

    /**
     * Gets the y2.
     *
     * @return the y2
     */
    int getY2();

    /**
     * Gets the z2.
     *
     * @return the z2
     */
    int getZ2();

    /**
     * Gets the area (surface).
     *
     * @return the area
     */
    long getArea();

    /**
     * Gets the volume.
     *
     * @return the volume
     */
    long getVolume();

    /**
     * Gets if the location is inside the area. This method ignore y value.
     *
     * @param x the x
     * @param z the z
     * @return if true or false
     */
    boolean isLocationInside(int x, int z);

    /**
     * Gets if the location is inside the area.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return if true or false
     */
    boolean isLocationInside(int x, int y, int z);

    /**
     * Gets if the location is inside the area.
     *
     * @param loc the location
     * @return if true or false
     */
    boolean isLocationInside(Location loc);

    /**
     * Gets if the location is in the square limit of the land. This method ignore
     * the world and the y values. Use isLocationInside methods if you want to check
     * an exact location.
     *
     * @param x the x
     * @param z the z
     * @return if true or false
     */
    boolean isLocationInsideSquare(int x, int z);

    /**
     * Gets the message path (format) for this area.
     * 
     * @return the message path
     */
    MessagePath getMessagePath();
}
