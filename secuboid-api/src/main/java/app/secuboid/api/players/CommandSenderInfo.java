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
package app.secuboid.api.players;

import app.secuboid.api.selection.SenderSelection;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains informations for a command sender.
 */
public interface CommandSenderInfo {

    /**
     * Gets the command sender for this player.
     *
     * @return the command sender
     */
    @NotNull CommandSender sender();

    /**
     * Gets the player name.
     *
     * @return the player name
     */
    @NotNull String getName();

    /**
     * Is the player admin mode?
     *
     * @return true if the player is admin mode
     */
    boolean isAdminMode();

    /**
     * Creates a new chat page for a multiple pages output.
     *
     * @param subject the subject in the header
     * @param text    the text
     */
    @NotNull ChatPage newChatPage(@NotNull String subject, @NotNull String text);

    /**
     * Gets the last chat page for a multiple pages output.
     *
     * @return the chat page
     */
    @Nullable ChatPage getChatPage();

    /**
     * Removes the chat page from the memory.
     */
    void removeChatPage();


    /**
     * Gets the sender selection.
     *
     * @return the sender selection
     */
    public @NotNull SenderSelection getSelection();
}
