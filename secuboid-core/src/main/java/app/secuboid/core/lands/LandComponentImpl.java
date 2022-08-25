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
package app.secuboid.core.lands;

import app.secuboid.api.lands.LandComponent;
import app.secuboid.api.lands.flags.Flags;
import app.secuboid.api.lands.residents.Residents;
import app.secuboid.core.lands.flags.FlagsImpl;
import app.secuboid.core.lands.residents.ResidentsImpl;

import java.util.Objects;

public class LandComponentImpl implements LandComponent {

    protected final String name;
    private final Flags flags;
    private final Residents residents;

    private int id;

    protected LandComponentImpl(String name) {
        this.name = name;
        flags = new FlagsImpl(this);
        residents = new ResidentsImpl(this);

        id = ID_NON_CREATED_VALUE;
    }

    @Override
    public final int getId() {
        return id;
    }

    @Override
    public final void setId(int id) {
        this.id = id;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final void setDefault() {
        // TODO Auto-generated method stub

    }

    @Override
    public final Flags getFlags() {
        return flags;
    }

    @Override
    public final Residents getResidents() {
        return residents;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        LandComponentImpl abstractLandImpl = (LandComponentImpl) o;
        return Objects.equals(id, abstractLandImpl.id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
