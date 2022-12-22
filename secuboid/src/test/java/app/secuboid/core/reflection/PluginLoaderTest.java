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
package app.secuboid.core.reflection;

import app.secuboid.api.flagtypes.FlagType;
import app.secuboid.api.reflection.FlagRegistered;
import app.secuboid.core.flags.FlagDeclarations;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PluginLoaderTest {

    private PluginLoader pluginLoader;

    @BeforeEach
    void beforeEach() {
        SimplePluginManager pluginManager = mock(SimplePluginManager.class);
        Plugin plugin = mock(Plugin.class);
        when(pluginManager.getPlugins()).thenReturn(new Plugin[]{plugin});
        ClassLoader classLoader = this.getClass().getClassLoader();
        when(plugin.getResource(PluginLoader.FILENAME_SECUBOID_PLUGIN))
                .thenReturn(classLoader.getResourceAsStream(PluginLoader.FILENAME_SECUBOID_PLUGIN));

        pluginLoader = new PluginLoader();
        pluginLoader.init(pluginManager);
    }

    @Test
    void when_send_yml_get_object() {
        Set<FlagType> flags = pluginLoader.getAnnotatedConstants(FlagRegistered.class, FlagType.class);

        assertFalse(flags.isEmpty());
        assertTrue(flags.contains(FlagDeclarations.FLAG_BUILD));
    }
}
