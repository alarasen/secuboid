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

import app.secuboid.api.players.CommandSenderInfo;
import app.secuboid.core.SecuboidImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

public class ChatGetter {

    private final ConcurrentMap<CommandSenderInfo, Consumer<String>> commandSenderInfoToCallback;

    public ChatGetter() {
        commandSenderInfoToCallback = new ConcurrentHashMap<>();
    }

    public void reset() {
        commandSenderInfoToCallback.clear();
    }

    public void remove(@NotNull CommandSenderInfo commandSenderInfo) {
        commandSenderInfoToCallback.remove(commandSenderInfo);
    }


    public void put(@NotNull CommandSenderInfo commandSenderInfo, @NotNull Consumer<String> callback) {
        this.commandSenderInfoToCallback.put(commandSenderInfo, callback);
    }

    public boolean checkAnswerAndCallBackIfNeeded(@NotNull CommandSenderInfo commandSenderInfo, @NotNull String message) {
        Consumer<String> callback = this.commandSenderInfoToCallback.remove(commandSenderInfo);

        if (callback != null) {
            SecuboidImpl.getJavaPLugin().getServer().getScheduler().callSyncMethod(SecuboidImpl.getJavaPLugin(), () -> {
                callback.accept(message);
                return null;
            });
            return true;
        }

        return false;
    }
}
