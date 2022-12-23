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
package app.secuboid.core.messages;

import app.secuboid.api.flagtypes.FlagType;
import app.secuboid.api.lands.LocationPath;
import app.secuboid.api.messages.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;

import static app.secuboid.core.config.Config.config;
import static java.lang.String.format;
import static java.util.logging.Level.WARNING;

public class MessageManagerImpl implements MessageManager {

    protected static final String FILENAME_LANG_PREFIX = "lang/";
    protected static final String FILENAME_LANG_FLAGS_PREFIX = "lang/flags-";
    protected static final String FILENAME_LANG_SUFFIX = ".yml";
    protected static final String LANG_DEFAULT = "en";

    private FileConfiguration fileConfiguration;
    private FileConfiguration fileConfigurationFlags;
    private Plugin plugin;
    private String pluginName;

    public MessageManagerImpl() {
        pluginName = null;
        load(null);
    }

    @Override
    public void load(@Nullable Plugin plugin) {
        this.plugin = plugin;

        if (plugin != null) {
            pluginName = plugin.getName();
        }

        String lang = config().lang();

        String langFilename = getFilePath(FILENAME_LANG_PREFIX, lang);
        String defaultLangFilename = getFilePath(FILENAME_LANG_PREFIX, LANG_DEFAULT);
        fileConfiguration = getFileConfiguration(lang, langFilename, defaultLangFilename);

        String langFilenameFlags = getFilePath(FILENAME_LANG_FLAGS_PREFIX, lang);
        String defaultLangFilenameFlags = getFilePath(FILENAME_LANG_FLAGS_PREFIX, LANG_DEFAULT
        );
        fileConfigurationFlags = getFileConfiguration(lang, langFilenameFlags, defaultLangFilenameFlags);
    }

    @Override
    public @NotNull String get(@NotNull MessageType messageType, @NotNull MessagePath path) {
        String yamlPath = path.yamlPath();
        String format = fileConfiguration.getString(yamlPath);

        if (format == null) {
            String message = format("Message path not found path=%s", path);
            Log.log().log(WARNING, () -> format("[Plugin:%s] %s", pluginName, message));
            return message;
        }

        String[] tags = path.replacedTags();
        Object[] args = path.args();
        return formatMessage(messageType, format, tags, args);
    }

    @Override
    public void sendMessage(@NotNull CommandSender sender, @NotNull MessageType messageType, @NotNull MessagePath path) {
        String message = get(messageType, path);
        sender.sendMessage(message);
    }

    @Override
    public void broadcastMessage(@NotNull MessageType messageType, @NotNull MessagePath path) {
        String message = get(messageType, path);
        plugin.getServer().broadcastMessage(message);
    }

    @Override
    public @NotNull String getFlagDescription(@NotNull FlagType flagType) {
        String name = flagType.name();
        String descrition = fileConfigurationFlags.getString(name);
        if (descrition == null) {
            String message = format("Flag description not found: %s", name);
            Log.log().log(WARNING, () -> format("[Plugin:%s] %s", pluginName, message));
            return message;
        }

        return descrition;
    }

    @Override
    public void sendFlagDescription(@NotNull CommandSender sender, @NotNull FlagType flagType) {
        String description = getFlagDescription(flagType);
        sender.sendMessage(description);
    }

    private String getFilePath(String prefix, String middle) {
        return prefix + middle + FILENAME_LANG_SUFFIX;
    }

    private FileConfiguration getFileConfiguration(String lang, String langFilename, String defaultLangFilename) {
        InputStream inputStream = getInputStream(plugin, langFilename);

        if (inputStream == null) {
            Log.log().log(WARNING,
                    () -> format("[Plugin:%s] Switch to default because this language is not found: %s:%s", pluginName,
                            lang, langFilename));
            inputStream = getInputStream(plugin, defaultLangFilename);
        }

        if (inputStream == null) {
            Log.log().log(Level.SEVERE, () -> format("[Plugin:%s] No language file found", pluginName));
            return new YamlConfiguration();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        return YamlConfiguration.loadConfiguration(inputStreamReader);
    }

    private InputStream getInputStream(Plugin plugin, String langFilename) {

        // Try first in class loader (for tests)
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(langFilename);
        if (inputStream == null && plugin != null) {

            // Try inside the jar file (normal use)
            inputStream = plugin.getResource(langFilename);
        }

        return inputStream;
    }

    private String formatMessage(MessageType messageType, String format, String[] tags, Object[] args) {
        String prefix = messageType.prefix;
        String color = messageType.color;
        int argsLength = args.length;

        String[] coloredArgs = new String[argsLength];
        for (int i = 0; i < argsLength; i++) {
            coloredArgs[i] = colorArg(messageType, color, args[i]);
        }

        return prefix + color + formatRaw(format, tags, coloredArgs);
    }

    private String formatRaw(String format, String[] tags, String[] args) {
        String output = format;
        for (int i = 0; i < tags.length; i++) {
            String tag = tags[i];
            String arg = args[i];
            output = output.replace(tag, arg);
        }

        return output;
    }

    private String colorArg(MessageType messageType, String color, Object arg) {
        if (messageType == MessageType.NO_COLOR) {
            return Objects.toString(arg);
        }

        if (arg instanceof MessageFormatter formatter) {
            return formatter + color;
        }

        if (arg instanceof Number number) {
            return (MessageColor.NUMBER + number + color)
                    .replaceAll("([.,])", color + "$1" + MessageColor.NUMBER);
        }

        if (arg instanceof LocationPath locationPath) {
            String pathName = locationPath.getPathName();
            return (MessageColor.NAME + pathName + color)
                    .replaceAll("([@/:])", color + "$1" + MessageColor.NAME);
        }

        return MessageColor.NAME + arg + color;
    }
}
