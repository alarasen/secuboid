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

package app.secuboid.core.lands.areas;

import app.secuboid.api.lands.Land;
import app.secuboid.api.lands.areas.AreaCuboid;
import app.secuboid.api.lands.areas.AreaType;
import app.secuboid.api.messages.MessagePath;
import app.secuboid.core.messages.MessagePaths;
import app.secuboid.core.utilities.LocalMath;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class AreaCuboidImpl extends AreaImpl implements AreaCuboid {

    private final long id;

    @Builder.Default
    private final Land land = null;

    private final int x1;
    private final int y1;
    private final int z1;
    private final int x2;
    private final int y2;
    private final int z2;

    @Override
    public AreaType getType() {
        return AreaType.CUBOID;
    }

    @Override
    public long getArea() {
        return (x2 - x1 + 1L) * (z2 - z1 + 1L);
    }

    @Override
    public long getVolume() {
        return getArea() * (y2 - y1 + 1);
    }

    @Override
    public boolean isLocationInside(int x, int z) {
        return LocalMath.isInRange(x, x1, x2)
                && LocalMath.isInRange(z, z1, z2);
    }

    @Override
    public MessagePath getMessagePath() {
        return MessagePaths.areaCuboid(x1, y1, z1, x2, y2, z2);
    }
}
