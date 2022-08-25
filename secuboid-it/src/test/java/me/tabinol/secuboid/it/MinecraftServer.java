package me.tabinol.secuboid.it;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.tabinol.secuboid.api.SecuboidCoponent;
import me.tabinol.secuboid.api.exceptions.SecuboidRuntimeException;
import me.tabinol.secuboid.core.SecuboidCorePluginImpl;
import me.tabinol.secuboid.core.SecuboidImpl;
import me.tabinol.secuboid.core.messages.Log;
import me.tabinol.secuboid.permission.group.SecuboidPermissionGroup;
import me.tabinol.secuboid.permission.group.SecuboidPermissionGroupPlugin;
import net.milkbowl.vault.permission.Permission;

public class MinecraftServer {

    private static final String PLUGIN_VERSION = "IT_VERSION";

    private final File pluginTempDir;

    private final List<SecuboidCoponent> secuboidComponents;
    private final List<JavaPlugin> plugins;

    public MinecraftServer(File pluginTempDir, List<Class<? extends SecuboidCoponent>> secuboidComponentImplClasses) {
        this.pluginTempDir = pluginTempDir;
        secuboidComponents = new ArrayList<>();
        plugins = new ArrayList<>();

        for (Class<? extends SecuboidCoponent> implClass : secuboidComponentImplClasses) {

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
        for (SecuboidCoponent secuboidCoponent : secuboidComponents) {
            secuboidCoponent.load(true);
        }
    }

    public void unload() {
        List<SecuboidCoponent> secuboidComponentsCopy = secuboidComponents.subList(0, secuboidComponents.size());
        Collections.reverse(secuboidComponentsCopy);
        for (SecuboidCoponent secuboidCoponent : secuboidComponentsCopy) {
            secuboidCoponent.unload();
        }
    }

    @SuppressWarnings("unchecked")
    public <C> C getSecuboidComponent(Class<C> secuboidComponentImplClasses) {
        for (SecuboidCoponent secuboidCoponent : secuboidComponents) {
            if (secuboidComponentImplClasses.isAssignableFrom(secuboidCoponent.getClass())) {
                return (C) secuboidCoponent;
            }
        }

        throw new SecuboidRuntimeException("Invalid class");
    }

    private SecuboidCorePluginImpl mockSecuboidCorePluginImpl() {
        String pluginName = "SecuboidCore";
        String mainClass = "me.tabinol.secuboid.core.SecuboidCorePluginImpl";
        File secuboidPluginYmlFile = new File("../secuboid-core/src/main/resources-filtered/secuboid-plugin.yml");

        return pluginCommunMock(SecuboidCorePluginImpl.class, pluginName, mainClass, secuboidPluginYmlFile);
    }

    private SecuboidPermissionGroupPlugin mockPermissionGroupPlugin() {
        String pluginName = "SecuboidPermissionGroup";
        String mainClass = "me.tabinol.secuboid.permission.group.SecuboidPermissionGroupPlugin";
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
