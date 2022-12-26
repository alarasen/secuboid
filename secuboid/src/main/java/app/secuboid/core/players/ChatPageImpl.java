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

import app.secuboid.api.players.ChatPage;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;
import org.jetbrains.annotations.NotNull;

public class ChatPageImpl implements ChatPage {

    private static final int PAGE_WIDTH = ChatPaginator.AVERAGE_CHAT_PAGE_WIDTH;
    private static final int PAGE_HEIGHT = ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 2;

    private final CommandSender sender;
    private final String header;
    private final String text;

    private int totalPages;

    public ChatPageImpl(@NotNull CommandSender sender, @NotNull String header, @NotNull String text) {
        this.sender = sender;
        this.header = header;
        this.text = text;

        totalPages = 0;
    }

    @Override
    public boolean show(int pageNumber) {
        ChatPaginator.ChatPage page = ChatPaginator.paginate(text, pageNumber, PAGE_WIDTH, PAGE_HEIGHT);
        totalPages = page.getTotalPages();

        if (pageNumber < 1 || pageNumber > totalPages) {
            return false;
        }

        sender.sendMessage(page.getLines());

        return true;
    }

    @Override
    public int getTotalPages() {
        // TODO Implements
        return 0;
    }
}
