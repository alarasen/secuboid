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
package me.tabinol.secuboid.core.listeners;

import static me.tabinol.secuboid.core.messages.Message.message;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.tabinol.secuboid.api.messages.MessageType;
import me.tabinol.secuboid.core.messages.MessagePaths;
import me.tabinol.secuboid.core.players.PlayerInfoImpl;
import me.tabinol.secuboid.core.selection.PlayerSelection;

public class PlayerMoveListener extends AbstractListener {

    private static final int MOVE_TIME_LAPS_TICKS = 500;

    PlayerMoveListener() {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMoveMonitor(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        PlayerInfoImpl playerInfoImpl = getPlayerInfoImpl(player);
        Location loc = player.getLocation();
        long last = playerInfoImpl.getLastUpdateTimeMillis();
        long now = System.currentTimeMillis();

        if (now - last < MOVE_TIME_LAPS_TICKS) {
            return;
        }

        playerInfoImpl.setLastUpdateTimeMillis(now);
        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();

        if (fromLocation.getWorld() == toLocation.getWorld() && fromLocation.distance(toLocation) == 0) {
            return;
        }

        playerInfoImpl.updatePosInfo(event, loc);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleportMonitor(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        PlayerInfoImpl playerInfoImpl = getPlayerInfoImpl(player);
        PlayerSelection playerSelection = playerInfoImpl.getPlayerSelection();
        playerSelection.removeSelection();
        message().sendMessage(player, MessageType.NORMAL, MessagePaths.selectionCancel());
    }
}