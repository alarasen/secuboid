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

package app.secuboid.core.storage.types;

import app.secuboid.api.lands.LandComponent;
import app.secuboid.core.lands.AreaLandImpl;
import app.secuboid.core.lands.ConfigurationSetImpl;
import app.secuboid.core.lands.WorldLandImpl;
import app.secuboid.core.storage.rows.LandRow;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public enum LandType {
    WORLD_LAND("W", r -> new WorldLandImpl(r.id(), r.name())),
    AREA_LAND("L", r -> new AreaLandImpl(r.id(), r.name(), null)),
    CONFIGURATION_SET("S", r -> new ConfigurationSetImpl(r.id(), r.name()));

    public static @NotNull LandType fromValue(@NotNull String value) {
        for (LandType landType : LandType.values()) {
            if (landType.value.equals(value)) {
                return landType;
            }
        }

        throw new IllegalArgumentException("Invalid land type: " + value);
    }

    public static @NotNull LandComponent newLandComponent(@NotNull LandRow landRow) {
        return landRow.type().creator.apply(landRow);
    }

    public final @NotNull String value;
    private final @NotNull Function<LandRow, LandComponent> creator;

    private LandType(@NotNull String value, @NotNull Function<LandRow, LandComponent> creator) {
        this.value = value;
        this.creator = creator;
    }
}
