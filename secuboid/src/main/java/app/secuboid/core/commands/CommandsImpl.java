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
package app.secuboid.core.commands;

import app.secuboid.api.Secuboid;
import app.secuboid.api.commands.CommandExec;
import app.secuboid.api.commands.Commands;
import app.secuboid.api.players.CommandSenderInfo;
import app.secuboid.api.reflection.CommandRegistered;
import app.secuboid.core.SecuboidImpl;
import app.secuboid.core.commands.exec.CommandPage;
import app.secuboid.core.messages.Log;
import app.secuboid.core.reflection.PluginLoader;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.logging.Level.SEVERE;

public class CommandsImpl implements Commands {

    private static final int COMMANDS_SEPARATOR = ' ';

    private final Map<String, CommandContainer> nameToCommand;
    private final Map<Class<? extends CommandExec>, CommandContainer> classToCommand;

    public CommandsImpl() {
        nameToCommand = new HashMap<>();
        classToCommand = new HashMap<>();
    }

    public void init(@NotNull PluginLoader pluginLoader) {
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

                long curLevel = commandRegistered.name().chars().filter(c -> c == COMMANDS_SEPARATOR).count();
                if (curLevel == commandLevel) {
                    found = true;
                    try {
                        registerCommand(clazz, commandRegistered);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                             | InvocationTargetException | SecurityException e) {
                        Log.log().log(SEVERE, e,
                                () -> "CommandExec constructor not visible or contains wrong parameters: " + clazz);
                    }
                }
            }
            commandLevel++;
        } while (found);
    }

    @Override
    public void executeCommandClass(@NotNull Class<? extends CommandExec> clazz, @NotNull CommandSenderInfo commandSenderInfo,
                                    @NotNull String[] subArgs) {

        CommandContainer commandContainer = classToCommand.get(clazz);
        executeCommand(commandContainer, commandSenderInfo, subArgs);
    }

    void executeCommandName(@NotNull CommandSenderInfo commandSenderInfo, @NotNull String[] args) {
        CommandSender sender = commandSenderInfo.sender();

        if (args.length == 0) {
            // TODO help with click
            sender.sendMessage("todo");
            return;
        }

        Map<String, CommandContainer> curNameToCommand = nameToCommand;
        int commandPathCounter = 0;
        CommandContainer commandContainer = null;
        CommandContainer curCommandContainer;
        do {
            String arg = args[commandPathCounter];
            String argLower = arg.toLowerCase();
            curCommandContainer = curNameToCommand.get(argLower);

            if (curCommandContainer != null) {
                commandContainer = curCommandContainer;
                curNameToCommand = commandContainer.nameToSubCommand();
            }

            commandPathCounter++;
        } while (curCommandContainer != null && commandPathCounter < args.length && !curNameToCommand.isEmpty());

        if (commandContainer == null) {
            // TODO help with null
            sender.sendMessage("todo");
            return;
        }

        String[] subArgs = Arrays.copyOfRange(args, commandPathCounter, args.length);
        executeCommand(commandContainer, commandSenderInfo, subArgs);
    }

    private void executeCommand(@NotNull CommandContainer commandContainer, @NotNull CommandSenderInfo commandSenderInfo,
                                @NotNull String[] subArgs) {
        CommandExec commandExec = commandContainer.commandExec();

        if (!(commandExec instanceof CommandPage)) {
            commandSenderInfo.removeChatPage();
        }

        // TODO check permissions

        // TODO subcommands

        commandExec.commandExec(commandSenderInfo, subArgs);
    }

    private void registerCommand(Class<? extends CommandExec> clazz, CommandRegistered commandRegistered)
            throws SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        Class<? extends JavaPlugin> pluginClass = commandRegistered.pluginClass();
        CommandExec commandExec = newInstance(clazz, pluginClass);

        if (commandExec == null) {
            Log.log().log(SEVERE, () -> "No valid constructor for this command class: " + clazz);
            return;
        }

        // TODO Retreve flags

        String commandName = commandRegistered.name();
        String[] nameSplit = commandName.split(Character.toString(COMMANDS_SEPARATOR));
        int nameSplitLengthMinusOne = nameSplit.length - 1;
        Map<String, CommandContainer> curNameToCommand = nameToCommand;

        for (int i = 0; i < nameSplitLengthMinusOne; i++) {
            CommandContainer curCommandContainer = curNameToCommand.get(nameSplit[i]);
            if (curCommandContainer == null) {
                Log.log().log(SEVERE, () -> "There is an error on registered command parent name: " + commandName);
                return;
            }
            curNameToCommand = curCommandContainer.nameToSubCommand();
        }

        String subName = nameSplit[nameSplitLengthMinusOne];
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

            if (parameterTypes.length == 0) {
                return (CommandExec) constructor.newInstance();
            }

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
