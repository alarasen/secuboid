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
package app.secuboid.api.lands.residents;

import app.secuboid.api.lands.LandComponent;
import app.secuboid.api.recipients.RecipientExec;

/**
 * Represents a resident.
 *
 * @param landComponent the land component
 * @param recipientExec the recipientExec
 * @param level         the level
 */
public record Resident(
        LandComponent landComponent,
        RecipientExec recipientExec,
        int level) {
}
