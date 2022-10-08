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
package app.secuboid.core.parameters.values;

import app.secuboid.api.exceptions.ParameterValueException;
import app.secuboid.api.lands.Land;
import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.parameters.values.ParameterValueType;
import app.secuboid.api.reflection.ParameterValueRegistered;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParameterValueRegistered(name = "nobody", shortName = "nobody", chatColor = "\u00A78")
public record ParameterValueNobody(@NotNull ParameterValueType type,
                                   long id
) implements ParameterValue {

    // Needed for load from database
    @SuppressWarnings({"unused", "java:S1172", "java:S1130"})
    public static ParameterValueNobody newInstance(@NotNull ParameterValueType type, long id, @Nullable String value) throws ParameterValueException {
        return new ParameterValueNobody(type, id);
    }

    @Override
    public @Nullable String getValue() {
        return null;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity) {
        return false;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand) {
        return false;
    }
}
