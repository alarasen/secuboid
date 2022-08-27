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

import app.secuboid.api.lands.flags.Flag;
import app.secuboid.api.reflection.TableRegistered;
import app.secuboid.api.storage.tables.Table;

@TableRegistered(row = Flag.class, dependsOn = {LandTable.class, ParameterValueTable.class})
public class FlagTable implements Table<Flag> {

    // Needed for automatic table create
    public static final String CREATE_TABLE_SQL = ""
            + "CREATE TABLE IF NOT EXISTS %1$sflag ("
            + " land_id BIGINT NOT NULL,"
            + " flag_type VARCHAR(45) NOT NULL,"
            + " source_parameter_value_id BIGINT NULL,"
            + " target_parameter_value_id BIGINT NULL,"
            + " metadata {{TINYTEXT}} NULL,"
            + " PRIMARY KEY (land_id, flag_type, source_parameter_value_id, target_parameter_value_id),"
            + " CONSTRAINT fk_flag_land_id FOREIGN KEY (land_id) REFERENCES %1$sland (id),"
            + " CONSTRAINT fk_flag_source_parameter_value_id FOREIGN KEY (source_parameter_value_id) REFERENCES %1$sparameter_value (id),"
            + " CONSTRAINT fk_flag_target_parameter_value_id FOREIGN KEY (target_parameter_value_id) REFERENCES %1$sparameter_value (id)"
            + ")";
}
