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

package app.secuboid.core.storage.rows;

import app.secuboid.api.storage.rows.RowWithId;
import app.secuboid.core.storage.types.AreaType;
import org.jetbrains.annotations.NotNull;

public record AreaRow(
        long id,
        long landId,
        @NotNull AreaType type,
        int x1,
        int y1,
        int z1,
        int x2,
        int y2,
        int z2
) implements RowWithId {
}
