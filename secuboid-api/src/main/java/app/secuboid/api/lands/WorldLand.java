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

import java.util.Set;

/**
 * Represents a land in the entire world.
 */
public interface WorldLand extends Land {

    /**
     * Gets the areas from a specific 2D point.
     *
     * @param x the x
     * @param z the z
     * @return the affected areas
     */
    Set<Area> get(int x, int z);

    /**
     * Gets the areas from a specific 3D point.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the affected areas
     */
    Set<Area> get(int x, int y, int z);
}