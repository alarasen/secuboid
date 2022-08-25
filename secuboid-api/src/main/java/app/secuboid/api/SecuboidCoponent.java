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
package app.secuboid.api;

/**
 * Only for secuboid coponent plugin (SecuboidCorePlugin, SecuboidEconomyPlugin,
 * ...)
 */
public interface SecuboidCoponent {

    /**
     * Loads the plugin. An unload must be done before if the server is already
     * running.
     * 
     * @param isServerBoot Is it from server start?
     */
    void load(boolean isServerBoot);

    /**
     * Unloads the plugin.
     */
    void unload();

    /**
     * Reload the configuration.
     */
    void reload();
}
