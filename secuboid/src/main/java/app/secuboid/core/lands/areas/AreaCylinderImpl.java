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
import app.secuboid.api.lands.areas.AreaCylinder;
import app.secuboid.api.lands.areas.AreaType;
import app.secuboid.api.messages.MessagePath;
import app.secuboid.core.messages.MessagePaths;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class AreaCylinderImpl extends AreaImpl implements AreaCylinder {

    private final long id;

    private final Land land;

    private final int x1;
    private final int y1;
    private final int z1;
    private final int x2;
    private final int y2;
    private final int z2;

    @EqualsAndHashCode.Exclude
    private boolean isSetValues = false;
    @EqualsAndHashCode.Exclude
    private double rX = 0;
    @EqualsAndHashCode.Exclude
    private double rZ = 0;
    @EqualsAndHashCode.Exclude
    private double originH = 0;
    @EqualsAndHashCode.Exclude
    private double originK = 0;

    @Builder
    public AreaCylinderImpl(long id, Land land, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.id = id;
        this.land = land;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public double getRX() {
        initValuesIfNeeded();
        return rX;
    }

    public double getRZ() {
        initValuesIfNeeded();
        return rZ;
    }

    public double getOriginH() {
        initValuesIfNeeded();
        return originH;
    }

    public double getOriginK() {
        initValuesIfNeeded();
        return originK;
    }

    @Override
    public AreaType getType() {
        return AreaType.CYLINDER;
    }

    @Override
    public int getZPosFromX(int x) {
        initValuesIfNeeded();
        return (int) Math.round(originK + (rZ * Math.sqrt((rX + x - originH) * (rX - x + originH))) / getRX());
    }

    @Override
    public int getZNegFromX(int x) {
        initValuesIfNeeded();
        return (int) Math.round(originK - (rZ * Math.sqrt((rX + x - originH) * (rX - x + originH))) / getRX());
    }

    @Override
    public int getXPosFromZ(int z) {
        initValuesIfNeeded();
        return (int) Math.round(originH + (rX * Math.sqrt((rZ + z - originK) * (rZ - z + originK))) / getRZ());
    }

    @Override
    public int getXNegFromZ(int z) {
        initValuesIfNeeded();
        return (int) Math.round(originH - (rX * Math.sqrt((rZ + z - originK) * (rZ - z + originK))) / getRZ());
    }

    @Override
    public long getArea() {
        initValuesIfNeeded();
        return Math.round(rX * rZ * Math.PI);
    }

    @Override
    public long getVolume() {
        initValuesIfNeeded();
        return Math.round(rX * rZ * Math.PI * (y2 - y1 + 1));
    }

    @Override
    public boolean isLocationInside(int x, int z) {
        initValuesIfNeeded();
        return ((Math.pow((x - originH), 2) / Math.pow(rX, 2))
                + (Math.pow((z - originK), 2) / Math.pow(rZ, 2))) < 1;
    }

    @Override
    public MessagePath getMessagePath() {
        initValuesIfNeeded();
        return MessagePaths.areaCylinder(originH, originK, rX, getRZ());
    }

    private void initValuesIfNeeded() {
        if (isSetValues) {
            return;
        }

        rX = (double) (x2 - x1) / 2;
        rZ = (double) (z2 - z1) / 2;
        originH = x1 + rX;
        originK = z1 + rZ;
        isSetValues = true;
    }
}
