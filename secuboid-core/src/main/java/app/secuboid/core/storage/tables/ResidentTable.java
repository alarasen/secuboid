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

import app.secuboid.api.reflection.TableRegistered;
import app.secuboid.api.storage.tables.Table;
import app.secuboid.core.storage.rows.ResidentRow;

@TableRegistered(row = ResidentRow.class, dependsOn = {LandTable.class, ParameterValueTable.class})
public class ResidentTable implements Table<ResidentRow> {

    // Needed for automatic table create
    public static final String CREATE_TABLE_SQL = ""
            + "CREATE TABLE IF NOT EXISTS %1$sresident ("
            + " land_id BIGINT NOT NULL,"
            + " parameter_value_id BIGINT NOT NULL,"
            + " level INT NOT NULL,"
            + " PRIMARY KEY (land_id, parameter_value_id),"
            + " CONSTRAINT fk_resident_land_id FOREIGN KEY (land_id) REFERENCES %1$sland (id),"
            + " CONSTRAINT fk_resident_parameter_value_id FOREIGN KEY (parameter_value_id) REFERENCES %1$sparameter_value (id)"
            + ")";

}
