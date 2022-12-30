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

/**
 * Represents a chat page for a command output.
 */
public interface ChatPage {

    /**
     * Show a page to the player or the console sender. If the page does not exist, an error message is sent to the
     * user.
     *
     * @param pageNumber the page number to show or an error message
     */
    void show(int pageNumber);

    /**
     * Gets the total number of pages for the last command. this method should be called after a first use of
     * {@link #show(int)} method, or it will return 0.
     *
     * @return the total number of pages
     */
    int getTotalPages();
}
