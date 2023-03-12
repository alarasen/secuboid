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
package app.secuboid.core.recipients;

import app.secuboid.api.lands.Land;
import app.secuboid.api.lands.flags.Flag;
import app.secuboid.api.recipients.RecipientExec;
import app.secuboid.api.registration.RecipientRegistered;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import static app.secuboid.api.utilities.CharacterCase.UPPERCASE;

@RecipientRegistered(name = "entity-type", shortName = "et", chatColor = "§5", needsValue = true,
        characterCase = UPPERCASE, priority = 60)
public class RecipientExecEntityType implements RecipientExec {

    @Override
    public boolean hasAccess(@NotNull Flag flag, @NotNull Entity entity) {
        // TODO Implement
        return true;
    }

    @Override
    public boolean hasAccess(@NotNull Flag flag, @NotNull Entity entity, @NotNull Land originLand) {
        return hasAccess(flag, entity);
    }
}