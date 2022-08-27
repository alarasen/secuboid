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

import app.secuboid.api.lands.areas.CuboidAreaForm;
import app.secuboid.api.messages.MessagePath;
import app.secuboid.core.messages.MessagePaths;
import app.secuboid.core.utilities.LocalMath;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class CuboidAreaFormImpl extends AreaFormImpl implements CuboidAreaForm {

    public CuboidAreaFormImpl(int x1, int y1, int z1, int x2, int y2, int z2) {
        super(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public long getArea() {
        return (getX2() - getX1() + 1l) * (getZ2() - getZ1() + 1l);
    }

    @Override
    public long getVolume() {
        return getArea() * (getY2() - getY1() + 1);
    }

    @Override
    public boolean isLocationInside(int x, int z) {
        return LocalMath.isInRange(x, getX1(), getX2())
                && LocalMath.isInRange(z, getZ1(), getZ2());
    }

    @Override
    public boolean isLocationInside(int x, int y, int z) {
        return isLocationInside(x, z) && LocalMath.isInRange(y, getY1(), getY2());
    }

    @Override
    public boolean isLocationInside(Location loc) {
        return isLocationInside(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public @NotNull MessagePath getMessagePath() {
        return MessagePaths.areaCuboid(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CuboidAreaFormImpl)) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
