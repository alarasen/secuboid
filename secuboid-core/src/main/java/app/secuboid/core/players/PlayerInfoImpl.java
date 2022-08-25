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
package app.secuboid.core.players;

import app.secuboid.api.lands.Land;
import app.secuboid.api.parameters.values.ParameterValuePlayer;
import app.secuboid.api.players.PlayerInfo;
import app.secuboid.core.selection.PlayerSelection;
import app.secuboid.core.selection.SenderSelection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.UUID;

public class PlayerInfoImpl extends CommandSenderInfoImpl implements PlayerInfo {

    private final Player player;
    private final PlayerSelection playerSelection;

    private boolean adminMode = false;

    private long lastUpdateTimeMillis = 0;
    private Location lastLocation = null;
    private boolean tpCancel = false;

    private int selectionTop;
    private int selectionBottom;

    PlayerInfoImpl(Player player) {
        super(player);
        this.player = player;
        playerSelection = new PlayerSelection(player);
    }

    @Override
    public ParameterValuePlayer getParameterValue() {
        // TODO Parameter value redesign
        return null;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public boolean isAdminMode() {

        // Security for adminmode
        if (adminMode && !player.hasPermission("secuboid.adminmode")) {
            adminMode = false;
            return false;
        }

        return adminMode;
    }

    @Override
    public void setAdminMode(boolean value) {
        adminMode = value;
    }

    public long getLastUpdateTimeMillis() {
        return lastUpdateTimeMillis;
    }

    public void setLastUpdateTimeMillis(long lastUpdateTimeMillis) {
        this.lastUpdateTimeMillis = lastUpdateTimeMillis;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public boolean hasTpCancel() {
        return tpCancel;
    }

    public void setTpCancel(boolean tpCancel) {
        this.tpCancel = tpCancel;
    }

    @Override
    public int getSelectionTop() {
        return selectionTop;
    }

    public void setSelectionTop(int selectionTop) {
        this.selectionTop = selectionTop;
    }

    @Override
    public int getSelectionBottom() {
        return selectionBottom;
    }

    @Override
    public Land getLastLand() {
        // TODO Generated
        return null;
    }

    @Override
    public SenderSelection getSelection() {
        return playerSelection;
    }

    @SuppressWarnings("java:S4144")
    public PlayerSelection getPlayerSelection() {
        return playerSelection;
    }

    public void updatePosInfo(Event event, Location loc) {

        // TODO Permissions and LandEvent
        // LandPermissionsFlags landPermissionsFlags;
        // LandPermissionsFlags oldPermissionsFlags;
        // PlayerLandChangeEvent landEvent;

        // landPermissionsFlags = secuboid.getLands().getPermissionsFlags(loc);

        // if (newPlayer) {
        // entry.setLastLandPermissionsFlags(oldPermissionsFlags =
        // landPermissionsFlags);
        // } else {
        // oldPermissionsFlags = entry.getLastLandPermissionsFlags();
        // }
        // if (newPlayer || landPermissionsFlags != oldPermissionsFlags) {
        // final boolean isTp = eventNullable instanceof PlayerTeleportEvent;
        // // First parameter : If it is a new player, it is null, if not new
        // // player, it is "old"
        // landEvent = new PlayerLandChangeEvent(newPlayer ? null : oldPermissionsFlags,
        // landPermissionsFlags, player,
        // entry.getLastLoc(), loc, isTp);
        // pm.callEvent(landEvent);
        //
        // if (landEvent.isCancelled()) {
        // if (isTp) {
        // ((PlayerTeleportEvent) eventNullable).setCancelled(true);
        // return;
        // }
        // if (landPermissionsFlags == oldPermissionsFlags) {
        // player.teleport(player.getWorld().getSpawnLocation());
        // } else {
        // final Location retLoc = entry.getLastLoc();
        // player.teleport(new Location(retLoc.getWorld(), retLoc.getX(),
        // retLoc.getBlockY(), retLoc.getZ(),
        // loc.getYaw(), loc.getPitch()));
        // }
        // entry.setTpCancel(true);
        // return;
        // }
        // entry.setLastLandPermissionsFlags(landPermissionsFlags);
        //
        // // Update player in the lands
        // final Land oldLandNullable = oldPermissionsFlags.getLandNullable();
        // if (oldLandNullable != null && oldPermissionsFlags != landPermissionsFlags) {
        // oldLandNullable.removePlayerInLand(player);
        // }
        // final Land landNullable = landPermissionsFlags.getLandNullable();
        // if (landNullable != null) {
        // landNullable.addPlayerInLand(player);
        // }
        // }
        lastLocation = loc;

        playerSelection.updateSelectionFromLocation();
    }
}
