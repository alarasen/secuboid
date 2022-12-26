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
package app.secuboid.api.commands;

import app.secuboid.api.players.CommandSenderInfo;
import org.jetbrains.annotations.NotNull;

/**
 * Class where the commands to be executed can be found.
 */
public interface Commands {

    /**
     * Executes the command from it class. The args array should not contain the
     * command call, just the arguments.
     *
     * @param clazz             the command exec class
     * @param commandSenderInfo the command sender Secuboid info (console or player)
     * @param subArgs           the args without the command call
     */
    void executeCommandClass(@NotNull Class<? extends CommandExec> clazz, @NotNull CommandSenderInfo commandSenderInfo, @NotNull String[] subArgs);
}
