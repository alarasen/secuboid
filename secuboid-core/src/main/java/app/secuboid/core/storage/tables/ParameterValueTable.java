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
package app.secuboid.core.storage.tables;

import app.secuboid.api.parameters.values.ParameterValue;
import app.secuboid.api.reflection.TableRegistered;
import app.secuboid.api.storage.tables.Table;

@TableRegistered(row = ParameterValue.class)
public class ParameterValueTable implements Table<ParameterValue> {

    // Needed for automatic table create
    public static final String CREATE_TABLE_SQL = ""
            + "CREATE TABLE IF NOT EXISTS %1$sparameter_value ("
            + " id INT NOT NULL {{AUTOINCREMENT}},"
            + " short_name VARCHAR(10) NOT NULL,"
            + " value VARCHAR(45) NULL,"
            + " PRIMARY KEY (id),"
            + " CONSTRAINT parameter_value_short_name_value_unique UNIQUE (short_name, value)"
            + ")";
}
