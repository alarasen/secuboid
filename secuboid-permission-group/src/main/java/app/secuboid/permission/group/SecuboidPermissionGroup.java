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
package app.secuboid.permission.group;

import app.secuboid.api.SecuboidCoponent;
import app.secuboid.api.exceptions.SecuboidRuntimeException;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SecuboidPermissionGroup implements SecuboidCoponent {

    private final SecuboidPermissionGroupPlugin secuboidPermissionGroupPlugin;

    private Permission permission = null;

    public SecuboidPermissionGroup(SecuboidPermissionGroupPlugin secuboidPermissionGroupPlugin) {
        this.secuboidPermissionGroupPlugin = secuboidPermissionGroupPlugin;
    }

    @Override
    public void load(boolean isServerBoot) {
        if (isServerBoot && !setupPermissions()) {
            throw new SecuboidRuntimeException("No Bukkit permission system installed");
        }
    }

    @Override
    public void unload() {
        // Empty
    }

    @Override
    public void reload() {
        // Empty
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = secuboidPermissionGroupPlugin.getServer().getServicesManager()
                .getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

    public Permission getPermission() {
        return permission;
    }
}
