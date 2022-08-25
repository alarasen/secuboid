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

import org.bukkit.event.HandlerList;

import app.secuboid.api.lands.Land;
import app.secuboid.api.parameters.values.ParameterValue;

/**
 * This events is called when a player is banned from a land.
 */
public class LandBanEvent extends LandEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ParameterValue parameterValue;

    /**
     * Instantiates a new land ban events.
     *
     * @param land           the land
     * @param ParameterValue parameterValue
     */
    public LandBanEvent(Land land, final ParameterValue parameterValue) {
        super(land);
        this.parameterValue = parameterValue;
    }

    /**
     * Gets the parameter value.
     *
     * @return the parameter value
     */
    public ParameterValue getParameterValue() {
        return parameterValue;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @SuppressWarnings("java:S4144")
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
