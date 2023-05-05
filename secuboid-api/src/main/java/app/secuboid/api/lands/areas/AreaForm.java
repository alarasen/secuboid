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
package app.secuboid.api.lands.areas;

import app.secuboid.api.messages.MessagePath;
import org.bukkit.Location;

/**
 * Represents an area form.
 */
public interface AreaForm {

    /**
     * @see Area#getX1()
     */
    int getX1();

    /**
     * @see Area#getY1()
     */
    int getY1();

    /**
     * @see Area#getZ1()
     */
    int getZ1();

    /**
     * @see Area#getX2()
     */
    int getX2();

    /**
     * @see Area#getY2()
     */
    int getY2();

    /**
     * @see Area#getZ2()
     */
    int getZ2();

    /**
     * @see Area#getArea()
     */
    long getArea();

    /**
     * @see Area#getVolume()
     */
    long getVolume();

    /**
     * @see Area#isLocationInside(int, int)
     */
    boolean isLocationInside(int x, int z);

    /**
     * @see Area#isLocationInside(int, int, int)
     */
    boolean isLocationInside(int x, int y, int z);

    /**
     * @see Area#isLocationInside(Location)
     */
    boolean isLocationInside(Location loc);

    /**
     * @see Area#isLocationInsideSquare(int, int)
     */
    boolean isLocationInsideSquare(int x, int z);

    /**
     * @see Area#getMessagePath()
     */
    MessagePath getMessagePath();
}