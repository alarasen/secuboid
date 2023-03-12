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
package app.secuboid.core.lands.areas;

import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.core.messages.Log;
import app.secuboid.core.utilities.LocalMath;

import java.util.Objects;

public abstract class AreaFormImpl implements AreaForm {

    protected int x1;
    protected int y1;
    protected int z1;
    protected int x2;
    protected int y2;
    protected int z2;

    protected boolean isResizable;

    protected AreaFormImpl(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);

        isResizable = true;
    }

    @Override
    public final int getX1() {
        return x1;
    }

    public final void setX1(int x1) {
        if (checkIsResizable()) {
            this.x1 = x1;
        }
    }

    @Override
    public final int getY1() {
        return y1;
    }

    public final void setY1(int y1) {
        if (checkIsResizable()) {
            this.y1 = y1;
        }
    }

    @Override
    public final int getZ1() {
        return z1;
    }

    public final void setZ1(int z1) {
        if (checkIsResizable()) {
            this.z1 = z1;
        }
    }

    @Override
    public final int getX2() {
        return x2;
    }

    public final void setX2(int x2) {
        if (checkIsResizable()) {
            this.x2 = x2;
        }
    }

    @Override
    public final int getY2() {
        return y2;
    }

    public final void setY2(int y2) {
        if (checkIsResizable()) {
            this.y2 = y2;
        }
    }

    @Override
    public final int getZ2() {
        return z2;
    }

    public final void setZ2(int z2) {
        if (checkIsResizable()) {
            this.z2 = z2;
        }
    }

    @Override
    public final boolean isLocationInsideSquare(int x, int z) {
        return LocalMath.isInRange(x, getX1(), getX2()) && LocalMath.isInRange(z, getZ1(), getZ2());
    }

    private boolean checkIsResizable() {
        if (!isResizable) {
            Log.log().severe("Area values can only modified from visual selection");
        }

        return isResizable;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AreaFormImpl areaFormImpl)) {
            return false;
        }
        return x1 == areaFormImpl.x1 && y1 == areaFormImpl.y1 && z1 == areaFormImpl.z1 && x2 == areaFormImpl.x2
                && y2 == areaFormImpl.y2 && z2 == areaFormImpl.z2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, y1, z1, x2, y2, z2);
    }
}
