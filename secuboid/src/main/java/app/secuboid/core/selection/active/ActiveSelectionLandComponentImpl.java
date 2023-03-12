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

package app.secuboid.core.selection.active;

import app.secuboid.api.lands.LandComponent;
import app.secuboid.api.selection.active.ActiveSelectionLandComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class ActiveSelectionLandComponentImpl extends ActiveSelectionImpl implements ActiveSelectionLandComponent {

    protected final @NotNull LandComponent landComponent;

    protected ActiveSelectionLandComponentImpl(@NotNull CommandSender commandSender,
                                               @NotNull LandComponent landComponent) {
        super(commandSender);
        this.landComponent = landComponent;
    }

    @Override
    public @NotNull LandComponent getLandComponent() {
        return landComponent;
    }
}
