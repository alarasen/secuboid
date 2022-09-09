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

import app.secuboid.api.lands.areas.CylinderAreaForm;
import app.secuboid.api.messages.MessagePath;
import app.secuboid.core.messages.MessagePaths;
import app.secuboid.core.utilities.LocalMath;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CylinderAreaFormImpl extends AreaFormImpl implements CylinderAreaForm {

    private final double rX;
    private final double rZ;
    private final double originH;
    private final double originK;

    public CylinderAreaFormImpl(int x1, int y1, int z1, int x2, int y2, int z2) {
        super(x1, y1, z1, x2, y2, z2);

        // Important: Use this here to because all values can be tranformed: ?1 is
        // smallest number, ?2 is bigest number.
        rX = (double) (this.x2 - this.x1) / 2;
        rZ = (double) (this.z2 - this.z1) / 2;
        originH = this.x1 + rX;
        originK = this.z1 + rZ;
    }

    @Override
    public double getRX() {
        return rX;
    }

    @Override
    public double getRZ() {
        return rZ;
    }

    @Override
    public double getOriginH() {
        return originH;
    }

    @Override
    public double getOriginK() {
        return originK;
    }

    @Override
    public int getZPosFromX(int x) {
        return (int) Math.round(originK + (rZ * Math.sqrt((rX + x - originH) * (rX - x + originH))) / rX);
    }

    @Override
    public int getZNegFromX(int x) {
        return (int) Math.round(originK - (rZ * Math.sqrt((rX + x - originH) * (rX - x + originH))) / rX);
    }

    @Override
    public int getXPosFromZ(int z) {
        return (int) Math.round(originH + (rX * Math.sqrt((rZ + z - originK) * (rZ - z + originK))) / rZ);
    }

    @Override
    public int getXNegFromZ(int z) {
        return (int) Math.round(originH - (rX * Math.sqrt((rZ + z - originK) * (rZ - z + originK))) / rZ);
    }

    @Override
    public long getArea() {
        return Math.round(rX * rZ * Math.PI);
    }

    @Override
    public long getVolume() {
        return Math.round(rX * rZ * Math.PI * (getY2() - getY1() + 1));
    }

    @Override
    public boolean isLocationInside(int x, int z) {
        return ((Math.pow((x - originH), 2) / Math.pow(rX, 2))
                + (Math.pow((z - originK), 2) / Math.pow(rZ, 2))) < 1;
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
        return MessagePaths.areaCylinder(originH, originK, rX, rZ);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CylinderAreaFormImpl)) {
            return false;
        }
        CylinderAreaFormImpl cylinderAreaFormImpl = (CylinderAreaFormImpl) o;
        return rX == cylinderAreaFormImpl.rX && rZ == cylinderAreaFormImpl.rZ && originH == cylinderAreaFormImpl.originH
                && originK == cylinderAreaFormImpl.originK;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rX, rZ, originH, originK);
    }
}
