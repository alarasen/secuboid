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
package app.secuboid.api.lands;

import app.secuboid.api.lands.areas.Area;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Represents a land with a number of areas.
 */
public interface AreaLand extends Land {

    /**
     * Adds the area.
     *
     * @param area the area
     */
    void addArea(Area area);

    /**
     * Removes the area.
     *
     * @param key the key
     * @return true, if successful
     */
    boolean removeArea(int key);

    /**
     * Removes the area.
     *
     * @param area the area
     * @return true, if successful
     */
    boolean removeArea(@NotNull Area area);

    /**
     * Replace area.
     *
     * @param key     the key
     * @param newArea the new area
     * @return true, if successful
     */
    boolean replaceArea(int key, @NotNull Area newArea);

    /**
     * Gets the area.
     *
     * @param key the key
     * @return the area
     */
    @Nullable Area getArea(int key);

    /**
     * Gets the area key.
     *
     * @param area the area
     * @return the area key or null if the area is not member of this land
     */
    @Nullable Integer getAreaKey(@NotNull Area area);

    /**
     * Gets the area keys.
     *
     * @return the area keys
     */
    @NotNull Set<Integer> getAreaKeys();

    /**
     * Gets the ids and areas map.
     *
     * @return the ids and areas map
     */
    @NotNull Map<Integer, Area> getIdToArea();

    /**
     * Gets the areas.
     *
     * @return the areas
     */
    @NotNull Collection<Area> getAreas();

    /**
     * Gets the parent.
     *
     * @return the parent or the land is a world
     */
    @NotNull Land getParent();

    /**
     * Sets the land parent. Set a null and the world become the parent
     *
     * @param newParent the land parent
     */
    void setParent(@Nullable AreaLand newParent);

    /**
     * Checks if the land is parent, grand-parent or ancestor.
     *
     * @param land the land
     * @return true, if is ancestor
     */
    boolean isParentOrAncestor(@NotNull Land land);

    /**
     * Checks if is player in land. No parent verify.
     *
     * @param player the player
     * @return true, if is player in land
     */
    boolean isPlayerInLand(@NotNull Player player);
}
