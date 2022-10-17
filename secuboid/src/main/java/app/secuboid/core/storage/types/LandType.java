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

import app.secuboid.api.lands.Land;
import app.secuboid.api.lands.LandComponent;
import app.secuboid.core.lands.AreaLandImpl;
import app.secuboid.core.lands.ConfigurationSetImpl;
import app.secuboid.core.lands.WorldLandImpl;
import app.secuboid.core.storage.rows.LandRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public enum LandType {
    WORLD_LAND("W", (r, p) -> new WorldLandImpl(r.id(), r.name())),
    AREA_LAND("L", (r, p) -> new AreaLandImpl(r.id(), r.name(), p)),
    CONFIGURATION_SET("S", (r, p) -> new ConfigurationSetImpl(r.id(), r.name()));

    public static @NotNull LandType fromValue(@NotNull String value) {
        for (LandType landType : LandType.values()) {
            if (landType.value.equals(value)) {
                return landType;
            }
        }

        throw new IllegalArgumentException("Invalid land type: " + value);
    }

    public static @NotNull LandComponent newLandComponent(@NotNull LandRow landRow, @Nullable Land parent) {
        return landRow.type().creator.apply(landRow, parent);
    }

    public final @NotNull String value;
    private final @NotNull BiFunction<LandRow, Land, LandComponent> creator;

    LandType(@NotNull String value, @NotNull BiFunction<LandRow, Land, LandComponent> creator) {
        this.value = value;
        this.creator = creator;
    }
}
