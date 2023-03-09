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
package app.secuboid.api.lands.flags;

import app.secuboid.api.flagtypes.FlagType;
import app.secuboid.api.lands.LandComponent;
import app.secuboid.api.recipients.RecipientExec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represent a flag with a value.
 *
 * @param landComponent the affected land
 * @param flagType      the flag type
 * @param source        the recipientExec source if needed or null
 * @param target        the recipientExec target if needed or null
 * @param metadata      the metadata if needed or null
 */
public record Flag(
        @NotNull LandComponent landComponent,
        @NotNull FlagType flagType,
        @Nullable RecipientExec source,
        @Nullable RecipientExec target,
        @Nullable String metadata) {
}
