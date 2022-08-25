package me.tabinol.secuboid.permission.group;

import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;

public class SecuboidPermissionGroupPlugin extends JavaPlugin {

    private static Permission permission;

    private SecuboidPermissionGroup secuboidPermissionGroup;

    @SuppressWarnings("java:S3010")
    public SecuboidPermissionGroupPlugin() {
        secuboidPermissionGroup = new SecuboidPermissionGroup(this);
        permission = secuboidPermissionGroup.getPermission();
    }

    @Override
    public void onEnable() {
        secuboidPermissionGroup.load(true);
    }

    public static Permission getPermission() {
        return permission;
    }
}
