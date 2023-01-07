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

import app.secuboid.api.lands.AreaLand;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.core.lands.areas.AreaImpl;
import app.secuboid.core.lands.areas.CuboidAreaFormImpl;
import app.secuboid.core.lands.areas.CylinderAreaFormImpl;
import app.secuboid.core.storage.rows.AreaRow;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public enum AreaType {
    CUBOID("B", (r, l) -> new AreaImpl(r.id(), new CuboidAreaFormImpl(r.x1(), r.y1(), r.z1(), r.x2(), r.y2(),
            r.z2()), l)),
    CYLINDER("C", (r, l) -> new AreaImpl(r.id(), new CylinderAreaFormImpl(r.x1(), r.y1(), r.z1(), r.x2(), r.y2(),
            r.z2()), l));

    public final @NotNull String value;
    private final @NotNull BiFunction<AreaRow, AreaLand, Area> creator;

    AreaType(@NotNull String value, @NotNull BiFunction<AreaRow, AreaLand, Area> creator) {
        this.value = value;
        this.creator = creator;
    }

    public static @NotNull AreaType fromValue(@NotNull String value) {
        for (AreaType areaType : AreaType.values()) {
            if (areaType.value.equals(value)) {
                return areaType;
            }
        }

        throw new IllegalArgumentException("Invalid area type: " + value);
    }

    public static @NotNull Area newArea(@NotNull AreaRow areaRow, @NotNull AreaLand areaLand) {
        return areaRow.type().creator.apply(areaRow, areaLand);
    }
}
