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
package app.secuboid.api.messages;

import java.lang.reflect.InvocationTargetException;

import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.api.flagtypes.FlagType;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Message manager for language files inside the plugin jar. At least,
 * lang/en.yml should be present.
 */
public interface MessageManager {

    /**
     * Creates a new instance of MessageManager.
     * 
     * @return the new instance
     */
    public static MessageManager newMessageManager() {
        try {
            return (MessageManager) Class
                    .forName("app.secuboid.core.messages.MessageManagerImpl")
                    .getConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
            throw new SecuboidRuntimeException("Unable to get the instance of MessageManagerImpl", e);
        }
    }

    /**
     * Loads the messages from the plugin. This is needed to be executed once before
     * the get method.
     * 
     * @param plugin the plugin the load the messages from
     */
    public void load(Plugin plugin);

    /**
     * Gets the message from a yaml path.
     * 
     * @param messageType the message type
     * @param path        the yaml path with arguments
     * @return the message
     */
    String get(MessageType messageType, MessagePath path);

    /**
     * Sends a messsage to this sender.
     * 
     * @param sender      the command sender (player or console)
     * @param messageType the message type
     * @param path        the yaml path with arguments
     */
    void sendMessage(CommandSender sender, MessageType messageType, MessagePath path);

    /**
     * Broadcasts a message to all connected players.
     * 
     * @param messageType the message type
     * @param path        the yaml path with arguments
     */
    void broadcastMessage(MessageType messageType, MessagePath path);

    /**
     * Gets the flag description.
     * 
     * @param flagType the flag type
     * @return the flag description
     */
    String getFlagDescription(FlagType flagType);

    /**
     * Sends a the flag description to this sender.
     * 
     * @param sender   the command sender (player or console)
     * @param flagType the flag type
     */
    void sendFlagDescription(CommandSender sender, FlagType flagType);
}
