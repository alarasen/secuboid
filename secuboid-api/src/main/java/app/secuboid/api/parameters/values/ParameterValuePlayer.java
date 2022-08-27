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
package app.secuboid.api.parameters.values;

import app.secuboid.api.exceptions.ParameterValueException;
import app.secuboid.api.lands.Land;
import app.secuboid.api.reflection.ParameterValueRegistered;
import app.secuboid.api.storage.tables.RowWithId;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;

/**
 * Represents a player with a uuid.
 */
@ParameterValueRegistered(name = "player", shortName = "p", chatColor = "\u00A76", priority = 80)
public class ParameterValuePlayer implements ParameterValue {

    private static final String NAME = ParameterValuePlayer.class.getAnnotation(ParameterValueRegistered.class).name();
    private static final String SHORT_NAME = ParameterValuePlayer.class.getAnnotation(ParameterValueRegistered.class)
            .shortName();
    private static final String CHAT_COLOR = ParameterValuePlayer.class.getAnnotation(ParameterValueRegistered.class)
            .chatColor();
    private static final int PRIORITY = ParameterValuePlayer.class.getAnnotation(ParameterValueRegistered.class)
            .priority();

    private final UUID uuid;

    private long id;

    public ParameterValuePlayer(@NotNull UUID uuid) {
        this.uuid = uuid;
        id = RowWithId.ID_NON_CREATED_VALUE;
    }

    // Needed for load from database
    public static ParameterValuePlayer newInstance(@NotNull String value) throws ParameterValueException {
        UUID uuid;

        try {
            uuid = UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            String msg = format(
                    "Not a player UUID: Wrong name in the database or Bukkit API is changed? [uuid=%s]",
                    value);
            throw new ParameterValueException(msg, e);
        }

        return new ParameterValuePlayer(uuid);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the player Minecraft unique id.
     *
     * @return the player Minecraft unique id
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Gets the player name from Bukkit server. If the player doesn't has played
     * before, it will return the uuid.
     *
     * @return the player name or the uuid
     */
    public String getPlayerName() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        if (offlinePlayer == null || !offlinePlayer.hasPlayedBefore()) {
            return uuid.toString();
        }

        return offlinePlayer.getName();
    }

    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public @NotNull String getShortName() {
        return SHORT_NAME;
    }

    @Override
    public @NotNull String getChatColor() {
        return CHAT_COLOR;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public @NotNull String getValue() {
        return uuid.toString();
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity) {
        if (entity instanceof Player player) {
            return player.getUniqueId().equals(uuid);
        }

        return false;
    }

    @Override
    public boolean hasAccess(@NotNull Entity entity, @NotNull Land originLand) {
        return hasAccess(entity);
    }

    @Override
    public String toString() {
        return "{" +
                " uuid='" + uuid + "'" +
                ", id='" + getId() + "'" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ParameterValuePlayer parameterValuePlayer)) {
            return false;
        }

        return Objects.equals(uuid, parameterValuePlayer.uuid) && id == parameterValuePlayer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, id);
    }
}
