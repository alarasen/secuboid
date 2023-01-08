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

import app.secuboid.api.lands.AreaLand;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.lands.areas.AreaForm;
import app.secuboid.api.messages.MessagePath;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AreaImpl implements Area {

    private final long id;
    private final @NotNull AreaForm areaForm;
    private final @NotNull AreaLand land;

    public AreaImpl(long id, @NotNull AreaForm areaForm, @NotNull AreaLand land) {
        this.id = id;
        this.areaForm = areaForm;
        this.land = land;

        ((AreaFormImpl) areaForm).isResizable = false;
    }

    @Override
    public long id() {
        return id;
    }

    @Override
    public @NotNull String getName() {
        return Long.toString(id);
    }

    @Override
    public @NotNull String getPathName() {
        return land.getPathName() + SEPARATOR_AREA + id;
    }

    @Override
    public @NotNull AreaForm getAreaForm() {
        return areaForm;
    }

    @Override
    public @NotNull AreaLand getLand() {
        return land;
    }

    @Override
    public int getX1() {
        return areaForm.getX1();
    }

    @Override
    public int getY1() {
        return areaForm.getY1();
    }

    @Override
    public int getZ1() {
        return areaForm.getZ1();
    }

    @Override
    public int getX2() {
        return areaForm.getX2();
    }

    @Override
    public int getY2() {
        return areaForm.getY2();
    }

    @Override
    public int getZ2() {
        return areaForm.getZ2();
    }

    public boolean isLocationInside(int x, int z) {
        return areaForm.isLocationInside(x, z);
    }

    public boolean isLocationInside(int x, int y, int z) {
        return areaForm.isLocationInside(x, y, z);
    }

    public boolean isLocationInside(@NotNull Location loc) {
        return areaForm.isLocationInside(loc);
    }

    @Override
    public boolean isLocationInsideSquare(int x, int z) {
        return areaForm.isLocationInsideSquare(x, z);
    }

    public @NotNull MessagePath getMessagePath() {
        return areaForm.getMessagePath();
    }

    @Override
    public long getArea() {
        return areaForm.getArea();
    }

    @Override
    public long getVolume() {
        return areaForm.getVolume();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AreaImpl areaImpl)) {
            return false;
        }
        return id == areaImpl.id && Objects.equals(areaForm, areaImpl.areaForm) && Objects.equals(land, areaImpl.land);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, areaForm, land);
    }
}
