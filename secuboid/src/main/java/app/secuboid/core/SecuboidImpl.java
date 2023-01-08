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

import app.secuboid.api.NewInstance;
import app.secuboid.api.Secuboid;
import app.secuboid.api.SecuboidComponent;
import app.secuboid.api.SecuboidPlugin;
import app.secuboid.api.commands.Commands;
import app.secuboid.api.flagtypes.FlagTypes;
import app.secuboid.api.lands.Lands;
import app.secuboid.api.parameters.values.ParameterValues;
import app.secuboid.api.players.PlayerInfos;
import app.secuboid.api.storage.StorageManager;
import app.secuboid.core.commands.CommandListener;
import app.secuboid.core.commands.CommandsImpl;
import app.secuboid.core.commands.items.SecuboidTool;
import app.secuboid.core.flagtypes.FlagTypesImpl;
import app.secuboid.core.lands.LandsImpl;
import app.secuboid.core.listeners.Listeners;
import app.secuboid.core.messages.ChatGetter;
import app.secuboid.core.messages.Log;
import app.secuboid.core.messages.Message;
import app.secuboid.core.parameters.values.ParameterValuesImpl;
import app.secuboid.core.players.PlayerInfosImpl;
import app.secuboid.core.reflection.PluginLoader;
import app.secuboid.core.storage.ConnectionManager;
import app.secuboid.core.storage.StorageManagerImpl;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static app.secuboid.core.config.Config.config;

public class SecuboidImpl implements Secuboid, SecuboidComponent {

    private static @NotNull SecuboidImpl secuboidImpl;
    private static @NotNull SecuboidPlugin secuboidPlugin;

    private final @NotNull NewInstance newInstance;
    private final @NotNull Commands commands;
    private final @NotNull CommandListener commandListener;
    private final @NotNull ParameterValues parameterValues;
    private final @NotNull Lands lands;
    private final @NotNull FlagTypes flags;
    private final @NotNull PlayerInfosImpl playerInfos;
    private final @NotNull SecuboidTool secuboidTool;
    private final @NotNull Listeners listeners;
    private final @NotNull StorageManager storageManager;

    private final @NotNull ChatGetter chatGetter;

    @SuppressWarnings("java:S3010")
    public SecuboidImpl(@NotNull SecuboidPluginImpl secuboidPluginImpl) {
        newInstance = new NewInstanceImpl();
        secuboidImpl = this;
        secuboidPlugin = secuboidPluginImpl;

        flags = new FlagTypesImpl();
        playerInfos = new PlayerInfosImpl();
        commands = new CommandsImpl();
        secuboidTool = new SecuboidTool();
        listeners = new Listeners();
        commandListener = new CommandListener((CommandsImpl) commands, playerInfos);
        storageManager = new StorageManagerImpl(secuboidPlugin);
        chatGetter = new ChatGetter();

        parameterValues = new ParameterValuesImpl();
        lands = new LandsImpl();
    }

    public static @NotNull SecuboidImpl instance() {
        return secuboidImpl;
    }

    public static @NotNull JavaPlugin getJavaPLugin() {
        return secuboidPlugin;
    }

    public static @NotNull PluginManager getPluginManager() {
        return secuboidPlugin.getServer().getPluginManager();
    }

    public static @Nullable ScoreboardManager getScoreboardManager() {
        return secuboidPlugin.getServer().getScoreboardManager();
    }

    public static <T extends JavaPlugin> @Nullable T getPlugin(Class<T> clazz) {
        return ((SecuboidPluginImpl) secuboidPlugin).getPluginFromClass(clazz);
    }

    @Override
    public void load(boolean isServerBoot) {

        if (isServerBoot) {

            // Set logger first
            Log.setLog(secuboidPlugin.getLogger());
            secuboidPlugin.saveDefaultConfig();
        } else {
            secuboidPlugin.reloadConfig();
        }
        config().load(secuboidPlugin.getConfig());

        if (isServerBoot) {

            // Everything here is what will not change after a configuration or persistence
            // change
            Message.message().load(secuboidPlugin);
            secuboidTool.init(secuboidPlugin);
            PluginLoader pluginLoader = new PluginLoader();
            PluginManager pluginManager = secuboidPlugin.getServer().getPluginManager();
            pluginLoader.init(pluginManager);
            ((FlagTypesImpl) flags).init(pluginLoader);
            ((CommandsImpl) commands).init(pluginLoader);
            ((ParameterValuesImpl) parameterValues).init(pluginLoader);
            commandListener.init();
            ((StorageManagerImpl) storageManager).init(pluginLoader);
        } else {
            ConnectionManager.init(); // Initiated by storage manager on a normal boot
            chatGetter.reset();
        }
        playerInfos.addConsoleCommandSender(secuboidPlugin.getServer().getConsoleSender());

        // Load
        ((StorageManagerImpl) storageManager).start();
        ((ParameterValuesImpl) parameterValues).load();
        ((LandsImpl) lands).load();

        if (isServerBoot) {
            listeners.register();
        }

        // Reload players, not only on "sd reload" because there is also the bukkit
        // "reload" command.
        for (Player player : secuboidPlugin.getServer().getOnlinePlayers()) {
            UUID playerUUID = player.getUniqueId();
            String playerName = player.getName();

            // Reload players
        }
    }

    @Override
    public void unload() {
        playerInfos.removeAll();
        // TODO refactor

        // Only when Secuboid is completely unloaded
        ((StorageManagerImpl) storageManager).stop();
        ConnectionManager.shutdown();
    }

    @Override
    public void reload() {
        unload();
        load(false);
    }

    @Override
    public @NotNull PlayerInfos getPlayerInfos() {
        return playerInfos;
    }

    @Override
    public @NotNull Commands getCommands() {
        return commands;
    }

    @Override
    public @NotNull FlagTypes getFlagTypes() {
        return flags;
    }

    @Override
    public @NotNull ParameterValues getParameterValues() {
        return parameterValues;
    }

    @Override
    public @NotNull Lands getLands() {
        return lands;
    }

    @Override
    public @NotNull StorageManager getStorageManager() {
        return storageManager;
    }

    @Override
    public @NotNull NewInstance getNewInstance() {
        return newInstance;
    }

    public @NotNull SecuboidTool getSecuboidTool() {
        return secuboidTool;
    }

    public @NotNull ChatGetter getChatGetter() {
        return chatGetter;
    }
}
