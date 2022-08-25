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

import app.secuboid.api.parameters.values.ParameterValuePlayer;
import app.secuboid.api.players.ConsoleCommandSenderInfo;
import app.secuboid.core.selection.SenderSelection;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleCommandSenderInfoImpl extends CommandSenderInfoImpl implements ConsoleCommandSenderInfo {

    private final SenderSelection selection;

    public ConsoleCommandSenderInfoImpl(ConsoleCommandSender consoleCommandSender) {
        super(consoleCommandSender);
        selection = new SenderSelection();
    }

    @Override
    public ParameterValuePlayer getParameterValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAdminMode() {
        return true;
    }

    // Not in api
    @Override
    public SenderSelection getSelection() {
        return selection;
    }
}
