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

package app.secuboid.api.storage.rows;

/**
 * Represents a row with an ID. The id can be null only before the insert to database.
 */
public interface RowWithId extends Row {

    /**
     * Represents a non-existing id, when the id is not yet know.
     */
    public static final long NON_EXISTING_ID = -1L;

    /**
     * Gets the row id. Can be {@link #NON_EXISTING_ID} only before the insert.
     *
     * @return the row id or {@link #NON_EXISTING_ID}
     */
    long id();
}
