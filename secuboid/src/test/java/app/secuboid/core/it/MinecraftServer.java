/*
 *  Secuboid: LandService and Protection plugin for Minecraft server
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

package app.secuboid.core.it;

public class MinecraftServer {

//    private static final String PLUGIN_VERSION = "IT_VERSION";
//
//    private final File pluginTempDir;
//
//    private final List<SecuboidComponent> secuboidComponents;
//    private final List<JavaPlugin> plugins;
//
//    public MinecraftServer(File pluginTempDir) {
//        this.pluginTempDir = pluginTempDir;
//        secuboidComponents = new ArrayList<>();
//        plugins = new ArrayList<>();
//
//        SecuboidPluginImpl secuboidPlugin = mockSecuboidPluginImpl();
//        SecuboidImpl secuboid = new SecuboidImpl(secuboidPlugin);
//
//        secuboidComponents.add(secuboid);
//        plugins.add(secuboidPlugin);
//    }
//
//    public void load() {
//        for (SecuboidComponent secuboidComponent : secuboidComponents) {
//            secuboidComponent.load(true);
//        }
//    }
//
//    public void unload() {
//        List<SecuboidComponent> secuboidComponentsCopy = secuboidComponents.subList(0, secuboidComponents.size());
//        Collections.reverse(secuboidComponentsCopy);
//        for (SecuboidComponent secuboidComponent : secuboidComponentsCopy) {
//            secuboidComponent.unload();
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    public <C> C getSecuboidComponent(Class<C> secuboidComponentImplClasses) {
//        for (SecuboidComponent secuboidComponent : secuboidComponents) {
//            if (secuboidComponentImplClasses.isAssignableFrom(secuboidComponent.getClass())) {
//                return (C) secuboidComponent;
//            }
//        }
//
//        throw new SecuboidRuntimeException("Invalid class");
//    }
//
//    private SecuboidPluginImpl mockSecuboidPluginImpl() {
//        String pluginName = "Secuboid";
//        String mainClass = "app.secuboid.core.SecuboidPluginImpl";
//        File secuboidPluginYmlFile = new File("../secuboid/src/main/resources-filtered/secuboid-plugin.yml");
//
//        return pluginCommonMock(SecuboidPluginImpl.class, pluginName, mainClass, secuboidPluginYmlFile);
//    }
//
//    public <P extends JavaPlugin> P pluginCommonMock(Class<P> clazz, String pluginName, String mainClass,
//                                                     File secuboidPluginYmlFile) {
//        P javaPlugin = mock(clazz);
//
//        when(javaPlugin.getLogger()).thenReturn(Log.log());
//        when(javaPlugin.getConfig()).thenReturn(new YamlConfiguration());
//        when(javaPlugin.getDescription()).thenReturn(new PluginDescriptionFile(pluginName, PLUGIN_VERSION, mainClass));
//        when(javaPlugin.getDataFolder()).thenReturn(new File(pluginTempDir, pluginName));
//        when(javaPlugin.getName()).thenReturn(pluginName);
//
//        Server server = mockServer();
//        when(javaPlugin.getServer()).thenReturn(server);
//
//        try {
//            when(javaPlugin.getResource("secuboid-plugin.yml")).thenReturn(new FileInputStream(secuboidPluginYmlFile));
//        } catch (FileNotFoundException e) {
//            throw new SecuboidRuntimeException("Unable to read the file: " + secuboidPluginYmlFile, e);
//        }
//
//        PluginCommand pluginCommand = mock(PluginCommand.class);
//        when(javaPlugin.getCommand(anyString())).thenReturn(pluginCommand);
//
//        return javaPlugin;
//    }
//
//    private Server mockServer() {
//        Server server = mock(Server.class);
//        PluginManager pluginManager = mockPluginManager();
//
//        when(server.getPluginManager()).thenReturn(pluginManager);
//
//        ServicesManager servicesManager = mock(ServicesManager.class);
//        when(server.getServicesManager()).thenReturn(servicesManager);
//
//        return server;
//    }
//
//    private PluginManager mockPluginManager() {
//        PluginManager pluginManager = mock(PluginManager.class);
//
//        when(pluginManager.getPlugins()).thenAnswer(a -> plugins.toArray(new JavaPlugin[0]));
//
//        return pluginManager;
//    }
}
