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
package app.secuboid.api.events;

import app.secuboid.api.lands.Land;
import app.secuboid.api.recipients.Recipient;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This events is called when a player is banned from a land.
 */
public class LandBanEvent extends LandEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Recipient recipient;

    /**
     * Instantiates a new land ban events.
     *
     * @param land      the land
     * @param recipient recipient
     */
    public LandBanEvent(@NotNull Land land, @NotNull Recipient recipient) {
        super(land);
        this.recipient = recipient;
    }

    @SuppressWarnings("java:S4144")
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the recipient.
     *
     * @return the recipient
     */
    @NotNull
    public Recipient getRecipient() {
        return recipient;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
