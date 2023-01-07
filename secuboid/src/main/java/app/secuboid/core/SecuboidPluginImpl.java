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
package app.secuboid.core;

import app.secuboid.api.Secuboid;
import app.secuboid.api.SecuboidPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SecuboidPluginImpl extends SecuboidPlugin {

    private final @NotNull Secuboid secuboid;

    public SecuboidPluginImpl() {
        super();

        secuboid = new SecuboidImpl(this);
        setSecuboid(secuboid);
    }

    @Override
    public void onEnable() {
        ((SecuboidImpl) secuboid).load(true);
        // TODO refactor
        // new SecuboidMetrics(this).start();
    }

    @Override
    public void onDisable() {
        ((SecuboidImpl) secuboid).unload();
    }

    @Override
    public @NotNull Secuboid getSecuboid() {
        return secuboid;
    }

    public <T extends JavaPlugin> T getPluginFromClass(Class<T> clazz) {
        return JavaPlugin.getPlugin(clazz);
    }
}
