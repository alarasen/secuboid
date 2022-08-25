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
package me.tabinol.secuboid.core.selection;

import me.tabinol.secuboid.api.lands.Land;
import me.tabinol.secuboid.api.lands.areas.Area;
import me.tabinol.secuboid.core.selection.active.ActiveSelection;
import org.jetbrains.annotations.Nullable;

public class SenderSelection {

    protected ActiveSelection activeSelection;

    public SenderSelection() {
        activeSelection = null;
    }

    @Nullable
    public final ActiveSelection getVisualSelection() {
        return activeSelection;
    }

    public final boolean hasSelection() {
        return activeSelection != null;
    }

    @Nullable
    public final Area getOriginArea() {
        return activeSelection != null ? activeSelection.getOriginArea() : null;
    }

    @Nullable
    public final Land getLand() {
        if (activeSelection != null) {
            Area originArea = activeSelection.getOriginArea();
            if (originArea != null) {
                return originArea.getLand();
            }
        }

        return null;
    }

    public boolean removeSelection() {
        if (activeSelection != null) {
            activeSelection = null;
            return true;
        }

        return false;
    }
}
