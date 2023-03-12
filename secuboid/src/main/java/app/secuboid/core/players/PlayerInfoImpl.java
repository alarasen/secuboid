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
package app.secuboid.core.players;

import app.secuboid.api.lands.Land;
import app.secuboid.api.lands.LocationPath;
import app.secuboid.api.lands.WorldLand;
import app.secuboid.api.lands.areas.Area;
import app.secuboid.api.players.PlayerInfo;
import app.secuboid.api.selection.PlayerSelection;
import app.secuboid.api.selection.SenderSelection;
import app.secuboid.core.selection.PlayerSelectionImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerInfoImpl extends CommandSenderInfoImpl implements PlayerInfo {

    private final @NotNull Player player;
    private final @NotNull PlayerSelection playerSelection;

    private boolean adminMode;

    private long lastUpdateTimeMillis;
    private @NotNull Location lastLocation;
    private @Nullable Area area;
    private @NotNull Land land;
    private @NotNull LocationPath locationPath;
    private boolean isTpCancel;

    PlayerInfoImpl(@NotNull Player player, @NotNull Location lastLocation, @Nullable Area area, @NotNull Land land,
                   @NotNull LocationPath locationPath) {
        super(player);
        this.player = player;
        playerSelection = new PlayerSelectionImpl(this);

        adminMode = false;
        lastUpdateTimeMillis = 0L;
        this.lastLocation = lastLocation;
        this.area = area;
        this.land = land;
        this.locationPath = locationPath;
        isTpCancel = false;
    }

    public void updatePlayerPosition(@NotNull Location lastLocation, @Nullable Area area, @NotNull Land land,
                                     @NotNull LocationPath locationPath) {
        this.lastLocation = lastLocation;
        this.area = area;
        this.land = land;
        this.locationPath = locationPath;

        ((PlayerSelectionImpl) playerSelection).updateSelectionFromLocation();
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull UUID getUUID() {
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

    public boolean hasTpCancel() {
        return isTpCancel;
    }

    public void setTpCancel(boolean tpCancel) {
        this.isTpCancel = tpCancel;
    }

    @Override
    public @Nullable Area getArea() {
        return area;
    }

    @Override
    public @NotNull Land getLand() {
        return land;
    }

    @Override
    public @NotNull LocationPath getLocationPath() {
        return locationPath;
    }

    @Override
    public @NotNull WorldLand getWorldLand() {
        return land.getWorldLand();
    }

    @Override
    public @NotNull SenderSelection getSelection() {
        return playerSelection;
    }

    @Override
    public @NotNull PlayerSelection getPlayerSelection() {
        return playerSelection;
    }
}
