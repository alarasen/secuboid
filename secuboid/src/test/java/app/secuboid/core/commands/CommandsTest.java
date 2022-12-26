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
import app.secuboid.api.SecuboidPlugin;
import app.secuboid.api.commands.CommandExec;
import app.secuboid.api.players.ChatPage;
import app.secuboid.api.players.CommandSenderInfo;
import app.secuboid.api.reflection.CommandRegistered;
import app.secuboid.core.players.ChatPageImpl;
import app.secuboid.core.reflection.PluginLoader;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommandsTest {

    private CommandsImpl commands;
    private CommandSender sender;
    private CommandSenderInfo commandSenderInfo;

    @BeforeEach
    void beforeEach() {
        SecuboidPlugin secuboidPlugin = mock(SecuboidPlugin.class);
        Secuboid secuboid = mock(Secuboid.class);
        PluginLoader pluginLoader = mock(PluginLoader.class);

        CommandRegistered commandRegistered = CommandTest.class.getAnnotation(CommandRegistered.class);
        Map<Class<? extends CommandExec>, CommandRegistered> classToAnnotation = Collections
                .singletonMap(CommandTest.class, commandRegistered);
        when(pluginLoader.getClassToAnnotation(CommandRegistered.class, CommandExec.class))
                .thenReturn(classToAnnotation);

        commands = spy(new CommandsImpl());
        doReturn(secuboidPlugin).when(commands).getPlugin(any());
        doReturn(secuboid).when(commands).getSecuboid();
        commands.init(pluginLoader);

        sender = mock(CommandSender.class);
        commandSenderInfo = new CommandSenderInfoTest(sender);
    }

    @Test
    void when_call_test_command_name_then_execute_it() {
        commands.executeCommandName(commandSenderInfo, new String[]{"test"});

        verify(sender, times(1)).sendMessage(anyString());
    }

    @Test
    void when_call_test_command_class_then_execute_it() {
        commands.executeCommandClass(CommandTest.class, commandSenderInfo, new String[]{});

        verify(sender, times(1)).sendMessage(anyString());
    }

    @CommandRegistered( //
            pluginClass = SecuboidPlugin.class, //
            name = "test" //
    )
    private static class CommandTest implements CommandExec {

        @SuppressWarnings("unused")
        CommandTest(Secuboid secuboid) {
        }

        @Override
        public void commandExec(@NotNull CommandSenderInfo commandSenderInfo, String[] subArgs) {
            CommandSender sender = commandSenderInfo.sender();
            sender.sendMessage("done!");
        }
    }

    private record CommandSenderInfoTest(CommandSender sender) implements CommandSenderInfo {

        @Override
        public @NotNull CommandSender sender() {
            return sender;
        }

        @Override
        public @NotNull String getName() {
            return "sender";
        }

        @Override
        public boolean isAdminMode() {
            return false;
        }

        @Override
        public @NotNull ChatPage newChatPage(@NotNull String header, @NotNull String text) {
            return new ChatPageImpl(sender, "header", "text");
        }

        @Override
        public @Nullable ChatPage getChatPage() {
            return null;
        }

        @Override
        public void removeChatPage() {

        }
    }
}
