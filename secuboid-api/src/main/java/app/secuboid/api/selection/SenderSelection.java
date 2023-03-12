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

package app.secuboid.api.selection;

import app.secuboid.api.selection.active.ActiveSelection;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a sender selection.
 */
public interface SenderSelection {

    /**
     * Gets the active selection. The class {@link app.secuboid.api.selection.active.ActiveSelectionNothing} means no
     * selection.
     *
     * @return the active selection.
     */
    @NotNull ActiveSelection getActiveSelection();

    /**
     * Checks if a selection is active.
     *
     * @return true if a selection is active.
     */
    boolean hasSelection();

    /**
     * Removes any selection.
     *
     * @return true if a selection was active.
     */
    boolean removeSelection();
}
