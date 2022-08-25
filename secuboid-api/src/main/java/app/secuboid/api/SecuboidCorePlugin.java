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

import org.bukkit.plugin.java.JavaPlugin;

/**
 * This abstract class is used only when we need to access Secuboid main class
 * from java plugin.
 */
public abstract class SecuboidCorePlugin extends JavaPlugin {

    protected static Secuboid secuboid;

    /**
     * Gets the Secuboid main instance from static way.
     * 
     * @return the Secuboid main instance
     */
    public static Secuboid secuboid() {
        return secuboid;
    }

    /**
     * Gets the Secuboid main instance.
     * 
     * @return the Secuboid main instance
     */
    public abstract Secuboid getSecuboid();
}
