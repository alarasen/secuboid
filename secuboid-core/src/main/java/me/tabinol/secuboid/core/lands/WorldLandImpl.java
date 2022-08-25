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

import me.tabinol.secuboid.api.lands.WorldLand;
import me.tabinol.secuboid.core.lands.areas.Areas;

public class WorldLandImpl extends LandImpl implements WorldLand {

    private final Areas areas;

    // A generated id, not the uuid of the world because it changes on world
    // regen.
    public WorldLandImpl(String worldName) {
        super(worldName);
        areas = new Areas();
    }
}
