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
package app.secuboid.api.storage.tables;

/**
 * Represents a row with an ID. A row must be created with a id=-1 first, then,
 * the database sets it to the correct value.
 */
public interface RowWithId extends Row {

    public static final int ID_NON_CREATED_VALUE = -1;

    /**
     * Gets the row id.
     * 
     * @return the row id or 0 if it is not yet created to the database
     */
    int getId();

    /**
     * DO NOT SET THE ROW ID!!! It will be set by the database.
     * 
     * @param id the id
     */
    void setId(int id);
}
