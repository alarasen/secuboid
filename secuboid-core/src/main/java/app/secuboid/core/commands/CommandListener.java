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
package app.secuboid.core.commands;

import app.secuboid.api.players.CommandSenderInfo;
import app.secuboid.api.players.PlayerInfos;
import app.secuboid.core.SecuboidImpl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandListener implements CommandExecutor {

    private static final String COMMAND_NAME = "secuboid";

    private final CommandsImpl commands;
    private final PlayerInfos playerInfos;

    public CommandListener(CommandsImpl commands, PlayerInfos playerInfos) {
        this.commands = commands;
        this.playerInfos = playerInfos;
    }

    public void init() {
        JavaPlugin javaPlugin = SecuboidImpl.getJavaPLugin();
        PluginCommand pluginCommand = javaPlugin.getCommand(COMMAND_NAME);
        pluginCommand.setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSenderInfo commandSenderInfo = playerInfos.get(sender);
        commands.executeCommandName(commandSenderInfo, args);

        return true;
    }

}