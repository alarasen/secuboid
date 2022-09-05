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

package app.secuboid.api.storage.rows;

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a row with an ID. The id can be null only before the insert to database.
 */
public interface RowWithId extends Row {

    /**
     * Gets the row id. Can be null only before the insert.
     *
     * @return the row id or null
     */
    @Nullable Long id();

    /**
     * Gets the row id. This method will fail with an exception if it is called before the insert.
     *
     * @return the row id
     */
    default long getId() {
        Long id = id();
        if (id == null) {
            throw new SecuboidRuntimeException("A requested ID for a row should not be null: " + this);
        }

        return id;
    }
}
