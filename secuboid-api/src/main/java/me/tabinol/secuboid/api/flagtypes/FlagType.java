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
package me.tabinol.secuboid.api.flagtypes;

/**
 * Represents a flag type. Hidden "true" is only for a flag
 * that contains sensitive or ununderstandable informations for a player.
 * 
 * @param name        the name of the flag lowercase with "-" if more than one
 *                    word
 * @param description the flag description
 * @param needSource  is this flag need a source parameter (ex: the player who
 *                    take the action, the explosed creeper)?
 * @param needTarget  is this flag need a target parameter (ex: to mob targeted
 *                    with an arrow)?
 * @param needMetada  is this flag need extra data?
 * @param isHidden    hidden "true" is only for a flag that contains sensitive
 *                    or ununderstandable informations for a player
 **/
public record FlagType(
        String name,
        String description,
        boolean needSource,
        boolean needTarget,
        boolean needMetadata,
        boolean isHidden) {
}
