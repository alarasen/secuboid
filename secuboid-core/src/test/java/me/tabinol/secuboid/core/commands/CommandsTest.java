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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.tabinol.secuboid.api.Secuboid;
import me.tabinol.secuboid.api.SecuboidCorePlugin;
import me.tabinol.secuboid.api.commands.CommandExec;
import me.tabinol.secuboid.api.parameters.values.ParameterValuePlayer;
import me.tabinol.secuboid.api.players.CommandSenderInfo;
import me.tabinol.secuboid.api.reflection.CommandRegistered;
import me.tabinol.secuboid.core.reflection.PluginLoader;

class CommandsTest {

    private CommandsImpl commands;
    private PluginLoader pluginLoader;
    private CommandSender sender;
    private CommandSenderInfo commandSenderInfo;

    @BeforeEach
    void beforeEach() {
        SecuboidCorePlugin secuboidCorePlugin = mock(SecuboidCorePlugin.class);
        Secuboid secuboid = mock(Secuboid.class);
        pluginLoader = mock(PluginLoader.class);

        CommandRegistered commandRegistered = CommandTest.class.getAnnotation(CommandRegistered.class);
        Map<Class<? extends CommandExec>, CommandRegistered> classToAnnotation = Collections
                .singletonMap(CommandTest.class, commandRegistered);
        when(pluginLoader.getClassToAnnotation(CommandRegistered.class, CommandExec.class))
                .thenReturn(classToAnnotation);

        commands = spy(new CommandsImpl());
        doReturn(secuboidCorePlugin).when(commands).getPlugin(any());
        doReturn(secuboid).when(commands).getSecuboid();
        commands.init(pluginLoader);

        sender = mock(CommandSender.class);
        commandSenderInfo = new CommandSenderInfoTest(sender);
    }

    @Test
    void when_call_test_command_name_then_execute_it() {
        commands.executeCommandName(commandSenderInfo, new String[] { "test" });

        verify(sender, times(1)).sendMessage(anyString());
    }

    @Test
    void when_call_test_command_class_then_execute_it() {
        commands.executeCommandClass(CommandTest.class, commandSenderInfo, new String[] {});

        verify(sender, times(1)).sendMessage(anyString());
    }

    @CommandRegistered( //
            pluginClass = SecuboidCorePlugin.class, //
            name = "test" //
    )
    private static class CommandTest implements CommandExec {

        @SuppressWarnings("unused")
        CommandTest(Secuboid secuboid) {
        }

        @Override
        public void commandExec(CommandSenderInfo commandSenderInfo, String[] subArgs) {
            CommandSender sender = commandSenderInfo.getSender();
            sender.sendMessage("done!");
        }
    }

    private static class CommandSenderInfoTest implements CommandSenderInfo {

        private final CommandSender sender;

        public CommandSenderInfoTest(CommandSender sender) {
            this.sender = sender;
        }

        @Override
        public CommandSender getSender() {
            return sender;
        }

        @Override
        public String getName() {
            return "sender";
        }

        @Override
        public ParameterValuePlayer getParameterValue() {
            // Not implemented
            return null;
        }

        @Override
        public boolean isAdminMode() {
            return false;
        }
    }
}
