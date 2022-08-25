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
package me.tabinol.secuboid.core.flagtypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.tabinol.secuboid.api.flagtypes.FlagType;
import me.tabinol.secuboid.api.flagtypes.FlagTypes;
import me.tabinol.secuboid.api.reflection.FlagRegistered;
import me.tabinol.secuboid.core.reflection.PluginLoader;

public class FlagTypesImpl implements FlagTypes {

    private final Map<String, FlagType> nameToFlagType;

    public FlagTypesImpl() {
        nameToFlagType = new HashMap<>();
    }

    public void init(PluginLoader pluginLoader) {
        if (!nameToFlagType.isEmpty()) {
            return;
        }

        Set<FlagType> flagTypes = pluginLoader.getAnnotatedConstants(FlagRegistered.class, FlagType.class);
        flagTypes.stream().forEach(f -> nameToFlagType.put(f.name(), f));

    }

    @Override
    public FlagType getFlagType(String flagName) {
        return nameToFlagType.get(flagName);
    }

    @Override
    public Set<String> getFlagTypeNames() {
        return nameToFlagType.keySet();
    }
}
