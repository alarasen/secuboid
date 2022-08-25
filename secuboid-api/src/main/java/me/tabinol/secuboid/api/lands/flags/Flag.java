/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.secuboid.api.lands.flags;

import me.tabinol.secuboid.api.flagtypes.FlagType;
import me.tabinol.secuboid.api.lands.LandComponent;
import me.tabinol.secuboid.api.parameters.values.ParameterValue;
import me.tabinol.secuboid.api.storage.tables.Row;

/**
 * Represent a flag with a value.
 * 
 * @param LandComponent the affected land
 * @param flagType      the flag type
 * @param source        the parameter value source if needed or null
 * @param target        the parameter value target if needed or null
 * @param metadata      the metadata if needed or null
 */
public record Flag(
        LandComponent land,
        FlagType flagType,
        ParameterValue source,
        ParameterValue target,
        String metadata) implements Row {
}
