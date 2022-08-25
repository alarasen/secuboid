/*
 Secuboid: Lands and Protection plugin for Minecraft server
 Copyright (C) 2014 Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.secuboid.core.commands;

import static me.tabinol.secuboid.core.messages.Log.log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.tabinol.secuboid.api.Secuboid;
import me.tabinol.secuboid.api.commands.CommandExec;
import me.tabinol.secuboid.api.commands.Commands;
import me.tabinol.secuboid.api.players.CommandSenderInfo;
import me.tabinol.secuboid.api.reflection.CommandRegistered;
import me.tabinol.secuboid.core.SecuboidImpl;
import me.tabinol.secuboid.core.reflection.PluginLoader;

public class CommandsImpl implements Commands {

    private final Map<String, CommandContainer> nameToCommand;
    private final Map<Class<? extends CommandExec>, CommandContainer> classToCommand;

    public CommandsImpl() {
        nameToCommand = new HashMap<>();
        classToCommand = new HashMap<>();
    }

    public void init(PluginLoader pluginLoader) {
        Map<Class<? extends CommandExec>, CommandRegistered> classToAnnotation = pluginLoader
                .getClassToAnnotation(CommandRegistered.class, CommandExec.class);

        long commandLevel = 0;
        boolean found;
        do {
            found = false;

            for (Map.Entry<Class<? extends CommandExec>, CommandRegistered> classToAnnotationEntry : classToAnnotation
                    .entrySet()) {
                Class<? extends CommandExec> clazz = classToAnnotationEntry.getKey();
                CommandRegistered commandRegistered = classToAnnotationEntry.getValue();

                long curLevel = commandRegistered.name().chars().filter(c -> c == '/').count();
                if (curLevel == commandLevel) {
                    found = true;
                    try {
                        registerCommand(clazz, commandRegistered);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException | SecurityException e) {
                        log().log(Level.WARNING, e,
                                () -> "CommandExec constructor not visible or contains wrong parameters: " + clazz);
                    }
                }
            }
            commandLevel++;
        } while (found);
    }

    @Override
    public void executeCommandClass(Class<? extends CommandExec> clazz, CommandSenderInfo commandSenderInfo,
            String[] subArgs) {

        CommandContainer commandContainer = classToCommand.get(clazz);
        executeCommand(commandContainer, commandSenderInfo, subArgs);
    }

    void executeCommandName(CommandSenderInfo commandSenderInfo, String[] args) {
        CommandSender sender = commandSenderInfo.getSender();

        if (args == null || args.length == 0) {
            // TODO help with click
            sender.sendMessage("todo");
            return;
        }

        String arg = args[0];
        String argLower = arg.toLowerCase();
        CommandContainer commandContainer = nameToCommand.get(argLower);

        if (commandContainer == null) {
            // TODO help with null
            sender.sendMessage("todo");
            return;
        }

        String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
        executeCommand(commandContainer, commandSenderInfo, subArgs);
    }

    private void executeCommand(CommandContainer commandContainer, CommandSenderInfo commandSenderInfo,
            String[] subArgs) {

        // TODO check permissions

        // TODO subcommands

        CommandExec commandExec = commandContainer.commandExec();
        commandExec.commandExec(commandSenderInfo, subArgs);
    }

    private void registerCommand(Class<? extends CommandExec> clazz, CommandRegistered commandRegistered)
            throws SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Class<? extends JavaPlugin> pluginClass = commandRegistered.pluginClass();
        CommandExec commandExec = newInstance(clazz, pluginClass);

        if (commandExec == null) {
            log().log(Level.WARNING, () -> "No valid constructor for this commmand class: " + clazz);
            return;
        }

        // TODO Retreive flags

        String commandName = commandRegistered.name();
        String[] nameSplit = commandName.split("\\/");
        Map<String, CommandContainer> curNameToCommand = nameToCommand;
        int i = 0;
        while (i < nameSplit.length - 1) {
            CommandContainer curCommandContainer = curNameToCommand.get(nameSplit[0]);
            if (curCommandContainer == null) {
                log().log(Level.WARNING,
                        () -> "There is an error on registered command parent name: " + commandName);
                return;
            }
            curNameToCommand = curCommandContainer.nameToSubCommand();
        }

        String subName = nameSplit[nameSplit.length - 1];
        CommandContainer commandContainer = new CommandContainer(commandExec, commandRegistered, Collections.emptySet(),
                new HashMap<>());
        classToCommand.put(clazz, commandContainer);
        curNameToCommand.put(subName, commandContainer);
        for (String alias : commandRegistered.aliases()) {
            curNameToCommand.put(alias, commandContainer);
        }
    }

    private CommandExec newInstance(Class<? extends CommandExec> clazz, Class<? extends JavaPlugin> pluginClass)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JavaPlugin plugin = getPlugin(pluginClass);
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Secuboid secuboid = getSecuboid();

            if (parameterTypes.length == 1) {
                if (parameterTypes[0].isAssignableFrom(Secuboid.class)) {
                    return (CommandExec) constructor.newInstance(secuboid);
                } else if (parameterTypes[0].isAssignableFrom(pluginClass)) {
                    return (CommandExec) constructor.newInstance(plugin);
                }

            } else if (parameterTypes.length == 2 && parameterTypes[0].isAssignableFrom(Secuboid.class)
                    && parameterTypes[1].isAssignableFrom(pluginClass)) {
                return (CommandExec) constructor.newInstance(secuboid, plugin);
            }
        }

        return null;
    }

    <T extends JavaPlugin> T getPlugin(Class<T> clazz) {
        return SecuboidImpl.getPlugin(clazz);
    }

    Secuboid getSecuboid() {
        return SecuboidImpl.instance();
    }
}
