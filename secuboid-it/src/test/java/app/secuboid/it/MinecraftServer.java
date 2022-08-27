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

package app.secuboid.it;

import app.secuboid.api.SecuboidComponent;
import app.secuboid.api.exceptions.SecuboidRuntimeException;
import app.secuboid.core.SecuboidCorePluginImpl;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.messages.Log;
import app.secuboid.permission.group.SecuboidPermissionGroup;
import app.secuboid.permission.group.SecuboidPermissionGroupPlugin;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MinecraftServer {

    private static final String PLUGIN_VERSION = "IT_VERSION";

    private final File pluginTempDir;

    private final List<SecuboidComponent> secuboidComponents;
    private final List<JavaPlugin> plugins;

    public MinecraftServer(File pluginTempDir, List<Class<? extends SecuboidComponent>> secuboidComponentImplClasses) {
        this.pluginTempDir = pluginTempDir;
        secuboidComponents = new ArrayList<>();
        plugins = new ArrayList<>();

        for (Class<? extends SecuboidComponent> implClass : secuboidComponentImplClasses) {

            if (implClass.isAssignableFrom(SecuboidImpl.class)) {
                SecuboidCorePluginImpl secuboidCorePlugin = mockSecuboidCorePluginImpl();
                SecuboidImpl secuboid = new SecuboidImpl(secuboidCorePlugin);

                secuboidComponents.add(secuboid);
                plugins.add(secuboidCorePlugin);
            } else if (implClass.isAssignableFrom(SecuboidPermissionGroup.class)) {
                SecuboidPermissionGroupPlugin secuboidPermissionGroupPlugin = mockPermissionGroupPlugin();
                SecuboidPermissionGroup secuboidPermissionGroup = new SecuboidPermissionGroup(
                        secuboidPermissionGroupPlugin);

                secuboidComponents.add(secuboidPermissionGroup);
                plugins.add(secuboidPermissionGroupPlugin);
            }
        }
    }

    public void load() {
        for (SecuboidComponent secuboidComponent : secuboidComponents) {
            secuboidComponent.load(true);
        }
    }

    public void unload() {
        List<SecuboidComponent> secuboidComponentsCopy = secuboidComponents.subList(0, secuboidComponents.size());
        Collections.reverse(secuboidComponentsCopy);
        for (SecuboidComponent secuboidComponent : secuboidComponentsCopy) {
            secuboidComponent.unload();
        }
    }

    @SuppressWarnings("unchecked")
    public <C> C getSecuboidComponent(Class<C> secuboidComponentImplClasses) {
        for (SecuboidComponent secuboidComponent : secuboidComponents) {
            if (secuboidComponentImplClasses.isAssignableFrom(secuboidComponent.getClass())) {
                return (C) secuboidComponent;
            }
        }

        throw new SecuboidRuntimeException("Invalid class");
    }

    private SecuboidCorePluginImpl mockSecuboidCorePluginImpl() {
        String pluginName = "SecuboidCore";
        String mainClass = "app.secuboid.core.SecuboidCorePluginImpl";
        File secuboidPluginYmlFile = new File("../secuboid-core/src/main/resources-filtered/secuboid-plugin.yml");

        return pluginCommunMock(SecuboidCorePluginImpl.class, pluginName, mainClass, secuboidPluginYmlFile);
    }

    private SecuboidPermissionGroupPlugin mockPermissionGroupPlugin() {
        String pluginName = "SecuboidPermissionGroup";
        String mainClass = "app.secuboid.permission.group.SecuboidPermissionGroupPlugin";
        File secuboidPluginYmlFile = new File(
                "../secuboid-permission-group/src/main/resources-filtered/secuboid-plugin.yml");

        return pluginCommunMock(SecuboidPermissionGroupPlugin.class, pluginName, mainClass, secuboidPluginYmlFile);
    }

    public <P extends JavaPlugin> P pluginCommunMock(Class<P> clazz, String pluginName, String mainClass,
                                                     File secuboidPluginYmlFile) {
        P javaPlugin = mock(clazz);

        when(javaPlugin.getLogger()).thenReturn(Log.log());
        when(javaPlugin.getConfig()).thenReturn(new YamlConfiguration());
        when(javaPlugin.getDescription()).thenReturn(new PluginDescriptionFile(pluginName, PLUGIN_VERSION, mainClass));
        when(javaPlugin.getDataFolder()).thenReturn(new File(pluginTempDir, pluginName));
        when(javaPlugin.getName()).thenReturn(pluginName);

        Server server = mockServer();
        when(javaPlugin.getServer()).thenReturn(server);

        try {
            when(javaPlugin.getResource("secuboid-plugin.yml")).thenReturn(new FileInputStream(secuboidPluginYmlFile));
        } catch (FileNotFoundException e) {
            throw new SecuboidRuntimeException("Unable to read the file: " + secuboidPluginYmlFile, e);
        }

        PluginCommand pluginCommand = mock(PluginCommand.class);
        when(javaPlugin.getCommand(anyString())).thenReturn(pluginCommand);

        return javaPlugin;
    }

    private Server mockServer() {
        Server server = mock(Server.class);
        PluginManager pluginManager = mockPluginManager();

        when(server.getPluginManager()).thenReturn(pluginManager);

        ServicesManager servicesManager = mock(ServicesManager.class);
        when(server.getServicesManager()).thenReturn(servicesManager);

        // Vault
        @SuppressWarnings("unchecked")
        RegisteredServiceProvider<Permission> rsp = mock(RegisteredServiceProvider.class);
        Permission permission = mock(Permission.class);
        when(rsp.getProvider()).thenReturn(permission);
        when(servicesManager.getRegistration(Permission.class)).thenReturn(rsp);

        return server;
    }

    private PluginManager mockPluginManager() {
        PluginManager pluginManager = mock(PluginManager.class);

        when(pluginManager.getPlugins()).thenAnswer(a -> plugins.toArray(new JavaPlugin[0]));

        return pluginManager;
    }
}
