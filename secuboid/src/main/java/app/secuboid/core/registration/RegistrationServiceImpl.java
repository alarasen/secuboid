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

package app.secuboid.core.registration;

import app.secuboid.api.commands.CommandExec;
import app.secuboid.api.flagtypes.FlagType;
import app.secuboid.api.recipients.RecipientExec;
import app.secuboid.api.registration.CommandRegistered;
import app.secuboid.api.registration.RecipientRegistered;
import app.secuboid.api.registration.RegistrationService;
import app.secuboid.api.services.Service;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static app.secuboid.core.messages.Log.log;
import static java.util.logging.Level.SEVERE;

public class RegistrationServiceImpl implements RegistrationService {

    private final @NotNull Map<Plugin, List<Service>> pluginToServices;
    private final @NotNull Map<CommandExec, CommandRegistered> commandExecToCommandRegistered;
    private final @NotNull Set<FlagType> flagTypes;
    private final @NotNull Map<RecipientExec, RecipientRegistered> recipientExecToRecipientRegistered;
    private boolean isRegistrationClosed;

    public RegistrationServiceImpl() {
        pluginToServices = new LinkedHashMap<>();
        commandExecToCommandRegistered = new HashMap<>();
        flagTypes = new HashSet<>();
        recipientExecToRecipientRegistered = new HashMap<>();
        isRegistrationClosed = false;
    }


    @Override
    public void onEnable(boolean isServerBoot) {
        if (isServerBoot) {
            isRegistrationClosed = true;
        }
    }

    @Override
    public void registerService(@NotNull Plugin plugin, @NotNull Service service) {
        if (isRegistrationClosedMessage("service", service.getClass().getName())) {
            return;
        }

        pluginToServices.computeIfAbsent(plugin, p -> new ArrayList<>()).add(service);
    }

    @Override
    public void registerCommand(@NotNull CommandExec commandExec) {
        Class<? extends CommandExec> clazz = commandExec.getClass();
        if (isRegistrationClosedMessage("command exec class", clazz.getName())) {
            return;
        }

        CommandRegistered commandRegistered = clazz.getAnnotation(CommandRegistered.class);

        if (commandRegistered != null) {
            commandExecToCommandRegistered.put(commandExec, commandRegistered);
        } else {
            log().log(SEVERE, "The class {} needs to have the \"@CommandRegistered\" annotation",
                    commandExec.getClass().getName());
        }
    }

    @Override
    public void registerFlagType(@NotNull Class<?> clazz) {
        if (isRegistrationClosedMessage("flag types class", clazz.getName())) {
            return;
        }

        Arrays.stream(clazz.getFields()).forEach(field -> {
            if (field.getDeclaringClass().isAssignableFrom(FlagType.class)) {
                try {
                    registerFlagType((FlagType) field.get(null));
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    log().log(SEVERE, e, () -> String.format("The field %s form class %s cannot be registered", field,
                            clazz.getName()));
                }
            }
        });
    }

    @Override
    public void registerFlagType(@NotNull FlagType flagType) {
        if (isRegistrationClosedMessage("flag type", flagType.name())) {
            return;
        }

        flagTypes.add(flagType);
    }

    @Override
    public void registerRecipient(@NotNull RecipientExec recipientExec) {
        Class<? extends RecipientExec> clazz = recipientExec.getClass();
        if (isRegistrationClosedMessage("recipient exec class", clazz.getName())) {
            return;
        }

        RecipientRegistered recipientRegistered = clazz.getAnnotation(RecipientRegistered.class);

        if (recipientRegistered != null) {
            recipientExecToRecipientRegistered.put(recipientExec, recipientRegistered);
        } else {
            log().log(SEVERE, "The class {} needs to have the \"@CommandRegistered\" annotation",
                    recipientExec.getClass().getName());
        }
    }

    public @NotNull Map<Plugin, List<Service>> getPluginToServices() {
        return pluginToServices;
    }

    public @NotNull Map<CommandExec, CommandRegistered> getCommandExecToCommandRegistered() {
        return commandExecToCommandRegistered;
    }

    public @NotNull Set<FlagType> getFlagTypes() {
        return flagTypes;
    }

    public @NotNull Map<RecipientExec, RecipientRegistered> getRecipientExecToRecipientRegistered() {
        return recipientExecToRecipientRegistered;
    }

    private boolean isRegistrationClosedMessage(@NotNull String componentName, @NotNull String name) {
        if (isRegistrationClosed) {
            log().log(SEVERE, "The {} \"{}\" needs to be registered in \"onLoad()\" method or Secuboid plugin not " +
                    "declared as dependence", new Object[]{componentName, name});
            return true;
        }

        return false;
    }
}
