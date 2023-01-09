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
package app.secuboid.core.listeners;

import app.secuboid.api.messages.MessageType;
import app.secuboid.api.selection.PlayerSelection;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.messages.MessagePaths;
import app.secuboid.core.players.PlayerInfoImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import static app.secuboid.core.messages.Message.message;
import static org.bukkit.event.EventPriority.MONITOR;

public class PlayerMoveListener extends AbstractListener {

    private static final int MOVE_TIME_LAPS_TICKS = 500;

    PlayerMoveListener() {
    }

    public void onPlayerSpawnMonitor(PlayerSpawnLocationEvent event) {
        Player player = event.getPlayer();
        PlayerInfoImpl playerInfoImpl = getPlayerInfoImpl(player);
        Location spawnLocation = event.getSpawnLocation();

        playerInfoImpl.updatePosInfo(event, spawnLocation);
    }

    @EventHandler(priority = MONITOR, ignoreCancelled = true)
    public void onPlayerMoveMonitor(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();

        if (toLocation == null) {
            return;
        }

        PlayerInfoImpl playerInfoImpl = getPlayerInfoImpl(player);
        long last = playerInfoImpl.getLastUpdateTimeMillis();
        long now = System.currentTimeMillis();

        if (now - last < MOVE_TIME_LAPS_TICKS) {
            return;
        }

        playerInfoImpl.setLastUpdateTimeMillis(now);

        if (fromLocation.getWorld() == toLocation.getWorld() && fromLocation.distance(toLocation) == 0) {
            return;
        }

        playerInfoImpl.updatePosInfo(event, toLocation);
    }

    @EventHandler(priority = MONITOR, ignoreCancelled = true)
    public void onPlayerTeleportMonitor(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        PlayerInfoImpl playerInfoImpl = getPlayerInfoImpl(player);
        PlayerSelection playerSelection = playerInfoImpl.getPlayerSelection();
        playerSelection.removeSelection();
        SecuboidImpl.instance().getChatGetter().remove(playerInfoImpl);
        message().sendMessage(player, MessageType.NORMAL, MessagePaths.selectionCancel());
    }
}