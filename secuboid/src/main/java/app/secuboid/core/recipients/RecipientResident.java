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
package app.secuboid.core.recipients;

import app.secuboid.api.exceptions.RecipientException;
import app.secuboid.api.lands.Land;
import app.secuboid.api.recipients.Recipient;
import app.secuboid.api.recipients.RecipientType;
import app.secuboid.api.reflection.RecipientRegistered;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;

@RecipientRegistered(name = "resident", shortName = "res", chatColor = "\u00A7A", needsValue = true, priority = 60)
public record RecipientResident(@NotNull RecipientType type,
                                long id,
                                int level
) implements Recipient {

    // Needed for load from database
    public static RecipientResident newInstance(@NotNull RecipientType type, long id,
                                                @NotNull String value) throws RecipientException {
        int level;

        try {
            level = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            String msg = format("Non parsable level number for a resident [level=%s]", value);
            throw new RecipientException(msg, e);
        }

        return new RecipientResident(type, id, level);
    }

    @Override
    public @NotNull String getValue() {
        return Integer.toString(level);
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity) {
        return false;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand) {
        // TODO Auto-generated method stub
        return false;
    }
}
